package com.example.pharmaeye.models;

public class PharmaEye {

     /*
     * This is a class (Singleton class) helps in maintaining the state of this app.so that we can skip
     * passing data from one activity to another
     */

    private static PharmaEye pharmaEye;

    private String currentUser = "";
    private Patient patientBeingViewed = null;
    private Prescription prescriptionBeingViewed = null;

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public Patient getPatientBeingViewed() {
        return patientBeingViewed;
    }

    public void setPatientBeingViewed(Patient patientBeingViewed) {
        this.patientBeingViewed = patientBeingViewed;
    }

    public Prescription getPrescriptionBeingViewed() {
        return prescriptionBeingViewed;
    }

    public void setPrescriptionBeingViewed(Prescription prescriptionBeingViewed) {
        this.prescriptionBeingViewed = prescriptionBeingViewed;
    }

    private PharmaEye(){
    }

    public static PharmaEye getPharmaEyeInstance(){
        if(pharmaEye == null)
            pharmaEye = new PharmaEye();
        return pharmaEye;
    }



}
