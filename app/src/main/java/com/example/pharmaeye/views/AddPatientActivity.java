package com.example.pharmaeye.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.pharmaeye.R;
import com.example.pharmaeye.databinding.ActivityAddPatientBinding;
import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.PharmaEye;
import com.example.pharmaeye.models.Prescription;
import com.example.pharmaeye.viewmodels.PatientViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddPatientActivity extends AppCompatActivity {

    ActivityAddPatientBinding binding;

    private DatePickerDialog datePickerDialog;
    Date DOB = new Date();

    // Adapter for the spinner
    private ArrayAdapter<String> spinnerAdaptor;

    // Data Source for Spinner
    private ArrayList<String> genders;

    // Maintaining the state of the currently selected patient
    private Patient currentPatient;
    private boolean editMode = false;


    private PatientViewModel patientViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TAG","---------------- AddPatientActivity Loaded ----------------");
        // configure bindings
        binding = ActivityAddPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Getting view model instance
        this.patientViewModel = PatientViewModel.getInstance(this.getApplication());

        // Initializing the genders(ArrayList) and adding the value
        this.genders = new ArrayList<>();
        Collections.addAll(genders,"Select Gender","Male","Female","Other");

        // Checking the editMode flag
        this.editMode = this.getIntent().getBooleanExtra("isEditingPatient",false);

        // Initializing & Populating the data(genders) in to the spinnerAdaptor
        this.spinnerAdaptor = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,this.genders);

        this.spinnerAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setting the default value for the spinner
        this.binding.genderSpinner.setSelection(0);

        // Binding the spinner with the adapter
        this.binding.genderSpinner.setAdapter(this.spinnerAdaptor);

        // Setting the Date picker for DOB
        initDatePicker();
        this.binding.datePickerButton.setText(getTodaysDate());

        // click handler for Add Patient button
        this.binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG","Add Patient button clicked");
                // TODO: Validate & Save the Patient Info
                saveOrUpdatePatient();
            }
        });

        // click handler for Update Patient button
        this.binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","Update Patient button clicked");
                // TODO: Validate & Update the Patient Info
                saveOrUpdatePatient();
            }
        });

        // click handler for date picker
        this.binding.datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ABC","Date picker clicked");
                openDatePicker(view);
            }
        });



        // TODO: Getting the currentPatient from the App State management class
        currentPatient = PharmaEye.getPharmaEyeInstance().getPatientBeingViewed();

        // Check if the User is in edit mode
        if(editMode){

            // Set the UI Elements to provide Update & Delete functionality
            binding.updateButton.setVisibility(View.VISIBLE);

            binding.addButton.setVisibility(View.GONE);

            populatePatientFormForUpdate(currentPatient);

        }

    }


    // Observers
    // Observing the savePatientOperationStatus in order to reset the patient form
    // once the Save process is completed
    final Observer<Boolean> savePatientOperationStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean status) {
            if (status){
                Log.e("TAG", "onChanged:Patient Successfully Addition Observed");
                // TODO: Reset the Patient form once save operation completed
                resetPatientForm();
            }

        }
    };

    private void observerSavePatientStatus(){
        patientViewModel.savePatientOperationStatus.observe(this,savePatientOperationStatusObserver);
    }


    // Observing the observerUpdatePatientStatus in order to reset the patient form
    // once the Save process is completed
    final Observer<Boolean> updatePatientOperationStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean status) {
            if (status){
                Log.e("TAG", "onChanged:Patient Successfully Updation Observed");
                // TODO: take the user to patient list screen
                goToPatientList();
            }

        }
    };

    private void observerUpdatePatientStatus(){
        patientViewModel.updatePatientOperationStatus.observe(this,updatePatientOperationStatusObserver);
    }

    // Helper Functions
    private String getTodaysDate()
    {
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
                // Updating the DOB
                DOB = new Date(year-1900,month,day);
                month = month + 1;
                // Updating the DOB with the change in date picker

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
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
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

    private void saveOrUpdatePatient(){
        //TODO: Validate patient info
        getPatientInfo();
    }



    // getPatientInfo(): Validate the Patient Info from the UI and return a Patient object if all
    // info is valid
    private void getPatientInfo(){
        Boolean validData = true;
        String patientName = "";
        String patientEmail = "";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        String patientGender = "";
        String address = "";
        String city = "";
        String province = "";
        String healthCardNumber = "";
        String phoneNumber = "";

        // DOB is non empty string by default therefore no validation required
        Log.d("TAG", "Patient DOB String: "+DOB);
        Log.d("TAG", "Patient DOB YEAR: "+DOB.getTime());
//        try {
//            DOB=new SimpleDateFormat("MMM-dd-yyyy").parse(patientDOB).toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Log.d("TAG", "Patient DOB After Conversion: "+binding.datePickerButton.getText().toString());


        // 1. Validate patient name
        if (this.binding.etPatientName.getText().toString().trim().isEmpty()){
            this.binding.etPatientName.setError("Provide Patient Name");
            validData = false;
        }else{
            patientName = this.binding.etPatientName.getText().toString();
        }

        // 2. Validate Patient Email
        if (this.binding.etEmailAddress.getText().toString().trim().isEmpty()){
            this.binding.etEmailAddress.setError("Provide Email Address");
            validData = false;
        }else{
            patientEmail = this.binding.etEmailAddress.getText().toString();
            if(!patientEmail.matches(emailPattern)){
                this.binding.etEmailAddress.setError("Email Address Invalid");
                validData = false;
            }
        }
        // 3. Validate Patient Address
        if (this.binding.etPostalAddress.getText().toString().trim().isEmpty()){
            this.binding.etPostalAddress.setError("Provide Postal Address");
            validData = false;
        }else{
            address = this.binding.etPostalAddress.getText().toString();
        }

        // 4. Validate Patient City
        if (this.binding.etAddressCity.getText().toString().trim().isEmpty()){
            this.binding.etAddressCity.setError("Provide City Name");
            validData = false;
        }else{
            city = this.binding.etAddressCity.getText().toString();
            city = city.substring(0, 1).toUpperCase() + city.substring(1);
        }

        // 5. Validate Patient Province
        if (this.binding.etAddressProvince.getText().toString().trim().isEmpty()){
            this.binding.etAddressProvince.setError("Provide Province Name");
            validData = false;
        }else{
            province = this.binding.etAddressProvince.getText().toString();

        }

        // 6. Validate Health-Card Number
        if (this.binding.etHealthCardNo.getText().toString().trim().isEmpty()){
            this.binding.etHealthCardNo.setError("Provide Health Card Number");
            validData = false;
        }else{
            healthCardNumber = this.binding.etHealthCardNo.getText().toString();
            if(healthCardNumber.length() != 5){
                this.binding.etHealthCardNo.setError("Provide Valid(5 digit) Health Card Number");
                validData = false;
            }
        }

        // 7. Validate Patient Gender
        if (this.binding.genderSpinner.getSelectedItemPosition() == 0){
            TextView errorTextview = (TextView) this.binding.genderSpinner.getSelectedView();
            errorTextview.setError("Provide Patient Gender");
            validData = false;
        }else{
            patientGender = this.binding.genderSpinner.getSelectedItem().toString();
        }

        // 8. Validate Phone Number
        if (this.binding.etPatientPhone.getText().toString().trim().isEmpty()){
            this.binding.etPatientPhone.setError("Provide Phone Number");
            validData = false;
        }else{
            phoneNumber = this.binding.etPatientPhone.getText().toString();
            if(phoneNumber.length() != 10){
                this.binding.etPatientPhone.setError("Provide Valid(10 digit) Phone Number");
                validData = false;
            }
        }

        if (validData){
            if(editMode){
                // 1. Update the editable patient properties
                currentPatient.setName(patientName);
                currentPatient.setDOB(DOB);
                currentPatient.setEmail(patientEmail);
                currentPatient.setGender(patientGender);

                currentPatient.setPostalAddress(address);
                currentPatient.setCity(city);
                currentPatient.setProvince(province);

                currentPatient.setPhoneNumber(phoneNumber);
                currentPatient.setHealthCardNumber(healthCardNumber);

                // 2. Use the ViewModel function to update the Patient into the Firestore(database)
                patientViewModel.updatePatient(currentPatient);

                // 3. Start observing the Update Patient Completion Status
                observerUpdatePatientStatus();

            } else{

                // 1. Create an patient objects
                Patient patientToBeSaved = new Patient(patientName,patientEmail,patientGender,DOB,
                        phoneNumber,address,city,province,healthCardNumber);

                // 2. Use the ViewModel function to insert the Patient into the Firestore(database)
                this.patientViewModel.addPatient(patientToBeSaved, binding.getRoot());

                // 3. Start observing the save Completion Status
                observerSavePatientStatus();

            }
        }else{
            Snackbar.make(binding.getRoot(),
                    "ERROR: Please Provide Valid Patient Information", Snackbar.LENGTH_LONG).show();
        }

    }

    private void resetPatientForm(){
        this.binding.etPatientName.setText("");
        this.binding.datePickerButton.setText(getTodaysDate());
        this.binding.etEmailAddress.setText("");
        this.binding.genderSpinner.setSelection(0);
        this.binding.etPostalAddress.setText("");
        this.binding.etAddressCity.setText("");
        this.binding.etAddressProvince.setText("");
        this.binding.etPatientPhone.setText("");
        this.binding.etHealthCardNo.setText("");
    }

    private void populatePatientFormForUpdate(Patient patient){
        this.binding.etPatientName.setText(patient.getName());
        this.binding.etEmailAddress.setText(patient.getEmail());

        this.binding.genderSpinner.setSelection(genders.indexOf(patient.getGender()));

        this.binding.etPostalAddress.setText(patient.getPostalAddress());
        this.binding.etAddressCity.setText(patient.getCity());
        this.binding.etAddressProvince.setText(patient.getProvince());
        this.binding.etPatientPhone.setText(patient.getPhoneNumber());
        this.binding.etHealthCardNo.setText(patient.getHealthCardNumber());

        Date fetchedDueBy = patient.getDOB();
        DOB = fetchedDueBy;

        // Setting the date button text
        String date = makeDateString(fetchedDueBy.getDate(),
                fetchedDueBy.getMonth()+1,
                fetchedDueBy.getYear()+1900);
        binding.datePickerButton.setText(date);


        // setting the date for datePickerDialog
        datePickerDialog.updateDate(fetchedDueBy.getYear()+1900,
                fetchedDueBy.getMonth()+1,fetchedDueBy.getDate());

    }

    private void goToPatientList(){
        finish();
        Intent patientListScreen = new Intent(this, PatientListActivity.class);
        startActivity(patientListScreen);
    }



}







