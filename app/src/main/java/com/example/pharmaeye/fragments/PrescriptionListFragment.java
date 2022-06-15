package com.example.pharmaeye.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pharmaeye.adapters.PatientAdapter;
import com.example.pharmaeye.adapters.PrescriptionAdapter;
import com.example.pharmaeye.databinding.FragmentPrescriptionListBinding;
import com.example.pharmaeye.listeners.PrescriptionListItemClickListener;
import com.example.pharmaeye.listeners.RowItemClickListener;
import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.PharmaEye;
import com.example.pharmaeye.models.Prescription;
import com.example.pharmaeye.viewmodels.PatientViewModel;
import com.example.pharmaeye.viewmodels.PrescriptionViewModel;
import com.example.pharmaeye.views.AddPatientActivity;
import com.example.pharmaeye.views.AddPrescriptionActivity;
import com.example.pharmaeye.views.PatientDetailActivity;

import java.util.ArrayList;
import java.util.List;


public class PrescriptionListFragment extends Fragment implements PrescriptionListItemClickListener {

    private FragmentPrescriptionListBinding binding;

    // TODO: adapter for recycler view(PrescriptionList)
    private PrescriptionAdapter prescriptionAdapter;

    // TODO: data source for the recycler view(PrescriptionList)
    private ArrayList<Prescription> prescriptionArrayList = new ArrayList<Prescription>();

    // Add the variable for prescriptionViewModel
    private PrescriptionViewModel prescriptionViewModel;



    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentPrescriptionListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Getting View Model Instance
        this.prescriptionViewModel = PrescriptionViewModel.getInstance(requireActivity().getApplication());

        // 2. Setting Text View Title
        this.binding.tvPrescriptionListFragmentTitle.setText("Prescriptions for, "+
                PharmaEye.getPharmaEyeInstance().getPatientBeingViewed().getName());

        // TODO: Configure the recyclerView

        // TODO 1. Configure the Adapter for the recyclerView
        this.prescriptionAdapter = new PrescriptionAdapter(view.getContext(), prescriptionArrayList,
                this::onRowItemClicked);

        // TODO 2. Configure recycler view
        this.binding.rvPrescriptionList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.binding.rvPrescriptionList.addItemDecoration(new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL));

        // TODO 3. Attach adapter to recyclerview
        this.binding.rvPrescriptionList.setAdapter(prescriptionAdapter);

        // detect when the user clicks on addNewPrescriptionButton &
        // navigate to another screen
        binding.addNewPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Navigate to the AddPrescriptionActivity
                goToAddPrescription();
            }
        });

        this.prescriptionViewModel.getAllPrescriptions(
                PharmaEye.getPharmaEyeInstance().getPatientBeingViewed().getId());
        // TODO: Observe the Prescription List for any changes the FireStoreDB
        prescriptionViewModel.prescriptionList.observe(getViewLifecycleOwner(),prescriptionListObserver);



    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG","----- PrescriptionListFragment onResume() called -------");
        this.prescriptionViewModel.getAllPrescriptions(
                PharmaEye.getPharmaEyeInstance().getPatientBeingViewed().getId());
        // TODO: Observe the Prescription List for any changes the FireStoreDB
        prescriptionViewModel.prescriptionList.observe(getViewLifecycleOwner(),prescriptionListObserver);

    }

    // Observers
    // Observing the prescriptionList for any changes so that UI List can be reloaded
    final Observer<List<Prescription>> prescriptionListObserver = new Observer<List<Prescription>>() {
        @Override
        public void onChanged(List<Prescription> prescriptions) {
            if (prescriptions.isEmpty()){
                // set the tvPrescriptionListStatus (TextView) with the appropriate Message
                binding.tvPrescriptionListStatus.setText("No, Prescriptions for the patient yet.");
                Log.d("TAG", "onChanged: No documents received");
            }else{
                // set the tvPrescriptionListStatus (TextView) with the appropriate Message
                binding.tvPrescriptionListStatus.setText("");

                for(Prescription prescription : prescriptions){
                    Log.e("TAG", "onChanged: patient : " + prescription.toString() );
                }
            }

            // Reloading the UI Patient List
            prescriptionArrayList.clear();
            prescriptionArrayList.addAll(prescriptions);
            prescriptionAdapter.notifyDataSetChanged();

        }
    };



    @Override
    public void onRowItemClicked(Prescription currPrescription) {
        Log.d("TAG", "Prescription ID:"+currPrescription.getId()+"clicked");

        Intent prescriptionEditScreenIntent = new Intent(requireActivity().getApplication(), AddPrescriptionActivity.class);

        // TODO: Take user to edit prescription screen
        // Set the patient in the Singleton class for handling the application state
        PharmaEye.getPharmaEyeInstance().setPrescriptionBeingViewed(currPrescription);
        prescriptionEditScreenIntent.putExtra("isEditingPrescription",true);
        startActivity(prescriptionEditScreenIntent);

    }

    // Helper Function
    private void goToAddPrescription(){
        Intent addPrescriptionScreenIntent = new Intent(requireActivity().getApplication(), AddPrescriptionActivity.class);
        startActivity(addPrescriptionScreenIntent);
    }
}