package com.padaks.todaktodak.reservation.realtime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/realtime")
public class RealTimeController {

    private final RealTimeService realTimeService;
    @PostMapping("/create")
    public void update(String id, String data){
        log.info("update");
        realTimeService.update(id,data);
    }
    @GetMapping("/read")
    public void read(String id){
        log.info("read");
        realTimeService.readDataOnce(id);
    }
}
