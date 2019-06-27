package com.example.clubolympus.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import static com.example.clubolympus.data.ClubOlympusContract.*;

public class OlympusContentProvider extends ContentProvider {
    private OlympusDatabaseHandler olympusDatabaseHandler;
    public static final int MEMBERS = 111;
    public static final int MEMBERS_ID = 222;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, PATH_MEMBERS, MEMBERS);
        sUriMatcher.addURI(AUTHORITY, PATH_MEMBERS + "/#", MEMBERS_ID);

    }

    @Override
    public boolean onCreate() {
        olympusDatabaseHandler = new OlympusDatabaseHandler(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = olympusDatabaseHandler.getReadableDatabase();

            int match = sUriMatcher.match(uri);
            switch (match) {
                case MEMBERS:
                    return db.query(MemberEntry.TABLE_NAME,
                            projection, selection, selectionArgs,
                            null, null, sortOrder);
                case MEMBERS_ID:
                    selection = MemberEntry._ID + "=?";
                    selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                    return db.query(MemberEntry.TABLE_NAME,
                            projection, selection, selectionArgs,
                            null, null, sortOrder);
                default:
                    Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_LONG).show();
                    throw new IllegalArgumentException("Can't query incorrect URI " + uri);

            }



    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try (SQLiteDatabase db = olympusDatabaseHandler.getWritableDatabase()) {
            int match = sUriMatcher.match(uri);
            if (match == MEMBERS) {
                long id = db.insert(MemberEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    Log.e("logCat", "Insertion of data in the table failed for " + uri);
                    return null;
                }
                return ContentUris.withAppendedId(uri, id);
            } else {
                Log.e("logCat", "Insertion of data in the table failed for " + uri);
                throw new IllegalArgumentException("Can't query incorrect URI " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
