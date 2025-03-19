package com.BS_Verifier.controller;
import com.BS_Verifier.model.Biometric;
import com.BS_Verifier.model.FingerprintData;
import com.BS_Verifier.service.BiometricService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/biometrics")
public class BiometricController {


    private final BiometricService biometricService;

    public BiometricController(BiometricService biometricService) {
        this.biometricService = biometricService;
    }

    @GetMapping
    public List<Biometric> getAllBiometrics() {
        return biometricService.getAllBiometrics();
    }

    @GetMapping("/{id}")
    public Optional<Biometric> getBiometricById(@PathVariable int id) {
        return biometricService.getBiometricById(id);
    }

    @PostMapping
    public List<FingerprintData> createBiometric(@RequestBody List<FingerprintData> biometrics) {
        biometrics.forEach(b -> System.out.println("Received Biometric: " + b));
        return biometricService.saveBiometrics(biometrics);
    }

    @PutMapping("/{id}")
    public Biometric updateBiometric(@PathVariable int id, @RequestBody Biometric biometric) {
        return biometricService.updateBiometric(id, biometric);
    }

    @DeleteMapping("/{id}")
    public void deleteBiometric(@PathVariable int id) {
        biometricService.deleteBiometric(id);
    }
}
