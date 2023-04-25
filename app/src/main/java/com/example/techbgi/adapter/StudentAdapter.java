package com.example.techbgi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techbgi.R;
import com.example.techbgi.model.ClassItem;
import com.example.techbgi.model.StudentItem;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    ArrayList<StudentItem> studentItems;
    Context context;

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new StudentViewHolder(itemView,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.rollNo.setText(studentItems.get(position).getRollNo());
        holder.studentName.setText(studentItems.get(position).getStudentName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }
    private int getColor(int position){
        String status = studentItems.get(position).getStatus();
        if(status.equals("P")){
            return Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(context,R.color.lite_green)));
        }else if(status.equals("A"))
        {
            return Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(context,R.color.red)));
        }
        return Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(context,R.color.normal)));
    }


    @Override
    public int getItemCount() {
        return studentItems.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView rollNo;
        TextView studentName;
        TextView status;
        CardView cardView;
        public StudentViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            rollNo = itemView.findViewById(R.id.rollNo);
            studentName = itemView.findViewById(R.id.studentName);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardview);
            itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"EDIT");
            menu.add(getAdapterPosition(),1,0,"DELETE");

        }
    }
}
