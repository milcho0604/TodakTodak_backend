package com.padaks.todaktodak.reservation.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "reservation-service", configuration = FeignClient.class)
public class ReservationFeign {

}
