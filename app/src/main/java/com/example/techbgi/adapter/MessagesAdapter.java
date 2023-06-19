package com.example.techbgi.adapter;

import static com.example.techbgi.activity.MessageActivity.recImage;
import static com.example.techbgi.activity.MessageActivity.senderImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.model.MessageMember;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageMember> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<MessageMember> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false);
            return new ReciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageMember messages = messagesArrayList.get(position);
        if(holder.getClass() == SenderViewHolder.class)
        {
            SenderViewHolder viewHolder = (SenderViewHolder)holder;
            viewHolder.txtMessages.setText(messages.getMessage());

            Glide.with(viewHolder.circleImageView).load(senderImage).into(viewHolder.circleImageView);
//            Picasso.get().load(senderImage).into(viewHolder.circleImageView);
        }else{
            ReciverViewHolder viewHolder = (ReciverViewHolder) holder;
            viewHolder.txtMessages.setText(messages.getMessage());

            Glide.with(viewHolder.circleImageView).load(recImage).into(viewHolder.circleImageView);

        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageMember messages = messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderuid()))
        {
            return  ITEM_SEND;
        }else{
            return ITEM_RECEIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtMessages;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image);
            txtMessages = itemView.findViewById(R.id.textMessages);
        }
    }
    // yaah ruk jao ya akhaa hai
    class ReciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtMessages;
        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image);
            txtMessages = itemView.findViewById(R.id.textMessages);
        }
    }
}
