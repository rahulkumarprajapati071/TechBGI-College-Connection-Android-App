package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.techbgi.R;
import com.example.techbgi.adapter.ClassAdapter;
import com.example.techbgi.database.DbHelper;
import com.example.techbgi.dialog.MyDialog;
import com.example.techbgi.model.ClassItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttendanceTaking extends AppCompatActivity {

    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems = new ArrayList<>();
    Toolbar toolbar;
    DbHelper dbHelper;


    FloatingActionButton fab;
     final FirebaseDatabase database = FirebaseDatabase.getInstance();
     final DatabaseReference reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    MyDialog dialog = new MyDialog();
    private String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_taking);

        setToolBar();

        dbHelper = new DbHelper();

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        classAdapter = new ClassAdapter(this, classItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivtity(position));

        loadClassData();
    }

    //load class data from firebase realtime database
    public void loadClassData() {
        reference.child("classtable").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ClassItem classItem = dataSnapshot.getValue(ClassItem.class);
                    assert classItem != null;
                    classItems.add(classItem);
                }
                if (classItems.size() != 0) {
                    TextView txv = findViewById(R.id.txv);
                    txv.setVisibility(View.GONE);
                }
                classAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("loadPost:onCancelled", error.toString());
            }
        });
    }


    private void setToolBar() {
        toolbar = findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Attendance");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivtity(int position) {
        Intent intent = new Intent(this,StudentAttandenceActivity.class);

        intent.putExtra("className", classItems.get(position).getClassName());
        intent.putExtra("subjectName", classItems.get(position).getSubjectName());
        intent.putExtra("position",position);
        classId = classItems.get(position).getClassId();
        intent.putExtra("classId",classItems.get(position).getClassId());
        startActivity(intent);
    }

    private void showDialog() {
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
        dialog.setListenter((semester,className, subjectName) -> {
            addClass(semester,className, subjectName);
            dialog.dismiss();
        });

    }

    private void addClass(String semester, String className, String subjectName) {
        dbHelper = new DbHelper();
        dbHelper.addClass(semester, className, subjectName, FirebaseAuth.getInstance().getUid());
        recyclerView.scrollToPosition(classItems.size() - 1);
        dialog.dismiss();
        loadClassData();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog = new MyDialog(classItems.get(position).getSemester(),classItems.get(position).getClassName(),classItems.get(position).getSubjectName());
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_UPDATE_DIALOG);
        dialog.setListenter((semester,className,subjectName)->updateClass(position,semester,className,subjectName));
    }

    private void updateClass(int position, String semester,String className, String subjectName) {
        dbHelper.updateClassData(classItems.get(position).getClassId(),semester,className,subjectName,FirebaseAuth.getInstance().getCurrentUser().getUid());
        loadClassData();

    }

    private void deleteClass(int position) {
        dbHelper.deleteClass(classItems.get(position).getClassId(),classItems.get(position).getSemester(),classItems.get(position).getClassName(),classItems.get(position).getSubjectName());
        loadClassData();
    }
}