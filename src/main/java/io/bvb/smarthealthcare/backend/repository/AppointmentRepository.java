package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT appointment FROM Appointment appointment, TimeSlot timeslot WHERE appointment.timeSlot.id = timeslot.id AND timeslot.doctor.id = :doctorId AND timeslot.date = :date")
    List<Appointment> findAppointmentsByDoctorIdAndDate(@Param("doctorId") Long doctorId, LocalDate date);

    boolean existsByTimeSlot(TimeSlot timeSlot);
}
