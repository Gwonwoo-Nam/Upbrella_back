package upbrella.be.rent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import upbrella.be.rent.dto.request.HistoryFilterRequest;
import upbrella.be.rent.dto.request.RentUmbrellaByUserRequest;
import upbrella.be.rent.dto.response.RentalHistoriesPageResponse;
import upbrella.be.rent.dto.response.RentalHistoryResponse;
import upbrella.be.rent.entity.History;
import upbrella.be.rent.exception.NonExistingHistoryException;
import upbrella.be.rent.repository.RentRepository;
import upbrella.be.store.entity.StoreMeta;
import upbrella.be.store.service.StoreMetaService;
import upbrella.be.umbrella.entity.Umbrella;
import upbrella.be.umbrella.service.UmbrellaService;
import upbrella.be.user.dto.response.AllHistoryResponse;
import upbrella.be.user.dto.response.SingleHistoryResponse;
import upbrella.be.user.entity.User;
import upbrella.be.user.exception.NonExistingMemberException;
import upbrella.be.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {

    @Mock
    private UmbrellaService umbrellaService;
    @Mock
    private StoreMetaService storeMetaService;
    @Mock
    private RentRepository rentRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private RentService rentService;
    private RentUmbrellaByUserRequest rentUmbrellaByUserRequest;
    private StoreMeta foundStoreMeta;
    private Umbrella foundUmbrella;
    private User userToRent;
    private History history;
    private HistoryFilterRequest filter;

    @BeforeEach
    void setUp() {
        rentUmbrellaByUserRequest = RentUmbrellaByUserRequest.builder()
                .region("신촌")
                .storeId(25L)
                .umbrellaId(99L)
                .conditionReport("상태 양호")
                .build();

        foundStoreMeta = StoreMeta.builder()
                .id(25L)
                .name("motive study cafe")
                .deleted(false)
                .build();

        foundUmbrella = Umbrella.builder()
                .id(99L)
                .uuid(99L)
                .deleted(false)
                .storeMeta(foundStoreMeta)
                .rentable(true)
                .build();

        userToRent = User.builder()
                .id(11L)
                .name("테스터")
                .phoneNumber("010-1234-5678")
                .adminStatus(false)
                .build();

        history = History.builder()
                .id(33L)
                .rentedAt(LocalDateTime.of(1000, 12, 3, 4, 24))
                .returnedAt(LocalDateTime.of(1000, 12, 3, 4, 25))
                .refundedAt(LocalDateTime.of(1000, 12, 3, 4, 26))
                .refundedBy(userToRent)
                .returnStoreMeta(foundStoreMeta)
                .umbrella(foundUmbrella)
                .user(userToRent)
                .rentStoreMeta(foundStoreMeta)
                .build();


    }

    @Nested
    @DisplayName("지역 분류, 협력 지점 고유번호, 우산 고유번호, 선택적으로 상태 신고 내역을 입력받아")
    class addRentTest {

        @Test
        @DisplayName("대여 이력에 정상적으로 추가하 수 있다.")
        void success() {

            // given
            given(storeMetaService.findStoreMetaById(25L))
                    .willReturn(foundStoreMeta);
            given(umbrellaService.findUmbrellaById(99L))
                    .willReturn(foundUmbrella);
            given(rentRepository.save(any(History.class)))
                    .willReturn(history);

            // when
            History addedRental = rentService.addRental(rentUmbrellaByUserRequest, userToRent);

            // then
            assertAll(() -> assertThat(addedRental)
                            .usingRecursiveComparison()
                            .isEqualTo(history),
                    () -> then(umbrellaService).should(times(1))
                            .findUmbrellaById(99L),
                    () -> then(storeMetaService).should(times(1))
                            .findStoreMetaById(25L),
                    () -> then(rentRepository).should(times(1))
                            .save(any(History.class))
            );
        }

        @Test
        @DisplayName("해당 협업 지점의 고유 번호가 존재하지 않으면 예외를 발생시킨다.")
        void isNotExistingStore() {

            // given
            given(umbrellaService.findUmbrellaById(99L))
                    .willReturn(foundUmbrella);
            given(storeMetaService.findStoreMetaById(25L))
                    .willThrow(new IllegalArgumentException());

            // when & then
            assertAll(() -> assertThatThrownBy(() ->
                            rentService.addRental(rentUmbrellaByUserRequest, userToRent))
                            .isInstanceOf(IllegalArgumentException.class),
                    () -> then(umbrellaService).should(times(1))
                            .findUmbrellaById(99L),
                    () -> then(storeMetaService).should(times(1))
                            .findStoreMetaById(25L),
                    () -> then(rentRepository).shouldHaveNoInteractions()
            );
        }

        @Test
        @DisplayName("해당 우산의 고유번호가 존재하지 않으면 예외를 발생시킨다.")
        void isNotExistingUmbrella() {

            // given
            given(umbrellaService.findUmbrellaById(99L))
                    .willThrow(new IllegalArgumentException());

            // when & then
            assertAll(() -> assertThatThrownBy(() ->
                            rentService.addRental(rentUmbrellaByUserRequest, userToRent))
                            .isInstanceOf(IllegalArgumentException.class),
                    () -> then(umbrellaService).should(times(1))
                            .findUmbrellaById(99L),
                    () -> then(storeMetaService).shouldHaveNoInteractions(),
                    () -> then(rentRepository).shouldHaveNoInteractions()
            );
        }
    }

    @Nested
    @DisplayName("사용자는 환급 여부 필터링 기능이 가능한 대여/반납 현황을 조회할 수 있다.")
    class findAllHistories {

        @Test
        @DisplayName("조건이 없는 경우 전체 대여/반납 현황을 조회할 수 있다.")
        void success() {

            // given
            filter = HistoryFilterRequest.builder()
                    .build();

            RentalHistoriesPageResponse historyResponse = RentalHistoriesPageResponse.builder()
                    .rentalHistoryResponsePage(
                            List.of(
                                    RentalHistoryResponse.builder()
                                            .id(33L)
                                            .name("테스터")
                                            .phoneNumber("010-1234-5678")
                                            .rentStoreName("motive study cafe")
                                            .rentAt(LocalDateTime.of(1000, 12, 3, 4, 24))
                                            .elapsedDay(0)
                                            .umbrellaUuid(99L)
                                            .returnStoreName("motive study cafe")
                                            .returnAt(LocalDateTime.of(1000, 12, 3, 4, 25))
                                            .totalRentalDay(0)
                                            .refundCompleted(true)
                                            .etc(history.getEtc())
                                            .build())
                    ).build();

            given(rentRepository.findAll(filter))
                    .willReturn(List.of(history));

            // when
            RentalHistoriesPageResponse allHistories = rentService.findAllHistories(filter);

            //then
            assertAll(() -> assertThat(allHistories)
                            .usingRecursiveComparison()
                            .isEqualTo(historyResponse),
                    () -> assertThat(allHistories.getRentalHistoryResponsePage().size())
                            .isEqualTo(1),
                    () -> then(rentRepository).should(times(1))
                            .findAll(filter)
            );

        }
    }

    @Nested
    @DisplayName("사용자의 고유 번호를 입력 받아")
    class findUserHistoryTest {

        @Test
        @DisplayName("해당 사용자의 대여 목록을 조회할 수 있다.")
        void success() {

            // given
            long loginedUserId = 7L;


            AllHistoryResponse historyResponse = AllHistoryResponse.builder()
                    .histories(
                            List.of(
                                    SingleHistoryResponse.builder()
                                            .umbrellaUuid(99L)
                                            .rentedAt(LocalDateTime.of(1000, 12, 3, 4, 24))
                                            .returnAt(LocalDateTime.of(1000, 12, 3, 4, 25))
                                            .rentedStore("motive study cafe")
                                            .isRefunded(true)
                                            .isReturned(true)
                                            .build())
                    ).build();


            given(rentRepository.findAllByUserId(loginedUserId))
                    .willReturn(List.of(history));

            // when
            AllHistoryResponse userHistory = rentService.findAllHistoriesByUser(loginedUserId);

            //then
            assertAll(() -> assertThat(userHistory)
                            .usingRecursiveComparison()
                            .isEqualTo(historyResponse),
                    () -> assertThat(userHistory.getHistories().size())
                            .isEqualTo(1),
                    () -> then(rentRepository).should(times(1))
                            .findAllByUserId(7L)
            );
        }

        @Test
        @DisplayName("대여 내역이 없으면 빈 목록을 반환한다.")
        void nonExistingHistory() {

            // given
            long loginedUserId = 7L;

            given(rentRepository.findAllByUserId(loginedUserId))
                    .willReturn(List.of());

            // when
            AllHistoryResponse userHistory = rentService.findAllHistoriesByUser(loginedUserId);

            //then
            assertAll(() -> assertThat(userHistory.getHistories().size())
                            .isEqualTo(0),
                    () -> then(rentRepository).should(times(1))
                            .findAllByUserId(7L)
            );
        }
    }

    @Nested
    @DisplayName("사용자의 고유 번호와 대여 내역의 고유번호를 입력 받아")
    class checkRefundTest {

        @Test
        @DisplayName("해당 대여 내역을 환급 완료 처리할 수 있다.")
        void success() {

            // given
            long loginedUserId = 7L;
            given(userService.findUserById(loginedUserId))
                    .willReturn(userToRent);
            given(rentRepository.findById(33L))
                    .willReturn(Optional.of(history));
            given(rentRepository.save(history))
                    .willReturn(history);

            // when
            rentService.checkRefund(33L, loginedUserId);

            //then
            assertAll(() -> assertThat(history.getRefundedBy())
                            .isEqualTo(userToRent),
                    () -> assertThat(history.getRefundedAt())
                            .isBeforeOrEqualTo(LocalDateTime.now()),
                    () -> then(userService).should(times(1))
                            .findUserById(loginedUserId),
                    () -> then(rentRepository).should(times(1))
                            .findById(33L),
                    () -> then(rentRepository).should(times(1))
                            .save(history)
            );
        }

        @Test
        @DisplayName("존재하지 않는 사용자면 예외를 발생시킨다.")
        void nonExistingUser() {

            // given
            long loginedUserId = 7L;
            given(userService.findUserById(loginedUserId))
                    .willThrow(NonExistingMemberException.class);

            // when


            //then
            assertAll(() -> assertThatThrownBy(() -> rentService.checkRefund(33L, loginedUserId))
                            .isInstanceOf(NonExistingMemberException.class),
                    () -> then(userService).should(times(1))
                            .findUserById(loginedUserId),
                    () -> then(rentRepository).shouldHaveNoInteractions());
        }

        @Test
        @DisplayName("존재하지 않는 대여 내역 고유번호이면 예외를 발생시킨다.")
        void nonExistingHistory() {

            // given
            long loginedUserId = 7L;
            given(userService.findUserById(loginedUserId))
                    .willReturn(userToRent);
            given(rentRepository.findById(33L))
                    .willThrow(NonExistingHistoryException.class);

            // when & then
            assertAll(() -> assertThatThrownBy(() -> rentService.checkRefund(33L, loginedUserId))
                            .isInstanceOf(NonExistingHistoryException.class),
                    () -> then(userService).should(times(1))
                            .findUserById(loginedUserId),
                    () -> then(rentRepository).should(times(1))
                            .findById(33L),
                    () -> then(rentRepository).shouldHaveNoMoreInteractions());
        }
    }

    @Nested
    @DisplayName("사용자의 고유 번호와 대여 내역의 고유번호를 입력 받아")
    class checkPaymentTest {

        @Test
        @DisplayName("해당 대여 내역을 지불 완료 처리할 수 있다.")
        void success() {

            // given
            long loginedUserId = 7L;
            given(userService.findUserById(loginedUserId))
                    .willReturn(userToRent);
            given(rentRepository.findById(33L))
                    .willReturn(Optional.of(history));
            given(rentRepository.save(history))
                    .willReturn(history);

            // when
            rentService.checkPayment(33L, loginedUserId);

            //then
            assertAll(() -> assertThat(history.getPaidBy())
                            .isEqualTo(userToRent),
                    () -> assertThat(history.getPaidAt())
                            .isBeforeOrEqualTo(LocalDateTime.now()),
                    () -> then(userService).should(times(1))
                            .findUserById(loginedUserId),
                    () -> then(rentRepository).should(times(1))
                            .findById(33L),
                    () -> then(rentRepository).should(times(1))
                            .save(history)
            );
        }

        @Test
        @DisplayName("존재하지 않는 사용자면 예외를 발생시킨다.")
        void nonExistingUser() {

            // given
            long loginedUserId = 7L;
            given(userService.findUserById(loginedUserId))
                    .willThrow(NonExistingMemberException.class);

            // when & then
            assertAll(() -> assertThatThrownBy(() -> rentService.checkPayment(33L, loginedUserId))
                            .isInstanceOf(NonExistingMemberException.class),
                    () -> then(userService).should(times(1))
                            .findUserById(loginedUserId),
                    () -> then(rentRepository).shouldHaveNoInteractions());
        }

        @Test
        @DisplayName("존재하지 않는 대여 내역 고유번호이면 예외를 발생시킨다.")
        void nonExistingHistory() {

            // given
            long loginedUserId = 7L;
            given(userService.findUserById(loginedUserId))
                    .willReturn(userToRent);
            given(rentRepository.findById(33L))
                    .willThrow(NonExistingHistoryException.class);

            // when & then
            assertAll(() -> assertThatThrownBy(() -> rentService.checkPayment(33L, loginedUserId))
                            .isInstanceOf(NonExistingHistoryException.class),
                    () -> then(userService).should(times(1))
                            .findUserById(loginedUserId),
                    () -> then(rentRepository).should(times(1))
                            .findById(33L),
                    () -> then(rentRepository).shouldHaveNoMoreInteractions());
        }
    }
}
