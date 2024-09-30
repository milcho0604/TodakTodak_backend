package com.padaks.todaktodak.chat.cs.repository;

import com.padaks.todaktodak.chat.cs.domain.Cs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;




@Repository
public interface CsRepository extends JpaRepository<Cs, Long> {
    // memberId로 CS 목록 조회
    @Query("SELECT cs FROM Cs cs JOIN cs.chatRoom cr WHERE cr.member.id = :memberId")
    List<Cs> findByMemberId(@Param("memberId") Long memberId);

    Optional<Cs> findByIdAndDeletedAtIsNull(Long id);

    default Cs findByIdOrThrow(Long id){
        return findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("CS가 존재하지 않습니다."));
    }
}
