package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;
import com.example.techbgi.model.FacultyRegistrationModel;
import com.example.techbgi.model.StudentRegistrationModel;
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
    EditText firstName,lastName,phone;
    TextView collegeId,username,change;
    Button updateBtn;
    ImageView profileImage,back;
    Uri imageUri;
    private String pass;
    String imageUrl;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        setContentView(R.layout.activity_faculty_profile);

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

                Glide.with(getApplicationContext()).load(image).into(profileImage);
                collegeId.setText(collgeid);
                firstName.setText(firstname);
                lastName.setText(lastname);
                phone.setText(phone1);
                username.setText(firstname+" "+lastname);
                pass = password;
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
                                        FacultyRegistrationModel facultyRegistrationModel = new FacultyRegistrationModel(firstName.getText().toString(),lastName.getText().toString(),pass,collegeId.getText().toString(),phone.getText().toString(),imageUrl);
                                        reference.child("faculty").child(phone.getText().toString()).setValue(facultyRegistrationModel);
                                    }
                                });
                            }
                        }
                    });
                }else{
                    FacultyRegistrationModel facultyRegistrationModel = new FacultyRegistrationModel(firstName.getText().toString(),lastName.getText().toString(),pass,collegeId.getText().toString(),phone.getText().toString(),image);
                    reference.child("faculty").child(phone.getText().toString()).setValue(facultyRegistrationModel);
                }
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
                                        pass = newpass.getText().toString();
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
        phone = findViewById(R.id.phone);
        phone.setEnabled(false);
        username = findViewById(R.id.username);
        change = findViewById(R.id.password);
        back = findViewById(R.id.back);
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