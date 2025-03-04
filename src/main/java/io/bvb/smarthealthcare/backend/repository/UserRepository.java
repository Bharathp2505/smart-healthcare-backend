package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailId(String emailId);
    boolean existsByEmailId(String emailId);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmergencyNumber(String phoneNumber);
}
