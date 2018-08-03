package com.example.jerry.dot_dash;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Jerry and Jishnu on 8/1/2018.
 */

public class SplashActivity extends Activity {


    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    FirebaseAuth mAuth;
    boolean isLoggedOut;

    private static int SPLASH_TIME_OUT=750;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("Login", 0);
        editor = sharedPreferences.edit();
        setContentView(R.layout.activity_splash);


        mAuth = FirebaseAuth.getInstance();

        final String emailPref = sharedPreferences.getString("EMAIL", null);
        final String passPref = sharedPreferences.getString("PASSWORD", null);


        //The handler function

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                isLoggedOut = sharedPreferences.getBoolean("LOGOUT", false);
                if(!isLoggedOut){

                if(emailPref != null && passPref != null){
                    if (fingerprintManager.isHardwareDetected()) {

                        mAuth.signInWithEmailAndPassword(emailPref, passPref).addOnCompleteListener(SplashActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(SplashActivity.this, "Invalid Authentication", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(SplashActivity.this,login_sign_up.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent mainIntent = new Intent(SplashActivity.this, FingeringActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }
                        });


                    }else{
                        Toast.makeText(SplashActivity.this, "No fingerprint", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SplashActivity.this, login_sign_up.class);
                        startActivity(intent);
                        finish();
                    }
                }else {


                    //Toast.makeText(SplashActivity.this, emailPref, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, login_sign_up.class);
                    startActivity(intent);
                    finish();
                }

            }
            else{
                    Intent intent=new Intent(SplashActivity.this,login_sign_up.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

    }

}
