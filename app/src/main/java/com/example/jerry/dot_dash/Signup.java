package com.example.jerry.dot_dash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by Jerry and Jishnu on 8/1/2018.
 */

public class Signup extends Activity {
    private FirebaseAuth firebaseAuth;
    LinearLayout layout1,layout2;
    Button login1,signup;
    EditText inputEmail,inputPassword;
    private ProgressDialog progressDialog;
    Animation uptodown,uptodown1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);




        signup=(Button)findViewById(R.id.signup);
        login1=(Button)findViewById(R.id.login1);
        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,login_sign_up.class);
                startActivity(intent);
                finish();
            }
        });

        inputEmail=(EditText)findViewById(R.id.email);
        inputPassword=(EditText)findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=inputEmail.getText().toString().trim();
                String password=inputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Signup.this, "Enter Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)&&TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Registering Please wait....");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if success
                                if(task.isSuccessful()){
                                    //Send mail to the user
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.sendEmailVerification();
                                    //display some message here
                                    Toast.makeText(Signup.this,"Successfully registered",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(Signup.this,login_sign_up.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    //display some message here
                                    Toast.makeText(Signup.this,"Registration Error",Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });



            }
        });



    }
}
