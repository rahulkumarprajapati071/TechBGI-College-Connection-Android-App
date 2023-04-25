package com.example.techbgi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.activity.ViewImage;
import com.example.techbgi.activity.ViewPDF;
import com.example.techbgi.model.NoticeModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {
    private List<NoticeModel> noticeModelList;
    private final Context context;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

    public NoticeAdapter(List<NoticeModel> noticeModelList, Context context) {
        this.noticeModelList = noticeModelList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.MyViewHolder holder, int position) {
        NoticeModel list2 = noticeModelList.get(position);
        String type = list2.getType();

        holder.filename.setText(list2.getFilename());
        holder.userName.setText(list2.getUsername());

        Glide.with(holder.filename.getContext()).load(list2.getUserimageurl()).into(holder.userprofile);

        holder.ccc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("pdf".equals(type)) {
                    // open pdf file
                    Intent intent = new Intent(holder.userName.getContext(), ViewPDF.class);
                    intent.putExtra("filename",list2.getFilename());
                    intent.putExtra("fileurl",list2.getFileurl());
                    holder.filename.getContext().startActivity(intent);
                } else if ("image".equals(type)) {
                    // open image file
                    Intent intent = new Intent(holder.userName.getContext(), ViewImage.class);
                    intent.putExtra("filename",list2.getFilename());
                    intent.putExtra("fileurl",list2.getFileurl());
                    holder.filename.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeModelList.size();
    }

    @NonNull
    @Override
    public NoticeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticeAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_notice_design,null));

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView filename,userName;
        ImageView userprofile;
        CardView ccc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            filename = itemView.findViewById(R.id.titleofnotice);
            userName = itemView.findViewById(R.id.userName);
            userprofile = itemView.findViewById(R.id.userimage);
            ccc = itemView.findViewById(R.id.ccc);
        }
    }
}
