package com.example.techbgi.activity;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.fullscreen.BaseActivity;
import com.example.techbgi.model.All_UserMmber;
import com.example.techbgi.model.FacultyRegistrationModel;
import com.example.techbgi.sharedsession.SharedPreferenceSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OtpVerificaiton extends BaseActivity {

    EditText input1,input2,input3,input4,input5,input6;
    TextView showMobile;
    String getOtpBackend;
    Button submitButton;
    String imageUrl;
    String firstName,lastName,branch,semester,password,rollNumber,phoneNumber,collegeId;
    Uri imageUri;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage firebaseStorage;
    SharedPreferenceSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
        firebaseStorage = FirebaseStorage.getInstance();

        setContentView(R.layout.activity_otp_verificaiton);
        session = new SharedPreferenceSession(this);

        firstName = getIntent().getStringExtra("firstname");
        lastName = getIntent().getStringExtra("lastname");
        branch = getIntent().getStringExtra("branch");
        semester = getIntent().getStringExtra("semester");
        phoneNumber = getIntent().getStringExtra("mobile");
        rollNumber = getIntent().getStringExtra("rollnumber");
        password = getIntent().getStringExtra("password");
        collegeId = getIntent().getStringExtra("collegeid");


        showMobile = findViewById(R.id.showmobilenumber);
        // play with image uri
        String image = getIntent().getStringExtra("imageUri");
        showMobile.setText(String.format("+91-%s",getIntent().getStringExtra("mobile")));
        getOtpBackend = getIntent().getStringExtra("otp");

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");


        input1 = findViewById(R.id.input_1);
        input2 = findViewById(R.id.input_2);
        input3 = findViewById(R.id.input_3);
        input4 = findViewById(R.id.input_4);
        input5 = findViewById(R.id.input_5);
        input6 = findViewById(R.id.input_6);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if(!input1.getText().toString().trim().isEmpty() &&!input2.getText().toString().trim().isEmpty() &&!input3.getText().toString().trim().isEmpty() &&!input4.getText().toString().trim().isEmpty() &&!input5.getText().toString().trim().isEmpty() &&!input6.getText().toString().trim().isEmpty())
                {
                    String enterCodeOtp = input1.getText().toString()+
                            input2.getText().toString()+
                            input3.getText().toString()+
                            input4.getText().toString()+
                            input5.getText().toString()+
                            input6.getText().toString();

                    if(getOtpBackend!=null){

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getOtpBackend,enterCodeOtp);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    //send data in data base in intent comes form registration screen other wise go..
                                    if(getIntent().getStringExtra("flag").equals("0"))
                                    {
                                        if(getIntent().getStringExtra("previous").equals("signup")){
                                            if(image != null)
                                            {
                                                imageUri = Uri.parse(image);
                                            }
                                            if(imageUri != null)
                                            {
                                                StorageReference storageReference = firebaseStorage.getReference().child("studentImages/"+rollNumber);
                                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {

                                                                    imageUrl = uri.toString();
                                                                    HashMap data = new HashMap();
                                                                    data.put("uid", FirebaseAuth.getInstance().getUid());
                                                                    data.put("semester",semester);
                                                                    data.put("branch",branch);
                                                                    data.put("lastName",lastName);
                                                                    data.put("firstName",firstName);
                                                                    data.put("imageUri",imageUrl);
                                                                    data.put("password",password);
                                                                    data.put("phoneNumber",phoneNumber);
                                                                    data.put("rollNumber",rollNumber);
//                                                                StudentRegistrationModel studentRegistrationModel = new StudentRegistrationModel(firstName,lastName,branch,semester,password,rollNumber,phoneNumber,imageUrl);
                                                                    session.setWho("0");
                                                                    All_UserMmber all_userMmber = new All_UserMmber(firstName+" "+lastName,FirebaseAuth.getInstance().getUid(),imageUrl,phoneNumber);
                                                                    reference.child("All Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(all_userMmber);
                                                                    reference.child("students").child(phoneNumber).setValue(data);
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }else
                                            {
                                                imageUrl = "https://cdn-icons-png.flaticon.com/512/1057/1057240.png";
                                                HashMap data = new HashMap();
                                                data.put("uid", FirebaseAuth.getInstance().getUid());
                                                data.put("semester",semester);
                                                data.put("branch",branch);
                                                data.put("lastName",lastName);
                                                data.put("firstName",firstName);
                                                data.put("imageUri",imageUrl);
                                                data.put("password",password);
                                                data.put("phoneNumber",phoneNumber);
                                                data.put("rollNumber",rollNumber);

//                                            StudentRegistrationModel studentRegistrationModel = new StudentRegistrationModel(firstName,lastName,branch,semester,password,rollNumber,phoneNumber,imageUrl);
                                                session.setWho("0");
                                                All_UserMmber all_userMmber = new All_UserMmber(firstName+" "+lastName,FirebaseAuth.getInstance().getUid(),imageUrl,phoneNumber);
                                                reference.child("All Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(all_userMmber);
                                                reference.child("students").child(phoneNumber).setValue(data);
                                            }
                                        }
                                        dialog.dismiss();
                                        session.setWho("0");
                                        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                        intent.putExtra("flag","students");
                                        intent.putExtra("mobile",phoneNumber);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else if(getIntent().getStringExtra("flag").equals("1"))
                                    {
                                        if(getIntent().getStringExtra("previous").equals("signup")){
                                            if(image != null)
                                            {
                                                imageUri = Uri.parse(image);
                                            }
                                            if(imageUri != null)
                                            {
                                                StorageReference storageReference = firebaseStorage.getReference().child("facultyImages/"+collegeId);
                                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {

                                                                    imageUrl = uri.toString();
                                                                    FacultyRegistrationModel facultyRegistrationModel = new FacultyRegistrationModel(firstName,lastName,password,collegeId,phoneNumber,imageUrl, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

                                                                    session.setWho("1");
                                                                    All_UserMmber all_userMmber = new All_UserMmber(firstName+" "+lastName,FirebaseAuth.getInstance().getUid(),imageUrl,phoneNumber);
                                                                    reference.child("All Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(all_userMmber);
                                                                    reference.child("faculty").child(phoneNumber).setValue(facultyRegistrationModel);
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }else
                                            {
                                                imageUrl = "https://cdn-icons-png.flaticon.com/512/1057/1057240.png";
                                                FacultyRegistrationModel facultyRegistrationModel = new FacultyRegistrationModel(firstName,lastName,password,collegeId,phoneNumber,imageUrl, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                                                session.setWho("1");
                                                All_UserMmber all_userMmber = new All_UserMmber(firstName+" "+lastName,FirebaseAuth.getInstance().getUid(),imageUrl,phoneNumber);
                                                reference.child("All Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(all_userMmber);
                                                reference.child("faculty").child(phoneNumber).setValue(facultyRegistrationModel);
                                            }
                                        }
                                        dialog.dismiss();
                                        session.setWho("1");
                                        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                        intent.putExtra("flag","faculty");
                                        intent.putExtra("mobile",phoneNumber);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        dialog.dismiss();
                                        Intent intent = new Intent(getApplicationContext(),ResetPasswordActivity.class);
                                        intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                                        intent.putExtra("flag",getIntent().getStringExtra("flag"));
                                        startActivity(intent);
                                        finish();
                                    }
                                }else{
                                    dialog.dismiss();
                                    Toast.makeText(OtpVerificaiton.this, "Enter correct OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        dialog.dismiss();
                        Toast.makeText(OtpVerificaiton.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    dialog.dismiss();
                    Toast.makeText(OtpVerificaiton.this, "Please enter code", Toast.LENGTH_SHORT).show();
                }
            }
        });
        numberOtpMove();
        findViewById(R.id.resendotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60, TimeUnit.SECONDS, OtpVerificaiton.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                dialog.dismiss();
                                Toast.makeText(OtpVerificaiton.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                dialog.dismiss();
                                getOtpBackend = backendOtp;
                            }
                        }
                );
            }
        });
    }
    private void numberOtpMove() {

        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }
}