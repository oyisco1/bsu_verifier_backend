package com.BS_Verifier.service;;
import com.BS_Verifier.model.Biometric;
import com.BS_Verifier.model.FingerprintData;
import com.BS_Verifier.repositories.BiometricRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BiometricService {
    private final BiometricRepository biometricRepository;
    public BiometricService(BiometricRepository biometricRepository) {
        this.biometricRepository = biometricRepository;
    }

    public List<Biometric> getAllBiometrics() {
        return biometricRepository.findAll();
    }

    public Optional<Biometric> getBiometricById(int id) {
        return biometricRepository.findById(id);
    }

    public List<FingerprintData> saveBiometrics(List<FingerprintData> biometrics) {
        // Map FingerprintData to Biometric entities
        List<Biometric> biometricEntities = biometrics.stream().map(this::convertToEntity).collect(Collectors.toList());
        // Save all the entities
        List<Biometric> savedEntities = biometricRepository.saveAll(biometricEntities);
        // Map the saved entities back to FingerprintData
        return savedEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private Biometric convertToEntity(FingerprintData data) {
        Biometric biometric = new Biometric();
        biometric.setTemplate(data.getTemplate().getBytes()); // Assuming template is Base64 encoded
        biometric.setFingerType(data.getFingerType());
        biometric.setEnrollmentDate(data.getEnrollmentDate());
        biometric.setReg_no(data.getReg_no());
        biometric.setStaff_name(data.getStaff_name());
        biometric.setUuid(data.getUuid());
        return biometric;
    }

    private FingerprintData convertToDto(Biometric biometric) {
        return new FingerprintData(
                new String(biometric.getTemplate()), // Assuming conversion back to Base64 string
                biometric.getFingerType(),
                biometric.getEnrollmentDate(),
                biometric.getReg_no(),
                biometric.getStaff_name(),
                biometric.getUuid()
        );
    }
    public void deleteBiometric(int id) {
        biometricRepository.deleteById(id);
    }

    public Biometric updateBiometric(int id, Biometric biometric) {
        return biometricRepository.findById(id)
                .map(existingBiometric -> {
                    existingBiometric.setTemplate(biometric.getTemplate());
                    existingBiometric.setFingerType(biometric.getFingerType());
                    existingBiometric.setEnrollmentDate(biometric.getEnrollmentDate());
                    existingBiometric.setReg_no(biometric.getReg_no());
                    existingBiometric.setStaff_name(biometric.getStaff_name());
                    existingBiometric.setUuid(biometric.getUuid());
                    return biometricRepository.save(existingBiometric);
                }).orElseThrow(() -> new RuntimeException("Biometric not found"));
    }
}
