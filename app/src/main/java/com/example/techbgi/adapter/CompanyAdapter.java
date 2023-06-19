package com.example.techbgi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.model.CompanyModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CompanyAdapter extends FirebaseRecyclerAdapter<CompanyModel,CompanyAdapter.MyViewHolder> {


    public CompanyAdapter(@NonNull FirebaseRecyclerOptions<CompanyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull CompanyModel model) {
        holder.company.setText(model.getName());
        holder.date.setText(model.getDate());

        Glide.with(holder.img1.getContext()).load(model.getImage()).into(holder.img1);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_view,parent,false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img1;
        TextView company,date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.img1);
            company = itemView.findViewById(R.id.company);
            date = itemView.findViewById(R.id.date);
        }
    }
}
