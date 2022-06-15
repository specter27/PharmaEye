package com.example.pharmaeye.listeners;

import com.example.pharmaeye.models.Patient;
import com.example.pharmaeye.models.Prescription;

public interface PrescriptionListItemClickListener {

    void onRowItemClicked(Prescription currPrescription);
}
