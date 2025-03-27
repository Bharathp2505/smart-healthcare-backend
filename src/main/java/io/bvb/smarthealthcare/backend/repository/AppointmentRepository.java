package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    boolean existsByTimeSlot(TimeSlot timeSlot);
}
