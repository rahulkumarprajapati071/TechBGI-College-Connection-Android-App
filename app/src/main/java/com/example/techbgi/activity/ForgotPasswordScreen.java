package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordScreen extends AppCompatActivity {

    EditText number;
    TextView next;
    String flag;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");

        number = findViewById(R.id.number);
        next = findViewById(R.id.next);
        flag = getIntent().getStringExtra("flag");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number.getText().toString().trim().equals("")){
                    dialog.dismiss();
                    Toast.makeText(ForgotPasswordScreen.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                }else{
                    if(number.getText().toString().length() < 10){
                        dialog.dismiss();
                        Toast.makeText(ForgotPasswordScreen.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
                    }else{
                        authenticateNumber(number.getText().toString().trim());
                    }
                }
            }
        });

    }
    public void authenticateNumber(String num){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
        reference.child(flag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(num)){
                    allProcessOfOTPVerification(num,flag);
                }else{
                    dialog.dismiss();
                    Toast.makeText(ForgotPasswordScreen.this, "Phone number not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void allProcessOfOTPVerification(String num,String flag) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + num,
                20, TimeUnit.SECONDS, ForgotPasswordScreen.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        dialog.dismiss();
                        Toast.makeText(ForgotPasswordScreen.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(),OtpVerificaiton.class);
                        intent.putExtra("otp",backendOtp);
                        intent.putExtra("flag",flag);
                        intent.putExtra("mobile",num);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

}