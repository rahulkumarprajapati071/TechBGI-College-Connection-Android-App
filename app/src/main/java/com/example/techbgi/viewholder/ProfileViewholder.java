package com.example.techbgi.viewholder;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techbgi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewholder extends RecyclerView.ViewHolder {

    public TextView textViewName;
    ImageView imageView;
    RelativeLayout pro_self;
    public CardView click;
    TextView status;

    public ProfileViewholder(@NonNull View itemView) {
        super(itemView);
    }
    public void setProfileInchat(Application fragmentActivity, String name, String uid, String url, boolean online){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        textViewName = itemView.findViewById(R.id.tv_name_profile);
        imageView = itemView.findViewById(R.id.imageView);
        click = itemView.findViewById(R.id.click);

        Glide.with(imageView).load(url).into(imageView);
        textViewName.setText(name);
        pro_self = itemView.findViewById(R.id.pro_self);

        if(userid.equals(uid)){
            pro_self.setVisibility(View.GONE);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.height = 0;
            params.width = 0;
            itemView.setLayoutParams(params);
        }else{
            Glide.with(imageView).load(url).into(imageView);
            textViewName.setText(name);
        }
        status = itemView.findViewById(R.id.status); // Initialize the status TextView

        if (online) {
            status.setText("Online");
            status.setTextColor(itemView.getContext().getResources().getColor(R.color.lite_green));
        } else {
            status.setText("Offline");
            status.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
        }

    }

}
