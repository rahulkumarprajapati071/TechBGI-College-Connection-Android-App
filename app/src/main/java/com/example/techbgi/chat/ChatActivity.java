package com.example.techbgi.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.activity.MemoryData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    private String chatKey;
    private ChatAdapter adapter;
    private final List<ChatList> arrayList = new ArrayList<>();
    private RecyclerView chattingRecyclerView;
    private boolean loadingFirstTime = true;

    String getUserMobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView nameTv = findViewById(R.id.name);
        final EditText messageEditText = findViewById(R.id.messageEditTxt);
        final CircleImageView profilePic = findViewById(R.id.profilePic);
        final ImageView sendBtn = findViewById(R.id.sendBtn);

        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);

        //get data from messages adapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");

        //for getting user mobile number from firebase
        getUserMobile = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();


        nameTv.setText(getName);
        Glide.with(this).load(getProfilePic).into(profilePic);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(ChatActivity.this,arrayList);
        chattingRecyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (chatKey.isEmpty()) {
                    //generate chat key. by default chatKey is 1
                    chatKey = "1";

                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }

                if(snapshot.hasChild("chat")){
                    if (snapshot.child("chat").child(chatKey).hasChild("messages")){
                        arrayList.clear();
                        for(DataSnapshot messagesSnapshot : snapshot.child("chat").child(chatKey).child("messages").getChildren()){
                            if(messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile")){
                                final String messagesTimestamps = messagesSnapshot.getKey();
                                final String getMobile = messagesSnapshot.child("mobile").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);

                                Timestamp timestamp = new Timestamp(Long.parseLong(messagesTimestamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                ChatList chatList1 = new ChatList(getMobile,getName,getMsg,simpleDateFormat.format(date),simpleTimeFormat.format(date));
                                arrayList.add(chatList1);

                                if(loadingFirstTime || Long.parseLong(messagesTimestamps) > Long.parseLong(MemoryData.getLastMsgTS(ChatActivity.this,chatKey))){
                                    loadingFirstTime = false;
                                    MemoryData.saveLastMsgTS(messagesTimestamps, chatKey, ChatActivity.this);
                                    adapter.updateChatList(arrayList);

                                    layoutManager.smoothScrollToPosition(chattingRecyclerView, null, adapter.getItemCount() - 1);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getTxtMessage = messageEditText.getText().toString();

                //get current timestamps
                if(getTxtMessage.trim().length() > 0)
                {
                    final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                    reference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile.substring(3,13));
                    reference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                    reference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessage);
                    reference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("mobile").setValue(getUserMobile);

                    //clear edit textnn
                    messageEditText.setText("");
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}