package com.example.pharmaeye.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmaeye.databinding.CustomRowLayoutPatientBinding;
import com.example.pharmaeye.databinding.CustomRowLayoutPrescriptionBinding;
import com.example.pharmaeye.listeners.PrescriptionListItemClickListener;
import com.example.pharmaeye.models.Prescription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder>{

    private final Context context;
    private final ArrayList<Prescription> dataSourceArray;
    CustomRowLayoutPatientBinding binding;
    private final PrescriptionListItemClickListener clickListener;

    public PrescriptionAdapter(Context context, ArrayList<Prescription> patients,
                               PrescriptionListItemClickListener clickListener){
        this.dataSourceArray = patients;
        this.context = context;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public PrescriptionAdapter.PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrescriptionAdapter.PrescriptionViewHolder(CustomRowLayoutPrescriptionBinding
                .inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionAdapter.PrescriptionViewHolder holder, int position) {
        Prescription item = dataSourceArray.get(position);
        holder.bind(context, item, clickListener);

    }

    @Override
    public int getItemCount() {
        Log.d("PatientAdapter", "getItemCount: Number of items " +this.dataSourceArray.size() );
        return this.dataSourceArray.size();
    }

    public static class PrescriptionViewHolder extends RecyclerView.ViewHolder{
        CustomRowLayoutPrescriptionBinding itemBinding;

        public PrescriptionViewHolder(CustomRowLayoutPrescriptionBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Context context, Prescription currPrescription, PrescriptionListItemClickListener clickListener){
            itemBinding.tvMedicationName.setText("Medication: "+currPrescription.getDrugName());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");

            itemBinding.tvDueOn.setText("DueOn: " + formatter.format(currPrescription.getDueBy()));
            itemBinding.tvMedicationQuantity.setText("Quantity: "+currPrescription.getQuantity());

            // TODO: Add text formatting for the patient name
            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRowItemClicked(currPrescription);
                }
            });


        }
    }
}
