package com.example.pharmaeye.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Patient {

    private String id;
    private String name;
    private String email;
    private String gender;
    private Date DOB;
    private String phoneNumber;
    private String postalAddress;
    private String city;
    private String province;
    private String healthCardNumber;
    private Date createdOn;


    public Patient() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }

    public Patient(String name, String email, String gender, Date DOB,
                   String phoneNumber, String postalAddress, String city, String province,
                   String healthCardNumber) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.DOB = DOB;
        this.phoneNumber = phoneNumber;
        this.postalAddress = postalAddress;
        this.city = city;
        this.province = province;
        this.healthCardNumber = healthCardNumber;
        this.createdOn = new Date();
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public Date getDOB() {
        return DOB;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getId() {
        return id;
    }

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", DOB=" + DOB +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", healthCardNumber='" + healthCardNumber + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getCreatedOn() {
        return createdOn;
    }
}
