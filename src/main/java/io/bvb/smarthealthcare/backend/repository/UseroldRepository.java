package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.User;
import io.bvb.smarthealthcare.backend.entity.Userold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UseroldRepository extends JpaRepository<Userold, Long> {
    Userold findByEmailId(String emailId);
    boolean existsByEmailId(String emailId);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmergencyNumber(String phoneNumber);
}
