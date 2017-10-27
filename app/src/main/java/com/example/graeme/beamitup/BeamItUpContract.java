package com.example.graeme.beamitup;

import android.provider.BaseColumns;

final class BeamItUpContract {
    //If changing schema, must update db version
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "BeamItUp.db";

    private BeamItUpContract(){}

    static class Account implements BaseColumns {
        static final String ACCOUNT_TABLE_NAME = "account";
        static final String ACCOUNT_COLUMN_EMAIL = "email";
        static final String ACCOUNT_COLUMN_PASSWORD_HASH = "password";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ACCOUNT_TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," + ACCOUNT_COLUMN_EMAIL + " TEXT," +
                ACCOUNT_COLUMN_PASSWORD_HASH + " BLOB)";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME;
    }

}
