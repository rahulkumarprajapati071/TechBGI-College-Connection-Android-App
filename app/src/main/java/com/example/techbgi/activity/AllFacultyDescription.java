package com.example.techbgi.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.model.FacultyRegistrationModel;
import com.example.techbgi.model.TestResultModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AllFacultyDescription extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button callfaculty,chatfaculty;
    DatabaseReference reference;
    String name,phone, imageUrl,uid;
    private TextView firstName,lastName,specialization,experience,status,email,mobile;

    public AllFacultyDescription() {
    }
    public AllFacultyDescription(String name, String phone, String imageUrl) {
        this.name = name;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    public static AllFacultyDescription newInstance(String param1, String param2) {
        AllFacultyDescription fragment = new AllFacultyDescription();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.description_faculty_all, container, false);

        Toolbar toolbar = view.findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Profile");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);

        ImageView imageholder = view.findViewById(R.id.imageholder);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        specialization = view.findViewById(R.id.spec);
        experience = view.findViewById(R.id.exp);
        status = view.findViewById(R.id.status);
        mobile = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        callfaculty = view.findViewById(R.id.callfaculty);
        chatfaculty = view.findViewById(R.id.chatfaculty);


        reference = FirebaseDatabase.getInstance().getReference().child("faculty");
        reference.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FacultyRegistrationModel data = snapshot.getValue(FacultyRegistrationModel.class);
                uid = data.getUid();
                setDetails(data.getEmail(),data.getExperience(),data.getFirstName(),data.getLastName(),data.getPhoneNumber(),data.getStatus(),data.getSpecialization());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        callfaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},101);
                }else{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+phone));
                    startActivity(intent);
                }
            }
        });

        chatfaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MessageActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("url",imageUrl);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });
        Glide.with(getContext()).load(imageUrl).into(imageholder);
        return  view;
    }

    private void setDetails(String emailg, String exp, String first, String last, String phoneNumber, String sts, String spec) {
        firstName.setText(first);
        lastName.setText(last);
        specialization.setText(spec);
        experience.setText(exp);
        email.setText(emailg);
        mobile.setText(phoneNumber);
        status.setText(sts);
    }

    public void onBackPressed(){
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new AllFacultyFragment()).addToBackStack(null).commit();
    }
}