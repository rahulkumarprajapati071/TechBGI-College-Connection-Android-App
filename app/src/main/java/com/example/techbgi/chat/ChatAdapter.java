package com.example.techbgi.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techbgi.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyHolderView>{

    private final Context context;
    private List<ChatList> chatList;
    private String userMobile;

    public ChatAdapter(Context context, List<ChatList> chatList) {
        this.context = context;
        this.chatList = chatList;
        this.userMobile = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
    }

    @NonNull
    @Override
    public ChatAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyHolderView holder, int position) {
        ChatList list = chatList.get(position);

        if(list.getMobile().equals(userMobile)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list.getMessage());
            holder.myTime.setText(list.getData()+" "+list.getTime());
        }
        else {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(list.getMessage());
            holder.oppoTime.setText(list.getData()+" "+list.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
    public void updateChatList(List<ChatList> chatList){
        this.chatList = chatList;
    }
    static class MyHolderView extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout,myLayout;
        private TextView oppoMessage,myMessage;
        private TextView oppoTime,myTime;
        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            oppoTime = itemView.findViewById(R.id.oppoMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);
        }
    }
}
