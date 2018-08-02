package com.example.jerry.dot_dash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Jerry and Jishnu on 8/1/2018.
 */

public class Reset_Password extends Activity {

    Button forgot;
    EditText inputEmail;
    String Email;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);

        auth=FirebaseAuth.getInstance();
        forgot=(Button)findViewById(R.id.forgot1);
        inputEmail=(EditText)findViewById(R.id.inputEmail);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                email = inputEmail.getText().toString().trim();
                Email=inputEmail.getText().toString().trim();
//
                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(Reset_Password.this, "Enter your Registered Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
               auth.sendPasswordResetEmail(Email).addOnCompleteListener(Reset_Password.this, new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()) {
                           Toast.makeText(Reset_Password.this, "We have sent you the Verifiation mail", Toast.LENGTH_SHORT).show();
                       }
                       else{
                           Toast.makeText(Reset_Password.this, "Failed to send Verification link", Toast.LENGTH_SHORT).show();
                       }
                       progressBar.setVisibility(View.GONE);
                       Intent intent=new Intent(Reset_Password.this,login_sign_up.class);
                       startActivity(intent);
                       finish();

                   }
               });
            }
        });
    }
}
