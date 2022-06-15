package com.example.pharmaeye.networks;

import com.example.pharmaeye.containers.DrugOuterClass;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    //https://rxnav.nlm.nih.gov/REST/Prescribe/allconcepts.json?tty=DFG
    String BASE_URL = "https://rxnav.nlm.nih.gov/REST/Prescribe/";

    //HTTP request --- GET Request
    @GET("./allconcepts.json")
    Call<DrugOuterClass> getDrugList(@Query("tty") String tty);

}
