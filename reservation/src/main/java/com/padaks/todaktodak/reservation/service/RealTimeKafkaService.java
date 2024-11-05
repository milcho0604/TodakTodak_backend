package com.padaks.todaktodak.reservation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.reservation.realtime.RealTimeService;
import com.padaks.todaktodak.reservation.realtime.WaitingTurnDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealTimeKafkaService {

    private final ObjectMapper objectMapper;
    private final RealTimeService realTimeService;

    @KafkaListener(topics = "immediate-realtime", containerFactory = "realTimeKafkaContainerFactory")
    public void immediateRealTim(String message, Acknowledgment acknowledgment){
        try {
            WaitingTurnDto dto =objectMapper.readValue(message, WaitingTurnDto.class);
            System.out.println(dto.toString());
            realTimeService.update(dto);

            acknowledgment.acknowledge();
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
