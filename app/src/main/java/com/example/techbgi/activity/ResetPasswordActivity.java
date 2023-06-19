package com.example.techbgi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.techbgi.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText newpass,repass;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newpass = findViewById(R.id.newpass);
        repass = findViewById(R.id.repass);

        submit = findViewById(R.id.submit);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if(newpass.getText().toString().trim().length() > 0){
                    if(newpass.getText().toString().trim().equals(repass.getText().toString().trim())){
                        reference.child(getIntent().getStringExtra("flag")).child(getIntent().getStringExtra("mobile")).child("password").setValue(newpass.getText().toString().trim());
                        Toast.makeText(ResetPasswordActivity.this, "Congrates! password reset successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        if(getIntent().getStringExtra("flag").equals("students")){
                            startActivity(new Intent(getApplicationContext(),StudentSignInScreen.class));
                            finish();
                        }else{
                            startActivity(new Intent(getApplicationContext(),FacultySignInActivity.class));
                            finish();
                        }
                    }else{
                        dialog.dismiss();
                        Toast.makeText(ResetPasswordActivity.this, "Confirm password not matching", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(ResetPasswordActivity.this, "Please enter new password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}