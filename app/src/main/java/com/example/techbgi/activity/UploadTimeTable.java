package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UploadTimeTable extends AppCompatActivity {

    Spinner semester,branch;
    Button uplaod;
    ImageView timetable;
    private Uri pdfData;
    private String pdfName;
    ProgressDialog progressDialog;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String semesterString,branchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_time_table);

        progressDialog = new ProgressDialog(this);


        semester = findViewById(R.id.semestertime);
        branch = findViewById(R.id.branchtime);
        uplaod = findViewById(R.id.upload);
        timetable = findViewById(R.id.timetable);

        TextView bc = findViewById(R.id.bc);
        View view = findViewById(R.id.v);
        TextView tx = findViewById(R.id.tx);

        progressDialog.setMessage("Please wait...");

        String[] valueSem = {"Current Semester","1","2","3","4","5","6","7","8"};
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(valueSem));
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList2);
        semester.setAdapter(arrayAdapter2);

        String[] valueBranch = {"Choose Branch","CSE","AIML","CSBS","CSE-IOT","CSIT","CIVIL","ME","EC","EEE",
                "IT","EE","EX","AUTO","CHEMICAL","FT","MINING","TX","OTHERS"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(valueBranch));
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList1);
        branch.setAdapter(arrayAdapter1);

        if(getIntent().getStringExtra("flag").equals("student")){
            uplaod.setText("NEXT");
            timetable.setVisibility(View.GONE);
            bc.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            tx.setText("Time Table");
        }

        uplaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(getIntent().getStringExtra("flag").equals("student")){
                    if(semester.getSelectedItemId() == 0 || branch.getSelectedItemId() == 0){
                        progressDialog.dismiss();
                        Toast.makeText(UploadTimeTable.this, "Please select all requirements", Toast.LENGTH_SHORT).show();
                    }else{
                        semesterString = semester.getSelectedItem().toString();
                        branchString = branch.getSelectedItem().toString();
                        reference.child("timetable").child(semesterString+branchString).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(),ViewPDF.class);
                                intent.putExtra("filename","Time Table");
                                intent.putExtra("fileurl",snapshot.child("pdfUrl").getValue(String.class));
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }else{
                    if(pdfData == null || semester.getSelectedItemId() == 0 || branch.getSelectedItemId() == 0){
                        progressDialog.dismiss();
                        Toast.makeText(UploadTimeTable.this, "Please select all requirements", Toast.LENGTH_SHORT).show();
                    }else{
                        StorageReference reference1 = storageReference.child("timetable/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
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
                                progressDialog.dismiss();
                                Toast.makeText(UploadTimeTable.this, "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            private void uploadData(String valueOf) {
                semesterString = semester.getSelectedItem().toString();
                branchString = branch.getSelectedItem().toString();

                HashMap data = new HashMap();
                data.put("pdfUrl",valueOf);
                reference.child("timetable").child(semesterString+branchString).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadTimeTable.this, "TimeTable Added succesfuly", Toast.LENGTH_SHORT).show();
                        timetable.setImageResource(0);
                        semester.setSelection(0);
                        branch.setSelection(0);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadTimeTable.this, "Failed to upload TimeTable", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfManager();
            }
        });
    }
    private void openPdfManager() {
        Intent pickPdf = new Intent();
        pickPdf.setType("application/pdf");
        pickPdf.addCategory(Intent.CATEGORY_OPENABLE);
        pickPdf.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(pickPdf,"Select File"),1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Toast.makeText(this, "PDF added succesfull", Toast.LENGTH_SHORT).show();
            pdfData = data.getData();
            if (pdfData.toString().startsWith("file://")) {
                pdfName = new File(pdfData.toString()).getName();
            }
            timetable.setImageResource(R.drawable.pdfpdf);
        }
    }
}