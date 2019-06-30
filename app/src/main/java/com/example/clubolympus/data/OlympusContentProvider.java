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
                Log.e("logCat", "Can't query incorrect URI " + uri);
                throw new IllegalArgumentException("Can't query incorrect URI " + uri);

        }


    }


    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                return MemberEntry.CONTENT_MULTIPLE_ITEMS;
            case MEMBERS_ID:

                return MemberEntry.CONTENT_SINGLE_ITEM;
            default:
                Log.e("logCat", "Incorrect URI " + uri);
                throw new IllegalArgumentException("Incorrect URI " + uri);

        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (values != null) {
            String firstName = values.getAsString(MemberEntry.KEY_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("First Name is NULL");
            }
            String lastName = values.getAsString(MemberEntry.KEY_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("Last Name is NULL");
            }
            String sport = values.getAsString(MemberEntry.KEY_SPORT);
            if (sport == null) {
                throw new IllegalArgumentException("Sport is NULL");
            }
            String gender = values.getAsString(MemberEntry.KEY_GENDER);
            if (gender == null) {
                throw new IllegalArgumentException("Gender is NULL");
            }
            if (!MemberEntry.GENDER.contains(gender)) {
                throw new IllegalArgumentException("Gender is invalid");
            }
        }

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
        SQLiteDatabase db = olympusDatabaseHandler.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                return db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);
            case MEMBERS_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);
            default:
                Log.e("logCat", "Can't delete this URI " + uri);
                throw new IllegalArgumentException("Can't delete this URI " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values != null) {
            if (values.containsKey(MemberEntry.KEY_FIRST_NAME)) {
                String firstName = values.getAsString(MemberEntry.KEY_FIRST_NAME);
                if (firstName == null) {
                    throw new IllegalArgumentException("First Name is NULL");
                }
            }
            if (values.containsKey(MemberEntry.KEY_LAST_NAME)) {
                String lastName = values.getAsString(MemberEntry.KEY_LAST_NAME);
                if (lastName == null) {
                    throw new IllegalArgumentException("Last Name is NULL");
                }
            }
            if (values.containsKey(MemberEntry.KEY_SPORT)) {
                String sport = values.getAsString(MemberEntry.KEY_SPORT);
                if (sport == null) {
                    throw new IllegalArgumentException("Sport is NULL");
                }
            }
            if (values.containsKey(MemberEntry.KEY_GENDER)) {
                String gender = values.getAsString(MemberEntry.KEY_GENDER);
                if (gender == null) {
                    throw new IllegalArgumentException("Sport is NULL");
                }
                if (!MemberEntry.GENDER.contains(gender)) {
                    throw new IllegalArgumentException("Sport is invalid");
                }
            }
        }

        SQLiteDatabase db = olympusDatabaseHandler.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                return db.update(MemberEntry.TABLE_NAME, values, selection, selectionArgs);
            case MEMBERS_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.update(MemberEntry.TABLE_NAME, values, selection, selectionArgs);
            default:
                Log.e("logCat", "Can't update this URI " + uri);
                throw new IllegalArgumentException("Can't update this URI " + uri);
        }
    }
}
