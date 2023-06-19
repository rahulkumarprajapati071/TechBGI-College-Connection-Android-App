package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.model.CompanyModel;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class AddCompanies extends AppCompatActivity {
    TextView selectDate;
    EditText comname;
    ImageView profileImage;
    Uri imageUri;
    String imageUrl;
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseStorage firebaseStorage;

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_companies);

        Toolbar toolbar = findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Add Companies");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameOfCompany,new CompanyRecyclerFragmetn()).commit();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_company_dialog);
        dialog.setCancelable(false);


        database = FirebaseDatabase.getInstance();
        reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
        firebaseStorage = FirebaseStorage.getInstance();

        Button add = dialog.findViewById(R.id.addbtn);
        Button cancle = dialog.findViewById(R.id.cancel);
        final Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        comname = dialog.findViewById(R.id.companyName);
        selectDate = dialog.findViewById(R.id.selectdate);
        selectDate.setText(currentDate);
        profileImage = dialog.findViewById(R.id.profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
        if(getIntent().getStringExtra("flag").toString().equals("student")){
            fab = findViewById(R.id.fab_main);
            fab.setVisibility(View.GONE);
        }else{
            fab = findViewById(R.id.fab_main);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comname.getText().toString().trim().equals("")){
                    Toast.makeText(AddCompanies.this, "Please add company name", Toast.LENGTH_SHORT).show();
                }else{
                    if(imageUri != null)
                    {
                        String companyName = comname.getText().toString();
                        StorageReference storageReference = firebaseStorage.getReference().child("companyImages/"+companyName);
                        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            imageUrl = uri.toString();
                                            CompanyModel companyModel = new CompanyModel(companyName,currentDate,imageUrl);
                                            String id = reference.child("company").push().getKey();
                                            reference.child("company").child(id).setValue(companyModel);
                                        }
                                    });
                                }
                            }
                        });
                    }else
                    {
                        String companyName = comname.getText().toString();
                        imageUrl = "https://cdn-icons-png.flaticon.com/512/3790/3790058.png";
                        CompanyModel companyModel = new CompanyModel(companyName,currentDate,imageUrl);
                        String id = reference.child("company").push().getKey();
                        reference.child("company").child(id).setValue(companyModel);
                    }
                    profileImage.setImageResource(R.drawable.adduser);
                    comname.setText("");
                    dialog.dismiss();
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