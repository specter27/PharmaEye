package com.example.pharmaeye.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.Prescription;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionRepository {
    private final FirebaseFirestore DB;
    private final String COLLECTION_PRESCRIPTION = "prescriptions";

    private final String FIELD_DRUG_NAME = "drugName";
    private final String FIELD_PATIENT_ID = "patientId";
    private final String FIELD_QUANTITY = "quantity";
    private final String FIELD_DUE_BY = "dueBy";
    private final String FIELD_PRESCRIBED_BY = "prescribedBy";

    private final String FIELD_PRESCRIBED_ON  = "prescribedOn";

    public MutableLiveData<List<Prescription>> fetchedPrescriptionList = new MutableLiveData<>();
    public MutableLiveData<Boolean> savePrescriptionCompletionStatus = new MutableLiveData<>();
    public MutableLiveData<Boolean> updatePrescriptionCompletionStatus = new MutableLiveData<>();
    public MutableLiveData<Boolean> deletePrescriptionOperationStatus = new MutableLiveData<>();

    public PrescriptionRepository(){
        DB = FirebaseFirestore.getInstance();
    }

    public void addPrescription(Prescription prescriptionToAdd){
        try{
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_DRUG_NAME,prescriptionToAdd.getDrugName());
            data.put(FIELD_QUANTITY,prescriptionToAdd.getQuantity());
            data.put(FIELD_DUE_BY,prescriptionToAdd.getDueBy());
            data.put(FIELD_PATIENT_ID,prescriptionToAdd.getPatientId());
            data.put(FIELD_PRESCRIBED_BY,prescriptionToAdd.getPrescribedBy());
            data.put(FIELD_PRESCRIBED_ON,prescriptionToAdd.getPrescribedOn());

            //TODO: ADD prescription to specific patient

            DB.collection(COLLECTION_PRESCRIPTION)
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.e("TAG","onSuccess: Document successfully created with ID"
                                    + documentReference.getId());
                            // TODO: Adding the data to the LiveData variable(savePrescriptionCompletionStatus)
                            //  in order to notify the changes in the UI
                            savePrescriptionCompletionStatus.setValue(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "onFailure: Error while creating document"
                                    + e.getLocalizedMessage() );
                            // TODO: Adding the data to the LiveData variable(savePrescriptionCompletionStatus)
                            //  in order to notify the changes in the UI
                            savePrescriptionCompletionStatus.setValue(false);
                        }
                    });
        }catch (Exception ex){
            Log.e("TAG", "addPrescription: " + ex.getLocalizedMessage());

        }
    }

    public void getPrescriptionList(String patientDocId){

        try {
            DB.collection(COLLECTION_PRESCRIPTION)
                    .whereEqualTo(FIELD_PATIENT_ID, patientDocId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Prescription> prescriptionList = new ArrayList<>();

                            if (queryDocumentSnapshots.isEmpty()) {
                                Log.e("TAG", "onSuccess: No data retrieved");
                            } else {
                                Log.e("TAG", "onSuccess: queryDocumentSnapshots" +
                                        queryDocumentSnapshots.getDocumentChanges());

                                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                    Prescription currentPrescription = documentChange.getDocument().toObject(Prescription.class);
                                    currentPrescription.setId(documentChange.getDocument().getId());
                                    prescriptionList.add(currentPrescription);
                                    Log.e("TAG", "onSuccess: currentPrescription " + currentPrescription.toString());
                                }

                            }
                            // TODO: Adding the fetchedPatientList to the LiveData variable( in order to notify
                            //  the changes in the UI)
                            fetchedPrescriptionList.postValue(prescriptionList);
                        }
                    });
        } catch (Exception ex) {
            Log.e("TAG", "getAllFriends: " + ex.getLocalizedMessage());

        }

    }


    public void deletePrescriptionByDocId(String docID){
        try{
            DB.collection(COLLECTION_PRESCRIPTION)
                    .document(docID)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.e("TAG", "onSuccess: document successfully deleted");

                            // TODO: Adding the data to the LiveData variable(deletePrescriptionOperationStatus)
                            //  in order to notify the changes in the UI
                            deletePrescriptionOperationStatus.setValue(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "onFailure: Unable to delete document" + e.getLocalizedMessage());

                            // TODO: Adding the data to the LiveData variable(deletePrescriptionOperationStatus)
                            //  in order to notify the changes in the UI
                            deletePrescriptionOperationStatus.setValue(false);
                        }
                    });

        }catch (Exception ex){
            Log.e("TAG", "deletePrescription: " + ex.getLocalizedMessage() );
        }
    }

    public void updatePrescription(Prescription prescriptionToUpdate){

        Map<String, Object> data = new HashMap<>();
        data.put(FIELD_DRUG_NAME, prescriptionToUpdate.getDrugName());
        data.put(FIELD_QUANTITY, prescriptionToUpdate.getQuantity());
        data.put(FIELD_DUE_BY, prescriptionToUpdate.getDueBy());
        data.put(FIELD_PRESCRIBED_BY, prescriptionToUpdate.getPrescribedBy());

        try{
            DB.collection(COLLECTION_PRESCRIPTION)
                    .document(prescriptionToUpdate.getId())
                    .update(data)
//                    .update(FIELD_ADDRESS, "Changed address")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.e("TAG", "onSuccess: document successfully updated");
                            // TODO: Adding the data to the LiveData variable(updatePrescriptionCompletionStatus)
                            //  in order to notify the changes in the UI
                            updatePrescriptionCompletionStatus.setValue(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "onFailure: Unable to update document" + e.getLocalizedMessage());
                            // TODO: Adding the data to the LiveData variable(updatePrescriptionCompletionStatus)
                            //  in order to notify the changes in the UI
                            updatePrescriptionCompletionStatus.setValue(false);
                        }
                    });
        }catch (Exception ex){
            Log.e("TAG", "updatePrescription: " + ex.getLocalizedMessage() );
        }
    }



}
