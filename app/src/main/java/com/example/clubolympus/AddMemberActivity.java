package com.example.clubolympus;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.clubolympus.data.ClubOlympusContract.MemberEntry;

public class AddMemberActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDIT_MEMBER_LOADER = 333;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etSport;
    private Spinner spinnerGender;
    private MemberEntry.GENDER gender = MemberEntry.GENDER.UNKNOWN;
    private ArrayAdapter spinnerAdapter;
    private Uri currentMemberUri;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_member_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_member:
                saveMember();
                return true;
            case R.id.delete_member:
                showDeleteMemberDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the member?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMember();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteMember() {
        if (currentMemberUri!=null){
            int rowsDeleted = getContentResolver()
                    .delete(currentMemberUri, null, null);
            if (rowsDeleted==0){
                Toast.makeText(this, "Deleting of data from the table failed",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Member Deleted",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();

        currentMemberUri = intent.getData();

        if (currentMemberUri == null) {
            setTitle("Add a Member");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the Member");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER, null, this);
        }


        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etSport = findViewById(R.id.etSport);
        spinnerGender = findViewById(R.id.spinnerGender);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(spinnerAdapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectedGender)) {
                    switch (selectedGender) {
                        case "Male":
                            gender = MemberEntry.GENDER.MALE;
                            break;
                        case "Female":
                            gender = MemberEntry.GENDER.FEMALE;
                            break;
                        default:
                            gender = MemberEntry.GENDER.UNKNOWN;
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentMemberUri==null){
            MenuItem menuItem = menu.findItem(R.id.delete_member);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void saveMember() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String sport = etSport.getText().toString().trim();

        if (firstName.isEmpty()){
            Toast.makeText(this, "Enter the first name", Toast.LENGTH_LONG).show();
            return;
        }else
        if (lastName.isEmpty()){
            Toast.makeText(this, "Enter the last name", Toast.LENGTH_LONG).show();
            return;
        }else
        if (sport.isEmpty()){
            Toast.makeText(this, "Enter the sport", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MemberEntry.KEY_FIRST_NAME, firstName);
        contentValues.put(MemberEntry.KEY_LAST_NAME, lastName);
        contentValues.put(MemberEntry.KEY_GENDER, gender.toString());
        contentValues.put(MemberEntry.KEY_SPORT, sport);

        if (currentMemberUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI, contentValues);
            if (uri == null) {
                Toast.makeText(this, "Insertion of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Data saved",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            int rowsChanged = getContentResolver()
                    .update(currentMemberUri, contentValues, null, null);
            if (rowsChanged==0){
                Toast.makeText(this, "Saving of data in the table failed",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Member updated",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                MemberEntry._ID,
                MemberEntry.KEY_FIRST_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_GENDER,
                MemberEntry.KEY_SPORT};
        CursorLoader cursorLoader = new CursorLoader(this,
                currentMemberUri,
                projection,
                null,
                null,
                null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex(MemberEntry.KEY_FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(MemberEntry.KEY_LAST_NAME);
            int genderIndex = cursor.getColumnIndex(MemberEntry.KEY_GENDER);
            int sportIndex = cursor.getColumnIndex(MemberEntry.KEY_SPORT);

            String firstName = cursor.getString(firstNameIndex);
            String lastName = cursor.getString(lastNameIndex);
            String gender = cursor.getString(genderIndex);
            String sport = cursor.getString(sportIndex);

            etFirstName.setText(firstName);
            etLastName.setText(lastName);
            etSport.setText(sport);

            if (MemberEntry.GENDER.valueFromString(gender) == null) {
                spinnerGender.setSelection(0);
            } else {
                switch (MemberEntry.GENDER.valueFromString(gender)) {
                    case UNKNOWN:
                        spinnerGender.setSelection(0);
                        break;
                    case MALE:
                        spinnerGender.setSelection(1);
                        break;
                    case FEMALE:
                        spinnerGender.setSelection(2);
                        break;
                    default:
                        spinnerGender.setSelection(0);
                        break;
                }
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
