package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.model.QuestionsDetailsModel;
import com.example.techbgi.model.QuestionsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class QuestionsAdding extends AppCompatActivity {
    EditText firstName;
    TextView date,timeStart,timeEnd;
    Spinner semester,branch,subject;
    Button uploadButton;
    ImageView quePreview;
    private Uri pdfData;
    private String pdfName;
    private Calendar selected = Calendar.getInstance();

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String firstString,dateString,timeStartString,timeEndString,subjectString,semesterString,branchString;
    Calendar selectedDate;
    Calendar currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_adding);

        firstName = findViewById(R.id.facultyName);
        date = findViewById(R.id.date);
        timeStart = findViewById(R.id.timeStart);
        timeEnd = findViewById(R.id.timeEnd);
        subject = findViewById(R.id.subject);
        semester = findViewById(R.id.semesterQue);
        branch = findViewById(R.id.branchQue);
        uploadButton = findViewById(R.id.uploadButton);
        quePreview = findViewById(R.id.quePreview);

        String[] valueSem = {"Current Semester","1","2","3","4","5","6","7","8"};
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(valueSem));
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList2);
        semester.setAdapter(arrayAdapter2);

        String[] valueBranch = {"Choose Branch","CSE","AIML","CSBS","CSE-IOT","CSIT","CIVIL","ME","EC","EEE",
                "IT","EE","EX","AUTO","CHEMICAL","FT","MINING","TX","OTHERS"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(valueBranch));
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList1);
        branch.setAdapter(arrayAdapter1);

        String[] valueSubject = {"Choose Subject","Technical","Logical Reasoning","Verbal Reasoning","Quantitative Apptitude"};
        ArrayList<String> arrayList3 = new ArrayList<>(Arrays.asList(valueSubject));
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList3);
        subject.setAdapter(arrayAdapter3);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        QuestionsAdding.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Create a Calendar instance for the current date
                                currentDate = Calendar.getInstance();

                                // Create a Calendar instance for the selected date
                                selectedDate = Calendar.getInstance();
                                selectedDate.set(year, monthOfYear, dayOfMonth);

                                // Validate the selected date
                                if (selectedDate.before(currentDate)) {
                                    // The selected date is in the past
                                    Toast.makeText(QuestionsAdding.this, "Please select a future date", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Update the selectedDate variable
                                selected.set(Calendar.YEAR, year);
                                selected.set(Calendar.MONTH, monthOfYear);
                                selected.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                // Update the date input field with the selected date
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                date.setText(dateFormat.format(selected.getTime()));
                            }
                        },
                        selected.get(Calendar.YEAR),
                        selected.get(Calendar.MONTH),
                        selected.get(Calendar.DAY_OF_MONTH)
                );

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        QuestionsAdding.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Create a Calendar instance for the current time
                                Calendar currentTime = Calendar.getInstance();

                                // Create a Calendar instance for the selected time
                                Calendar selectedTime = Calendar.getInstance();
                                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedTime.set(Calendar.MINUTE, minute);

                                // Validate the selected time
                                if (selectedTime.before(currentTime) && selectedDate.equals(currentDate)) {
                                    // The selected time is in the past
                                    Toast.makeText(QuestionsAdding.this, "Please select a future time", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Update the selectedTime variable
                                selected.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selected.set(Calendar.MINUTE, minute);

                                // Update the time input field with the selected time
                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                timeStart.setText(timeFormat.format(selected.getTime()));
                            }
                        },
                        selected.get(Calendar.HOUR_OF_DAY),
                        selected.get(Calendar.MINUTE),
                        false
                );

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        QuestionsAdding.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Create a Calendar instance for the selected start time
                                Calendar startTime = Calendar.getInstance();
                                startTime.setTime(selected.getTime());

                                // Create a Calendar instance for the selected end time
                                Calendar endTime = Calendar.getInstance();
                                endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                endTime.set(Calendar.MINUTE, minute);

                                // Validate the selected end time
                                if (endTime.before(startTime) && selectedDate.equals(currentDate)) {
                                    // The selected end time is before the start time
                                    Toast.makeText(QuestionsAdding.this, "Please select an end time after the start time", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Update the selected end time
                                selected.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selected.set(Calendar.MINUTE, minute);

                                // Update the time input field with the selected end time
                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                timeEnd.setText(timeFormat.format(selected.getTime()));
                            }
                        },
                        selected.get(Calendar.HOUR_OF_DAY),
                        selected.get(Calendar.MINUTE),
                        false
                );

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        //yaha upload button ka logic bohot alag hoga sabse......dhyan se shanti se karna padega...

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfData == null || firstName.getText().toString().trim().isEmpty() || date.getText().toString().trim().isEmpty() || timeStart.getText().toString().trim().isEmpty() || timeEnd.getText().toString().trim().isEmpty() || semester.getSelectedItemId() == 0 || branch.getSelectedItemId() == 0 || subject.getSelectedItemId() == 0){
                    Toast.makeText(QuestionsAdding.this, "Please fill all requirements", Toast.LENGTH_SHORT).show();
                }
                else{

                    try {
                        uploadData(extractTextFromPDF(pdfData));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        quePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfManager();
            }
        });
    }
    private void uploadData(String json) {
        System.out.println("Main Uploading button Hu");
        firstString = firstName.getText().toString();
        dateString = date.getText().toString();
        timeStartString = timeStart.getText().toString();
        timeEndString = timeEnd.getText().toString();
        subjectString = subject.getSelectedItem().toString();
        semesterString = semester.getSelectedItem().toString();
        branchString = branch.getSelectedItem().toString();

        reference.child("Questions").orderByChild("date").equalTo(dateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("Main onDataChange hu");
                String uniqueKey = reference.child("Questions").push().getKey();

                HashMap data = new HashMap();
                data.put("uid", FirebaseAuth.getInstance().getUid());
                data.put("subjectName",subjectString);
                data.put("facultyName",firstString);
                data.put("date",dateString);
                data.put("timeStart",timeStartString);
                data.put("timeEnd",timeEndString);
                data.put("semester",semesterString);
                data.put("branch",branchString);
                data.put("jsonQuestion",json);

                if(snapshot.getValue() != null){
                    System.out.println("Main snapshot hu..");
                    List<QuestionsDetailsModel> filteredQuestions = new ArrayList<>();

                    for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                        QuestionsDetailsModel question = questionSnapshot.getValue(QuestionsDetailsModel.class);

                        // Check if timeStart matches the desired value
                        String endT = question.getTimeEnd().substring(0,2)+question.getTimeEnd().substring(3,5);
                        String startT = timeStartString.substring(0,2)+timeStartString.substring(3,5);

                        if ((question.getTimeStart().equals(timeStartString) || Integer.parseInt(startT) < Integer.parseInt(endT)) && question.getSemester().equals(semesterString) && question.getBranch().equals(branchString)) {
                            filteredQuestions.add(question);
                        }
                    }
                    if(filteredQuestions.size() > 0){
                        Toast.makeText(QuestionsAdding.this, "Text/Quiz may be scheduled around this time ", Toast.LENGTH_SHORT).show();
                    }else{
                        reference.child("Questions").child(uniqueKey).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(QuestionsAdding.this, "Uploading successs", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(QuestionsAdding.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else{
                    assert uniqueKey != null;
                    reference.child("Questions").child(uniqueKey).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(QuestionsAdding.this, "Test Successfuly Scheduled", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QuestionsAdding.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private String extractTextFromPDF(Uri uri) throws FileNotFoundException {
        InputStream inputStream = QuestionsAdding.this.getContentResolver().openInputStream(uri);
        List<String> jsonQuestions = new ArrayList<>();
        List<QuestionsModel> questionList = new ArrayList<>();

        PdfReader reader = null;

        try {
            reader = new PdfReader(inputStream);
            int pages = reader.getNumberOfPages();

            for (int i = 0; i < pages; i++) {
                String fileContent = PdfTextExtractor.getTextFromPage(reader, i + 1); // Use i + 1 to extract each page
                List<QuestionsModel> extractedQuestions = extractQuestionsFromPage(fileContent);
                questionList.addAll(extractedQuestions);
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (QuestionsModel question : questionList) {
            String jsonQuestion = question.toJSON();
            jsonQuestions.add(jsonQuestion);
        }

        return jsonQuestions.toString();
    }

    private List<QuestionsModel> extractQuestionsFromPage(String fileContent) {
        List<QuestionsModel> questionList = new ArrayList<>();

        String[] lines = fileContent.split("\\r?\\n");
        StringBuilder questionTextBuilder = new StringBuilder();
        List<String> options = new ArrayList<>();
        String correctAnswer = null;

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("Answer:")) {
                correctAnswer = line.substring(line.indexOf(":") + 1).trim();
            } else if (line.matches("^(\\d+)\\.\\s.+")) {
                if (questionTextBuilder.length() > 0) {
                    String questionText = questionTextBuilder.toString();
                    QuestionsModel question = new QuestionsModel(questionText, options, correctAnswer);
                    questionList.add(question);

                    // Reset variables for the next question
                    questionTextBuilder = new StringBuilder();
                    options = new ArrayList<>();
                    correctAnswer = null;
                }

                String questionNumberString = line.replaceAll("^(\\d+)\\..+", "$1");
                int questionNumber = Integer.parseInt(questionNumberString);
                String questionText = line.replaceAll("^\\d+\\.\\s(.+)", "$1").trim();
                questionTextBuilder.append(questionNumber).append(". ").append(questionText);
            } else if (line.matches("^[A-Z]\\)\\s.+")) {
                options.add(line.trim());
            } else {
                // Append subsequent lines to the question text
                questionTextBuilder.append(" ").append(line);
            }
        }

        // Add the last question to the list
        if (questionTextBuilder.length() > 0) {
            String questionText = questionTextBuilder.toString();
            QuestionsModel question = new QuestionsModel(questionText, options, correctAnswer);
            questionList.add(question);
        }

        return questionList;
    }


    private void openPdfManager() {
        Intent pickPdf = new Intent();
        pickPdf.setType("application/pdf");
        pickPdf.addCategory(Intent.CATEGORY_OPENABLE);
        pickPdf.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(pickPdf,"Select File"),1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfData = data.getData();
            quePreview.setImageResource(R.drawable.pdfpdf);
        }
    }
}