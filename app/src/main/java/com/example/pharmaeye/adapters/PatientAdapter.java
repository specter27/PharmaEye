package com.example.pharmaeye.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmaeye.databinding.CustomRowLayoutPatientBinding;
import com.example.pharmaeye.listeners.RowItemClickListener;
import com.example.pharmaeye.models.Patient;

import java.util.ArrayList;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {


    private final Context context;
    private final ArrayList<Patient> dataSourceArray;
    CustomRowLayoutPatientBinding binding;
    private final RowItemClickListener clickListener;

    public PatientAdapter(Context context, ArrayList<Patient> patients, RowItemClickListener clickListener){
        this.dataSourceArray = patients;
        this.context = context;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PatientViewHolder(CustomRowLayoutPatientBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient item = dataSourceArray.get(position);
        holder.bind(context, item, clickListener);

    }

    @Override
    public int getItemCount() {
        Log.d("PatientAdapter", "getItemCount: Number of items " +this.dataSourceArray.size() );
        return this.dataSourceArray.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder{
        CustomRowLayoutPatientBinding itemBinding;

        public PatientViewHolder(CustomRowLayoutPatientBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Context context, Patient currPatient, RowItemClickListener clickListener){
            itemBinding.tvLine1.setText("Name: "+currPatient.getName());
            itemBinding.tvLine2.setText("Health-Card ID: " + currPatient.getHealthCardNumber());

            // TODO: Add text formatting for the patient name
            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRowItemClicked(currPatient);
                }
            });


        }
    }
}
