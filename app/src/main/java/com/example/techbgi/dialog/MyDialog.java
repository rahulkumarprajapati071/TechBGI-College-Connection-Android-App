package com.example.techbgi.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.techbgi.R;

import java.util.ArrayList;
import java.util.Arrays;


public class MyDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG = "addClass";
    public static final String CLASS_UPDATE_DIALOG = "updateClass";
    public static final String STUDENT_ADD_DIALOG = "addStudent";
    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";
    OnclickListenter listenter;
    private String rollNo;
    private String studentName;
    private String semester;
    private String className;
    private String subjectName;

    public MyDialog(String rollNo, String studentName) {

        this.rollNo = rollNo;
        this.studentName = studentName;
    }
    public MyDialog(String semester,String className, String subjectName) {

        this.semester = semester;
        this.className = className;
        this.subjectName = subjectName;
    }
    public MyDialog(){

    }

    public  interface OnclickListenter{
        void onClick(String text1,String text2,String text3);
    }

    public void setListenter(OnclickListenter listenter) {
        this.listenter = listenter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if(getTag().equals(CLASS_ADD_DIALOG)) dialog=getAddClassDialog();
        if(getTag().equals(STUDENT_ADD_DIALOG))dialog = getStudentDialog();
        if(getTag().equals(CLASS_UPDATE_DIALOG))dialog = getUpdateClassDialog();
        if(getTag().equals(STUDENT_UPDATE_DIALOG))dialog = getUpdateStudentDialog();
        assert dialog != null;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;

    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Student");

        EditText roll_edt = view.findViewById(R.id.edt1);
        roll_edt.setHint("RollNumber");
        EditText name_edt = view.findViewById(R.id.edt2);
        name_edt.setHint("Name");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        roll_edt.setText(rollNo);
        roll_edt.setEnabled(false);
        name_edt.setText(studentName);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String rollNo = roll_edt.getText().toString();
                    String studentName = name_edt.getText().toString();
                    if(rollNo.length() <= 0 && studentName.length() <= 0){
                        Toast.makeText(getContext(), "Please enter all required data", Toast.LENGTH_SHORT).show();
                    }else{
                        listenter.onClick("",rollNo,studentName);
                        dismiss();
                    }
                }catch (NumberFormatException nfe)
                {
                    Toast.makeText(getContext(), "Add Valid Roll Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialogsemester,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Class");

        Spinner spinner1 = view.findViewById(R.id.spinner1);
        String[] valueSem = {"Current Semester","1","2","3","4","5","6","7","8"};
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(valueSem));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,arrayList2);
        spinner1.setAdapter(adapter);
        spinner1.setSelection(adapter.getPosition(semester));

        Spinner spinner2 = view.findViewById(R.id.spinner2);
        String[] valueBranch = {"Choose Branch","CSE","AIML","CSBS","CSE-IOT","CSIT","CIVIL","ME","EC","EEE",
                "IT","EE","EX","AUTO","CHEMICAL","FT","MINING","TX","OTHERS"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(valueBranch));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,arrayList1);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(adapter2.getPosition(className));

        Spinner spinner3 = view.findViewById(R.id.spinner3);
        String[] valueSubject = {"Choose Subject","Data Mining","TOC","DBMS","OS","CN","Computer Graphics","Software Engineering","ADA","Wireless and mobile computing",
                "Compiler Designing","ML","CA","E-Commerce","EEE","Engineering Drawing","Data Structure"};
        ArrayList<String> arrayList3 = new ArrayList<>(Arrays.asList(valueSubject));
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,arrayList3);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setSelection(adapter3.getPosition(subjectName));

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semester = spinner1.getSelectedItem().toString();
                String className = spinner2.getSelectedItem().toString();
                String subjectName = spinner3.getSelectedItem().toString();

                if(spinner1.getSelectedItemId() <= 0 && spinner2.getSelectedItemId() <= 0 && spinner3.getSelectedItemId() <= 0){
                    Toast.makeText(getContext(), "Please enter all required data", Toast.LENGTH_SHORT).show();
                }else{
                    listenter.onClick(semester,className,subjectName);
                    dismiss();
                }
            }
        });
        return builder.create();

    }

    private Dialog getStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Student");

        EditText roll_edt = view.findViewById(R.id.edt1);
        roll_edt.setHint("RollNumber");
        EditText name_edt = view.findViewById(R.id.edt2);
        name_edt.setHint("Name");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String rollNo = roll_edt.getText().toString();
                    String studentName = name_edt.getText().toString();
                    roll_edt.setText(String.valueOf(Integer.parseInt(rollNo)+1));
                    name_edt.setText("");
                    if(rollNo.length() <= 0 && studentName.length() <= 0){
                        Toast.makeText(getContext(), "Please enter all required data", Toast.LENGTH_SHORT).show();
                    }else{
                        listenter.onClick("",rollNo,studentName);
                    }
                }catch (NumberFormatException nfe)
                {
                    Toast.makeText(getContext(), "Add Valid Roll Number", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return builder.create();
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialogsemester,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Class");

        Spinner sem_edt = view.findViewById(R.id.spinner1);
        Spinner class_edt = view.findViewById(R.id.spinner2);
        Spinner subject_edt = view.findViewById(R.id.spinner3);

        Spinner spinner1 = view.findViewById(R.id.spinner1);
        String[] valueSem = {"Current Semester","1","2","3","4","5","6","7","8"};
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(valueSem));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,arrayList2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        Spinner spinner2 = view.findViewById(R.id.spinner2);
        String[] valueBranch = {"Choose Branch","CSE","AIML","CSBS","CSE-IOT","CSIT","CIVIL","ME","EC","EEE",
                "IT","EE","EX","AUTO","CHEMICAL","FT","MINING","TX","OTHERS"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(valueBranch));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,arrayList1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        Spinner spinner3 = view.findViewById(R.id.spinner3);
        String[] valueSubject = {"Choose Subject","Data Mining","TOC","DBMS","OS","CN","Computer Graphics","Software Engineering","ADA","Wireless and mobile computing",
                "Compiler Designing","ML","CA","E-Commerce","EEE","Engineering Drawing","Data Structure"};
        ArrayList<String> arrayList3 = new ArrayList<>(Arrays.asList(valueSubject));
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,arrayList3);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String semester = sem_edt.getSelectedItem().toString();
                String className = class_edt.getSelectedItem().toString();
                String subjectName = subject_edt.getSelectedItem().toString();

                if(sem_edt.getSelectedItemId() <= 0 && class_edt.getSelectedItemId() <= 0 && subject_edt.getSelectedItemId() <= 0){
                    Toast.makeText(getContext(), "Please enter all required data", Toast.LENGTH_SHORT).show();
                }else{
                    listenter.onClick(semester,className,subjectName);
                    dismiss();
                }
            }
        });
        return builder.create();
    }

}
