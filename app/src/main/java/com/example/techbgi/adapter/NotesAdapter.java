package com.example.techbgi.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techbgi.R;
import com.example.techbgi.activity.ViewPDF;
import com.example.techbgi.model.NotesModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class NotesAdapter extends FirebaseRecyclerAdapter<NotesModel,NotesAdapter.MyViewHolder> {
    public NotesAdapter(@NonNull FirebaseRecyclerOptions<NotesModel> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull NotesModel model) {
        holder.subjectName.setText(model.getPdfSubject());
        holder.userName.setText(model.getName());
        holder.noteViewCount.setText(String.valueOf(model.getNoteView()));

                holder.ccc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.subjectName.getContext(), ViewPDF.class);
                        intent.putExtra("filename",model.getPdfSubject());
                        intent.putExtra("fileurl",model.getPdfUrl());

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        holder.subjectName.getContext().startActivity(intent);
                    }
                });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_notes_design,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView subjectName,userName,noteViewCount;
        CardView ccc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            userName = itemView.findViewById(R.id.userName);
            noteViewCount = itemView.findViewById(R.id.noteViewCount);
            ccc = itemView.findViewById(R.id.ccc);
        }
    }
}
