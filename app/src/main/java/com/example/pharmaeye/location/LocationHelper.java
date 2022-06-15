package com.example.pharmaeye.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class LocationHelper {

    private final String TAG = this.getClass().getCanonicalName();

    public boolean locationPermissionGranted = false;
    public final int REQUEST_CODE_LOCATION = 101;


    private static final LocationHelper instance = new LocationHelper();
    public static LocationHelper getInstance(){
        return instance;
    }


    public void checkPermissions(Context context){
        this.locationPermissionGranted =
                (PackageManager.PERMISSION_GRANTED == (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)));

        Log.e(TAG, "checkPermissions: LocationPermissionGranted " + this.locationPermissionGranted );

        if (!this.locationPermissionGranted){
            //request permission
            requestLocationPermission(context);
        }
    }

    public void requestLocationPermission(Context context){
        ActivityCompat.requestPermissions( (Activity) context,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_LOCATION);
    }

    public LatLng performReverseGeocoding(Context context, String address){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try{

            List<Address> locationList = geocoder.getFromLocationName(address, 1);

            if (locationList.size() > 0){
                LatLng obtainedCoords = new LatLng(locationList.get(0).getLatitude(), locationList.get(0).getLongitude());

                Log.e(TAG, "performReverseGeocoding: Obtained Coords : " + obtainedCoords.toString() );
                return  obtainedCoords;
            }

        }catch (Exception ex){
            Log.e(TAG, "performReverseGeocoding: Couldn't get the LatLng for the given address" + ex.getLocalizedMessage() );
        }

        return  null;
    }

}
