package com.BS_Verifier.model;

public class FingerprintData {
    private String template;
    private FingerPositions fingerType;
    private String enrollmentDate;
    private String reg_no;
    private String staff_name;
    private String uuid;

    public FingerprintData() {
    }

    public FingerprintData(String template, FingerPositions fingerType, String enrollmentDate, String reg_no, String staff_name, String uuid) {
        this.template = template;
        this.fingerType = fingerType;
        this.enrollmentDate = enrollmentDate;
        this.reg_no = reg_no;
        this.staff_name = staff_name;
        this.uuid = uuid;
    }

    public FingerPositions getFingerType() {
        return fingerType;
    }

    public void setFingerType(FingerPositions fingerType) {
        this.fingerType = fingerType;
    }

    public void setTemplate(String template) {
        this.template = template;
    }


    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    // Getters and setters (if needed)
    public String getTemplate() {
        return template;
    }


    public String getEnrollmentDate() {
        return enrollmentDate;
    }


    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "FingerprintData{" +
                "template='" + template + '\'' +
                ", fingerType='" + fingerType + '\'' +
                ", enrollmentDate='" + enrollmentDate + '\'' +
                ", regNo='" + reg_no + '\'' +
                ", staffName='" + staff_name + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}



