package com.padaks.todaktodak.child.controller;

import com.padaks.todaktodak.child.dto.ChildRegisterReqDto;
import com.padaks.todaktodak.child.dto.ChildUpdateReqDto;
import com.padaks.todaktodak.child.service.ChildService;
import com.padaks.todaktodak.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/update")
    public ResponseEntity<CommonResDto> updateChild(@RequestBody ChildUpdateReqDto dto){
        childService.updateChild(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"자녀 수정 성공",null),HttpStatus.OK);
    }

}
