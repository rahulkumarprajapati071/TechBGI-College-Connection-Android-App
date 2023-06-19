package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.adapter.StudentAdapter;
import com.example.techbgi.database.DbHelper;
import com.example.techbgi.dialog.MyDialog;
import com.example.techbgi.model.StudentItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class StudentAttandenceActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String subjectName;
    private StudentAdapter adapter;
    ArrayList<StudentItem> arraylist =  new ArrayList<>();

    private String classId;
    private  MyCalender calender;
      TextView subtitle;
    DbHelper dbHelper = new DbHelper();

     final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
     final DatabaseReference reference = firebaseDatabase.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_attandence);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calender = new MyCalender();

        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        int position = intent.getIntExtra("position", -1);
        classId = intent.getStringExtra("classId");

        setToolBar();
        loadStudentData();
        RecyclerView recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true); 
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,arraylist);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this::changetStatus);

    }
//    load student data from firebase realtime database
    public void loadStudentData() {

        reference.child("studenttable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arraylist.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    StudentItem studentItem = dataSnapshot.getValue(StudentItem.class);
                    assert studentItem != null;
                    if(studentItem.getClassId().equals(classId))
                    {
                        arraylist.add(studentItem);
                    }
                }
                for(StudentItem studentItem: arraylist){
                    String studentId = studentItem.getStudentId();
                    String date = calender.getDate();
                    reference.child("statustable").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot snap : snapshot.getChildren()){
                                    if(snap.getKey().matches(studentId+date)){
                                        String status = snap.child("status").getValue().toString();
                                        studentItem.setStatus(status);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if(arraylist.size() != 0){
                    TextView txv = findViewById(R.id.txv);
                    txv.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void changetStatus(int position) {
        String status = arraylist.get(position).getStatus();
        if(status.equals("P")) status = "A";
        else status = "P";

        arraylist.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStatus();
            }
        });

        title.setText(className);
        subtitle.setText(subjectName+" | "+calender.getDate());

        back.setOnClickListener(v->onBackPressed());

        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }


    private void saveStatus() {
        if(arraylist.size() > 0)
        {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(StudentItem studentItem: arraylist){

                        String className = snapshot.child("classtable").child(classId).child("className").getValue().toString();
                        String semester = snapshot.child("classtable").child(classId).child("semester").getValue().toString();
                        String subjectName = snapshot.child("classtable").child(classId).child("subjectName").getValue().toString();
                        String status = studentItem.getStatus();
                        if(!Objects.equals(status, "P")) status = "A";
                        dbHelper.addStatus(semester,className,subjectName, studentItem.getStudentName(), studentItem.getRollNo(), studentItem.getStudentId(),classId,calender.getDate(),status);
                    }
                    Toast.makeText(StudentAttandenceActivity.this, "Attendence Added succesfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(StudentAttandenceActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "First add some students here", Toast.LENGTH_SHORT).show();
        }
    }
    // studentIems null hai problemmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm<
//    public void loadStatusData(){
//
//        //<<<<<<<<<<<<<<---another login--->>>>>>>>>>>>>>>>>>>
//
//        for(StudentItem studentItem: arraylist){
//            String studentId = studentItem.getStudentId();
//            String date = calender.getDate();
//            reference.child("statustable").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()){
//                        for(DataSnapshot snap : snapshot.getChildren()){
//                            if(snap.getKey().matches(studentId+date)){
//                                String status = snap.child("status").getValue().toString();
//                                studentItem.setStatus(status);
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//
//    }
    private boolean onMenuItemClick(MenuItem menuItem){
        if(menuItem.getItemId() == R.id.add_student)
        {
            showAddStudentDialog();
        }
        else if (menuItem.getItemId() == R.id.show_calender) {
            showCalender();
        }else if (menuItem.getItemId() == R.id.show_attendance_sheet) {
            openSheetList();
        }
        return true;
    }

    private void openSheetList() {
        reference.child("studenttable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arraylist.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    StudentItem studentItem = dataSnapshot.getValue(StudentItem.class);
                    assert studentItem != null;
                    if(studentItem.getClassId().equals(classId))
                    {
                        arraylist.add(studentItem);
                    }
                }
                String[] idArray = new String[arraylist.size()];
                String[] rollNoArray = new String[arraylist.size()];
                String[] studentNameArray = new String[arraylist.size()];
                for(int i = 0; i < idArray.length; i++)
                {
                    idArray[i] = arraylist.get(i).getStudentId();
                }
                for(int i = 0; i < rollNoArray.length; i++)
                {
                    rollNoArray[i] = arraylist.get(i).getRollNo();
                }
                for(int i = 0; i < studentNameArray.length; i++)
                {
                    studentNameArray[i] = arraylist.get(i).getStudentName();
                }
                Intent intent = new Intent(getApplicationContext(),SheeListActivity.class);
                intent.putExtra("classId",classId);
                intent.putExtra("idArray",idArray);
                intent.putExtra("rollNoArray",rollNoArray);
                intent.putExtra("studentNameArray",studentNameArray);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showCalender() {

        calender.show(getSupportFragmentManager(),"");
        calender.setOnCalenderOkClickListener(this::onCalendarOkClicker);
    }

    private void onCalendarOkClicker(int year, int month, int day) {
        calender.setDate(year,month,day);
        subtitle.setText(subjectName+" | "+calender.getDate());
        loadStudentData();
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListenter((blank,rollNo,studentName)->addStudent(rollNo,studentName));
    }

    private void addStudent(String rollNo, String studentName) {
        if(rollNo.length() > 0 && studentName.length() > 0)
        {
            dbHelper = new DbHelper();
            String studentId = dbHelper.addStudent(classId,rollNo,studentName);
            arraylist.add(new StudentItem(studentId,studentName,rollNo));
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this, "Please add rollnumber and name both", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(arraylist.get(position).getRollNo(),arraylist.get(position).getStudentName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListenter((blank,rollNo,studentName)->updateStudent(position,rollNo,studentName));
    }

    private void updateStudent(int position, String rollNo, String studentName) {
        dbHelper.updateStudentData(arraylist.get(position).getStudentId(),studentName);
        arraylist.get(position).setStudentName(studentName);
        arraylist.get(position).setRollNo(rollNo);
//        adapter.notifyItemChanged(position);
        adapter.notifyDataSetChanged();
    }

    private void deleteClass(int position) {
        dbHelper.deleteStudent(arraylist.get(position).getStudentId(),calender.getDate());
        arraylist.remove(position);
//        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
    }
}