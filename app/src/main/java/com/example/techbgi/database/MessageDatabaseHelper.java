package com.example.techbgi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.techbgi.model.MessageMember;

import java.util.ArrayList;
import java.util.List;

public class MessageDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "message_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MESSAGES = "messages";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_SENDER_UID = "sender_uid";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public MessageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_MESSAGES +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MESSAGE + " TEXT," +
                COLUMN_SENDER_UID + " TEXT," +
                COLUMN_TIMESTAMP + " LONG" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }

    public void insertMessage(MessageMember message) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE, message.getMessage());
        values.put(COLUMN_SENDER_UID, message.getSenderuid());
        values.put(COLUMN_TIMESTAMP, message.getTimeStamp());
        db.insert(TABLE_MESSAGES, null, values);
        db.close();
    }

    public List<MessageMember> getAllMessages() {
        List<MessageMember> messageList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MESSAGES, null);
        if (cursor.moveToFirst()) {
            do {
                MessageMember message = new MessageMember();
                message.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE)));
                message.setSenderuid(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SENDER_UID)));
                message.setTimeStamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)));
                messageList.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return messageList;
    }

    public void updateMessages(List<MessageMember> messages) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MESSAGES);
        for (MessageMember message : messages) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MESSAGE, message.getMessage());
            values.put(COLUMN_SENDER_UID, message.getSenderuid());
            values.put(COLUMN_TIMESTAMP, message.getTimeStamp());
            db.insert(TABLE_MESSAGES, null, values);
        }
        db.close();
    }

    public void deleteChat(String senderUID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MESSAGES, COLUMN_SENDER_UID + " = ?", new String[]{senderUID});
        db.close();
    }
}
