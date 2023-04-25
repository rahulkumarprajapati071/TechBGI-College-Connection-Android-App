package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewImage extends AppCompatActivity {
    ImageView imageView;
    TextView filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = findViewById(R.id.image);
        filename = findViewById(R.id.filename);
        String uriString = getIntent().getStringExtra("fileurl");
        String nameString = getIntent().getStringExtra("filename");

        filename.setText(nameString);
        Uri uri = Uri.parse(uriString);

        Glide.with(this).load(uri).into(imageView);

    }
}