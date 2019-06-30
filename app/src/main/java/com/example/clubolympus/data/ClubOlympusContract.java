package com.example.clubolympus.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ClubOlympusContract {
    private ClubOlympusContract() {
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "olympus";
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.clubolympus";
    public static final String PATH_MEMBERS = "members";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);


    public static final class MemberEntry implements BaseColumns {
        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
        public static final String TABLE_NAME = "members";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);
        public static final String KEY_ID = _ID;
        public static final String KEY_FIRST_NAME = "first_name";
        public static final String KEY_LAST_NAME = "last_name";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_SPORT = "sport";

        public enum GENDER {
            UNKNOWN("Unknown"),
            MALE("Male"),
            FEMALE("Female");
            String s;

            GENDER(String s) {
                this.s = s;
            }

            public static boolean contains(String gender) {
                for (GENDER g : GENDER.values()) {
                    if (gender.equals(g.toString())) {
                        return true;
                    }
                }
                return true;
            }


            @Override
            public String toString() {
                return s;
            }
        }
    }
}
