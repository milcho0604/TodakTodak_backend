package com.padaks.todaktodak.reservation.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationTimeSlot {

    public static List<LocalTime> timeSlots(){
        List<LocalTime> timeSlots = new ArrayList<>();

        // 9시부터 11시59분까지
        for (LocalTime time = LocalTime.of(9, 0); time.isBefore(LocalTime.of(12, 0)); time = time.plusMinutes(30)) {
            timeSlots.add(time);
        }

        // 13시부터 17시59분까지
        for (LocalTime time = LocalTime.of(13, 0); time.isBefore(LocalTime.of(18, 0)); time = time.plusMinutes(30)) {
            timeSlots.add(time);
        }

        // 18시부터 22시까지
        for (LocalTime time = LocalTime.of(18, 0); time.isBefore(LocalTime.of(22, 0)); time = time.plusMinutes(30)) {
            timeSlots.add(time);
        }

        return timeSlots;
    }
}
