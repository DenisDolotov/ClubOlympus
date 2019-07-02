package com.example.clubolympus;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clubolympus.data.ClubOlympusContract;
import com.example.clubolympus.data.OlympusCursorAdapter;

import static com.example.clubolympus.data.ClubOlympusContract.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView listView;
    public static final int MEMBER_LOADER = 123;
    OlympusCursorAdapter olympusCursorAdapter;

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
        listView = findViewById(R.id.listView);
        olympusCursorAdapter = new OlympusCursorAdapter(this,null);
        listView.setAdapter(olympusCursorAdapter);
        getSupportLoaderManager().initLoader(MEMBER_LOADER,null,this);
    }

//    protected void displayData(View view) {
//        String[] projection = {
//                MemberEntry._ID,
//                MemberEntry.KEY_FIRST_NAME,
//                MemberEntry.KEY_LAST_NAME,
//                MemberEntry.KEY_GENDER,
//                MemberEntry.KEY_SPORT};
//        Cursor cursor = getContentResolver().query(
//                MemberEntry.CONTENT_URI,
//                projection,
//                null,
//                null,
//                null);
//        listView.setAdapter(new OlympusCursorAdapter(this, cursor));
//    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                MemberEntry._ID,
                MemberEntry.KEY_FIRST_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_SPORT};
        CursorLoader cursorLoader = new CursorLoader(this,
                MemberEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        olympusCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        olympusCursorAdapter.swapCursor(null);
    }
}
