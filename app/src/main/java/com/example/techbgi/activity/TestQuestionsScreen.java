package com.example.techbgi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techbgi.R;
import com.example.techbgi.model.QuestionsModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TestQuestionsScreen extends AppCompatActivity {

    private TextView questionText;//sdfs
    private long QUIZ_DURATION_MS; // 10 minute
    private CountDownTimer countDownTimer;
    private long timeRemainingMs;
    private int userAnswerCount;
    private RadioGroup optionsRadioGroup;
    private List<QuestionsModel> questionsList;
    private int currentQuestionIndex = 0;
    private Map<Integer, String> userAnswers;
    private int unauthorizedActionsCount = 0;
    private boolean quizSubmitted = false;
    String date, timeStart, timeEnd,currentTime;

    private Button previousButton, nextButton, submitButton;
    TextView timeRemainingText;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_questions_screen);

        questionText = findViewById(R.id.questionText);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
        timeRemainingText = findViewById(R.id.timerText);
//dfsfsfss
        userAnswers = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentTime = sdf.format(new Date());

        QUIZ_DURATION_MS = getIntent().getLongExtra("quizDurationMs",0);
        timeRemainingMs = QUIZ_DURATION_MS;

        date = getIntent().getStringExtra("date");
        timeEnd = getIntent().getStringExtra("timeEnd");
        timeStart = getIntent().getStringExtra("timeStart");

        String json = getIntent().getStringExtra("json");
        loadQuestionsFromJson(json);
        showCurrentQuestion();

        countDownTimer = new CountDownTimer(timeRemainingMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingMs = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                try {
                    submitQuiz();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        countDownTimer.start();

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousQuestion();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextQuestion();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitQuiz();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void updateTimerText() {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeRemainingMs);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeRemainingMs) % 60;
        String timeRemaining = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timeRemainingText.setText(timeRemaining);
    }

    private void loadQuestionsFromJson(String json) {
        if (isValidJson(json)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                questionsList = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, QuestionsModel.class));
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(TestQuestionsScreen.this, "Please check your JSON and re-upload it in proper format", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.start();
    }
    private void showCurrentQuestion() {
        QuestionsModel currentQuestion = questionsList.get(currentQuestionIndex);

        questionText.setText(currentQuestion.getQuestionText());

        optionsRadioGroup.removeAllViews();
        for (int i = 0; i < currentQuestion.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(currentQuestion.getOptions().get(i));
            optionsRadioGroup.addView(radioButton);
        }

        // Check if the user has previously answered the current question
        String selectedAnswer = userAnswers.get(currentQuestionIndex);
        if (selectedAnswer != null) {
            int radioButtonId = optionsRadioGroup.getChildAt(currentQuestion.getOptions().indexOf(selectedAnswer)).getId();
            optionsRadioGroup.check(radioButtonId);
        }

        previousButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < questionsList.size() - 1);
        submitButton.setVisibility(currentQuestionIndex == questionsList.size() - 1 ? View.VISIBLE : View.GONE);
    }

    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            saveUserAnswer();
            currentQuestionIndex--;
            showCurrentQuestion();
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questionsList.size() - 1) {
            saveUserAnswer();
            currentQuestionIndex++;
            showCurrentQuestion();
        }
    }

    private void saveUserAnswer() {
        int selectedRadioButtonId = optionsRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            if (selectedRadioButton != null) {
                String selectedAnswer = selectedRadioButton.getText().toString();
                userAnswers.put(currentQuestionIndex, selectedAnswer);
                userAnswerCount++;
            }
        }
    }

    public boolean isValidJson(String jsonString) {
        JsonParser parser = new JsonParser();
        parser.parse(jsonString);
        return true;
    }

    private void submitQuiz() throws ParseException {
        if (quizSubmitted) {
            return; // Quiz has already been submitted, do not proceed
        }
        saveUserAnswer();
        calculateScore();
        quizSubmitted = true;
    }

    private void calculateScore() throws ParseException {
        int numQuestions = questionsList.size();

        Intent intent = new Intent(this, TestScorePage.class);
        intent.putExtra("total", numQuestions);
        intent.putExtra("date", date);
        intent.putExtra("timeEnd", timeEnd);
        intent.putExtra("timeStart", timeStart);
        intent.putExtra("name", getIntent().getStringExtra("name"));
        intent.putExtra("rollNumber", getIntent().getStringExtra("rollNumber"));
        intent.putExtra("semester", getIntent().getStringExtra("semester"));
        intent.putExtra("branch", getIntent().getStringExtra("branch"));
        intent.putExtra("subject", getIntent().getStringExtra("subject"));

        // Make sure userAnswers and questionsList have the same size
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Calendar currentDate = Calendar.getInstance();
        Date firebaseDateTime = sdf.parse(date+" "+timeEnd);

        if(currentDate.getTime().compareTo(firebaseDateTime) >= 0){
            for (int i = 0; i < numQuestions; i++) {
                QuestionsModel question = questionsList.get(i);
                String userAnswer = userAnswers.get(i);
                if (userAnswer != null && userAnswer.equals(question.getCorrectAnswer())) {
                    score++;
                }
            }
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        }
        else if(unauthorizedActionsCount < 3){
            if (userAnswers.size() == numQuestions) {
                for (int i = 0; i < numQuestions; i++) {
                    QuestionsModel question = questionsList.get(i);
                    String userAnswer = userAnswers.get(i);
                    if (userAnswer != null && userAnswer.equals(question.getCorrectAnswer())) {
                        score++;
                    }
                }
                intent.putExtra("score", score);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please answer all questions before submitting", Toast.LENGTH_SHORT).show();
            }
        }else{
            for (int i = 0; i < numQuestions; i++) {
                QuestionsModel question = questionsList.get(i);
                String userAnswer = userAnswers.get(i);
                if (userAnswer != null && userAnswer.equals(question.getCorrectAnswer())) {
                    score++;
                }
            }
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (unauthorizedActionsCount >= 3) {
            try {
                submitQuiz(); // Auto submit the quiz
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            unauthorizedActionsCount++;
            Toast.makeText(this, "Unauthorized action! Test will be submitted after " + (3 - unauthorizedActionsCount) + " warnings.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (unauthorizedActionsCount >= 3) {
            try {
                submitQuiz(); // Auto submit the quiz
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            unauthorizedActionsCount++;
            View rootView = findViewById(android.R.id.content);
            Snackbar.make(rootView, "Unauthorized action! Test will be submitted after " + (3 - unauthorizedActionsCount) + " warnings.", Snackbar.LENGTH_LONG).show();
        }
    }

}