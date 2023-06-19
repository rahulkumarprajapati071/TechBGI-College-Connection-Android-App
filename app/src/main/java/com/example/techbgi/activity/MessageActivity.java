package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.adapter.MessagesAdapter;
import com.example.techbgi.database.MessageDatabaseHelper;
import com.example.techbgi.model.MessageMember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    String reciverImage,reciverUID,reciverName,senderUID;
    CircleImageView profileImage;
    TextView reciverNames,sts;
    FirebaseDatabase database;
    FirebaseAuth auth;
    public static String senderImage;
    public static String recImage;

    RecyclerView messageAdapter;
    ArrayList<MessageMember> messagesArrayList;

    ImageView sendBtn;
    private ToggleButton autoDeleteToggle;
    EditText edtMessage;

    String senderRoom,receiverRoom;

    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        reciverImage = getIntent().getStringExtra("url");
        reciverName = getIntent().getStringExtra("name");
        reciverUID = getIntent().getStringExtra("uid");
        senderUID = auth.getUid();

        autoDeleteToggle = findViewById(R.id.autoDeleteToggle);

        messagesArrayList = new ArrayList<>();

        senderRoom = senderUID+reciverUID;
        receiverRoom = reciverUID+senderUID;

        profileImage = findViewById(R.id.profilePic);
        reciverNames = findViewById(R.id.name);
        sendBtn = findViewById(R.id.sendBtn);
        edtMessage = findViewById(R.id.messageEditTxt);
        messageAdapter = findViewById(R.id.chattingRecyclerView);
        sts = findViewById(R.id.sts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        adapter = new MessagesAdapter(getApplicationContext(),messagesArrayList);
        messageAdapter.setAdapter(adapter);

        Glide.with(profileImage).load(reciverImage).into(profileImage);

        reciverNames.setText(reciverName);

        DatabaseReference reference = database.getReference().child("All Users").child(auth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");


        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageMember messages = dataSnapshot.getValue(MessageMember.class);
                    messagesArrayList.add(messages);
                }

// Update the local database with the received messages
                MessageDatabaseHelper databaseHelper = new MessageDatabaseHelper(getApplicationContext());
                databaseHelper.updateMessages(messagesArrayList);

                adapter.notifyDataSetChanged();

// Scroll to the last position only if the last message is already visible
                int messageCount = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (messageCount > 1 && lastVisiblePosition >= (messageCount - 2))) {
                    messageAdapter.scrollToPosition(messageCount - 1);
                }
            }

            //fdsdfsfsf

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled() event
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImage = snapshot.child("url").getValue().toString();
                recImage = reciverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //rahul ruk ja ayaa khartta hai

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();

                if (message.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter something", Toast.LENGTH_SHORT).show();
                    return;
                }
                edtMessage.setText("");
                Date date = new Date();

                MessageMember messages = new MessageMember(message, senderUID, date.getTime());

                // Save the message to the sender's local database
                MessageDatabaseHelper senderDatabaseHelper = new MessageDatabaseHelper(getApplicationContext());
                senderDatabaseHelper.insertMessage(messages);

                // Save the message to the receiver's local database
                MessageDatabaseHelper receiverDatabaseHelper = new MessageDatabaseHelper(getApplicationContext());
                receiverDatabaseHelper.insertMessage(messages);

                DatabaseReference senderReference = database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push();

                DatabaseReference receiverReference = database.getReference().child("chats")
                        .child(receiverRoom)
                        .child("messages")
                        .push();

                senderReference.setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        receiverReference.setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Scroll to the latest message after sending
                                messageAdapter.smoothScrollToPosition(messagesArrayList.size() - 1);
                                senderDatabaseHelper.close();
                                receiverDatabaseHelper.close();

                                // Delete messages from Firebase after 10 seconds if the toggle is on
                                boolean autoDeleteEnabled = autoDeleteToggle.isChecked();
                                if (autoDeleteEnabled) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            senderReference.removeValue();
                                            receiverReference.removeValue();
                                        }
                                    }, 100000); // 100 seconds delay before deleting messages
                                }
                            }
                        });
                    }
                });
            }
        });



    }
    public void showDeleteConfirmationDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Chat");
        builder.setMessage("Are you sure you want to delete this chat?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed, delete the chat
                deleteChat();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    private void deleteChat() {
        // Delete the chat from the local database
        MessageDatabaseHelper databaseHelper = new MessageDatabaseHelper(getApplicationContext());
        databaseHelper.deleteChat(senderUID);

        // Remove messages sent by the sender UID from the Firebase database
        DatabaseReference senderChatReference = database.getReference().child("chats").child(senderRoom).child("messages");;
        senderChatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    messageSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        // Remove messages received by the sender UID from the Firebase database
        DatabaseReference receiverChatReference = database.getReference().child("chats").child(receiverRoom).child("messages");
        Query deleteReceivedMessagesQuery = receiverChatReference.orderByChild("senderUID").equalTo(senderUID);
        deleteReceivedMessagesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    messageSnapshot.getRef().removeValue();
                }
                Toast.makeText(getApplicationContext(), "Chat deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateOnlineStatus(true); // Set user's status as online
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("All Users").child(reciverUID).child("online").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean online = snapshot.getValue(Boolean.class);
                if (online) {
                    sts.setText("Online");
                } else {
                    sts.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateOnlineStatus(boolean online) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("All Users").child(user.getUid());
            userRef.child("online").setValue(online);
        }
    }
}

