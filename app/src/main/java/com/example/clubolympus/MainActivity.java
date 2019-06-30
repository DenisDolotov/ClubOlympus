package com.example.clubolympus;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.clubolympus.data.ClubOlympusContract;

import static com.example.clubolympus.data.ClubOlympusContract.*;

public class MainActivity extends AppCompatActivity {
    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });

        tvData = findViewById(R.id.tvData);
    }

    protected void displayData(View view) {
        String[] projection = {
                MemberEntry._ID,
                MemberEntry.KEY_FIRST_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_GENDER,
                MemberEntry.KEY_SPORT
        };
        Cursor cursor = getContentResolver().query(
                MemberEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        tvData.setText("All members\n\n");
        tvData.append(MemberEntry._ID + " " +
                MemberEntry.KEY_FIRST_NAME + " " +
                MemberEntry.KEY_LAST_NAME + " " +
                MemberEntry.KEY_GENDER + " " +
                MemberEntry.KEY_SPORT+"\n");
          int countColumns = cursor.getColumnCount();
//        int idIndex = cursor.getColumnIndex(MemberEntry._ID);
//        int FirstNameIndex = cursor.getColumnIndex(MemberEntry.KEY_FIRST_NAME);
//        int LastNameIndex = cursor.getColumnIndex(MemberEntry.KEY_LAST_NAME);
//        int GenderIndex = cursor.getColumnIndex(MemberEntry.KEY_GENDER);
//        int SportIndex = cursor.getColumnIndex(MemberEntry.KEY_SPORT);
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < countColumns; i++) {
                    tvData.append(cursor.getString(i)+" ");
                }
                tvData.append("\n");
                //  int currentId = cursor.getInt(idIndex);
                //tvData.append(DatabaseUtils.dumpCursorToString(cursor));
            } while (cursor.moveToNext());
        }
    }
}
