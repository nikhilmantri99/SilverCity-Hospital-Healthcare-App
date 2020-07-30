package com.example.SilverCity.models;

public class Consultation {
    String doctorName;
    String doctorEmail;
    String patientEmail;
    String disease;
    String date;
    String price;
    String prescription;
    String prescription_image_name=null;

    public Consultation() {
    }

    public Consultation(String doctorName, String doctorEmail, String patientEmail, String disease, String date, String price, String prescription) {
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.patientEmail = patientEmail;
        this.disease = disease;
        this.date = date;
        this.price = price;
        this.prescription = prescription;
    }

    public void set_prescription_image_name(String temp){
        prescription_image_name=temp;
    }

    public String get_prescription_image_name(){
        return prescription_image_name;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
}
