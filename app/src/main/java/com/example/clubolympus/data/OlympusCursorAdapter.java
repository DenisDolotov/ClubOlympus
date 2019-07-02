package com.example.clubolympus.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.clubolympus.R;

import static com.example.clubolympus.data.ClubOlympusContract.*;

public class OlympusCursorAdapter extends CursorAdapter {

    public OlympusCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.cursor_item_view,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvFirstName = view.findViewById(R.id.tvFirstName);
        TextView tvLastName = view.findViewById(R.id.tvLastName);
        TextView tvSport = view.findViewById(R.id.tvSport);
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_LAST_NAME));
        String sport = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_SPORT));
        tvFirstName.setText(firstName);
        tvLastName.setText(lastName);
        tvSport.setText(sport);
    }
}
