package com.padaks.todaktodak.doctoroperatinghours.service;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.common.feign.ReservationFeignClient;
import com.padaks.todaktodak.doctoroperatinghours.domain.DoctorOperatingHours;
import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursReqDto;
import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursResDto;
import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursSimpleResDto;
import com.padaks.todaktodak.doctoroperatinghours.repository.DoctorOperatingHoursRepository;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import com.padaks.todaktodak.member.dto.HospitalOperatingHoursResDto;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DoctorOperatingHoursService {
    private final DoctorOperatingHoursRepository doctorOperatingHoursRepository;
    private final MemberRepository memberRepository;
    private final ReservationFeignClient reservationFeignClient;

    public void addOperatingHours(Long doctorId, List<DoctorOperatingHoursReqDto> dtos) {
        //ContextHolder로 memberEmail 찾기(hospital admin)
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member hospitalAdmin = memberRepository.findByMemberEmailOrThrow(memberEmail);
        List<HospitalOperatingHoursResDto> hospitalTime = reservationFeignClient.getHospitalTime(hospitalAdmin.getHospitalId()); //Feign으로 해당 병원의 영업시간 가져옴

        // controller에서 요청한 id로 해당의사 찾아야함
        Member doctor = memberRepository.findById(doctorId).orElseThrow(() -> new EntityNotFoundException("의사를 찾을 수 없습니다. ID : " + doctorId));

        // hospitalAdmin의 병원에 소속된 의사의 영업시간만 등록 가능
        if (!hospitalAdmin.getRole().equals(Role.HospitalAdmin)) {
            throw new IllegalArgumentException("병원 admin만 근무시간을 등록 할 수 있습니다.");
        }
        if (!hospitalAdmin.getHospitalId().equals(doctor.getHospitalId())) {
            throw new IllegalArgumentException("다른 병원의 의사의 근무시간은 등록할 수 없습니다.");
        }

        // 의사의 모든 영업시간 불러옴
        List<DoctorOperatingHours> existingOperatingHours = doctorOperatingHoursRepository.findAllByMemberAndDeletedAtIsNull(doctor);

        // 기존 의사 영업시간 요일 저장
        Set<DayOfHoliday> existingDays = existingOperatingHours.stream()
                .map(DoctorOperatingHours::getDayOfWeek)
                .collect(Collectors.toSet());

        for (DoctorOperatingHoursReqDto hoursReqDto : dtos) {
            // 중복 저장 방지
            if (existingDays.contains(hoursReqDto.getDayOfWeek())) {
                throw new IllegalArgumentException("근무시간 중복 저장 불가 : " + hoursReqDto.getDayOfWeek() + "요일에 영업시간이 이미 등록 되어 있습니다.");
            }
            // openTime과 closeTime 유효성 검사
            if (hoursReqDto.getOpenTime() != null && hoursReqDto.getCloseTime() != null && hoursReqDto.getOpenTime().isAfter(hoursReqDto.getCloseTime())) {
                throw new IllegalArgumentException("영업 시작 시간이 종료 시간보다 늦을 수 없습니다.");
            }

            HospitalOperatingHoursResDto hospitalBreakTime = hospitalTime.stream()
                    .filter(time -> time.getDayOfWeek().equals(hoursReqDto.getDayOfWeek()))
                    .findFirst()
                    .orElse(null);

            DoctorOperatingHoursReqDto updatedHoursReqDto = hoursReqDto; // 새로운 변수

            if (hospitalBreakTime != null) {
                updatedHoursReqDto = DoctorOperatingHoursReqDto.builder()
                        .dayOfWeek(hoursReqDto.getDayOfWeek())
                        .openTime(hoursReqDto.getOpenTime())
                        .closeTime(hoursReqDto.getCloseTime())
                        .untact(hoursReqDto.getUntact())
                        .breakStart(hospitalBreakTime.getBreakStart())
                        .breakEnd(hospitalBreakTime.getBreakEnd())
                        .build();
            }

            DoctorOperatingHours operatingHours = DoctorOperatingHoursReqDto.toEntity(doctor, updatedHoursReqDto);
            doctorOperatingHoursRepository.save(operatingHours);
        }
    }


    public List<DoctorOperatingHoursSimpleResDto> getOperatingHoursByDoctorId(Long doctorId){
        List<DoctorOperatingHours> operatingHoursList = doctorOperatingHoursRepository.findByMemberIdAndDeletedAtIsNull(doctorId);
        return operatingHoursList.stream()
                .map(hours -> new DoctorOperatingHoursSimpleResDto(
                        hours.getId(),
                        hours.getMember().getName(),
                        hours.getDayOfWeek(),
                        hours.getOpenTime(),
                        hours.getCloseTime(),
                        hours.getUntact())
                ).collect(Collectors.toList());
    }

    public void updateOperatingHours(Long operatingHoursId, DoctorOperatingHoursReqDto dto){
        //ContextHolder로 memberEmail 찾기
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member hospitalAdmin = memberRepository.findByMemberEmailOrThrow(memberEmail);
        //근무시간 변경하려는 의사
        DoctorOperatingHours hours =doctorOperatingHoursRepository.findById(operatingHoursId).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 근무시간 입니다."));
        Member doctor = memberRepository.findById(hours.getMember().getId()).orElseThrow(()-> new EntityNotFoundException("의사를 찾을 수 없습니다. ID : " + hours.getMember().getId()));

        // hospitalAdmin의 병원에 소속된 의사의 영업시간만 등록 가능
        if (!hospitalAdmin.getRole().equals(Role.HospitalAdmin)){
            throw new IllegalArgumentException("병원 admin만 근무시간을 수정 할 수 있습니다.");
        }
        if (!hospitalAdmin.getHospitalId().equals(doctor.getHospitalId())){
            throw new IllegalArgumentException("다른 병원의 의사의 근무시간은 수정할 수 없습니다.");
        }
        if (!hours.getMember().getId().equals(doctor.getId())){
            throw new EntityNotFoundException("수정하려는 의사와 동일 하지 않습니다.");
        }

        hours.updateOperatingHours(dto);
    }

    public void deleteOperatingHours(Long operatingHoursId){
        //ContextHolder로 memberEmail 찾기
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member hospitalAdmin = memberRepository.findByMemberEmailOrThrow(memberEmail);

        DoctorOperatingHours hours =doctorOperatingHoursRepository.findById(operatingHoursId).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 근무시간 입니다."));

        if (!hospitalAdmin.getRole().equals(Role.HospitalAdmin)){
            throw new IllegalArgumentException("병원 admin만 근무시간을 삭제 할 수 있습니다.");
        }
        if (!hospitalAdmin.getHospitalId().equals(hours.getMember().getHospitalId())){
            throw new IllegalArgumentException("다른 병원의 의사의 근무시간은 삭제할 수 없습니다.");
        }

        hours.setDeletedTimeAt(LocalDateTime.now());
    }

    public DoctorOperatingHoursResDto getTodayOperatingHourByDoctorId(Long id) {
        // 오늘의 요일 가져오기
        DayOfHoliday today = getTodayAsDayOfHoliday();

        // 요일과 의사 ID로 운영 시간 조회
        DoctorOperatingHours operatingHours = doctorOperatingHoursRepository.findByMemberIdAndDayOfWeekAndDeletedAtIsNull(id, today).orElseThrow(()-> new EntityNotFoundException("해당 운영시간이 없습니다."));

        return new DoctorOperatingHoursResDto().fromEntity(operatingHours);
    }

    private DayOfHoliday getTodayAsDayOfHoliday() {
        // 오늘의 요일 가져오기
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        // DayOfWeek -> DayOfHoliday로 변환
        switch (dayOfWeek) {
            case MONDAY:
                return DayOfHoliday.Monday;
            case TUESDAY:
                return DayOfHoliday.Tuesday;
            case WEDNESDAY:
                return DayOfHoliday.Wednesday;
            case THURSDAY:
                return DayOfHoliday.Thursday;
            case FRIDAY:
                return DayOfHoliday.Friday;
            case SATURDAY:
                return DayOfHoliday.Saturday;
            case SUNDAY:
                return DayOfHoliday.Sunday;
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }
    }
}
