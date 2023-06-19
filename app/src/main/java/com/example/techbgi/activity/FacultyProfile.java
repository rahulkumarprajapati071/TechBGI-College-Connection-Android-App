package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.fullscreen.BaseActivity;
import com.example.techbgi.model.All_UserMmber;
import com.example.techbgi.model.FacultyRegistrationModel;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class FacultyProfile extends BaseActivity {
    EditText firstName,lastName,status,spec,exp,email;
    TextView collegeId,username,change,phone;
    Button updateBtn,logoutBtn,deleteBtn;
    ImageView profileImage,back;
    Uri imageUri;
    private String pass;
    String imageUrl;
    String image;
    SharedPreferenceSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        setContentView(R.layout.activity_faculty_profile);

        session = new SharedPreferenceSession(this);

        findId();


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.password_change_dialog);
        dialog.setCancelable(false);

        reference.child("faculty").child(getIntent().getStringExtra("mobile")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                image = Objects.requireNonNull(snapshot.child("imageUri").getValue()).toString();
                String firstname = snapshot.child("firstName").getValue(String.class);
                String lastname = snapshot.child("lastName").getValue(String.class);
                String password = snapshot.child("password").getValue(String.class);
                String phone1 = snapshot.child("phoneNumber").getValue(String.class);
                String collgeid = snapshot.child("collegeId").getValue(String.class);
                String status1 = snapshot.child("status").getValue(String.class);
                String specialization = snapshot.child("specialization").getValue(String.class);
                String experience = snapshot.child("experience").getValue(String.class);
                String email1 = snapshot.child("email").getValue(String.class);

                Glide.with(getApplicationContext()).load(image).into(profileImage);
                collegeId.setText(collgeid);
                firstName.setText(firstname);
                lastName.setText(lastname);
                phone.setText(phone1);
                username.setText(firstname+" "+lastname);
                pass = password;
                status.setText(status1 == null?"Asst.Professor":status1);
                spec.setText(specialization == null?"Opto Electronics":specialization);
                exp.setText(experience == null?"11 Years":experience);
                email.setText(email1==null?"jayesh.joshi@sdbc.ac.in":email1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference storageReference = firebaseStorage.getReference().child("facultyImages/"+collegeId.getText().toString());
                if(imageUri != null)
                {
                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        imageUrl = uri.toString();
                                        FacultyRegistrationModel facultyRegistrationModel = new FacultyRegistrationModel(firstName.getText().toString(),FirebaseAuth.getInstance().getUid(),lastName.getText().toString(),pass,collegeId.getText().toString(),phone.getText().toString(),imageUrl,spec.getText().toString(),email.getText().toString(),exp.getText().toString(),status.getText().toString());
                                        All_UserMmber all_userMmber = new All_UserMmber(firstName.getText().toString()+" "+lastName.getText().toString(), FirebaseAuth.getInstance().getUid(),imageUrl);
                                        reference.child("All Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(all_userMmber);
                                        reference.child("faculty").child(phone.getText().toString()).setValue(facultyRegistrationModel);
                                    }
                                });
                            }
                        }
                    });
                }else{
                    FacultyRegistrationModel facultyRegistrationModel = new FacultyRegistrationModel(firstName.getText().toString(),FirebaseAuth.getInstance().getUid(),lastName.getText().toString(),pass,collegeId.getText().toString(),phone.getText().toString(),image,spec.getText().toString(),email.getText().toString(),exp.getText().toString(),status.getText().toString());
                    All_UserMmber all_userMmber = new All_UserMmber(firstName.getText().toString()+" "+lastName.getText().toString(), FirebaseAuth.getInstance().getUid(),image);
                    reference.child("All Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(all_userMmber);
                    reference.child("faculty").child(phone.getText().toString()).setValue(facultyRegistrationModel);
                }
                Toast.makeText(FacultyProfile.this, "Profile Successfuly Updated", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(FacultyProfile.this);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete your account?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent arguments = new Intent(getApplicationContext(), DeleteAccountVerification.class);
                        arguments.putExtra("mobileNumber", phone.getText().toString());
                        arguments.putExtra("flag", "1");
                        startActivity(arguments);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FacultyProfile.this);
                builder.setTitle("Confirm Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(FacultyProfile.this, "LOGOUT", Toast.LENGTH_SHORT).show();
                        session.setWho("none");
                        startActivity(new Intent(getApplicationContext(), FrontScreen.class));
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button cancel = dialog.findViewById(R.id.cancel);
                Button change = dialog.findViewById(R.id.change);

                TextView curpass = dialog.findViewById(R.id.currentpassword);
                TextView newpass = dialog.findViewById(R.id.newpassword);
                TextView repass = dialog.findViewById(R.id.confirmpassword);

                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(Objects.requireNonNull(snapshot.child("faculty").child(getIntent().getStringExtra("mobile")).child("password").getValue()).toString().equals(curpass.getText().toString())){
                                    if(newpass.getText().toString().equals(repass.getText().toString())){
                                        reference.child("faculty").child(getIntent().getStringExtra("mobile")).child("password").setValue(pass);
                                        dialog.dismiss();
                                    }else{
                                        Toast.makeText(FacultyProfile.this, "Confirm password not matching", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(FacultyProfile.this, "Current password is not correct", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });

            }
        });
    }


    public void findId()
    {
        profileImage = findViewById(R.id.profile_image);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        collegeId = findViewById(R.id.collegeId);
        updateBtn = findViewById(R.id.updateBtn);
        phone = findViewById(R.id.mobile);
        status = findViewById(R.id.status);
        email = findViewById(R.id.email);
        exp = findViewById(R.id.exp);
        spec = findViewById(R.id.spec);
        username = findViewById(R.id.username);
        change = findViewById(R.id.password);
        back = findViewById(R.id.back);
        logoutBtn = findViewById(R.id.logout);
        deleteBtn = findViewById(R.id.delete);
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
}