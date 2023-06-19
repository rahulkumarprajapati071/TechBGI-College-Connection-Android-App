package com.example.techbgi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techbgi.R;
import com.example.techbgi.activity.AddQuestions;
import com.example.techbgi.activity.QuestionsList;
import com.example.techbgi.activity.TestResultList;
import com.example.techbgi.model.QuestionsDetailsModel;
import com.example.techbgi.model.TestResultModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuestionsCardAdapter extends FirebaseRecyclerAdapter<QuestionsDetailsModel,QuestionsCardAdapter.MyViewHolder> {


    private String semester;
    private String subject;
    private String timeStart;
    private String branch;
    private String timeEnd;
    private String date;

    public QuestionsCardAdapter(@NonNull FirebaseRecyclerOptions<QuestionsDetailsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull QuestionsDetailsModel model) {
        holder.startTime.setText(model.getTimeStart());
        holder.endTime.setText(model.getTimeEnd());
        holder.date.setText(model.getDate());
        holder.subjectName.setText(model.getSubjectName());
        holder.semester.setText(model.getSemester());
        holder.branch.setText(model.getBranch());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDeleteDialog(holder.cardView.getContext(),position);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showConfirmationDialog(holder.cardView.getContext(), position);
                return true;
            }
        });
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_added_layout,parent,false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView subjectName,date,endTime,startTime,semester,branch;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectName = itemView.findViewById(R.id.subjectnameQue);
            date = itemView.findViewById(R.id.date);
            endTime = itemView.findViewById(R.id.endTime);
            startTime = itemView.findViewById(R.id.startTime);
            cardView = itemView.findViewById(R.id.questionscard);
            semester = itemView.findViewById(R.id.semester);
            branch = itemView.findViewById(R.id.branch);
        }
    }
    private void showConfirmationDialog(Context context, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");
        builder.setMessage("Please confirm because You will lost Result Related to this Test");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User confirmed the deletion, perform the deletion here
            deleteItem(position);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // User canceled the deletion, do nothing
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem(int position) {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReferenceFromUrl(getRef(position).toString());
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subject = snapshot.child("subjectName").getValue(String.class);
                branch = snapshot.child("branch").getValue(String.class);
                semester = snapshot.child("semester").getValue(String.class);
                date = snapshot.child("date").getValue(String.class);
                timeEnd = snapshot.child("timeEnd").getValue(String.class);
                timeStart = snapshot.child("timeStart").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
        reference.child("TestScore").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String sem = snapshot.child("semester").getValue(String.class);
                        String bran = snapshot.child("branch").getValue(String.class);
                        String sub = snapshot.child("subjectName").getValue(String.class);
                        String dat = snapshot.child("date").getValue(String.class);
                        String timEnd = snapshot.child("timeEnd").getValue(String.class);
                        String timStart = snapshot.child("timeStart").getValue(String.class);

                        System.out.println(sem+"  "+semester+"|  |"+bran+"  "+branch+"|  |"+sub+"  "+subject+"|  |"+dat+"  "+date+"|  |"+timEnd+"  "+timeEnd+"|  |"+timStart+"  "+timeStart);

                        if (sem != null && sem.equals(semester) &&
                                bran != null && bran.equals(branch) &&
                                sub != null && sub.equals(subject) &&
                                dat != null && dat.equals(date) &&
                                timEnd != null && timEnd.equals(timeEnd) &&
                                timStart != null && timStart.equals(timeStart)) {
                            // Delete the node
                            snapshot.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

        reference1.removeValue();

    }

    private void showEditDeleteDialog(Context context, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.long_press_dialog, null);
        builder.setView(dialogView);

        // Find the buttons in the dialog layout
        Button questionsButton = dialogView.findViewById(R.id.question);
        Button resultButton = dialogView.findViewById(R.id.result);

        // Set click listeners for edit and delete buttons
        questionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionsList.class);
                intent.putExtra("ref",getRef(position).toString());
                context.startActivity(intent);
            }
        });

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TestResultList.class);
                intent.putExtra("ref",getRef(position).toString());
                context.startActivity(intent);
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
