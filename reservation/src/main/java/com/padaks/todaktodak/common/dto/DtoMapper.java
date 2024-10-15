package com.padaks.todaktodak.common.dto;

import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.domain.ReserveType;
import com.padaks.todaktodak.reservation.dto.*;
import com.padaks.todaktodak.reservation.domain.ReservationHistory;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

// Mapper 어노테이션을 붙이면 MapStruct 가 자동으로 DtoMapper 구현체를 생성해준다.
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface DtoMapper {
    //    매퍼 클래스에서 DtoMapper 를 찾을수 있도록 하는 코드.
//    instance를 생성해 주어야 매퍼에 대한 접근이 가능.
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    @Mapping(target = "hospital", source = "hospital")
    @Mapping(target = "memberName", source = "member.name")
    @Mapping(target = "id", ignore = true)
    Reservation toReservation(ReservationSaveReqDto reservationSaveReqDto, MemberFeignDto member, Hospital hospital);

    @Mapping(target = "hospitalId", source = "reservation.hospital.id")
    @Mapping(target = "hospitalName", source = "reservation.hospital.name")
    CheckListReservationReqDto toListReservation(Reservation reservation);

    @AfterMapping
    default void setReservationTime(@MappingTarget CheckListReservationReqDto dto, Reservation reservation){
        if(dto.getReservationTime() == null){
            dto.setReservationTime(reservation.getCreatedAt().toLocalTime().withSecond(0));
        }
    }

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

    @Mapping(source = "hospitalId", target = "hospitalId")
    ReservationHistory toReservationHistory(Reservation reservation, Long hospitalId);

    @Mapping(source = "childName", target = "childName")
    @Mapping(source = "childSsn", target = "childSsn")
    CheckHospitalListReservationResDto toHospitalListReservation(Reservation reservation, String childName, String childSsn);

    RedisDto toRedisDto(Reservation reservation);

    @Mapping(source = "childResDto.name", target = "childName")
    @Mapping(source = "reservation.hospital.name", target = "hospitalName")
    @Mapping(source = "childResDto.imageUrl", target = "profileImgUrl")
    @Mapping(source = "reservation.id", target = "id")
    ComesReservationResDto toTodayReservationResDto(Reservation reservation, ChildResDto childResDto);

    @AfterMapping
    default void setReservationTime(@MappingTarget ComesReservationResDto dto, Reservation reservation){
        if(reservation.getReservationTime() != null){
            dto.setReservationTime(reservation.getReservationTime().withSecond(0));
        } else {
            dto.setReservationTime(reservation.getCreatedAt().toLocalTime().withSecond(0));
        }
    }

    @Mapping(source = "childResDto.name", target = "childName")
    @Mapping(source = "hospitalName", target = "hospitalName")
    @Mapping(source = "childResDto.imageUrl", target = "profileImgUrl")
    @Mapping(source = "reservationHistory.id", target = "id")
    ComesReservationResDto toTodayReservationResDto(ReservationHistory reservationHistory, ChildResDto childResDto, String hospitalName);

    @Mapping(source = "reservation.hospital.name", target = "hospitalName")
    @Mapping(source = "member.name", target = "memberName")
    @Mapping(source = "reservation.hospital.hospitalImageUrl", target = "hospitalImgUrl")
    CheckListChildReservationResDto toChildListReservation(Reservation reservation, MemberFeignDto member, String doctorImgUrl);

    @AfterMapping
    default void setReservationTime(@MappingTarget CheckListChildReservationResDto dto, Reservation reservation){
        if(dto.getReservationTime() == null){
            dto.setReservationTime(reservation.getCreatedAt().toLocalTime().withSecond(0));
        }
    }
}
