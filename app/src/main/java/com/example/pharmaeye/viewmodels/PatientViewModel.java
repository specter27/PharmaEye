package com.example.pharmaeye.viewmodels;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.Prescription;
import com.example.pharmaeye.repositories.PatientRepository;

import java.util.List;

public class PatientViewModel extends AndroidViewModel {
    private final PatientRepository repository = new PatientRepository();
    private static PatientViewModel instance;


    public MutableLiveData<Boolean> savePatientOperationStatus;
    public MutableLiveData<List<Patient>> patientList;
    public MutableLiveData<List<Patient>> searchedPatientList;
    public MutableLiveData<Boolean> updatePatientOperationStatus;
    public MutableLiveData<Boolean> deletionStatus;

    public PatientViewModel(@NonNull Application application) {
        super(application);
    }

    public static PatientViewModel getInstance(Application application){
        if (instance == null){
            instance = new PatientViewModel(application);
        }
        return instance;
    }

    public PatientRepository getPatientRepository(){
        return this.repository;
    }

    public void addPatient(Patient patient, View view){
        this.repository.addPatient(patient, view);
        // TODO: setting the value of LiveData Variable (in the ViewModel) with the
        //  LiveData Variable(from repository)
        this.savePatientOperationStatus = this.repository.addPatientCompletionStatus;
    }

    public void getAllPatients(){
        this.repository.getPatientList();
        // TODO: setting the value of LiveData Variable (in the ViewModel) with the
        //  LiveData Variable(from repository)
        this.patientList = this.repository.fetchedPatientList;
    }

    public void updatePatient(Patient patientToUpdate){
        this.repository.updatePatient(patientToUpdate);
        // TODO: setting the value of LiveData Variable (in the ViewModel) with the
        //  LiveData Variable(from repository)
        this.updatePatientOperationStatus = this.repository.updatePatientCompletionStatus;

    }
    public void searchPatient(String searchId){
        this.repository.searchPatientById(searchId);
        // TODO: setting the value of LiveData Variable (in the ViewModel) with the
        //  LiveData Variable(from repository)
        this.searchedPatientList = this.repository.searchedPatientList;

    }


    public void deletePatient(String docID){
        this.repository.deletePatientByDocIc(docID);
        // TODO: setting the value of LiveData Variable (in the ViewModel) with the
        //  LiveData Variable(from repository)
        this.deletionStatus = this.repository.deletionStatusFromRepository;
    }
}
