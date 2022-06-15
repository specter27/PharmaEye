package com.example.pharmaeye.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pharmaeye.R;
import com.example.pharmaeye.databinding.ActivitySignInBinding;
import com.example.pharmaeye.models.PharmaEye;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignInBinding binding;
    private final String TAG = this.getClass().getCanonicalName();
//    private SharedPreferences prefs;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.binding.signUpBtn.setOnClickListener(this);
        this.binding.signInBtn.setOnClickListener(this);

        //initialize firebase object
        this.mAuth = FirebaseAuth.getInstance();
        //shared pref to save User Email and Password

//        prefs = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
//        if(prefs.contains("USER_EMAIL")){
//            this.binding.editEmail.setText(this.prefs.getString("USER_EMAIL",""));
//        }
//        if(prefs.contains("USER_PASSWORD")){
//            this.binding.editPassword.setText(this.prefs.getString("USER_PASSWORD",""));
//        }
    }

    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.signInBtn:{
                    Log.d(TAG,"onClick: SignIn Button clicked");
                    this.validateData();
                    break;
                }
                case R.id.signUpBtn:{
                    Log.d(TAG, "onCLick: Sign UP Button Clicked");
                    Intent intent = new Intent(this, SignUpActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    //function to get validation of user details
    private void validateData(){
        Boolean validData = true;
        String email = "";
        String password = "";

        if(this.binding.editEmail.getText().toString().isEmpty()){
            this.binding.editEmail.setError("Email Cannot be Empty");
            validData = false;
        }
        else {
            email = this.binding.editEmail.getText().toString();
        }

        if(this.binding.editPassword.getText().toString().isEmpty()){
            this.binding.editPassword.setError("Password Cannot be Empty");
            validData = false;
        }
        else {
            password = this.binding.editPassword.getText().toString();
        }
        if(validData){
            this.signIn(email, password);
        }
        else {
            Toast.makeText(this,"Please provide correct inputs",Toast.LENGTH_SHORT).show();
        }
    }

    //function for authentication with firebase data inputs
    private void signIn(String email, String password){
        this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.e(TAG,"onComplete: Sign In Successful");
//                    saveToPrefs(email,password);
                    // TODO: Set the currentUser in the StateManagement class
                    PharmaEye.getPharmaEyeInstance().setCurrentUser(email);
                    goToHome();
                }else {
                    Log.e(TAG,"onComplete: Sign In Failed" + task.getException().getLocalizedMessage());
                    Toast.makeText(SignInActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();
                }

                resetValues();
            }
        });
    }

//    //function to save email and password to shared pref
//    private void saveToPrefs(String email, String password){
//        if(this.binding.swRememberMe.isChecked()){
//            prefs.edit().putString("USER_EMAIL",email).apply();
//            prefs.edit().putString("USER_PASSWORD",password).apply();
//        }else {
//            if (prefs.contains("USER_EMAIL")){
//                prefs.edit().remove("USER_EMAIL").apply();
//            }
//            if (prefs.contains("USER_PASSWORD")){
//                prefs.edit().remove("USER_PASSWORD").apply();
//            }
//        }
//    }

    private void goToHome(){
        this.finishAffinity();
        Intent intent = new Intent(this, PatientListActivity.class);
        startActivity(intent);
    }

    private void resetValues(){
        binding.editEmail.setText("");
        binding.editPassword.setText("");
    }

}