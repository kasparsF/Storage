package com.example.kasparsfisers.storage.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kasparsfisers.storage.data.Contract.LoginEntry;

/**
 * Created by kaspars.fisers on 10/12/2016.
 */

public class LoginDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "login.db";
    private static final int DATABASE_VERSION = 1;

    public LoginDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USER_TABLE ="CREATE TABLE " + LoginEntry.TABLE_NAME + " ("
                + LoginEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LoginEntry.COLUMN_USER_NAME + " TEXT NOT NULL, "
                + LoginEntry.COLUMN_PASSWORD + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
