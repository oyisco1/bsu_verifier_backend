package com.BS_Verifier.repositories;

import com.BS_Verifier.model.Biometric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometricRepository extends JpaRepository<Biometric, Integer> {
    // You can add custom queries if needed
}
