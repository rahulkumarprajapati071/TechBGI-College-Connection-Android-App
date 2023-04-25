package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;
import com.example.techbgi.model.StudentModel;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class FacultySignInActivity extends BaseActivity {

    EditText mobileNumber,password;
    Button signInButton;
    TextView signUp,forgotPassword;
    ProgressDialog dialog;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_in_screen);


        database = FirebaseDatabase.getInstance();
        reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        dialog = new ProgressDialog(this);
        mobileNumber = findViewById(R.id.mobNo);
        password = findViewById(R.id.password);
        signInButton = findViewById(R.id.signInBtn);
        signUp = findViewById(R.id.signup);
        forgotPassword = findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ForgotPasswordScreen.class);
                intent.putExtra("flag","faculty");
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FacultySignUpActivity.class));
                finish();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if(mobileNumber.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty())
                {
                    dialog.dismiss();
                    Toast.makeText(FacultySignInActivity.this, "Please enter required details", Toast.LENGTH_SHORT).show();
                }else if(mobileNumber.getText().toString().trim().length() != 10)
                {
                    dialog.dismiss();
                    Toast.makeText(FacultySignInActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                }
                else{
                    reference.child("faculty").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(mobileNumber.getText().toString())){

                                String passwordString = password.getText().toString();

                                reference.child("faculty").child(mobileNumber.getText().toString()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        StudentModel realData = snapshot.getValue(StudentModel.class);
                                        String pass = realData.getPassword();
                                        if(!passwordString.equals(pass)){
                                            dialog.dismiss();
                                            Toast.makeText(FacultySignInActivity.this, "Please enter correct password", Toast.LENGTH_SHORT).show();
                                        }else{
                                            dialog.dismiss();
                                            Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                            intent.putExtra("mobile",mobileNumber.getText().toString());
                                            intent.putExtra("flag","faculty");
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        dialog.dismiss();
                                        Toast.makeText(FacultySignInActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else{
                                dialog.dismiss();
                                Toast.makeText(FacultySignInActivity.this, "Phone number does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

}