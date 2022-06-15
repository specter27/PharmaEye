package com.example.pharmaeye.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pharmaeye.R;
import com.example.pharmaeye.adapters.PatientAdapter;
import com.example.pharmaeye.databinding.ActivityPatientListBinding;
import com.example.pharmaeye.listeners.RowItemClickListener;
import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.PharmaEye;
import com.example.pharmaeye.viewmodels.PatientViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PatientListActivity extends AppCompatActivity implements RowItemClickListener {

    ActivityPatientListBinding binding;

    // TODO: adapter for recycler view(PatientList)
    private PatientAdapter patientAdapter;

    // TODO: data source for the recycler view(PatientList)
    private ArrayList<Patient> patientArrayList = new ArrayList<Patient>();

    // Add the variable for patientViewModel
    private PatientViewModel patientViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TAG","---------------- PatientListActivity Loaded ----------------");

        // 1. configure bindings
        binding = ActivityPatientListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2. Getting View Model Instance
        this.patientViewModel = PatientViewModel.getInstance(this.getApplication());

        // TODO: Configure the recyclerView

        // TODO 1. Configure the Adapter for the recyclerView
        this.patientAdapter = new PatientAdapter(getApplicationContext(),
                patientArrayList,
                this);

        // TODO 2. Configure recycler view
        this.binding.rvPatientList.setLayoutManager(new LinearLayoutManager(this));
        this.binding.rvPatientList.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        // TODO 3. Attach adapter to recyclerview
        this.binding.rvPatientList.setAdapter(patientAdapter);


        // click listener for search button
        this.binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "Search Button clicked");
                // TODO: validate the searched Parameter
                validateSearchParameter();
            }
        });

        // click listener for Reset Patient Button
        this.binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "Reset Button clicked");
                binding.tvpatientListStatus.setText("");
                patientViewModel.getAllPatients();
            }
        });

        // click listener for Add Patient Button
        this.binding.addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Take user to Next Screen
                goToAddPatient();
            }
        });


    }


    // when we return to this screen, update the recyclerview with the latest data
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG","PatientListActivity----------onResume()-----------");

        this.patientViewModel.getAllPatients();
        // TODO: Observe the Patient List for any changes the FireStoreDB
        observerPatientList();

    }


    // TODO: Configuring the Navigation-Menu for adding LOGOUT Button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_button:{
                Intent signInIntent = new Intent(this, SignInActivity.class);
                startActivity(signInIntent);
                this.finishAffinity();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // Observers
    // Observing the patientList for any changes so that UI List can be reloaded
    final Observer<List<Patient>> patientListObserver = new Observer<List<Patient>>() {
        @Override
        public void onChanged(List<Patient> patients) {
            if (patients.isEmpty()){
                Log.e("TAG", "onChanged: No documents received");
            }else{
                for(Patient patient : patients){
                    Log.e("TAG", "onChanged: patient : " + patient.toString() );
                }

                binding.tvpatientListStatus.setText("");
                binding.editSearchedId.setText("");
                // Reloading the UI Patient List
                patientArrayList.clear();
                patientArrayList.addAll(patients);
                patientAdapter.notifyDataSetChanged();
            }

        }
    };

    private void observerPatientList(){
        patientViewModel.patientList.observe(this,patientListObserver);
    }

    // Observers
    // Observing the patientList for any changes so that UI List can be reloaded
    final Observer<List<Patient>> searchPatientListObserver = new Observer<List<Patient>>() {
        @Override
        public void onChanged(List<Patient> patients) {
            if (patients.isEmpty()){
                Log.e("TAG", "onChanged: No documents received");

                binding.tvpatientListStatus.setText("No Patients Available!");
            }else{
                for(Patient patient : patients){
                    Log.e("TAG", "onChanged: patient : " + patient.toString() );
                }

                binding.tvpatientListStatus.setText("");

            }

            // Reloading the UI Patient List
            patientArrayList.clear();
            patientArrayList.addAll(patients);
            patientAdapter.notifyDataSetChanged();

        }
    };

    private void observerSearchedPatientList(){
        patientViewModel.searchedPatientList.observe(this,searchPatientListObserver);
    }


    // Observing the deletionStatus in order to reset the patient form
    // once the Save process is completed
//        final Observer<Boolean> deletionObserver = new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean status) {
//                if (status){
//                    Log.e("TAG", "onChanged:Patient Successfully Deletion Observed");
//                    // TODO: Reload the PatientList once Deletion operation completed
//                    patientViewModel.getAllPatients();
//
//                } else{
//                    // TODO: Display the Deletion Failure in the Snackbar
//                    Snackbar.make(binding.getRoot(), "ERROR: Unable to Delete the Patient", Snackbar.LENGTH_SHORT).show();
//
//                }
//
//            }
//        };
//
//    private void observerPatientDeletion(){
//        patientViewModel.deletionStatus.observe(this,deletionObserver);
//    }


    @Override
    public void onRowItemClicked(Patient currPatient) {

        Log.d("TAG", "Patient ID:"+currPatient.getId()+"clicked");

        Intent patientDetailScreenIntent = new Intent(this, PatientDetailActivity.class);

        // TODO: Take user to patient detail screen
        // Set the patient in the Singleton class for handling the application state
        PharmaEye.getPharmaEyeInstance().setPatientBeingViewed(currPatient);
        startActivity(patientDetailScreenIntent);
    }


    // Helper Function

    private void goToAddPatient(){

        Intent mainIntent = new Intent(this, AddPatientActivity.class);
        // TODO: Send the loggedInUser(Variable) to next Screen.
        startActivity(mainIntent);
    }

    private void validateSearchParameter(){
        Boolean validData = true;
        String searchedParameter = "";

        if (binding.editSearchedId.getText().toString().isEmpty()){
            this.binding.editSearchedId.setError("Provide Patient Health Card Number");
            validData = false;
        }else{
            searchedParameter = this.binding.editSearchedId.getText().toString();
            if(searchedParameter.length() != 5){
                validData = false;
            }
        }

        if (validData){
            // searchPatient(): This function will search the patients(Collection) in the Firestore
            // for patient whose healthCardNumber=searchedParameter

            // 1. search patient in the fireStore DB
            patientViewModel.searchPatient(searchedParameter);

            // 2. Observing the changes in the liveData variable(of the PatientViewModel)
            observerSearchedPatientList();
        }else{
            Snackbar.make(binding.getRoot(), "Invalid Health Card No", Snackbar.LENGTH_LONG).show();
        }

    }

}