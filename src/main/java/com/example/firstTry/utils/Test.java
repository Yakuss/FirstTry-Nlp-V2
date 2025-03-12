package com.example.firstTry.utils;

import com.example.firstTry.model.Doctor;
import com.example.firstTry.repository.DoctorRepository;
import com.example.firstTry.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tt")
public class Test {
    private final LocationSpecialityService appointmentNlpService;
    private final DoctorService doctorService;


    public Test(LocationSpecialityService appointmentNlpService, DoctorService doctorService) {
        this.appointmentNlpService = appointmentNlpService;
        this.doctorService = doctorService;
    }

    @PostMapping("/parse")
    public ParsedAppointmentRequest parseAppointment(@RequestBody String query) {
        return appointmentNlpService.parse(query);
    }

    @GetMapping("/jin/{id}")
    public Doctor getdoc(@PathVariable Long id) {
        return doctorService.jibou(id);
    }

}
