package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;
import com.example.techbgi.model.NoticeModel;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class NoticeScreen extends AppCompatActivity {

    Button uploadFile;
    ImageView uploadimage,uploadPdf,imagePreview;
    EditText titlenotice;
    LinearLayout layout;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri pdfData;
    private Uri imageData;
    private Bitmap bitmap;
    private String title;
    private int req;
    private String pdfName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_screen);

        uploadFile = findViewById(R.id.uploadFile);
        uploadimage = findViewById(R.id.uploadimage);
        uploadPdf = findViewById(R.id.uploadpdf);
        titlenotice = findViewById(R.id.titlenotice);
        imagePreview = findViewById(R.id.imagePreview);
        layout = findViewById(R.id.layouts);



        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titlenotice.getText().toString();
                if(title.isEmpty()){
                    titlenotice.setError("empty");
                    titlenotice.requestFocus();
                }else if(pdfData == null && imageData == null){
                    Toast.makeText(NoticeScreen.this, "Please select file", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(NoticeScreen.this, "data uploaded", Toast.LENGTH_SHORT).show();
                    uploadFileInfirebase();
                }
            }
        });
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageManager();
            }
        });
        uploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfManager();
            }
        });

    }

    private void uploadFileInfirebase() {
        if(req == 1){
            StorageReference reference1 = storageReference.child("NoticeData/"+"image"+"-"+System.currentTimeMillis());
            reference1.putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isComplete());
                    Uri uri = uriTask.getResult();
                    uploadData(String.valueOf(uri));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NoticeScreen.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            StorageReference reference1 = storageReference.child("NoticeData/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
            reference1.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isComplete());
                    Uri uri = uriTask.getResult();
                    uploadData(String.valueOf(uri));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NoticeScreen.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private void uploadData(String valueOf) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String mobile = Objects.requireNonNull(auth.getCurrentUser()).getPhoneNumber().substring(3,13);
        String uniqueKey = reference.child("Notice").push().getKey();

        //<-----------------------Geting faculty image and name from faculty database------------------------->

        reference.child("faculty").child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userimageurl;
                String username;
                userimageurl = snapshot.child("imageUri").getValue(String.class);
                username = snapshot.child("firstName").getValue(String.class)+" "+snapshot.child("lastName").getValue(String.class);
                NoticeModel noticeModel;
                if(req == 1){
                    noticeModel = new NoticeModel(title,valueOf,username,userimageurl,"image");
                }else{
                    noticeModel = new NoticeModel(title,valueOf,username,userimageurl,"pdf");
                }
                reference.child("Notice").child(uniqueKey).setValue(noticeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(NoticeScreen.this, "Notice Added succesfuly", Toast.LENGTH_SHORT).show();
                        titlenotice.setText("");
                        imagePreview.setImageResource(0);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoticeScreen.this, "Failed to upload notice", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //<-----------------------Geting faculty image and name from faculty database------------------------->
        
    }

    private void openPdfManager() {
        Intent pickPdf = new Intent();
        pickPdf.setType("application/pdf");
        pickPdf.addCategory(Intent.CATEGORY_OPENABLE);
        pickPdf.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(pickPdf,"Select File"),2);

    }

    private void OpenImageManager() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            req = requestCode;
            imageData = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageData);
                Toast.makeText(this, "Image added succesfull", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imagePreview.setImageBitmap(bitmap);

        }else{
            if(data != null && data.getData() != null){
                req = requestCode;
                Toast.makeText(this, "PDF added succesfull", Toast.LENGTH_SHORT).show();
                pdfData = data.getData();
                if(pdfData.toString().startsWith("file://")){
                    pdfName = new File(pdfData.toString()).getName();
                }
                imagePreview.setImageResource(R.drawable.pdfpdf);
            }
        }
    }
}