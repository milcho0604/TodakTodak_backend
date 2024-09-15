package com.padaks.todaktodak.child.controller;

import com.padaks.todaktodak.child.dto.ChildRegisterReqDto;
import com.padaks.todaktodak.child.service.ChildService;
import com.padaks.todaktodak.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChildController {
    private final ChildService childService;
    @PostMapping("/child")
    public ResponseEntity<CommonResDto> registerChild(@RequestBody ChildRegisterReqDto dto){
        childService.createChild(dto.getName(), dto.getSsn());
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED,"자녀등록 성공",null),HttpStatus.CREATED);
    }
}
