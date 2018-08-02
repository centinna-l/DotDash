package com.example.jerry.dot_dash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

/**
 * Created by Jerry and Jishnu on 8/1/2018.
 */

public class MainActivity extends Activity {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    TextView textView;
    Button signOut;
    boolean logout=false;
    boolean sharedPrefLogout;
    public static final String KEY_INTENT="Email";

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.textView1);
        sharedPreferences = getApplicationContext().getSharedPreferences("Login", 0);
        editor = sharedPreferences.edit();
        signOut = (Button)findViewById(R.id.signOut);

        Intent em =getIntent();
        String userName=em.getStringExtra(KEY_INTENT);


        final String emailPref = sharedPreferences.getString("EMAIL", null);
        final String passPref = sharedPreferences.getString("PASSWORD", null);

        if (emailPref!=null){
            String user= emailPref
                    .substring(0,emailPref.indexOf('@'));
            textView.setText("Hello "+user);
        }else
        {
            String user= userName.substring(0,userName.indexOf('@'));
            textView.setText("Hello "+user);
        }



        setupFirebaseListener();


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get  the instance and then logs out.
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    //logout function
    private void setupFirebaseListener(){
        Log.d(TAG, "setupFirebaseListener: setting up the auth state listener.");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                }else{
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                    logout=true;
                    editor.putBoolean("LOGOUT", logout);
                    sharedPrefLogout = sharedPreferences.getBoolean("LOGOUT", true);
                    Intent intentSplash = new Intent("Bitch").putExtra("Logout", sharedPrefLogout);
                    LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intentSplash);

                    Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), login_sign_up.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }
}


