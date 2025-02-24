package upbrella.be.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upbrella.be.store.dto.request.UpdateStoreRequest;
import upbrella.be.store.dto.response.*;
import upbrella.be.store.entity.BusinessHour;
import upbrella.be.store.entity.Classification;
import upbrella.be.store.entity.StoreDetail;
import upbrella.be.store.entity.StoreMeta;
import upbrella.be.store.exception.NonExistingStoreDetailException;
import upbrella.be.store.repository.StoreDetailRepository;
import upbrella.be.umbrella.service.UmbrellaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreDetailService {

    private final ClassificationService classificationService;
    private final StoreMetaService storeMetaService;
    private final UmbrellaService umbrellaService;
    private final StoreDetailRepository storeDetailRepository;
    private final BusinessHourService businessHourService;
    private final StoreImageService storeImageService;

    @Transactional
    public void updateStore(Long storeId, UpdateStoreRequest request) {

        StoreDetail storeDetailById = findStoreDetailById(storeId);
        long storeMetaId = storeDetailById.getStoreMeta().getId();

        Classification classification = classificationService.findClassificationById(request.getClassificationId());
        Classification subClassification = classificationService.findSubClassificationById(request.getSubClassificationId());

        List<BusinessHour> businessHours = businessHourService.updateBusinessHour(storeMetaId, request.getBusinessHours());

        StoreMeta storeMetaForUpdate = StoreMeta.createStoreMetaForUpdate(request, classification, subClassification, businessHours);

        StoreMeta foundStoreMeta = storeDetailById.getStoreMeta();
        foundStoreMeta.updateStoreMeta(storeMetaForUpdate);

        storeDetailById.updateStore(foundStoreMeta, request);
    }

    @Transactional(readOnly = true)
    public StoreDetail findStoreDetailById(Long storeId) {

        return storeDetailRepository.findById(storeId)
                .orElseThrow(() -> new NonExistingStoreDetailException("[ERROR] 존재하지 않는 가게입니다."));
    }

    @Transactional
    public StoreFindByIdResponse findStoreDetailByStoreMetaId(long storeMetaId) {

        StoreDetail storeDetail = storeDetailRepository.findByStoreMetaIdUsingFetchJoin(storeMetaId)
                .orElseThrow(() -> new NonExistingStoreDetailException("[ERROR] 해당하는 협업 지점이 존재하지 않습니다."));

        long availableUmbrellaCount = umbrellaService.countAvailableUmbrellaAtStore(storeMetaId);

        return StoreFindByIdResponse.fromStoreDetail(storeDetail, availableUmbrellaCount);
    }

    @Transactional
    public List<SingleStoreResponse> findAllStores() {

        List<StoreDetail> storeDetails = storeDetailRepository.findAllStores();
        return storeDetails.stream()
                .map(this::createSingleStoreResponse)
                .collect(Collectors.toList());
    }

    public SingleStoreResponse createSingleStoreResponse(StoreDetail storeDetail) {

        List<SingleImageUrlResponse> imageUrls = storeImageService.createImageUrlResponse(storeDetail.getStoreImages());
        String thumbnail = storeImageService.createThumbnail(imageUrls);
        Set<BusinessHour> businessHourSet = storeDetail.getStoreMeta().getBusinessHours();
        List<BusinessHour> businessHourList = new ArrayList<>(businessHourSet);
        List<SingleBusinessHourResponse> businessHours = businessHourService.createBusinessHourResponse(businessHourList);
        return SingleStoreResponse.ofCreateSingleStoreResponse(storeDetail, thumbnail, imageUrls, businessHours);
    }

    public AllStoreIntroductionResponse findAllStoreIntroductions() {

        List<StoreDetail> storeIntroductions = storeDetailRepository.findAllStores();

        List<SingleStoreIntroductionResponse> collect = storeIntroductions.stream()
                .map(this::createSingleIntroduction)
                .collect(Collectors.toList());

        return AllStoreIntroductionResponse.of(collect);
    }

    public void saveStoreDetail(StoreDetail storeDetail) {

        storeDetailRepository.save(storeDetail);
    }

    private SingleStoreIntroductionResponse createSingleIntroduction(StoreDetail storeDetail) {

        List<SingleImageUrlResponse> imageUrls = storeImageService.createImageUrlResponse(storeDetail.getStoreImages());
        String thumbnail = storeImageService.createThumbnail(imageUrls);
        StoreMeta storeMeta = storeDetail.getStoreMeta();
        return SingleStoreIntroductionResponse.of(storeDetail.getId(), thumbnail, storeMeta.getName(), storeMeta.getCategory());
    }
}
