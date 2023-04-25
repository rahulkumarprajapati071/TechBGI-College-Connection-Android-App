package com.example.techbgi.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techbgi.R;
import com.example.techbgi.model.MarksStudentModel;

import java.util.ArrayList;

public class AdapterOfMarks extends RecyclerView.Adapter<AdapterOfMarks.MyViewHolder> {
    public AdapterOfMarks(Context context, ArrayList<MarksStudentModel> list) {
        this.context = context;
        this.list = list;
    }

    Context context;
   ArrayList<MarksStudentModel> list;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_and_marks,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOfMarks.MyViewHolder holder, int position) {
        MarksStudentModel marksStudentModel = list.get(position);
        holder.name.setText(marksStudentModel.getStudentName());
        holder.roll.setText(marksStudentModel.getRollNo());
        holder.marks.setText(marksStudentModel.getMarks());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //how to set MenuItemlistener
    //1.first implements View.onCreateContextMenuListenter whith MyViewHolder
    //2.override the method onCreateContextMenu
    //3. set all menu using menu which comes in constructer
    //4. Go to main activity and override onContextItemSelected method in last
    //5. than set switch case and work with your task with method getItemId() and getGroudId()
    //6. after all set it on any of thing like in my case i set it on cardview
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView name,roll,marks;
        CardView cardView;

        public MyViewHolder(View itemview){
            super(itemview);
            name = itemview.findViewById(R.id.studentname);
            roll = itemview.findViewById(R.id.rollnum);
            marks = itemview.findViewById(R.id.marksofstudent);
            cardView = itemview.findViewById(R.id.cardview);
            cardView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),101,0,"Edit");
            menu.add(getAdapterPosition(),102,1,"Delete");
        }
    }
}
