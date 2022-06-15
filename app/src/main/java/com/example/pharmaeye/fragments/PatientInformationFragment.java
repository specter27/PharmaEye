package com.example.pharmaeye.fragments;

import static java.time.Period.between;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pharmaeye.R;
import com.example.pharmaeye.databinding.FragmentPatientInformationBinding;
import com.example.pharmaeye.location.LocationHelper;
import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.PharmaEye;
import com.example.pharmaeye.views.AddPatientActivity;
import com.example.pharmaeye.views.MapsActivity;
import com.example.pharmaeye.views.PatientListActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class PatientInformationFragment extends Fragment {

    private FragmentPatientInformationBinding binding;

    Double addressLat = 0.0;
    Double addressLng = 0.0;
    private LocationHelper locationHelper;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentPatientInformationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    // fragment lifecycle functions
    // function will be executed after the screen has been created and loaded
    // - same as the onCreate() in the Activity
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Getting the Location Helper instance and checking location permissions
        this.locationHelper = LocationHelper.getInstance();

        // Setting the Latitude and Longitude for the address using reverse geocoding
        Patient currentPatient = PharmaEye.getPharmaEyeInstance().getPatientBeingViewed();
        doReverseGeocoding(currentPatient.getPostalAddress()+" "+
                currentPatient.getCity()+" "+currentPatient.getProvince());



        // Populate the patient info
        populatePatientInfo(PharmaEye.getPharmaEyeInstance().getPatientBeingViewed());


        //detect when the person clicks the button
        // navigate to another screen
        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(binding.getRoot(), "Edit Button clicked!", Snackbar.LENGTH_LONG).show();
                // navigate to another screen (3rd screen --- fragment)

                Intent addPatientScreen = new Intent(requireActivity().getApplication(), AddPatientActivity.class);
                addPatientScreen.putExtra("isEditingPatient", true);
                startActivity(addPatientScreen);
            }
        });

        binding.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addressLat != 0.0 && addressLng != 0.0){
                    // display patient address on map
                    openMap();
                }else{
                    Snackbar.make(binding.getRoot(), "Couldn't get coordinates from address", Snackbar.LENGTH_LONG).show();
                }


            }
        });

        // click handler for delete button
//        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(binding.getRoot(), "Delete Button clicked!", Snackbar.LENGTH_LONG).show();
//                // navigate to another screen (3rd screen --- fragment)
//            }
//        });

    }



    // Helper functions

    private void populatePatientInfo(Patient patient){
        binding.tvPatientName.setText("Name: "+patient.getName());
        binding.tvPatientEmail.setText("Email: "+patient.getEmail());
        binding.tvPatientPhoneNo.setText("Phone: "+patient.getPhoneNumber());

        Date DateOfBirth = patient.getDOB();
        LocalDate birthDate = LocalDate.of(DateOfBirth.getYear()+1900,
                DateOfBirth.getMonth()+1, DateOfBirth.getDate() );
        Log.d("TAG", "Year of birth: "+DateOfBirth.getYear()+
                " Date now: "+LocalDate.now()+"birthDate: "+birthDate);

        int age = calculateAge(birthDate, LocalDate.now());

        Log.d("TAG","Patient's Date-of-Birth: "+patient.getDOB()+" Age: "+age);

        binding.tvPatientAge.setText("Age: "+Integer.toString(age));

        // TODO: Display the patient address on map

    }


    private  int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        int age = Period.between(birthDate, currentDate).getYears();
        //prints the differnce in years, months, and days
        return age;
    }

    private int getMonthFormat(String month) {
        if(month == "JAN")
            return 1;
        if(month == "FEB")
            return 2;
        if(month == "MAR")
            return 3;
        if(month == "APR")
            return 4;
        if(month == "MAY")
            return 5;
        if(month == "JUN")
            return 6;
        if(month == "JUL")
            return 7;
        if(month == "AUG")
            return 8;
        if(month == "SEP")
            return 9;
        if(month == "OCT")
            return 10;
        if(month == "NOV")
            return 11;
        if(month == "DEC")
            return 12;

        //default should never happen
        return 1;
    }


    private void openMap(){
        Log.d("TAG", "onClick: Open map to show location");

        Intent intent = new Intent(requireActivity().getApplication(), MapsActivity.class);
        intent.putExtra("EXTRA_LAT", addressLat);
        intent.putExtra("EXTRA_LNG", addressLng);
        startActivity(intent);
    }

    private void doReverseGeocoding(String patientAddress){
        Log.d("TAG", "onClick: Perform reverse geocoding to get coordinates from patientAddress.");

        LatLng obtainedCoords = this.locationHelper.performReverseGeocoding(requireActivity().getApplication(), patientAddress);

        if (obtainedCoords != null){
            addressLat = obtainedCoords.latitude;
            addressLng = obtainedCoords.longitude;
        }

    }




}