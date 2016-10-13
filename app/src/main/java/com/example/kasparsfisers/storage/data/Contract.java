package com.example.kasparsfisers.storage.data;

import android.provider.BaseColumns;

/**
 * Created by kaspars.fisers on 10/12/2016.
 */

public final class Contract {

    private Contract(){}

    public static abstract class LoginEntry implements BaseColumns {

        public static final String TABLE_NAME = "users";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_USER_NAME = "username";
        public static final String COLUMN_PASSWORD = "password";

    }
}
