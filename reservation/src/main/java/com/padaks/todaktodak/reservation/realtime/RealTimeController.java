package com.padaks.todaktodak.reservation.realtime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/realtime")
// postman 테스트용 API
public class RealTimeController {

    private final RealTimeService realTimeService;
//    @PostMapping("/create")
//    public void update(String id, String data){
//        log.info("update");
//        realTimeService.update(id,data);
//    }
//    @GetMapping("/read")
//    public void read(String id){
//        log.info("read");
//        realTimeService.readDataOnce(id);
//    }
//    // 테스트를 위한 api
//    @PostMapping("/updateTurn")
//    public void updateTurn(@RequestBody List<WaitingTurnDto> dto){
//        realTimeService.updateWaitingLine(dto);
//    }
}
