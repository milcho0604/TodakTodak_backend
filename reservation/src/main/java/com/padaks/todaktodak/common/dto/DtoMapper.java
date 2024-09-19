package com.padaks.todaktodak.common.dto;

import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.domain.ReserveType;
import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationResDto;
import com.padaks.todaktodak.reservation.dto.CheckListReservationReqDto;
import com.padaks.todaktodak.reservation.dto.ResType;
import com.padaks.todaktodak.reservation.domain.ReservationHistory;
import com.padaks.todaktodak.reservation.dto.ReservationSaveReqDto;
import com.padaks.todaktodak.review.domain.Review;
import com.padaks.todaktodak.review.dto.CreateReviewReqDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// Mapper 어노테이션을 붙이면 MapStruct 가 자동으로 DtoMapper 구현체를 생성해준다.
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface DtoMapper {
    //    매퍼 클래스에서 DtoMapper 를 찾을수 있도록 하는 코드.
//    instance를 생성해 주어야 매퍼에 대한 접근이 가능.
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    Reservation toReservation(ReservationSaveReqDto reservationSaveReqDto);

    CheckListReservationReqDto toListReservation(Reservation reservation);

//    default 를 통해 수동 매핑을 구현,
//    MapStruct가 자동으로 매핑하지 못하거나, 복잡한 매핑 로직을 수행하고자 할 때 사용.
    default ReserveType resTypeToReserveType(ResType resType) {
        if (resType == null) {
            return null;
        }
        switch (resType) {
            case Immediate:
                return ReserveType.Immediate;
            case Scheduled:
                return ReserveType.Scheduled;
            default:
                throw new IllegalArgumentException("Unknown ResType: " + resType);
        }
    }
    ReservationHistory toReservationHistory(Reservation reservation);

    CheckHospitalListReservationResDto toHospitalListReservation(Reservation reservation);

//    Review 의 reservation 을 파라미터로 받음 reservation으로 매핑.
    @Mapping(source = "reservation", target = "reservation")
//    CreateReviewReqDto 에서 id 는 매핑 타겟에서 제외하겠다.
//    제외 하지 않으면 CreateReviewReqDto 에서 id 도 매핑하려고 했어 오류가 나거나 잘못된 값 매핑 가능성이 있음.
    @Mapping(target = "id", ignore = true)
    Review toReview(CreateReviewReqDto createReviewReqDto, Reservation reservation);
}
