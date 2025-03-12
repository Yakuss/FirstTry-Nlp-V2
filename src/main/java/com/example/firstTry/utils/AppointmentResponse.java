package com.example.firstTry.utils;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AppointmentResponse {
    private LocalDate date;
    private List<LocalTime> availableSlots;
}
