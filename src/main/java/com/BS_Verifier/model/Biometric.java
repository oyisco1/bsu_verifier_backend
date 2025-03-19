package com.BS_Verifier.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "biometrics")
@Data
@Getter
@Setter
public class Biometric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Lob
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] template;
    @Enumerated(EnumType.STRING)
    private FingerPositions fingerType;
    private String enrollmentDate;
    private String reg_no;
    private String staff_name;
    private String uuid;

// No need to manually write getters and setters, Lombok handles them
}
