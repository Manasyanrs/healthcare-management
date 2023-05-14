package com.example.healthcaremanagement.entity;

import com.example.healthcaremanagement.util.DoctorProfession;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    @Column(name = "specialty")
    private DoctorProfession profession;
    private String phoneNumber;
    @Column(name = "profile_pic")
    private String avatar;

}
