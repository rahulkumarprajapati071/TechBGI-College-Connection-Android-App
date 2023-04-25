package com.example.techbgi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techbgi.R;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {
    ArrayList<ViewPaggerItem> viewPaggerItemsArrayList;

    public VPAdapter(ArrayList<ViewPaggerItem> viewPaggerItemsArrayList) {
        this.viewPaggerItemsArrayList = viewPaggerItemsArrayList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewPaggerItem viewPaggerItem = viewPaggerItemsArrayList.get(position);
        holder.imageView.setImageResource(viewPaggerItem.imageID);
        holder.tvHeading.setText(viewPaggerItem.heading);
        holder.tvDesc.setText(viewPaggerItem.description);

    }

    @Override
    public int getItemCount() {
        return viewPaggerItemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvHeading,tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivimage);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvDesc = itemView.findViewById(R.id.tvDesc);

        }
    }
}
