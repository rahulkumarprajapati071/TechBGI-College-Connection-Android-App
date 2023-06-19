package com.example.techbgi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techbgi.R;
import com.example.techbgi.sharedsession.SharedPreferenceSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DeleteAccountVerification extends AppCompatActivity {

    EditText pass;
    Button delete;
    ProgressDialog dialog;
    DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account_verification);

        delete = findViewById(R.id.deletebtn);
        pass = findViewById(R.id.etdPass);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Deleting....");


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if(pass.getText().toString().isEmpty()){
                    dialog.dismiss();
                    pass.setError("empty");
                    pass.requestFocus();
                }else{
                    String mobileNumber = getIntent().getStringExtra("mobileNumber");
                    String flag = getIntent().getStringExtra("flag");
                    deleteAccount(flag,mobileNumber);
                }
            }
        });
    }
    private void deleteAccount(String flag,String mob) {
        // Get the reference to the user's node in the database
        // Get the reference to the user's node in the database
        if (flag.equals("1")) {
            userRef = FirebaseDatabase.getInstance().getReference()
                    .child("faculty")
                    .child(mob);
        } else {
            userRef = FirebaseDatabase.getInstance().getReference()
                    .child("students")
                    .child(mob);
        }

        userRef.child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Objects.equals(snapshot.getValue(), pass.getText().toString())){
                    FirebaseDatabase.getInstance().getReference().child("All Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).removeValue();
                    SharedPreferenceSession sharedPreferenceSession = new SharedPreferenceSession(getApplicationContext());
                    sharedPreferenceSession.setWho("none");
                    // Remove the user's data from the database
                    FirebaseAuth.getInstance().getCurrentUser().delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    del();
                                }
                            });
                }else{
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Check Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
    }

    private void del() {
        userRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.show();
                if (task.isSuccessful()) {
                    // Account deleted successfully, log out the user

                    // Redirect the user to the login screen or any other appropriate screen
                    // ...
                    dialog.dismiss();
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                    FirebaseAuth.getInstance().signOut();
                    // Close all activities and exit the app

                } else {
                    // Failed to delete account
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}