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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.techbgi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SelectNotes extends AppCompatActivity {

    EditText firstName,lastName,subject;
    Spinner semester,branch;
    Button uploadButton;
    ImageView notesPreview;
    private Uri pdfData;
    private String pdfName;
    ProgressDialog dialog;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String firstString,lastString,subjectString,semesterString,branchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_notes);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        subject = findViewById(R.id.subject);
        semester = findViewById(R.id.semesterNote);
        branch = findViewById(R.id.branchNote);
        uploadButton = findViewById(R.id.uploadButton);
        notesPreview = findViewById(R.id.notesPreview);

        dialog = new ProgressDialog(this);

        dialog.setMessage("Please wait...");

        String[] valueSem = {"Current Semester","1","2","3","4","5","6","7","8"};
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(valueSem));
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList2);
        semester.setAdapter(arrayAdapter2);

        String[] valueBranch = {"Choose Branch","CSE","AIML","CSBS","CSE-IOT","CSIT","CIVIL","ME","EC","EEE",
                "IT","EE","EX","AUTO","CHEMICAL","FT","MINING","TX","OTHERS"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(valueBranch));
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList1);
        branch.setAdapter(arrayAdapter1);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if(pdfData == null || firstName.getText().toString().trim().isEmpty() || lastName.getText().toString().trim().isEmpty() || semester.getSelectedItemId() == 0 || semester.getSelectedItemId() == 0 || subject.getText().toString().trim().isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(SelectNotes.this, "Please fill all requirements", Toast.LENGTH_SHORT).show();
                }else{
                    StorageReference reference1 = storageReference.child("NotesPdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
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
                            dialog.dismiss();
                            Toast.makeText(SelectNotes.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private void uploadData(String valueOf) {
                firstString = firstName.getText().toString();
                lastString = lastName.getText().toString();
                subjectString = subject.getText().toString();
                semesterString = semester.getSelectedItem().toString();
                branchString = branch.getSelectedItem().toString();
                String uniqueKey = reference.child("Notes").child(semesterString+branchString).push().getKey();

                HashMap data = new HashMap();
                data.put("pdfSubject",subjectString);
                data.put("name",firstString+" "+lastString);
                data.put("pdfUrl",valueOf);
                data.put("noteView",0);
                reference.child("Notes").child(semesterString+branchString).child(uniqueKey).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(SelectNotes.this, "Notes Added succesfuly", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(SelectNotes.this, "Failed to upload Notes", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        notesPreview.setOnClickListener(new View.OnClickListener() {
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
            notesPreview.setImageResource(R.drawable.pdfpdf);
        }
    }
}