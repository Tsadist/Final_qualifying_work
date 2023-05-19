package com.example.FQW.service;

import org.springframework.stereotype.Service;

@Service
public class TimeToString {

    public String numberToTime (float time) {
        float minutes = time * 60;
        int min = (int)minutes % 60;
        int hour = (int)minutes / 60;
        if (hour == 1){
            return String.format("%d час %d минут", hour, min);
        }else if (hour > 1 && hour < 5){
            return String.format("%d часа %d минут", hour, min);
        }
        return String.format("%d часов %d минут", hour, min);
    }
}
