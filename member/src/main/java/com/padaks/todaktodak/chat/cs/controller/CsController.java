package com.padaks.todaktodak.chat.cs.controller;

import com.padaks.todaktodak.chat.cs.domain.Cs;
import com.padaks.todaktodak.chat.cs.domain.CsStatus;
import com.padaks.todaktodak.chat.cs.dto.CsCreateReqDto;
import com.padaks.todaktodak.chat.cs.dto.CsListResDto;
import com.padaks.todaktodak.chat.cs.dto.CsResDto;
import com.padaks.todaktodak.chat.cs.dto.CsUpdateReqDto;
import com.padaks.todaktodak.chat.cs.service.CsService;
import com.padaks.todaktodak.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cs")
public class CsController {

    private final CsService csService;

    // 회원 별 CS리스트 조회
    @GetMapping("/list/member/{memberId}")
    public ResponseEntity<?> getCsByMemberId(@PathVariable Long memberId){
        List<CsResDto> csList = csService.getCsByMemberId(memberId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "회원 별 CS 내역 조회", csList), HttpStatus.OK);
    }

    // CS 생성
    @PostMapping("/create")
    public ResponseEntity<?> createCs(@RequestBody CsCreateReqDto dto){
        Cs cs = csService.createCs(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "CS 생성완료", cs.getId()), HttpStatus.CREATED);
    }

    // CS 수정
    @PostMapping("/update")
    public ResponseEntity<?> updateCs(@RequestBody CsUpdateReqDto dto){
        Cs cs = csService.updateCs(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "CS 내역 수정완료", cs.getId()), HttpStatus.OK);
    }

    // CS id로 CS 조회
    @GetMapping("/detail/cs-id/{id}")
    public ResponseEntity<?> detailCsByCsId(@PathVariable Long id){
        CsResDto csResDto = csService.detailCsByCsId(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "CS내역 조회 성공 : CS id로 조회", csResDto), HttpStatus.OK);
    }

    // 채팅방 id로 CS 조회
    @GetMapping("/detail/chatroom-id/{chatRoomId}")
    public ResponseEntity<?> detailCsByChatRoomId(@PathVariable Long chatRoomId){
        List<CsResDto> csResDtoList = csService.detailCsByChatRoomId(chatRoomId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "CS내역 조회 성공 : 채팅방 id로 조회", csResDtoList), HttpStatus.OK);
    }

    // CS 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCs(@PathVariable Long id){
        csService.deleteCs(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "CS 삭제 성공", null), HttpStatus.OK);
    }


    // Cs 리스트 검색 및 필터링
    @GetMapping("/list")
    public ResponseEntity<Page<CsListResDto>> paymentListSearch(
            @RequestParam(required = false) String query, // 검색어 (impUid나 memberEmail에 포함될 수 있음)
            @RequestParam(required = false) CsStatus csStatus, // 필터링용 결제 방식 (정기, 단건)
            Pageable pageable) {

        // 결제 리스트를 검색어와 필터링으로 조회
        Page<CsListResDto> csListSearch = csService.csListSearch(query, csStatus, pageable);

        return ResponseEntity.ok(csListSearch);
    }
}
