package com.example.techbgi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.techbgi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class UploadNotesFaculty extends AppCompatActivity {

    Spinner semester,branch;
    FloatingActionButton fabNote;
    Button next;
    String semesterString,branchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes_faculty);

        semester = findViewById(R.id.semester);
        branch = findViewById(R.id.branch);
        next = findViewById(R.id.next);
        fabNote = findViewById(R.id.fabNote);

        String[] valueSem = {"Current Semester","1","2","3","4","5","6","7","8"};
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(valueSem));
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList2);
        semester.setAdapter(arrayAdapter2);

        String[] valueBranch = {"Choose Branch","CSE","AIML","CSBS","CSE-IOT","CSIT","CIVIL","ME","EC","EEE",
                "IT","EE","EX","AUTO","CHEMICAL","FT","MINING","TX","OTHERS"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(valueBranch));
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList1);
        branch.setAdapter(arrayAdapter1);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(semester.getSelectedItemId() == 0 || branch.getSelectedItemId() == 0){
                    Toast.makeText(UploadNotesFaculty.this, "Select all requirements", Toast.LENGTH_SHORT).show();
                }else{
                    semesterString = semester.getSelectedItem().toString();
                    branchString = branch.getSelectedItem().toString();
                    Intent intent = new Intent(getApplicationContext(),AllNotesScreen.class);
                    intent.putExtra("semester",semesterString);
                    intent.putExtra("branch",branchString);
                    startActivity(intent);
                }
            }
        });

        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SelectNotes.class);
                startActivity(intent);
            }
        });


    }
}