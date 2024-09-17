package com.padaks.todaktodak.child.controller;

import com.padaks.todaktodak.child.dto.ChildRegisterReqDto;
import com.padaks.todaktodak.child.dto.ChildResDto;
import com.padaks.todaktodak.child.dto.ChildShareReqDto;
import com.padaks.todaktodak.child.dto.ChildUpdateReqDto;
import com.padaks.todaktodak.child.service.ChildService;
import com.padaks.todaktodak.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/child")
public class ChildController {
    private final ChildService childService;
    @PostMapping("/create")
    public ResponseEntity<CommonResDto> registerChild(@RequestBody ChildRegisterReqDto dto){
        childService.createChild(dto.getName(), dto.getSsn());
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED,"자녀 등록 성공",null),HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<CommonResDto> myChild(){
        List<ChildResDto> childList = childService.myChild();
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"자녀 조회 성공",childList),HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<CommonResDto> updateChild(@RequestBody ChildUpdateReqDto dto){
        childService.updateChild(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"자녀 수정 성공",null),HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<CommonResDto> updateChild(@PathVariable Long id){
        childService.deleteChild(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"자녀 삭제 성공",null),HttpStatus.OK);
    }

    @PostMapping("/share")
    public ResponseEntity<CommonResDto> shareChild(@RequestBody ChildShareReqDto dto){
        childService.shareChild(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"자녀 공유 성공",null),HttpStatus.OK);
    }
}
