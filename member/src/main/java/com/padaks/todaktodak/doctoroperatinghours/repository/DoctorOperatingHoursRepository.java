package com.padaks.todaktodak.doctoroperatinghours.repository;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.doctoroperatinghours.domain.DoctorOperatingHours;
import com.padaks.todaktodak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorOperatingHoursRepository extends JpaRepository<DoctorOperatingHours, Long> {
    List<DoctorOperatingHours> findByMemberId(Long memberId);
    List<DoctorOperatingHours> findByMemberIdAndDeletedAtIsNull(Long memberId);

    Optional<DoctorOperatingHours> findByIdAndDeletedAtIsNull(Long id);

    default DoctorOperatingHours findByIdOrThrow(Long id) {
        return findByIdAndDeletedAtIsNull(id).orElseThrow(()-> new EntityNotFoundException("해당하는 의사가 없습니다."));
    }

    //의사에 속한 모든 영업시간 가져오
    List<DoctorOperatingHours> findAllByMember(Member member);
    List<DoctorOperatingHours> findAllByMemberAndDeletedAtIsNull(Member member);

    // 오늘 요일과 untact 필터, 삭제되지 않은 운영 시간을 기준으로 멤버 찾기
    @Query("SELECT d.member FROM DoctorOperatingHours d WHERE d.dayOfWeek = :dayOfWeek AND d.untact = true AND d.deletedAt IS NULL")
    List<Member> findUntactMembersByDayOfWeekAndDeletedAtIsNull(DayOfHoliday dayOfWeek);

    Optional<DoctorOperatingHours> findByMemberIdAndDayOfWeekAndDeletedAtIsNull(Long memberId, DayOfHoliday dayOfWeek);
}
