package com.example.pharmaeye.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.Prescription;
import com.example.pharmaeye.repositories.PatientRepository;
import com.example.pharmaeye.repositories.PrescriptionRepository;

import java.util.List;

public class PrescriptionViewModel extends AndroidViewModel {

    private final PrescriptionRepository repository = new PrescriptionRepository();
    private static PrescriptionViewModel instance;

    public MutableLiveData<List<Prescription>> prescriptionList;
    public MutableLiveData<Boolean> savePrescriptionOperationStatus;
    public MutableLiveData<Boolean> updatePrescriptionOperationStatus;
    public MutableLiveData<Boolean> deletePrescriptionOperationStatus;



    public PrescriptionViewModel(@NonNull Application application){
        super(application);
    }

    public static PrescriptionViewModel getInstance(Application application){
        if(instance == null){
            instance = new PrescriptionViewModel(application);
        }
        return instance;
    }

    public PrescriptionRepository getRepository(){
        return this.repository;
    }

    public void addPrescription(Prescription prescription){
        this.repository.addPrescription(prescription);
        // TODO: setting the value of LiveData Variable (in the ViewModel) with the
        //  LiveData Variable(from repository)
        this.savePrescriptionOperationStatus = this.repository.savePrescriptionCompletionStatus;
    }

    public void getAllPrescriptions(String patientId){
        this.repository.getPrescriptionList(patientId);
        // TODO: setting the value of LiveData Variable (in the ViewModel) with the
        //  LiveData Variable(from repository)
        this.prescriptionList = this.repository.fetchedPrescriptionList;
    }

    public void deletePrescription(String docID){
        this.repository.deletePrescriptionByDocId(docID);
        // TODO: setting the value of LiveData Variable (in the ViewModel) with the
        //  LiveData Variable(from repository)
        this.deletePrescriptionOperationStatus = this.repository.deletePrescriptionOperationStatus;
    }

    public void updatePrescription(Prescription prescriptionToUpdate){
        this.repository.updatePrescription(prescriptionToUpdate);
        // TODO: setting the value of LiveData Variable (in the ViewModel) with the
        //  LiveData Variable(from repository)
        this.updatePrescriptionOperationStatus = this.repository.updatePrescriptionCompletionStatus;

    }


}
