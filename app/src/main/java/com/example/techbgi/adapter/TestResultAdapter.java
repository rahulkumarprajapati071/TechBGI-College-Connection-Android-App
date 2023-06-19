package com.example.techbgi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techbgi.R;
import com.example.techbgi.model.TestResultModel;

import java.util.List;

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.ViewHolder> {

    private List<TestResultModel> testResults;

    public TestResultAdapter(List<TestResultModel> testResults) {
        this.testResults = testResults;
    }

    public void setTestResults(List<TestResultModel> testResults) {
        this.testResults = testResults;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_result_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestResultModel testResult = testResults.get(position);
        holder.bind(testResult);
    }

    @Override
    public int getItemCount() {
        return testResults.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView studentNameTextView;
        private TextView rollNumberTextView;
        private TextView marksTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            rollNumberTextView = itemView.findViewById(R.id.rollNumberTextView);
            marksTextView = itemView.findViewById(R.id.marksTextView);
        }

        public void bind(TestResultModel testResult) {
            studentNameTextView.setText(testResult.getStudentName());
            rollNumberTextView.setText(testResult.getRollNumber());
            marksTextView.setText(String.format("%d/%d", testResult.getObtainedMarks(), testResult.getTotalMarks()));
        }
    }
}
