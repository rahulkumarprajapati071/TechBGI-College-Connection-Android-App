package com.example.techbgi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.activity.AllFacultyDescription;
import com.example.techbgi.model.FacultyRegistrationModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AllFacultyAdapter extends FirebaseRecyclerAdapter<FacultyRegistrationModel,AllFacultyAdapter.myviewholder> {

    public AllFacultyAdapter(@NonNull FirebaseRecyclerOptions<FacultyRegistrationModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull FacultyRegistrationModel model) {
        holder.nametext.setText(model.getFirstName()+" "+model.getLastName());
        holder.mob.setText(model.getPhoneNumber());
        Glide.with(holder.img1.getContext()).load(model.getImageUri()).into(holder.img1);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new AllFacultyDescription(model.getFirstName()+" "+model.getLastName(),model.getPhoneNumber(),model.getImageUri())).addToBackStack(null).commit();
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_faculty_design,parent,false);

        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        ImageView img1;
        TextView nametext,mob;
        CardView cardView;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.img1);
            nametext = itemView.findViewById(R.id.nametext);
            mob = itemView.findViewById(R.id.mob);
            cardView = itemView.findViewById(R.id.cardview);

        }
    }
}
