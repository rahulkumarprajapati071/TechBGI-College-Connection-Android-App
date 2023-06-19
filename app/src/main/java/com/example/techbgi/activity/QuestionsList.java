package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.adapter.QuestionsListAdapter;
import com.example.techbgi.model.QuestionsDetailsModel;
import com.example.techbgi.model.QuestionsModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class QuestionsList extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseStorage firebaseStorage;

    FloatingActionButton fab;
    List<QuestionsModel> questionsList;
    List<QuestionsModel> mm;

    TextView txv;

    private RecyclerView recyclerView;
    private QuestionsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_list);

        Toolbar toolbar = findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        txv = findViewById(R.id.txv);

        title.setText("All Questions");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);

        recyclerView = findViewById(R.id.recOfQues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionsListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        String ref = getIntent().getStringExtra("ref");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Data exists, retrieve the value
                    QuestionsDetailsModel value = dataSnapshot.getValue(QuestionsDetailsModel.class);

                    if(isValidJson(value.getJsonQuestion())){
                        ObjectMapper objectMapper = new ObjectMapper();
                        // Define the type of the target list using TypeReference
                        TypeReference<List<QuestionsModel>> questionListTypeRef = new TypeReference<List<QuestionsModel>>() {};

                        // Parse the JSON string into a list of Question objects
                        try {
                            questionsList = objectMapper.readValue(value.getJsonQuestion(), questionListTypeRef);
                            mm = new ArrayList<>(questionsList.size());
                            for (int i = 0; i < questionsList.size(); i++) {
                                mm.add(questionsList.get(i));
                            }
                            adapter.setQuestionsList(mm); // Update the adapter with the new list
                            if(mm.size() != 0)txv.setVisibility(View.GONE);//for hide textview.........
                            adapter.notifyDataSetChanged();
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        Toast.makeText(QuestionsList.this,"Please check your pdf and re-upload in proper formate", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Data does not exist
                    Toast.makeText(QuestionsList.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });


    }

    public static boolean isValidJson(String jsonString) {
        try {
            JsonParser parser = new JsonParser();
            parser.parse(jsonString);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

}