package com.example.techbgi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.activity.ViewImage;
import com.example.techbgi.activity.ViewPDF;
import com.example.techbgi.model.AttendanceViewModel;
import com.example.techbgi.model.NoticeModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class AttendanceViewAdapter extends RecyclerView.Adapter<AttendanceViewAdapter.MyViewHolder> {
    private List<AttendanceViewModel> attendanceViewModelList;
    private final Context context;


    public AttendanceViewAdapter(List<AttendanceViewModel> attendanceViewModelList, Context context) {
        this.attendanceViewModelList = attendanceViewModelList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewAdapter.MyViewHolder holder, int position) {
        AttendanceViewModel list2 = attendanceViewModelList.get(position);

        holder.date.setText(list2.getDate());
        if(Objects.equals(list2.getStatus(), "A")){
            holder.status.setText("Absent");
            holder.carcolor.setBackgroundResource(R.color.red);
        }else if(Objects.equals(list2.getStatus(), "P")){
            holder.status.setText("Present");
            holder.carcolor.setBackgroundResource(R.color.lite_green);
        }else{
            holder.carcolor.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return attendanceViewModelList.size();
    }

    @NonNull
    @Override
    public AttendanceViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendanceViewAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.status_layout_attendance,null));

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date,status;
        RelativeLayout carcolor;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            carcolor = itemView.findViewById(R.id.cardcolor);
        }
    }
}
