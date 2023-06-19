package com.example.techbgi.activity;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.fullscreen.BaseActivity;
import com.example.techbgi.sharedsession.SharedPreferenceSession;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FrontScreen extends BaseActivity {

    Button asFaculty,asStudent;
    ImageView suggestion;
    SharedPreferenceSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_front_screen);
        session = new SharedPreferenceSession(this);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("please wait...");

        // Check internet connection before proceeding

        asStudent = findViewById(R.id.asStudent);
        asStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (isInternetAvailable()) {
                    // Internet is available, continue with your app logic
                    if(session.getWho().equals("0")){
                        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                        intent.putExtra("flag","students");
                        intent.putExtra("mobile", Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getPhoneNumber()).substring(3,13));
                        startActivity(intent);
                        finish();
                    }else if(session.getWho().equals("1")){
                        startActivity(new Intent(getApplicationContext(),StudentSignUpScreen.class));
                    }else{
                        startActivity(new Intent(getApplicationContext(),StudentSignInScreen.class));
                    }
                } else {
                    // Internet is not available, show the Toast message
                    showInternetConnectionToast();
                }
            }
        });

        suggestion = findViewById(R.id.suggestionBtn);

        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SuggestionActivity.class));
            }
        });

        asFaculty = findViewById(R.id.asFaculty);
        asFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (isInternetAvailable()) {
                    // Internet is available, continue with your app logic
                    if(session.getWho().equals("1")){
                        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                        intent.putExtra("flag","faculty");
                        intent.putExtra("mobile", Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getPhoneNumber()).substring(3,13));
                        startActivity(intent);
                        finish();
                    }else if(session.getWho().equals("0")){
                        startActivity(new Intent(getApplicationContext(),FacultySignUpActivity.class));
                    }else{
                        startActivity(new Intent(getApplicationContext(),FacultySignInActivity.class));
                    }
                } else {
                    // Internet is not available, show the Toast message
                    showInternetConnectionToast();
                }
            }
        });


    }
    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Show Toast message to the user
    public void showInternetConnectionToast() {
        Toast.makeText(getApplicationContext(), "Please connect to the internet or Wi-Fi", Toast.LENGTH_LONG).show();
    }

}