package com.example.healthcaremanagement.repository;

import com.example.healthcaremanagement.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query(value = "select date_time from appointments where date_time LIKE %?1%", nativeQuery = true)
    List<String> findAllByDateTimeLike(LocalDate date);
//    List<Appointment> findAllByDateTimeStartingWith(LocalDate date);

}
