package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.fullscreen.BaseActivity;
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

public class FacultySignUpActivity extends BaseActivity {

    Button signUpBtn;
    ProgressDialog dialog;
    EditText firstName,lastName,mobileNumber,collegeId,password;
    TextView signInDirect;
    ImageView profileImage;
    Uri imageUri;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");


        setContentView(R.layout.activity_faculty_sign_up);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");

        //id finding of all edittexts...
        findId();

        //for opening signing activity
        signInDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacultySignUpActivity.this, FacultySignInActivity.class));
                finish();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });

//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        if(auth.getCurrentUser() != null)
//        {
//            Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
//            intent.putExtra("flag","1");
//            intent.putExtra("mobile",auth.getCurrentUser().getPhoneNumber());
//            startActivity(intent);
//            finish();
//        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                String firstNameString = firstName.getText().toString();
                String lastNameString = lastName.getText().toString();
                String mobileNumberString = mobileNumber.getText().toString();
                String collegeIdString = collegeId.getText().toString();
                String passwordString = password.getText().toString();

                if(firstNameString.trim().isEmpty() || lastNameString.trim().isEmpty() || mobileNumberString.trim().isEmpty() || collegeIdString.trim().isEmpty() || passwordString.trim().isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(FacultySignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else if(mobileNumberString.length()!=10)
                {
                    dialog.dismiss();
                    Toast.makeText(FacultySignUpActivity.this, "Please Enter valid mobile number", Toast.LENGTH_SHORT).show();
                }
                else{

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("students").hasChild(mobileNumberString) || snapshot.child("faculty").hasChild(mobileNumberString)){
                                dialog.dismiss();
                                Toast.makeText(FacultySignUpActivity.this, "Phone number is already registered", Toast.LENGTH_SHORT).show();
                            }else{
                                    allProcessOfOTPVerification();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(FacultySignUpActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
    }
    private void allProcessOfOTPVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNumber.getText().toString(),
                20, TimeUnit.SECONDS,FacultySignUpActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        dialog.dismiss();
                        Toast.makeText(FacultySignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        dialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(),OtpVerificaiton.class);
                        intent.putExtra("mobile",mobileNumber.getText().toString().trim());
                        intent.putExtra("firstname",firstName.getText().toString().trim());
                        intent.putExtra("lastname",lastName.getText().toString().trim());
                        intent.putExtra("collegeid",collegeId.getText().toString().trim());
                        intent.putExtra("password",password.getText().toString().trim());
                        intent.putExtra("otp",backendOtp);
                        if(imageUri!=null) {
                            String image = imageUri.toString();
                            intent.putExtra("imageUri", image);
                        }
                        intent.putExtra("flag","1");
                        intent.putExtra("previous","signup");
                        startActivity(intent);
                    }
                }
        );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10)
        {
            if(data!=null)
            {
                imageUri=data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }
    public void findId()
    {
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        mobileNumber = findViewById(R.id.mobNo);
        collegeId = findViewById(R.id.collegeid);
        password = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.signUpBtn);
        signInDirect = findViewById(R.id.signin);
        profileImage = findViewById(R.id.profileImage);
    }
}