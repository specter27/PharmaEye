package com.example.pharmaeye.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;


import com.example.pharmaeye.R;
import com.example.pharmaeye.containers.DrugOuterClass;
import com.example.pharmaeye.databinding.ActivityAddPatientBinding;
import com.example.pharmaeye.databinding.ActivityAddPrecriptionBinding;
import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.PharmaEye;
import com.example.pharmaeye.models.Prescription;
import com.example.pharmaeye.networks.RetrofitClient;
import com.example.pharmaeye.viewmodels.PatientViewModel;
import com.example.pharmaeye.viewmodels.PrescriptionViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPrescriptionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityAddPrecriptionBinding binding;

    private DatePickerDialog datePickerDialog;
    private ArrayList<String> drugsList;
    private ArrayAdapter<String> adapter;
    private PrescriptionViewModel prescriptionViewModel;

    private boolean editMode = false;

    // Maintaining the state of the currently selected prescription
    private Prescription currentPrescription;

    Date dueBy = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TAG","---------------- ActivityAddPrescriptionBinding Loaded ----------------");

        // 1. configure bindings
        binding = ActivityAddPrecriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Getting view model instance
        this.prescriptionViewModel = PrescriptionViewModel.getInstance(this.getApplication());

        //setting spinner item for the drug list
        this.drugsList = new ArrayList<>();

        // Checking the editMode flag
        this.editMode = this.getIntent().getBooleanExtra("isEditingPrescription",false);

        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.drugsList);
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.binding.spnDrugList.setAdapter(this.adapter);
        this.getDrugsList("DFG");


        this.binding.spnDrugList.setOnItemSelectedListener(this);

        // Setting date picker for due date
        initDatePicker();
        this.binding.datePickerButton.setText(getTodaysDate());

        // click handler for date picker
        this.binding.datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ABC","Date picker clicked");
                openDatePicker(view);
            }
        });

        // click handler for Add Prescription button
        this.binding.addPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editMode){
                    Log.d("TAG","Update Prescription button clicked");

                    // TODO: Validate & Update the Prescription Info
                    updatePrescription();

                }else{
                    Log.d("TAG","Add Prescription button clicked");

                    // TODO: Validate & Save the Prescription Info
                    savePrescription();
                }

            }
        });

        // click handler for Delete Prescription button
        this.binding.deletePrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. delete from database
                prescriptionViewModel.deletePrescription(currentPrescription.getId());

                // 2. Start observing the Delete Prescription Completion Status
                observerDeletePrescriptionStatus();

            }
        });

        // TODO: Getting the currentPrescription from the App State management class
        currentPrescription = PharmaEye.getPharmaEyeInstance().getPrescriptionBeingViewed();

        // Check if the User is in edit mode
        if(editMode){

            // Set the UI Elements to provide Update & Delete functionality
            binding.deletePrescriptionButton.setVisibility(View.VISIBLE);

            binding.addPrescriptionButton.setText("Update");
            populatePrescriptionFormForUpdate(currentPrescription);

        }
    }

    // Observers
    // Observing the savePrescriptionOperationStatus in order to reset the prescription form
    // once the Save process is completed

    final Observer<Boolean> savePrescriptionStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean status) {
            if (status){
                Log.e("TAG", "onChanged:Prescription Successfully Addition Observed");
                Snackbar.make(binding.getRoot(), "SUCCESS: Prescription Added Successfully", Snackbar.LENGTH_LONG)
                        .show();

            } else{
                Snackbar.make(binding.getRoot(), "ERROR: Unable to Add Prescription", Snackbar.LENGTH_LONG)
                        .show();
            }

            // TODO: Reset the Prescription form once save operation completed
            resetPrescriptionForm();
        }
    };

    private void observerSavePrescriptionStatus(){
        prescriptionViewModel.savePrescriptionOperationStatus.observe(this,savePrescriptionStatusObserver);
    }


    final Observer<Boolean> updatePrescriptionStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean status) {
            if (status){
                Log.e("TAG", "onChanged:Prescription Successfully Updation Observed");
                finish();

            } else{
                Snackbar.make(binding.getRoot(), "ERROR: Unable to Update Prescription", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    };

    private void observerUpdatePrescriptionStatus(){
        prescriptionViewModel.updatePrescriptionOperationStatus.observe(this,updatePrescriptionStatusObserver);
    }


    // Observing the deletePrescriptionOperationStatus in order to perform UI changes
    // once the delete process is completed
    final Observer<Boolean> deletePrescriptionStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean status) {
            if (status){
                Log.e("TAG", "onChanged:Prescription Successfully Deletion Observed");
//                PharmaEye.getPharmaEyeInstance().setPrescriptionBeingViewed(null);
                finish();

            } else{
                Snackbar.make(binding.getRoot(), "ERROR: Unable to Delete Prescription", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    };

    private void observerDeletePrescriptionStatus(){
        prescriptionViewModel.deletePrescriptionOperationStatus.observe(this,deletePrescriptionStatusObserver);
    }


    //Creating retrofit call for the spinner to populate with the drug name
    private void getDrugsList(String DFG){
        Call<DrugOuterClass> drugContainer = RetrofitClient.getInstance().getApi().getDrugList(DFG);
        try{
            drugContainer.enqueue(new Callback<DrugOuterClass>() {
                @Override
                public void onResponse(Call<DrugOuterClass> call, Response<DrugOuterClass> response) {
                    if (response.code() == 200) {
                        DrugOuterClass mainResponse = response.body();
                        Log.e("TAG", "onResponse: Received response" + mainResponse.toString());
                        Log.e("TAG", "onResponse: Received response" + mainResponse.getMinConcept().toString());
                        if (mainResponse.getMinConcept().getDrugArrayList().isEmpty()) {
                            Log.e("TAG", "onResponse: No categories received");
                        } else {
                            drugsList.clear();
                            drugsList.add("Select Medication");
                            for (int i = 0; i < mainResponse.getMinConcept().getDrugArrayList().size(); i++) {
                                Log.d("TAG", "onResponse: CategoryList objects " + mainResponse.getMinConcept().getDrugArrayList().get(i).toString());
                                drugsList.add(mainResponse.getMinConcept().getDrugArrayList().get(i).getName());
                            }
                            // TODO: Setting Medication in Spinner
                            if(editMode){
                                binding.spnDrugList.setSelection(drugsList.indexOf(currentPrescription.getDrugName()));
                            }else{
                                binding.spnDrugList.setSelection(0);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        Log.e("TAG", "onResponse: Unsuccessful response " + response.code() + response.errorBody() );
                    }
                    call.cancel();
                }
                @Override
                public void onFailure(Call<DrugOuterClass> call, Throwable t) {
                    call.cancel();
                    Log.e("TAG", "onFailure: Failed to get the List from API" + t.getLocalizedMessage() );
                }
            });
        }catch (Exception ex){
            Log.e("TAG", "getDrugList: Cannot retrieve drug list " + ex.getLocalizedMessage() );
        }
    }


    //Helper Functions

    private void updatePrescription(){
        //TODO: Validate Prescription Details & Update
        getPrescriptionDetails();
    }

    private void savePrescription(){
        //TODO: Validate Prescription Details & Save
        getPrescriptionDetails();
    }


    public void resetPrescriptionForm(){
        this.binding.etQuantity.setText("");
        this.binding.datePickerButton.setText(getTodaysDate());
        this.binding.spnDrugList.setSelection(0);
    }


    public void getPrescriptionDetails(){

        boolean validData = true;
        String quantity = "";
        String drugName = "";

        // 1. Validate patient name
        if (this.binding.etQuantity.getText().toString().trim().isEmpty()){
            this.binding.etQuantity.setError("Provide Quantity for Medication");
            validData = false;
        }else{
            quantity = this.binding.etQuantity.getText().toString();
        }

        if (this.binding.spnDrugList.getSelectedItemPosition() == 0){
            TextView errorTextview = (TextView) this.binding.spnDrugList.getSelectedView();
            errorTextview.setError("Provide Medication Type");
            validData = false;
        }else{
            drugName = this.binding.spnDrugList.getSelectedItem().toString();
        }

        if (validData){
            if(editMode){
                // 1. Update the editable prescription properties
                currentPrescription.setDrugName(drugName);
                currentPrescription.setQuantity(quantity);
                currentPrescription.setDueBy(dueBy);
                currentPrescription.setPrescribedBy(
                        PharmaEye.getPharmaEyeInstance().getCurrentUser());

                // 2. Use the ViewModel function to update the Prescription into the Firestore(database)
                prescriptionViewModel.updatePrescription(currentPrescription);

                // 3. Start observing the Update Prescription Completion Status
                observerUpdatePrescriptionStatus();

            } else{
                // 1. Create an prescription objects
                Prescription prescriptionToBeSaved = new Prescription(
                        PharmaEye.getPharmaEyeInstance().getPatientBeingViewed().getId(),
                        drugName, quantity, dueBy,
                        PharmaEye.getPharmaEyeInstance().getCurrentUser());
                Log.d("TAG","prescriptionToBeSaved: "+prescriptionToBeSaved.toString());

                // 2. Use the ViewModel function to insert the Prescription into the Firestore(database)
                prescriptionViewModel.addPrescription(prescriptionToBeSaved);

                // 3. Start observing the Save Prescription Completion Status
                observerSavePrescriptionStatus();
            }

        }else{
            Snackbar.make(binding.getRoot(),
                    "ERROR: Please Provide Valid Patient Information", Snackbar.LENGTH_LONG).show();
        }
    }

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                Log.d("TAG","Year: "+year);
                // Updating the dueBy
                dueBy = new Date(year-1900,month,day);
                month = month + 1;
                String date = makeDateString(day, month, year);
                binding.datePickerButton.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
//        if(!editMode){
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
//        }


    }
    private  String makeDateString(int day,int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }
    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //set spinner item selection
        Log.e("TAG","onSpinner Click:" + this.drugsList.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void populatePrescriptionFormForUpdate(Prescription prescription){

        this.binding.etQuantity.setText(prescription.getQuantity());

        Date fetchedDueBy = prescription.getDueBy();

        dueBy = fetchedDueBy;

        // Setting the date button text
        String date = makeDateString(fetchedDueBy.getDate(),
                fetchedDueBy.getMonth()+1,
                fetchedDueBy.getYear()+1900);
        binding.datePickerButton.setText(date);


        // setting the date for datePickerDialog
        datePickerDialog.updateDate(fetchedDueBy.getYear()+1900,
                fetchedDueBy.getMonth()+1,fetchedDueBy.getDate());

    }

}