package com.example.firstTry.utils;

import com.example.firstTry.dto.AvailabilityResponse;
import com.example.firstTry.model.Doctor;
import com.example.firstTry.repository.DoctorRepository;
import com.example.firstTry.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
//@RequiredArgsConstructor
//public class AppointmentFacade {
//    private final DoctorRepository doctorRepo;
//    private final AppointmentNlpService nlpService;
//    private final AppointmentService appointmentService;
//
//    public AvailabilityResponse processNaturalLanguageRequest(String query) {
//        NlpParsedResult parsed = nlpService.parseQuery(query);
//
//
//
//        List<Doctor> doctors = doctorRepo.findBySpecializationName(parsed.getSpecialty());
//        List<AvailabilitySlot> allSlots = new ArrayList<>();
//
//        for (Doctor doctor : doctors) {
//            AvailabilityResponse response = appointmentService.getAvailability(
//                    doctor.getId(),
//                    parsed.getDate(),
//                    parsed.getDurationMinutes()
//            );
//
//            response.getAvailableSlots().stream()
//                    .filter(slot -> isTimeMatch(slot, parsed.getTime()))
//                    .findFirst()
//                    .ifPresent(slot -> allSlots.add(
//                            new AvailabilitySlot(doctor, slot.getStartTime(), parsed.getDurationMinutes())
//                    ));
//        }
//
//        return new AvailabilityResponse(
//                parsed.getDate(),
//                allSlots.stream().map(AvailabilitySlot::getTime).toList(),
//                Collections.emptyList() // Breaks not needed here
//        );
//    }
//
//    private boolean isTimeMatch(LocalTime slotTime, LocalTime requestedTime) {
//        return slotTime.equals(requestedTime) ||
//                slotTime.plusMinutes(30).equals(requestedTime);
//    }
//}