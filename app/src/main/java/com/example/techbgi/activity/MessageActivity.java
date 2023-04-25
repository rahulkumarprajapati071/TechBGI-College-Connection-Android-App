package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;
import com.example.techbgi.adapter.MessagesAdapter;
import com.example.techbgi.model.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();
    private String mobile;
    private String child;
    private int unseenMessages = 0;
    private String lastMessage = "";
    private String chatKey = "";

    private  boolean dataSet = false;
    private RecyclerView messsagesRecyclerView;
    private DatabaseReference reference;
    private MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);

        messsagesRecyclerView = findViewById(R.id.messsagesRecyclerView);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        //get intent data from HomeScreen.class activity
        mobile = getIntent().getStringExtra("mobile");
        child = getIntent().getStringExtra("child");

        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("child").equals("students")){
                    startActivity(new Intent(getApplicationContext(),StudentProfile.class));
                }else{
                    startActivity(new Intent(getApplicationContext(),FacultyProfile.class));
                }
            }
        });

        messsagesRecyclerView.setHasFixedSize(true);
        messsagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set adapter to recyclerview
        messagesAdapter = new MessagesAdapter(messagesLists,getApplicationContext());

        messsagesRecyclerView.setAdapter(messagesAdapter);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //get profile pic form firebase database for user
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String profilePicUrl = snapshot.child(child).child(mobile).child("imageUri").getValue(String.class);
                assert profilePicUrl != null;
                if(!profilePicUrl.isEmpty()){
                    //set profile pic to circle image view
                    Glide.with(getApplicationContext()).load(profilePicUrl).into(userProfilePic);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                chatKey = "";

                //getting all faculties and do operation of messages
                for(DataSnapshot dataSnapshot : snapshot.child("faculty").getChildren())
                {
                    final String getMobile = dataSnapshot.getKey();
                    dataSet = false;
                    if(!getMobile.equals(mobile)){
                        final String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        final String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("imageUri").getValue(String.class);


                        reference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCounts = (int)snapshot.getChildrenCount();

                                if(getChatCounts > 0)
                                {
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);


                                            if((getUserOne.equals(getMobile) && getUserTwo.equals(mobile)) || (getUserOne.equals(mobile) && getUserTwo.equals(getMobile))){
                                                for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(MessageActivity.this,getKey));

                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if(getMessageKey > getLastSeenMessage){
                                                        unseenMessages++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if(!dataSet){
                                    MessagesList messagesList = new MessagesList(firstName+" "+lastName,getMobile,lastMessage,getProfilePic,unseenMessages,chatKey);
                                    messagesLists.add(messagesList);
                                    messagesAdapter.updateData(messagesLists);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }



                //getting all students do opertation of messages
                for(DataSnapshot dataSnapshot : snapshot.child("students").getChildren())
                {
                    dataSet = false;
                    final String getMobile = dataSnapshot.getKey();
                    if(!getMobile.equals(mobile)){
                        final String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        final String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("imageUri").getValue(String.class);


                        reference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCounts = (int)snapshot.getChildrenCount();

                                if(getChatCounts > 0)
                                {
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);


                                            if((getUserOne.equals(getMobile) && getUserTwo.equals(mobile)) || (getUserOne.equals(mobile) && getUserTwo.equals(getMobile))){
                                                for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(MessageActivity.this,getKey));

                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if(getMessageKey > getLastSeenMessage){
                                                        unseenMessages++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if(!dataSet){
                                    MessagesList messagesList = new MessagesList(firstName+" "+lastName,getMobile,lastMessage,getProfilePic,unseenMessages,chatKey);
                                    messagesLists.add(messagesList);
                                    messagesAdapter.updateData(messagesLists);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                progressDialog.dismiss();
            }
        });




    }
}