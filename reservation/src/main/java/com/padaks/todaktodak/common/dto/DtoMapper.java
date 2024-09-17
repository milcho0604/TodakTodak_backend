package com.padaks.todaktodak.common.dto;

import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.dto.CheckListReservationReqDto;
import com.padaks.todaktodak.reservation.dto.ReservationSaveReqDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// Mapper 어노테이션을 붙이면 MapStruct 가 자동으로 DtoMapper 구현체를 생성해준다.
@Mapper(componentModel = "spring")
public interface DtoMapper {
//    매퍼 클래스에서 DtoMapper 를 찾을수 있도록 하는 코드.
//    instance를 생성해 주어야 매퍼에 대한 접근이 가능.
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    Reservation toReservation(ReservationSaveReqDto reservationSaveReqDto);

    CheckListReservationReqDto toListReservation(Reservation reservation);
}
