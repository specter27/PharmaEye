package com.example.pharmaeye.containers;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DrugsContainer {
    private @SerializedName("minConcept") ArrayList<Drugs> drugArrayList;

    public ArrayList<Drugs> getDrugArrayList() {
        return drugArrayList;
    }

    @Override
    public String toString() {
        return "DrugsContainer{" +
                "drugArrayList=" + drugArrayList +
                '}';
    }
}
