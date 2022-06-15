package com.example.pharmaeye.containers;

import com.google.gson.annotations.SerializedName;

public class DrugOuterClass {
    private @SerializedName("minConceptGroup")
    DrugsContainer minConcept;

    public DrugsContainer getMinConcept() {
        return minConcept;
    }

    @Override
    public String toString() {
        return "DrugOuterClass{" +
                "minConcept=" + minConcept +
                '}';
    }
}
