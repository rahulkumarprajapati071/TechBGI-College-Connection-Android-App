package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.model.All_UserMmber;
import com.example.techbgi.viewholder.ProfileViewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference profileRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    RecyclerView recyclerView;
    EditText searchEt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        searchEt = findViewById(R.id.search_user);
        recyclerView = findViewById(R.id.rv_ch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        profileRef = FirebaseDatabase.getInstance().getReference().child("All Users");

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!searchEt.getText().toString().substring(0).equals("")){
                    String up = searchEt.getText().toString().substring(0,1).toUpperCase();
                    String low = searchEt.getText().toString().substring(1).toLowerCase();
                    Query search = profileRef.orderByChild("name").startAt(up).endAt(up + low + "\uf8ff");

                    FirebaseRecyclerOptions<All_UserMmber> options =
                            new FirebaseRecyclerOptions.Builder<All_UserMmber>()
                                    .setQuery(search,All_UserMmber.class)
                                    .build();

                    FirebaseRecyclerAdapter<All_UserMmber, ProfileViewholder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<All_UserMmber, ProfileViewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull ProfileViewholder holder, int position, @NonNull All_UserMmber model) {
                                    final String postKey = getRef(position).getKey();

                                    boolean online = getItem(position).isOnline(); // Get the online status from the model

                                    holder.setProfileInchat(getApplication(), model.getName(), model.getUid(), model.getUrl(), online);

                                    String name = getItem(position).getName();
                                    String url = getItem(position).getUrl();
                                    String uid = getItem(position).getUid();

                                    profileRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("url").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ImageView chat_user_profile_pic = findViewById(R.id.chat_user_profile_pic);
                                            Glide.with(chat_user_profile_pic).load(snapshot.getValue()).into(chat_user_profile_pic);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    holder.click.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(ChatActivity.this,MessageActivity.class);
                                            intent.putExtra("name",name);
                                            intent.putExtra("url",url);
                                            intent.putExtra("uid",uid);
                                            startActivity(intent);
                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public ProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.chat_profile_item,parent,false);

                                    return new ProfileViewholder(view);
                                }
                            };
                    firebaseRecyclerAdapter.startListening();;
                    recyclerView.setAdapter(firebaseRecyclerAdapter);}
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        updateOnlineStatus(true); // Set user's status as online

        FirebaseRecyclerOptions<All_UserMmber> options =
                new FirebaseRecyclerOptions.Builder<All_UserMmber>()
                        .setQuery(profileRef,All_UserMmber.class)
                        .build();

        FirebaseRecyclerAdapter<All_UserMmber, ProfileViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<All_UserMmber, ProfileViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProfileViewholder holder, int position, @NonNull All_UserMmber model) {
                        final String postKey = getRef(position).getKey();

                        boolean online = getItem(position).isOnline(); // Get the online status from the model

                        holder.setProfileInchat(getApplication(), model.getName(), model.getUid(), model.getUrl(), online);

                        String name = getItem(position).getName();
                        String url = getItem(position).getUrl();
                        String uid = getItem(position).getUid();

                        profileRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("url").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ImageView chat_user_profile_pic = findViewById(R.id.chat_user_profile_pic);
                                Glide.with(chat_user_profile_pic).load(snapshot.getValue()).into(chat_user_profile_pic);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        holder.click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChatActivity.this,MessageActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("url",url);
                                intent.putExtra("uid",uid);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.chat_profile_item,parent,false);

                        return new ProfileViewholder(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateOnlineStatus(false); // Set user's status as offline
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateOnlineStatus(false); // Set user's status as offline
    }

    private void updateOnlineStatus(boolean online) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("All Users").child(user.getUid());
            userRef.child("online").setValue(online);
        }
    }

}