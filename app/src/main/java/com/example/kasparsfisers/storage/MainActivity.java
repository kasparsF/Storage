package com.example.kasparsfisers.storage;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasparsfisers.storage.data.Contract.LoginEntry;
import com.example.kasparsfisers.storage.data.LoginDbHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginDbHelper mDbHelper;
    Button fromDB, dummy, fromTxt, fromPref, clear;
    TextView displayView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new LoginDbHelper(this);
        fromDB = (Button) findViewById(R.id.dB);
        dummy = (Button) findViewById(R.id.dummyData);
        clear = (Button) findViewById(R.id.clear);
        fromPref = (Button) findViewById(R.id.sharedPref);
        fromTxt = (Button) findViewById(R.id.txt);
        fromDB.setOnClickListener(this);
        dummy.setOnClickListener(this);
        clear.setOnClickListener(this);
        fromPref.setOnClickListener(this);
        fromTxt.setOnClickListener(this);
        displayView = (TextView) findViewById(R.id.textView);


    }


    private void inserUser() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LoginEntry.COLUMN_USER_NAME, "Kaspars");
        values.put(LoginEntry.COLUMN_PASSWORD, "Fisers");
        db.insert(LoginEntry.TABLE_NAME, null, values);

    }


    private void displayDatabaseInfo() {
        //LoginDbHelper mDbHelper = new LoginDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] proj = {
                LoginEntry._ID,
                LoginEntry.COLUMN_USER_NAME,
                LoginEntry.COLUMN_PASSWORD
        };
        Cursor cursor = db.query(
                LoginEntry.TABLE_NAME,
                proj,
                null,
                null,
                null,
                null,
                null
        );

        displayView = (TextView) findViewById(R.id.textView);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // table).
            displayView.setText("user table contains " + cursor.getCount() + " users.\n\n");
            displayView.append(LoginEntry._ID + " - " +
                    LoginEntry.COLUMN_USER_NAME + " - " +
                    LoginEntry.COLUMN_PASSWORD + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(LoginEntry._ID);
            int usernameColumnIndex = cursor.getColumnIndex(LoginEntry.COLUMN_USER_NAME);
            int passwordColumnIndex = cursor.getColumnIndex(LoginEntry.COLUMN_PASSWORD);

            while (cursor.moveToNext()) {

                int currentID = cursor.getInt(idColumnIndex);
                String currentUsername = cursor.getString(usernameColumnIndex);
                String currentPassword = cursor.getString(passwordColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentUsername + " - " +
                        currentPassword));
            }
        } finally {
            cursor.close();
        }

    }

    private void savePref(String key, String value) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void loadPref() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        displayView = (TextView) findViewById(R.id.textView);
        displayView.setText("Username: " + username + "\n" + "Password: " + password);
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.dB) {
            displayDatabaseInfo();
        }
        if (v.getId() == R.id.dummyData) {
            inserUser();
        }
        if (v.getId() == R.id.sharedPref) {
            loadPref();
        }
        if (v.getId() == R.id.save) {
            savePref("username", "Kaspars");
            savePref("password", "Fisers");
        }

        if (v.getId() == R.id.clear) {
            displayView.setText("");
        }

        if (v.getId() == R.id.txt) {

            //Save to internal storage
            String fileName = "users";
            try {
                FileOutputStream fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
                fileOutputStream.write("Kaspars Fisers".getBytes());
                fileOutputStream.close();
                Toast.makeText(this, "Msg Saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Read from internal storage
            try {
                String Message;
                FileInputStream fileInputStream = openFileInput("users");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                while ((Message = bufferedReader.readLine()) != null) {
                    stringBuffer.append(Message + "\n");
                }
                displayView.setText(stringBuffer.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //External storage
            //Saving to file
//
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//            File file = new File(path, "data.txt");
//            String msg = "Kaspars Fisers";
//
//            try {
//                FileOutputStream out = new FileOutputStream(file);
//                out.write(msg.getBytes());
//                out.close();
//                Toast.makeText(this, "Msg Saved", Toast.LENGTH_SHORT).show();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//          // Reading from file
//            try {
//                int length = (int) file.length();
//                byte[] bytes = new byte[length];
//
//                FileInputStream fi = new FileInputStream(file);
//                fi.read(bytes);
//                String text = new String(bytes);
//
//                displayView.setText(text);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
