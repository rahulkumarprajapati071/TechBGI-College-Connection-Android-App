package com.example.techbgi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.example.techbgi.activity.AddCompanies;
import com.example.techbgi.activity.AddQuestions;
import com.example.techbgi.activity.TestRoom;
import com.example.techbgi.activity.AllFaculty;
import com.example.techbgi.activity.AttendanceTaking;
import com.example.techbgi.activity.KeepNotes;
import com.example.techbgi.activity.NoticeScreen;
import com.example.techbgi.activity.UploadMarks;
import com.example.techbgi.activity.UploadNotesFaculty;
import com.example.techbgi.activity.UploadTimeTable;
import com.example.techbgi.model.CategoryModel;

import java.util.ArrayList;

public class FacultyCategoryAdapter extends RecyclerView.Adapter<FacultyCategoryAdapter.CategoryViewHolder>{

    Context context;
    ArrayList<CategoryModel> categoryModels;
    public FacultyCategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels){
        this.context = context;
        this.categoryModels = categoryModels;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel model = categoryModels.get(position);

        holder.textView.setText(model.getCategoryName());
        Glide.with(context).load(model.getCategoryImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (model.getCategoryID())
                {
                    case 1:
                        context.startActivity(new Intent(context, AllFaculty.class));
                        break;
                    case 2:
                        context.startActivity(new Intent(context, NoticeScreen.class));
                        break;
                    case 3:
                        context.startActivity(new Intent(context, AttendanceTaking.class));
                        break;
                    case 4:
                        Intent intent = new Intent(context,UploadTimeTable.class);
                        intent.putExtra("flag","faculty");
                        context.startActivity(intent);
                        break;
                    case 5:
                        context.startActivity(new Intent(context, UploadMarks.class));
                        break;
                    case 6:
                        Intent intent1 = new Intent(context,AddCompanies.class);
                        intent1.putExtra("flag","faculty");
                        context.startActivity(intent1);
                        break;
                    case 7:
                        context.startActivity(new Intent(context, KeepNotes.class));
                        break;
                    case 8:
                        context.startActivity(new Intent(context, UploadNotesFaculty.class));
                        break;
                    case 9:
                        context.startActivity(new Intent(context, AddQuestions.class));
                        break;

                    default:
                        Toast.makeText(v.getContext(), "For Future Planning", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // fro open apptitude website...

//    private void openAptitude(){
//        Uri uri = Uri.parse("https://www.indiabix.com/");
//        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//        context.startActivity(intent);
//    }
    // fro open apptitude website...


    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public CategoryViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.category);
        }
    }
}
