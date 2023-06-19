package com.example.techbgi.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.techbgi.R;
import com.example.techbgi.model.QuestionsModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionsListAdapter extends RecyclerView.Adapter<QuestionsListAdapter.ViewHolder> {
    private List<QuestionsModel> data;

    public QuestionsListAdapter(List<QuestionsModel> data) {
        this.data = data;
    }
    public void setQuestionsList(List<QuestionsModel> questionsList) {
        data = questionsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuestionsModel item = data.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question,option1,option2,option3,option4,answer;

        public ViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.quest);
            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);
            option4 = itemView.findViewById(R.id.option4);
            answer = itemView.findViewById(R.id.answer);
        }

        public void bindData(QuestionsModel item) {
            question.setText(item.getQuestionText());
            option1.setText(item.getOptions().get(0));
            option2.setText(item.getOptions().get(1));
            option3.setText(item.getOptions().get(2));
            option4.setText(item.getOptions().get(3));
            answer.setText(item.getCorrectAnswer());
        }
    }
}
