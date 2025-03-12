package com.example.firstTry.controller;

import com.example.firstTry.model.Admin;
import com.example.firstTry.model.Doctor;
import com.example.firstTry.model.Notification;
import com.example.firstTry.model.Patient;
import com.example.firstTry.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<Notification>> getAdminNotifications(Authentication authentication) {
        Admin admin = (Admin) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getAdminNotifications(admin));
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor")
    public ResponseEntity<List<Notification>> getDoctorNotifications(Authentication authentication) {
        Doctor doctor = (Doctor) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getDoctorNotifications(doctor));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient")
    public ResponseEntity<List<Notification>> getPatientNotifications(Authentication authentication) {
        Patient patient = (Patient) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getPatientNotifications(patient));
    }

    // Ajouter un nouveau endpoint dans NotificationController.java
    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    // NotificationController.java
    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<Long> getUnreadNotificationsCount(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof Admin) {
            return ResponseEntity.ok(notificationService.getUnreadAdminCount((Admin) principal));
        } else if (principal instanceof Doctor) {
            return ResponseEntity.ok(notificationService.getUnreadDoctorCount((Doctor) principal));
        } else if (principal instanceof Patient) {
            return ResponseEntity.ok(notificationService.getUnreadPatientCount((Patient) principal));
        }
        return ResponseEntity.badRequest().build();
    }
}
