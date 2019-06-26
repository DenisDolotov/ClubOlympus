package com.example.clubolympus.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.clubolympus.data.ClubOlympusContract.*;

public class OlympusDatabaseHandler extends SQLiteOpenHelper {
    public OlympusDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + MemberEntry.TABLE_NAME + "(" +
                MemberEntry.KEY_ID + " INTEGER PRIMARY KEY,"
                + MemberEntry.KEY_FIRST_NAME + " TEXT,"
                + MemberEntry.KEY_LAST_NAME + " TEXT,"
                + MemberEntry.KEY_GENDER + " TEXT,"
                + MemberEntry.KEY_SPORT + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MemberEntry.TABLE_NAME);
        onCreate(db);
    }
}
