package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;
import com.example.techbgi.model.ClassItem;
import com.example.techbgi.model.CompanyModel;
import com.example.techbgi.model.StudentRegistrationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UploadMarks extends AppCompatActivity {

    EditText classname, subjectname;
    ImageView imageView;

    FirebaseDatabase database;

    DbHelper dbHelper = new DbHelper();
    DatabaseReference reference;

    FirebaseStorage firebaseStorage;

    FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_marks);

        Toolbar toolbar = findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Add Class");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameOfMarks,new ClassMarksFragment()).commit();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_class_and_subject_dialog);
        dialog.setCancelable(false);


        database = FirebaseDatabase.getInstance();
        reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
        firebaseStorage = FirebaseStorage.getInstance();

        Button add = dialog.findViewById(R.id.addbtn);
        Button cancle = dialog.findViewById(R.id.cancel);

        classname = dialog.findViewById(R.id.classname);
        subjectname = dialog.findViewById(R.id.subjectname);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = classname.getText().toString();
                String subjectName = subjectname.getText().toString();
                if(className.trim().isEmpty() || subjectName.trim().isEmpty()){
                    Toast.makeText(UploadMarks.this, "Please fill all data", Toast.LENGTH_SHORT).show();
                }else{
                    String id = reference.child("classOfMarks").push().getKey();
                    ClassItem classItem = new ClassItem(id,className,subjectName);
                    assert id != null;
                    reference.child("classOfMarks").child(id).setValue(classItem).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadMarks.this, "Class Not Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    subjectname.setText("");
                    classname.setText("");
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}