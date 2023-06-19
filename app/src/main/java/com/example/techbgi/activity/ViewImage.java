package com.example.techbgi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;

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