package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_front_screen);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("please wait...");

        asStudent = findViewById(R.id.asStudent);
        asStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(auth.getCurrentUser() != null)
                {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("students").hasChild(auth.getCurrentUser().getPhoneNumber().substring(3,13))){
                                dialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                intent.putExtra("flag","students");
                                intent.putExtra("mobile",auth.getCurrentUser().getPhoneNumber().substring(3,13));
                                startActivity(intent);
                            }else{
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),StudentSignInScreen.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            dialog.dismiss();
                            Toast.makeText(FrontScreen.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),StudentSignInScreen.class));
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
                dialog.show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(auth.getCurrentUser() != null)
                {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("faculty").hasChild(auth.getCurrentUser().getPhoneNumber().substring(3,13))){
                                dialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                intent.putExtra("flag","faculty");
                                intent.putExtra("mobile",auth.getCurrentUser().getPhoneNumber().substring(3,13));
                                startActivity(intent);
                            }else{
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),FacultySignInActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            dialog.dismiss();
                            Toast.makeText(FrontScreen.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),FacultySignInActivity.class));
                }
            }
        });


    }

}