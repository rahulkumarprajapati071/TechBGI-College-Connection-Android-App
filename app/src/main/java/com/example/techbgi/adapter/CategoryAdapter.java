package com.example.techbgi.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.techbgi.activity.AllFaculty;
import com.example.techbgi.activity.CampusActivity;
import com.example.techbgi.activity.FeesActivity;
import com.example.techbgi.activity.KeepNotes;
import com.example.techbgi.activity.NoticePage;
import com.example.techbgi.activity.PaperActivity;
import com.example.techbgi.activity.StudentAttandanceSearch;
import com.example.techbgi.activity.TestRoom;
import com.example.techbgi.activity.TopZone;
import com.example.techbgi.activity.UploadNotesFaculty;
import com.example.techbgi.activity.UploadTimeTable;
import com.example.techbgi.model.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<CategoryModel> categoryModels;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, null);
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
                switch (model.getCategoryID()) {
                    case 1:
                        openWeb();
                        break;
                    case 2:
                        context.startActivity(new Intent(context, UploadNotesFaculty.class));
                        break;
                    case 3:
                        context.startActivity(new Intent(context, StudentAttandanceSearch.class));
                        break;
                    case 4:
                        Intent intent = new Intent(context, UploadTimeTable.class);
                        intent.putExtra("flag", "student");
                        context.startActivity(intent);
                        break;
                    case 5:
                        context.startActivity(new Intent(context, KeepNotes.class));
                        break;
                    case 6:
                        context.startActivity(new Intent(context, TopZone.class));
                        break;
                    case 7:
                        openRGPV();
                        break;
                    case 8:
                        context.startActivity(new Intent(context, FeesActivity.class));
                        break;
                    case 9:
                        context.startActivity(new Intent(context, AllFaculty.class));
                        break;
                    case 10:
                        Intent intent1 = new Intent(context, AddCompanies.class);
                        intent1.putExtra("flag", "student");
                        context.startActivity(intent1);
                        break;
                    case 11:
                        context.startActivity(new Intent(context, TestRoom.class));
                        break;
                    case 12:
                        openMap();
                        break;
                    case 13:
                        context.startActivity(new Intent(context, CampusActivity.class));
                        break;
                    case 14:
                        openPaper();
                        break;
                    default:
                        Toast.makeText(v.getContext(), "For Fututre Planning", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openAptitude() {
        Uri uri = Uri.parse("https://www.indiabix.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    private void openMap() {
        Uri uri = Uri.parse("geo:0, 0?q=Sushila Devi Bansal College Umariya");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    private void openWeb() {
        Uri uri = Uri.parse("https://sdbc.ac.in/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    private void openRGPV() {
        Uri uri = Uri.parse("https://www.rgpv.ac.in/Login/StudentLogin.aspx");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    private void openPaper() {
        context.startActivity(new Intent(context, PaperActivity.class));
    }


    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.category);
        }
    }
}
