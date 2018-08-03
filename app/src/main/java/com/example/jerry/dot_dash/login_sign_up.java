package com.example.jerry.dot_dash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

/**
 * Created by Jerry and Jishnu on 8/1/2018.
 */

public class login_sign_up extends Activity {

    Button signup, login, forgot;
    EditText inputEmail, inputPassword;
    LinearLayout layout1, layout2;
    Animation uptodown;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    String email, password;
    CheckBox remberMe;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    String prefEmail = "EMAIL";
    String prefPass = "PASSWORD";
    boolean shared = false;
    String emailPref;
    String passPref;
    boolean rememberChecked;
    //private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);


        //uptodown = AnimationUtils.loadAnimation(login_sign_up.this, R.anim.uptodown);


        //All the buttons declared here.
        signup = (Button) findViewById(R.id.signup);
        remberMe = findViewById(R.id.remChkbox);
        login = (Button) findViewById(R.id.login);
        forgot = (Button) findViewById(R.id.forgot);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        //Creating the instance of the firebase instance.
        mAuth = FirebaseAuth.getInstance();
        //SharedPreferences.
        sharedPreferences = getApplicationContext().getSharedPreferences("Login", 0);
        editor = sharedPreferences.edit();
        progressDialog = new ProgressDialog(this);
        //Intent for the Sign Up Activity.
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_sign_up.this, Reset_Password.class);
                startActivity(intent);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_sign_up.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(login_sign_up.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(login_sign_up.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Logging in please wait....");
                progressDialog.show();

                //The predefined function to login using firebase.
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(login_sign_up.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.min_pass));
                            } else {
                                Toast.makeText(login_sign_up.this, "Authentication failed", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        } else {
                            checkIfEmailVerified();
                            rememberMeChecked();
                        }

                    }
                });

            }
        });

        //if the shared pref is true then it will set the text in the editText if not it will just pass a null reff.
        emailPref = sharedPreferences.getString(prefEmail, null);
        passPref = sharedPreferences.getString(prefPass, null);
        inputEmail.setText(emailPref);
        inputPassword.setText(passPref);

    }


    //if the remember me is checked then this function is called.
    public void rememberMeChecked() {

        if (remberMe.isChecked()) {
            editor.putBoolean("NonNull", true);
            editor.putString(prefEmail, email);
            editor.putString(prefPass, password);
            editor.commit();
            String e = sharedPreferences.getString(prefEmail, null);
            Log.i("EMAIL", e);
            emailPref = sharedPreferences.getString(prefEmail, null);
            passPref = sharedPreferences.getString(prefPass, null);
            Intent intent = new Intent("Bitch").putExtra("Email", emailPref).putExtra("Password", passPref);
            LocalBroadcastManager.getInstance(login_sign_up.this).sendBroadcast(intent);

        }
        if (!remberMe.isChecked()) {
            if (prefEmail != null) {
                editor.remove(prefEmail);
                editor.remove(prefPass);
                editor.commit();
                Intent intent = new Intent("Bitch").putExtra("Email", emailPref).putExtra("Password", passPref);
                LocalBroadcastManager.getInstance(login_sign_up.this).sendBroadcast(intent);
            }


        }


    }

    //this function is used when we register and then we try to login. Only those users will be able to login who verified their email.
    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            Intent intent = new Intent(login_sign_up.this, MainActivity.class);
            intent.putExtra(MainActivity.KEY_INTENT,email);
            startActivity(intent);
            finish();
            Toast.makeText(login_sign_up.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(login_sign_up.this, "Email not Verified", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

            //restart this activity

        }
    }
}
