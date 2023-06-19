package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.adapter.AdapterOfMarks;
import com.example.techbgi.model.MarksStudentModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MarksOfStudentActivity extends AppCompatActivity {

    DatabaseReference reference;
    String classId;
    String type;
    String count;

    RecyclerView recyclermarksofstudent;
    ArrayList<MarksStudentModel> arrayList;

    AdapterOfMarks adapter;

    EditText rollNumber,studentN,studentMarks;

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_of_student);

        Toolbar toolbar = findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Add Student and Marks");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);

        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        classId = getIntent().getStringExtra("classId");
        type = getIntent().getStringExtra("type");
        count = getIntent().getStringExtra("count");

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_student_marks);
        dialog.setCancelable(false);

        rollNumber = dialog.findViewById(R.id.rollno);
        studentN = dialog.findViewById(R.id.name);
        studentMarks = dialog.findViewById(R.id.marks);

        Button add = dialog.findViewById(R.id.addbtn);
        Button cancle = dialog.findViewById(R.id.cancel);

        fab = findViewById(R.id.fabo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<For add student and marks>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rollNo = rollNumber.getText().toString();
                String studentName = studentN.getText().toString();
                String marks = studentMarks.getText().toString();
                if(rollNo.trim().isEmpty() || studentName.trim().isEmpty() || marks.trim().isEmpty()){
                    Toast.makeText(MarksOfStudentActivity.this, "Please fill data", Toast.LENGTH_SHORT).show();
                }else{
                    String id = reference.push().getKey();
                    MarksStudentModel marksStudentModel = new MarksStudentModel(rollNo,studentName,marks,id);
                    reference.child("studentOfMarks").child(classId+type+count).child(id).setValue(marksStudentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MarksOfStudentActivity.this, "succes", Toast.LENGTH_SHORT).show();
                            rollNumber.setClickable(true);
                            rollNumber.setText("");
                            studentN.setText("");
                            studentMarks.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MarksOfStudentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<For add student and marks>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<for show data in recyclerview from firebase>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        recyclermarksofstudent = findViewById(R.id.recyclermarksofstudent);
        arrayList = new ArrayList<>();
        recyclermarksofstudent.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterOfMarks(this,arrayList);
        recyclermarksofstudent.setAdapter(adapter);

        reference.child("studentOfMarks").child(classId+type+count).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MarksStudentModel marksStudentModel = dataSnapshot.getValue(MarksStudentModel.class);
                    arrayList.add(marksStudentModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MarksOfStudentActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<for show data in recyclerview from firebase>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<portion on long press on item of recycler view Edit and Delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()){
            case 101:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_student_marks);
                dialog.setCancelable(false);

                TextView titleOfDialog = dialog.findViewById(R.id.addstudentandmarks);
                titleOfDialog.setText("Update Marks");

                rollNumber = dialog.findViewById(R.id.rollno);
                studentN = dialog.findViewById(R.id.name);
                studentMarks = dialog.findViewById(R.id.marks);
                Button add = dialog.findViewById(R.id.addbtn);
                Button cancle = dialog.findViewById(R.id.cancel);

                rollNumber.setText(arrayList.get(item.getGroupId()).getRollNo());
                studentN.setText(arrayList.get(item.getGroupId()).getStudentName());
                studentMarks.setText(arrayList.get(item.getGroupId()).getMarks());
                rollNumber.setEnabled(false);
                add.setText("Update");
                dialog.show();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String rollNo = rollNumber.getText().toString();
                        String studentName = studentN.getText().toString();
                        String marks = studentMarks.getText().toString();

                        if(rollNo.trim().isEmpty() || studentName.trim().isEmpty() || marks.trim().isEmpty()){
                            Toast.makeText(MarksOfStudentActivity.this, "Please fill data", Toast.LENGTH_SHORT).show();
                        }else{
                            HashMap<String, Object> hs = new HashMap<>();
                            hs.put("rollNo", rollNo);
                            hs.put("marks", marks);
                            hs.put("studentId", arrayList.get(item.getGroupId()).getStudentId());
                            hs.put("studentName", studentName);

                            MarksStudentModel marksStudentModel = new MarksStudentModel(rollNo,studentName,marks,arrayList.get(item.getGroupId()).getStudentId());
                            reference.child("studentOfMarks").child(classId+type+count).child(arrayList.get(item.getGroupId()).getStudentId()).updateChildren(hs).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MarksOfStudentActivity.this, "succes", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(MarksOfStudentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return true;
            case 102:
                reference.child("studentOfMarks").child(classId+type+count).child(arrayList.get(item.getGroupId()).getStudentId()).removeValue();
                return true;
        }
        return false;
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<portion on long press on item of recycler view Edit and Delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}