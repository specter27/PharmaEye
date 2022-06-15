package com.example.pharmaeye.repositories;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.Prescription;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientRepository {

    /**
     * Repository classes are responsible for the following tasks:
     *
     * Exposing data to the rest of the app.
     * Centralizing changes to the data.
     * Resolving conflicts between multiple data sources.
     * Abstracting sources of data from the rest of the app.
     * Containing business logic.
     */

    // Access a Cloud Firestore instance
    private final FirebaseFirestore DB;

    private final String COLLECTION_PATIENT = "Pharmacy-Patient";

    /*
    * private String name;
    private String email;
    private String gender;
    private Date DOB;
    private String phoneNumber;
    private String postalAddress;
    private String city;
    private String province;
    private String healthCardNumber;
    private Date createdOn;*/

    private final String FIELD_NAME = "name";
    private final String FIELD_EMAIL = "email";
    private final String FIELD_GENDER = "gender";
    private final String FIELD_DOB = "DOB";
    private final String FIELD_PHONE = "phoneNumber";
    private final String FIELD_ADDRESS = "postalAddress";
    private final String FIELD_CITY = "city";
    private final String FIELD_PROVINCE = "province";
    private final String FIELD_HEALTH_CARD = "healthCardNumber";
    private final String FIELD_CREATED_ON = "createdOn";


    public MutableLiveData<List<Patient>> fetchedPatientList = new MutableLiveData<>();
    public MutableLiveData<List<Patient>> searchedPatientList = new MutableLiveData<>();
    public MutableLiveData<Boolean> addPatientCompletionStatus = new MutableLiveData<>();
    public MutableLiveData<Boolean> updatePatientCompletionStatus = new MutableLiveData<>();
    public MutableLiveData<Boolean> deletionStatusFromRepository = new MutableLiveData<>();



    public PatientRepository() {
        DB = FirebaseFirestore.getInstance();
    }

    public void addPatient(Patient patientToAdd, View view){
        Log.d("TAG","patient to be saved in the repository: "+patientToAdd);

        try{
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_NAME, patientToAdd.getName());
            data.put(FIELD_EMAIL, patientToAdd.getEmail());
            data.put(FIELD_GENDER, patientToAdd.getGender());
            data.put(FIELD_DOB, patientToAdd.getDOB());
            data.put(FIELD_PHONE, patientToAdd.getPhoneNumber());
            data.put(FIELD_ADDRESS, patientToAdd.getPostalAddress());
            data.put(FIELD_CITY, patientToAdd.getCity());
            data.put(FIELD_PROVINCE, patientToAdd.getProvince());
            data.put(FIELD_HEALTH_CARD, patientToAdd.getHealthCardNumber());
            data.put(FIELD_CREATED_ON, patientToAdd.getCreatedOn());

            // add():- create a new document with randomly generated ID
            DB.collection(COLLECTION_PATIENT)
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.e("TAG", "onSuccess: Document successfully created with ID "
                                    + documentReference.getId() );
                            // TODO: Adding the data to the LiveData variable(addPatientCompletionStatus)
                            //  in order to notify the changes in the UI
                            addPatientCompletionStatus.setValue(true);
                            Snackbar.make(view, "SUCCESS: Patient Added", Snackbar.LENGTH_LONG)
                                    .show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure: Error while creating document" + e.getLocalizedMessage() );
                    // TODO: Adding the data to the LiveData variable(addPatientCompletionStatus)
                    //  in order to notify the changes in the UI
                    addPatientCompletionStatus.setValue(true);
                    Snackbar.make(view, "ERROR: Unable to Add Patient", Snackbar.LENGTH_LONG)
                            .show();

                }
            });

        }catch (Exception ex){
            Log.e("TAG", "addPatient: " + ex.getLocalizedMessage() );
        }

    }

    // getPatientList(): will give the list of all the patients registered with the pharmacy
    public void getPatientList() {

        try {
            DB.collection(COLLECTION_PATIENT)
//                    .whereEqualTo(FIELD_NAME, "John")
                    .orderBy(FIELD_NAME, Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Patient> patientList = new ArrayList<>();

                            if (queryDocumentSnapshots.isEmpty()) {
                                Log.e("TAG", "onSuccess: No data retrieved");
                            } else {
                                Log.e("TAG", "onSuccess: queryDocumentSnapshots" + queryDocumentSnapshots.getDocumentChanges());

                                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                    Patient currentPatient = documentChange.getDocument().toObject(Patient.class);
                                    currentPatient.setId(documentChange.getDocument().getId());
                                    patientList.add(currentPatient);
                                    Log.e("TAG", "onSuccess: currentPatient " + currentPatient.toString());
                                }
                                // TODO: Adding the fetchedPatientList to the LiveData variable( in order to notify
                                //  the changes in the UI)
                                fetchedPatientList.postValue(patientList);
                            }
                        }
                    });
        } catch (Exception ex) {
            Log.e("TAG", "getAllPatients: " + ex.getLocalizedMessage());

        }

    }

    public void deletePatientByDocIc(String docID){
        try{
            DB.collection(COLLECTION_PATIENT)
                    .document(docID)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.e("TAG", "onSuccess: document successfully deleted");

                            // TODO: Adding the data to the LiveData variable(deletionStatus)
                            //  in order to notify the changes in the UI
                            deletionStatusFromRepository.setValue(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "onFailure: Unable to delete document" + e.getLocalizedMessage());

                            // TODO: Adding the data to the LiveData variable(deletionStatus)
                            //  in order to notify the changes in the UI
                            deletionStatusFromRepository.setValue(false);
                        }
                    });

        }catch (Exception ex){
            Log.e("TAG", "deletePatient: " + ex.getLocalizedMessage() );
        }
    }


    public void updatePatient(Patient patientToUpdate){

        Map<String, Object> data = new HashMap<>();
        data.put(FIELD_NAME, patientToUpdate.getName());
        data.put(FIELD_EMAIL, patientToUpdate.getEmail());
        data.put(FIELD_GENDER, patientToUpdate.getGender());
        data.put(FIELD_DOB, patientToUpdate.getDOB());

        data.put(FIELD_ADDRESS, patientToUpdate.getPostalAddress());
        data.put(FIELD_CITY, patientToUpdate.getCity());
        data.put(FIELD_PROVINCE, patientToUpdate.getProvince());

        data.put(FIELD_PHONE, patientToUpdate.getPhoneNumber());
        data.put(FIELD_HEALTH_CARD, patientToUpdate.getHealthCardNumber());

        try{
            DB.collection(COLLECTION_PATIENT)
                    .document(patientToUpdate.getId())
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.e("TAG", "onSuccess: document successfully updated");
                            // TODO: Adding the data to the LiveData variable(updatePatientCompletionStatus)
                            //  in order to notify the changes in the UI
                            updatePatientCompletionStatus.setValue(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "onFailure: Unable to update document" + e.getLocalizedMessage());
                            // TODO: Adding the data to the LiveData variable(updatePatientCompletionStatus)
                            //  in order to notify the changes in the UI
                            updatePatientCompletionStatus.setValue(false);
                        }
                    });
        }catch (Exception ex){
            Log.e("TAG", "updatePatient: " + ex.getLocalizedMessage() );
        }
    }


    public void searchPatientById(String searchId) {

        try {
            DB.collection(COLLECTION_PATIENT)
                    .whereEqualTo(FIELD_HEALTH_CARD, searchId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Patient> patientList = new ArrayList<>();

                            if (queryDocumentSnapshots.isEmpty()) {
                                Log.e("TAG", "onSuccess: No data retrieved");
                            } else {
                                Log.e("TAG", "onSuccess: queryDocumentSnapshots" + queryDocumentSnapshots.getDocumentChanges());

                                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                    Patient currentPatient = documentChange.getDocument().toObject(Patient.class);
                                    currentPatient.setId(documentChange.getDocument().getId());
                                    patientList.add(currentPatient);
                                    Log.e("TAG", "onSuccess: currentPatient " + currentPatient.toString());
                                }

                            }
                            // TODO: Adding the searchedPatientList to the LiveData variable( in order to notify
                            //  the changes in the UI)
                            searchedPatientList.postValue(patientList);
                        }
                    });
        } catch (Exception ex) {
            Log.e("TAG", "getSearchedPatients: " + ex.getLocalizedMessage());

        }


    }
}
