package com.example.techbgi.database;

import androidx.annotation.NonNull;

import com.example.techbgi.activity.AttendanceTaking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DbHelper {
    int flag = 1;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

    public void addClass(String semester,String className, String subjectName,String uid) {
        String id = reference.child("classtable").push().getKey();
        HashMap<String, String> hs = new HashMap<>();
        hs.put("classId", id);
        hs.put("semester",semester);
        hs.put("className", className);
        hs.put("subjectName", subjectName);
        hs.put("uid", uid);
        reference.child("classtable").child(id).setValue(hs);
    }

    public String addStudent(String classId, String rollNo, String studentName) {
        String id = reference.child("studenttable").push().getKey();
        HashMap<String, String> hs = new HashMap<>();
        hs.put("studentId", id);
        hs.put("studentName", studentName);
        hs.put("classId", classId);
        hs.put("rollNo", rollNo);

        assert id != null;
        reference.child("studenttable").child(id).setValue(hs);
        return id;
    }

    public void deleteClass(String classId,String semester,String className,String subjectName) {
        reference.child("classtable").child(classId).removeValue();
        delelteStudentTable(classId,semester,className,subjectName);
    }

    private void delelteStudentTable(String classId_Imp, String semester, String className, String subjectName) {
        reference.child("studenttable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if(snapshot1.child("classId").getValue().toString().equals(classId_Imp))
                    {
//                        String roll = snapshot1.child("rollNo").getValue().toString();
//                        String studentId = snapshot1.child("studentId").getValue().toString();
//                        statusTableDelete(roll,semester,className,subjectName,studentId);
                        snapshot1.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void statusTableDelete(String roll, String semester, String className, String subjectName, String studentId_Imp) {
//        reference.child("statustable").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot snapshot1 : snapshot.getChildren())
//                {
//                    snapshot1.
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void AttendanceOfStudent(String roll_Imp, String semester_Imp, String className_Imp, String subjectName_Imp, String studentId) {
//        reference.child("AttendanceOfStudent").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                snapshot.child(roll_Imp+semester_Imp+className_Imp+subjectName_Imp).getValue
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void deleteStudent(String studentId, String date) {
        reference.child("studenttable").child(studentId).removeValue();
        reference.child("statustable").child(studentId+date).removeValue();

//        reference.child("statustable").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                snapshot.child(studentId+date).
//                for(DataSnapshot snapshot1 : snapshot.getChildren())
//                {
//                    if(snapshot1.child(studentId+date).getValue().toString().equals(studentId))
//                    {
//                        reference.child("statustable").child(studentId).getParent().removeValue();
//                    }
//                }
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    public void updateClassData(String classId,String semester, String className, String subjectName,String uid) {
            HashMap<String, Object> hs = new HashMap<>();
            hs.put("semester", semester);
            hs.put("classId", classId);
            hs.put("className", className);
            hs.put("subjectName", subjectName);
            hs.put("uid",uid);

        reference.child("classtable").child(classId).updateChildren(hs);
    }

    public void updateStudentData(String studentId, String studentName) {
        HashMap<String, Object> hs = new HashMap<>();
        hs.put("studentName", studentName);

        reference.child("studenttable").child(studentId).updateChildren(hs);
    }

    public void addStatus(String semester, String className, String subjectName, String studentName, String roll, String studentId, String classId, String date, String status) {
        HashMap<String, Object> hs = new HashMap<>();
        hs.put("studentId", studentId);
        hs.put("date", date);
        hs.put("status", status);
        hs.put("classId", classId);

        HashMap<String, Object> hs1 = new HashMap<>();
        hs1.put("studentName", studentName);
        hs1.put("date", date);
        hs1.put("status", status);


        if (reference.child("statustable").child(studentId + date).get().isComplete()) {
            reference.child("statustable").child(studentId + date).updateChildren(hs);
            DatabaseReference attRef = reference.child("AttendanceOfStudent").child(roll+semester+className+subjectName+ "/" +date);
            attRef.updateChildren(hs1);
        } else {
            reference.child("statustable").child(studentId + date).setValue(hs);
            DatabaseReference attRef = reference.child("AttendanceOfStudent").child(roll+semester+className+subjectName+ "/" +date);
            attRef.setValue(hs1);
        }
    }

}