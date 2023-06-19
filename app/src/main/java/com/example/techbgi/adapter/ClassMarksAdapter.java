package com.example.techbgi.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techbgi.R;
import com.example.techbgi.activity.MarksOfStudentActivity;
import com.example.techbgi.model.ClassItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ClassMarksAdapter extends FirebaseRecyclerAdapter<ClassItem,ClassMarksAdapter.MyViewHolder> {

    FirestoreRecyclerAdapter<ClassItem, ClassMarksAdapter.MyViewHolder> noteAdapter;
    public ClassMarksAdapter(@NonNull FirebaseRecyclerOptions<ClassItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder,int i, @NonNull ClassItem model) {


        holder.classname.setText(model.getClassName());
        holder.subjectname.setText(model.getSubjectName());
        holder.imageView.setImageResource(R.drawable.edit);
        holder.imageView.setVisibility(View.GONE);
        holder.semester.setText(model.getSemester());

        holder.imageView.setOnClickListener((view) -> {
            final Dialog dialog = new Dialog(holder.imageView.getContext());
            dialog.setContentView(R.layout.add_class_and_subject_dialog);
            dialog.setCancelable(false);

            TextView textView = dialog.findViewById(R.id.toptext);
            Button update = dialog.findViewById(R.id.addbtn);
            Button cancle = dialog.findViewById(R.id.cancel);
            EditText semn = dialog.findViewById(R.id.semester);
            EditText classn = dialog.findViewById(R.id.classname);
            EditText subn = dialog.findViewById(R.id.subjectname);


            textView.setText("Update Class");
            update.setText("Update");

            semn.setText(model.getSemester());
            classn.setText(model.getClassName());
            subn.setText(model.getSubjectName());

            dialog.show();

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClassItem classItem = new ClassItem(classn.getText().toString(),FirebaseAuth.getInstance().getUid(),semn.getText().toString(),subn.getText().toString(),getRef(holder.getAdapterPosition()).getKey());
                    FirebaseDatabase.getInstance().getReference().child("classOfMarks").child(Objects.requireNonNull(getRef(holder.getAdapterPosition()).getKey())).setValue(classItem).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(dialog.getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.dismiss();
                }
            });

            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });




        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(holder.imageView.getContext());
                dialog.setContentView(R.layout.marks_type_dialog);
                dialog.setCancelable(false);

                dialog.show();

                //for current date
//                final Date currDate = Calendar.getInstance().getTime();
//                DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
//                String currentDate = format.format(currDate);

                Button assignments = dialog.findViewById(R.id.assignment);
                Button unittest = dialog.findViewById(R.id.unittest);
                Button midmarks = dialog.findViewById(R.id.midsemtest);
                Button cancle = dialog.findViewById(R.id.cancel);
                EditText count = dialog.findViewById(R.id.count);


                assignments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(count.getText().toString().trim().isEmpty()){
                            Toast.makeText(holder.cardView.getContext(), "Please specify which number of test", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(holder.cardView.getContext(), MarksOfStudentActivity.class);
                            intent.putExtra("classId",getRef(holder.getAdapterPosition()).getKey());
                            intent.putExtra("type","assignments");
                            intent.putExtra("count",count.getText().toString());
                            holder.cardView.getContext().startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });
                unittest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(count.getText().toString().trim().isEmpty()){
                            Toast.makeText(holder.cardView.getContext(), "Please specify which number of test", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(holder.cardView.getContext(), MarksOfStudentActivity.class);
                            intent.putExtra("classId",getRef(holder.getAdapterPosition()).getKey());
                            intent.putExtra("type","unittest");
                            intent.putExtra("count",count.getText().toString());

                            holder.cardView.getContext().startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });
                midmarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(count.getText().toString().trim().isEmpty()){
                            Toast.makeText(holder.cardView.getContext(), "Please specify which number of test", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(holder.cardView.getContext(), MarksOfStudentActivity.class);
                            intent.putExtra("classId",getRef(holder.getAdapterPosition()).getKey());
                            intent.putExtra("type","midsettest");
                            intent.putExtra("count",count.getText().toString());

                            holder.cardView.getContext().startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                showConfirmationDialog(holder.cardView.getContext(),i);
                return false;
            }
        });


    }
    private void showConfirmationDialog(Context context, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this item?");
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
        FirebaseDatabase.getInstance().getReference().child("classOfMarks").child(FirebaseAuth.getInstance().getUid()).child(Objects.requireNonNull(getRef(position).getKey())).removeValue();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView classname,subjectname,semester;
        ImageView imageView;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            classname = itemView.findViewById(R.id.class_tv);
            semester = itemView.findViewById(R.id.sem_tv);
            subjectname = itemView.findViewById(R.id.subject_tv);
            cardView = itemView.findViewById(R.id.cardview);
            imageView = itemView.findViewById(R.id.edtttt);
        }

    }
}
