package upbrella.be.store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import upbrella.be.store.dto.request.SingleBusinessHourRequest;
import upbrella.be.store.dto.response.SingleBusinessHourResponse;
import upbrella.be.store.entity.BusinessHour;
import upbrella.be.store.repository.BusinessHourRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class BusinessHourServiceTest {

    @Mock
    private BusinessHourRepository businessHourRepository;
    @InjectMocks
    private BusinessHourService businessHourService;

    @Test
    @DisplayName("saveAllBusinessHour() 호출하면, 모든 요일이 저장된다.")
    void saveAllBusinessHour() {
        // given
        BusinessHour monday = BusinessHour.builder()
                .date(DayOfWeek.MONDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour tuesday = BusinessHour.builder()
                .date(DayOfWeek.TUESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour wednesday = BusinessHour.builder()
                .date(DayOfWeek.WEDNESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour thursday = BusinessHour.builder()
                .date(DayOfWeek.THURSDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour friday = BusinessHour.builder()
                .date(DayOfWeek.FRIDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour saturday = BusinessHour.builder()
                .date(DayOfWeek.SATURDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour sunday = BusinessHour.builder()
                .date(DayOfWeek.SUNDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        List<BusinessHour> businessHours = List.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        // when
        businessHourService.saveAllBusinessHour(businessHours);

        // then
        then(businessHourRepository).should(times(1)).saveAll(businessHours);
    }

    @Test
    @DisplayName("id 를 기준으로 조회하면 모든 요일이 조회된다.")
    void findBusinessHourByStoreMetaIdTest() {
        // given
        BusinessHour monday = BusinessHour.builder()
                .date(DayOfWeek.MONDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour tuesday = BusinessHour.builder()
                .date(DayOfWeek.TUESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour wednesday = BusinessHour.builder()
                .date(DayOfWeek.WEDNESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour thursday = BusinessHour.builder()
                .date(DayOfWeek.THURSDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour friday = BusinessHour.builder()
                .date(DayOfWeek.FRIDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour saturday = BusinessHour.builder()
                .date(DayOfWeek.SATURDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour sunday = BusinessHour.builder()
                .date(DayOfWeek.SUNDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        List<BusinessHour> businessHours = List.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        given(businessHourRepository.findByStoreMetaId(1L)).willReturn(businessHours);

        // when
        List<BusinessHour> businessHourList = businessHourService.findBusinessHourByStoreMetaId(1L);

        // then
        assertAll(
                () -> assertThat(businessHourList).hasSize(7),
                () -> assertThat(businessHourList).containsAll(businessHours)
        );

    }

    @Test
    @DisplayName("id를 기준으로 조회했는데 id가 없으면 빈 리스트가 조회된다.")
    void emptyBusinessTest() {
        // given
        given(businessHourRepository.findByStoreMetaId(1L)).willReturn(List.of());

        // when
        List<BusinessHour> businessHours = businessHourService.findBusinessHourByStoreMetaId(1L);

        // then
        assertThat(businessHours).isEmpty();
    }

    @Test
    @DisplayName("협업지점의 영업시간을 id 를 기준으로 수정할 수 있다.")
    void updateBusinessHourTest() {
        // given
        BusinessHour monday = BusinessHour.builder()
                .date(DayOfWeek.MONDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour tuesday = BusinessHour.builder()
                .date(DayOfWeek.TUESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour wednesday = BusinessHour.builder()
                .date(DayOfWeek.WEDNESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour thursday = BusinessHour.builder()
                .date(DayOfWeek.THURSDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour friday = BusinessHour.builder()
                .date(DayOfWeek.FRIDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour saturday = BusinessHour.builder()
                .date(DayOfWeek.SATURDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour sunday = BusinessHour.builder()
                .date(DayOfWeek.SUNDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();

        SingleBusinessHourRequest updateMonday = SingleBusinessHourRequest.builder()
                .date(DayOfWeek.MONDAY)
                .openAt(LocalTime.of(9, 10))
                .closeAt(LocalTime.of(18, 10))
                .build();

        SingleBusinessHourRequest updateTuesday = SingleBusinessHourRequest.builder()
                .date(DayOfWeek.TUESDAY)
                .openAt(LocalTime.of(9, 10))
                .closeAt(LocalTime.of(18, 10))
                .build();

        SingleBusinessHourRequest updateWednesday = SingleBusinessHourRequest.builder()
                .date(DayOfWeek.WEDNESDAY)
                .openAt(LocalTime.of(9, 10))
                .closeAt(LocalTime.of(18, 10))
                .build();

        SingleBusinessHourRequest updateThursday = SingleBusinessHourRequest.builder()
                .date(DayOfWeek.THURSDAY)
                .openAt(LocalTime.of(9, 10))
                .closeAt(LocalTime.of(18, 10))
                .build();

        SingleBusinessHourRequest updateFriday = SingleBusinessHourRequest.builder()
                .date(DayOfWeek.FRIDAY)
                .openAt(LocalTime.of(9, 10))
                .closeAt(LocalTime.of(18, 10))
                .build();

        SingleBusinessHourRequest updateSaturday = SingleBusinessHourRequest.builder()
                .date(DayOfWeek.SATURDAY)
                .openAt(LocalTime.of(9, 10))
                .closeAt(LocalTime.of(18, 10))
                .build();

        SingleBusinessHourRequest updateSunday = SingleBusinessHourRequest.builder()
                .date(DayOfWeek.SUNDAY)
                .openAt(LocalTime.of(9, 10))
                .closeAt(LocalTime.of(18, 10))
                .build();

        List<BusinessHour> businessHours = List.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        List<SingleBusinessHourRequest> updateBusinessHours = List.of(updateMonday, updateTuesday, updateWednesday, updateThursday, updateFriday, updateSaturday, updateSunday);

        given(businessHourRepository.findByStoreMetaId(1L)).willReturn(businessHours);

        // when
        businessHourService.updateBusinessHour(1L, updateBusinessHours);

        // then
        assertAll(
                () -> assertThat(monday.getOpenAt()).isEqualTo(updateMonday.getOpenAt()),
                () -> assertThat(monday.getCloseAt()).isEqualTo(updateMonday.getCloseAt()),
                () -> assertThat(tuesday.getOpenAt()).isEqualTo(updateTuesday.getOpenAt()),
                () -> assertThat(tuesday.getCloseAt()).isEqualTo(updateTuesday.getCloseAt()),
                () -> assertThat(wednesday.getOpenAt()).isEqualTo(updateWednesday.getOpenAt()),
                () -> assertThat(wednesday.getCloseAt()).isEqualTo(updateWednesday.getCloseAt()),
                () -> assertThat(thursday.getOpenAt()).isEqualTo(updateThursday.getOpenAt()),
                () -> assertThat(thursday.getCloseAt()).isEqualTo(updateThursday.getCloseAt()),
                () -> assertThat(friday.getOpenAt()).isEqualTo(updateFriday.getOpenAt()),
                () -> assertThat(friday.getCloseAt()).isEqualTo(updateFriday.getCloseAt()),
                () -> assertThat(saturday.getOpenAt()).isEqualTo(updateSaturday.getOpenAt()),
                () -> assertThat(saturday.getCloseAt()).isEqualTo(updateSaturday.getCloseAt()),
                () -> assertThat(sunday.getOpenAt()).isEqualTo(updateSunday.getOpenAt()),
                () -> assertThat(sunday.getCloseAt()).isEqualTo(updateSunday.getCloseAt())
        );
    }

    @Test
    @DisplayName("협업지점 응답을 위해 entity를 dto로 변경할 수 있다.")
    void test() {
        // given
        // given
        BusinessHour monday = BusinessHour.builder()
                .date(DayOfWeek.MONDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour tuesday = BusinessHour.builder()
                .date(DayOfWeek.TUESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour wednesday = BusinessHour.builder()
                .date(DayOfWeek.WEDNESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour thursday = BusinessHour.builder()
                .date(DayOfWeek.THURSDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour friday = BusinessHour.builder()
                .date(DayOfWeek.FRIDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour saturday = BusinessHour.builder()
                .date(DayOfWeek.SATURDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();
        BusinessHour sunday = BusinessHour.builder()
                .date(DayOfWeek.SUNDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();

        SingleBusinessHourResponse responseMonday = SingleBusinessHourResponse.builder()
                .date(DayOfWeek.MONDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();

        SingleBusinessHourResponse responseTuesday = SingleBusinessHourResponse.builder()
                .date(DayOfWeek.TUESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();

        SingleBusinessHourResponse responseWednesday = SingleBusinessHourResponse.builder()
                .date(DayOfWeek.WEDNESDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();

        SingleBusinessHourResponse responseThursday = SingleBusinessHourResponse.builder()
                .date(DayOfWeek.THURSDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();

        SingleBusinessHourResponse responseFriday = SingleBusinessHourResponse.builder()
                .date(DayOfWeek.FRIDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();

        SingleBusinessHourResponse responseSaturday = SingleBusinessHourResponse.builder()
                .date(DayOfWeek.SATURDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();

        SingleBusinessHourResponse responseSunday = SingleBusinessHourResponse.builder()
                .date(DayOfWeek.SUNDAY)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .build();

        List<BusinessHour> businessHours = List.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        List<SingleBusinessHourResponse> businessHourResponses = List.of(responseMonday, responseTuesday, responseWednesday, responseThursday, responseFriday, responseSaturday, responseSunday);

        // when
        List<SingleBusinessHourResponse> response = businessHourService.createBusinessHourResponse(businessHours);

        // then
        assertAll(
                () -> assertThat(response.size()).isEqualTo(businessHourResponses.size()),
                () -> assertThat(response.get(0).getDate()).isEqualTo(businessHourResponses.get(0).getDate()),
                () -> assertThat(response.get(1).getDate()).isEqualTo(businessHourResponses.get(1).getDate()),
                () -> assertThat(response.get(2).getDate()).isEqualTo(businessHourResponses.get(2).getDate()),
                () -> assertThat(response.get(3).getDate()).isEqualTo(businessHourResponses.get(3).getDate()),
                () -> assertThat(response.get(4).getDate()).isEqualTo(businessHourResponses.get(4).getDate()),
                () -> assertThat(response.get(5).getDate()).isEqualTo(businessHourResponses.get(5).getDate()),
                () -> assertThat(response.get(6).getDate()).isEqualTo(businessHourResponses.get(6).getDate())
        );

    }
}
