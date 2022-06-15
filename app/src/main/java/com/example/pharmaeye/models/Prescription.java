package com.example.pharmaeye.models;

import java.util.Date;

public class Prescription {

    private String id;
    private String patientId;
    private String drugName;
    private String quantity;
    private Date dueBy;
    private String prescribedBy;
    private Date prescribedOn;

    public Prescription() {
    }

    public Prescription(String patientId, String drugName, String quantity, Date dueBy, String prescribedBy) {
        this.patientId = patientId;
        this.drugName = drugName;
        this.quantity = quantity;
        this.dueBy = dueBy;
        this.prescribedBy = prescribedBy;
        this.prescribedOn = new Date();
    }


    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDrugName() {
        return drugName;
    }

    public String getQuantity() {
        return quantity;
    }

    public Date getDueBy() {
        return dueBy;
    }

    public void setPrescribedBy(String prescribedBy) {
        this.prescribedBy = prescribedBy;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setDueBy(Date dueBy) {
        this.dueBy = dueBy;
    }

    public Date getPrescribedOn() {
        return prescribedOn;
    }

    public String getPrescribedBy() {
        return prescribedBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id='" + id + '\'' +
                ", patientId='" + patientId + '\'' +
                ", drugName='" + drugName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", dueBy=" + dueBy +
                ", prescribedBy='" + prescribedBy + '\'' +
                ", prescribedOn=" + prescribedOn +
                '}';
    }
}
