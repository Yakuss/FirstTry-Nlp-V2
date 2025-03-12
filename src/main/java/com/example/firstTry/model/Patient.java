package com.example.firstTry.model;

import com.example.firstTry.Enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "patients")
@Data
@EqualsAndHashCode(callSuper = false)
public class Patient extends User {
    private LocalDate dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "patient" ,fetch = FetchType.EAGER)
    @JsonManagedReference("patient-appointments") // Child side
    private Set<Appointment> appointments = new HashSet<>();

    public Patient() {
        this.setRole(Role.ROLE_PATIENT);
    }

    @Override
    public Role getRole() {
        return Role.ROLE_PATIENT;
    }



    @Override
    public int hashCode() {
        return Objects.hash(getId(),getFirstName(), getLastName(), getEmail());
    }
}
