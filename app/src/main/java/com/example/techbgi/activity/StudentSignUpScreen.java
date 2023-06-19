package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.fullscreen.BaseActivity;
import com.example.techbgi.sharedsession.SharedPreferenceSession;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class StudentSignUpScreen extends BaseActivity {

    Spinner semester,branch;
    ProgressDialog dialog;
    EditText firstName,lastName,mobileNumber,rollNumber,password;
    TextView signInDirect;
    Button signUpBtn;
    ImageView profileImage;

    FirebaseDatabase database;
    DatabaseReference reference;

    SharedPreferenceSession session;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
        Log.d("facutly Activity","kd");


        setContentView(R.layout.activity_student_sign_up_screen);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        semester = findViewById(R.id.semester);
        branch = findViewById(R.id.branch);
        session = new SharedPreferenceSession(this);

//        if(session.getWho().equals()){
//
//        }

        String[] valueSem = {"Current Semester","1","2","3","4","5","6","7","8"};
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(valueSem));
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList2);
        semester.setAdapter(arrayAdapter2);

        String flag = getIntent().getStringExtra("flag");

        String[] valueBranch = {"Choose Branch","CSE","AIML","CSBS","CSE-IOT","CSIT","CIVIL","ME","EC","EEE",
                "IT","EE","EX","AUTO","CHEMICAL","FT","MINING","TX","OTHERS"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(valueBranch));
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList1);
        branch.setAdapter(arrayAdapter1);

        //id finding of all edittexts...
        findId();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });

        //for opening signing activity
        signInDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentSignUpScreen.this, StudentSignInScreen.class));
                finish();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                String firstNameString = firstName.getText().toString();
                String lastNameString = lastName.getText().toString();
                String mobileNumberString = mobileNumber.getText().toString();
                String rollNumberString = rollNumber.getText().toString();
                String passwordString = password.getText().toString();

                if(firstNameString.trim().isEmpty() || lastNameString.trim().isEmpty() || semester.getSelectedItemId() == 0 || branch.getSelectedItemId() == 0 || mobileNumberString.trim().isEmpty() || rollNumberString.trim().isEmpty() || passwordString.trim().isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(StudentSignUpScreen.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else if(mobileNumberString.length()!=10)
                {
                    dialog.dismiss();
                    Toast.makeText(StudentSignUpScreen.this, "Please Enter valid mobile number", Toast.LENGTH_SHORT).show();
                }
                else{

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("students").hasChild(mobileNumberString) || snapshot.child("faculty").hasChild(mobileNumberString)){
                                dialog.dismiss();
                                Toast.makeText(StudentSignUpScreen.this, "Phone number is already registered", Toast.LENGTH_SHORT).show();
                            }else{
                                allProcessOfOTPVerification();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(StudentSignUpScreen.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
    }
    private void allProcessOfOTPVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNumber.getText().toString(),
                20, TimeUnit.SECONDS, StudentSignUpScreen.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        dialog.dismiss();
                        Toast.makeText(StudentSignUpScreen.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                       dialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(),OtpVerificaiton.class);
                        intent.putExtra("mobile",mobileNumber.getText().toString().trim());
                        intent.putExtra("firstname",firstName.getText().toString().trim());
                        intent.putExtra("lastname",lastName.getText().toString().trim());
                        intent.putExtra("semester",semester.getSelectedItem().toString());
                        intent.putExtra("branch",branch.getSelectedItem().toString());
                        intent.putExtra("rollnumber",rollNumber.getText().toString().trim());
                        intent.putExtra("password",password.getText().toString().trim());
                        //playing with image uri
                        if(imageUri!=null) {
                            String image = imageUri.toString();
                            intent.putExtra("imageUri", image);
                        }
                        intent.putExtra("otp",backendOtp);
                        intent.putExtra("flag","0");
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
        profileImage = findViewById(R.id.profileImage);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        mobileNumber = findViewById(R.id.mobNo);
        rollNumber = findViewById(R.id.rollNo);
        password = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.signUpBtn);
        signInDirect = findViewById(R.id.signin);
    }
}