package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.database.DbHelper;
import com.example.techbgi.dialog.MyDialog;
import com.example.techbgi.model.ClassItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class UploadMarks extends AppCompatActivity {

    EditText classname, subjectname;
    ImageView imageView;

    FirebaseDatabase database;

    DbHelper dbHelper = new DbHelper();
    DatabaseReference reference;
    FirebaseUser firebaseUser;


    FirebaseStorage firebaseStorage;
    MyDialog dialog = new MyDialog();

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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        classname = dialog.findViewById(R.id.classname);
        subjectname = dialog.findViewById(R.id.subjectname);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }
    private void showDialog() {
        dialog.show(getSupportFragmentManager(), MyDialog.CLASS_ADD_DIALOG);
        dialog.setListenter((semester,className, subjectName) -> {
            addClass(semester,className, subjectName);
            dialog.dismiss();
        });

    }

    private void addClass(String semester, String className, String subjectName) {
        String id = reference.child("classOfMarks").push().getKey();
        ClassItem classItem = new ClassItem(className,FirebaseAuth.getInstance().getUid(),semester,subjectName,id);
        assert id != null;
        reference.child("classOfMarks").child(firebaseUser.getUid()).child(id).setValue(classItem).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadMarks.this, "Class Not Added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}