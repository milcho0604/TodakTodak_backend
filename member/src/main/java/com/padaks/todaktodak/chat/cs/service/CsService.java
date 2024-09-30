package com.padaks.todaktodak.chat.cs.service;

import com.padaks.todaktodak.chat.cs.domain.Cs;
import com.padaks.todaktodak.chat.cs.dto.CsResDto;
import com.padaks.todaktodak.chat.cs.repository.CsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsService {
    private final CsRepository csRepository;

    // 회원별 CS 조회
    public List<CsResDto> getCsByMemberId(Long memberId) {
        List<Cs> csList = csRepository.findByMemberId(memberId);
        return csList.stream()
                .map(CsResDto::fromEntity)
                .collect(Collectors.toList());
    }
}
