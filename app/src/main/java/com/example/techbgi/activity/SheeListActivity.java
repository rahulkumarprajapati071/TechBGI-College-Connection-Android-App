package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.techbgi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SheeListActivity extends AppCompatActivity {
    private ListView sheetList;
    private ArrayAdapter adapter;
    private  ArrayList<String> listItems = new ArrayList();
    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference reference = firebaseDatabase.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    String classId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shee_list);

        Toolbar toolbar = findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Attendances Date");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);


        classId = getIntent().getStringExtra("classId");
        loadListItem();
        sheetList = findViewById(R.id.sheetList);
        adapter = new ArrayAdapter(this,R.layout.sheet_list,R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActivity(position));
    }

    private void openSheetActivity(int position) {

        String[] idArray = getIntent().getStringArrayExtra("idArray");
        String[] rollNoArray = getIntent().getStringArrayExtra("rollNoArray");
        String[] studentNameArray = getIntent().getStringArrayExtra("studentNameArray");

        Intent intent = new Intent(this,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollNoArray",rollNoArray);
        intent.putExtra("studentNameArray",studentNameArray);
        intent.putExtra("month",listItems.get(position));
        intent.putExtra("classId",classId);

        startActivity(intent);
    }

    private void loadListItem() {
        reference.child("statustable").orderByValue().startAt(classId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    reference.child("statustable").orderByChild("date").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    if(snapshot1.child("classId").getValue().toString().equals(classId)){
                                        String date = snapshot1.child("date").getValue().toString();
                                        if (!listItems.contains(date.substring(3))) {
                                            String month = date.substring(3,5);
                                            listItems.add(date.substring(3));
                                        }
                                    }
                                }
                            }
                            if(listItems.size() != 0){
                                TextView txv = findViewById(R.id.txv);
                                txv.setVisibility(View.GONE);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}