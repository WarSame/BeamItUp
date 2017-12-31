package com.example.graeme.beamitup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.sql.SQLException;

import static com.example.graeme.beamitup.DbAdapter.AccountTable.ACCOUNT_TABLE_NAME;

class DbAdapter {
    //If changing schema, must update db version
    static final int DATABASE_VERSION = 9;
    static final String DATABASE_NAME = "BeamItUp.db";

    private DatabaseHelper DbHelper;
    SQLiteDatabase db;

    DbAdapter(Context context){
        this.DbHelper = new DatabaseHelper(context);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(AccountTable.SQL_CREATE_TABLE);
            db.execSQL(EthTable.SQL_CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            db.execSQL(AccountTable.SQL_DELETE_TABLE);
            db.execSQL(EthTable.SQL_DELETE_TABLE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private void open() throws SQLException
    {
        this.db = this.DbHelper.getWritableDatabase();
    }

    void close()
    {
        this.DbHelper.close();
    }

    static class AccountTable implements BaseColumns {
        static final String ACCOUNT_TABLE_NAME = "account";
        static final String ACCOUNT_EMAIL = "email";
        static final String ACCOUNT_PASSWORD_HASH = "password";
        static final String ACCOUNT_SALT = "salt";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ACCOUNT_TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY,"
                + ACCOUNT_EMAIL + " TEXT,"
                + ACCOUNT_PASSWORD_HASH + " BLOB,"
                + ACCOUNT_SALT + " BLOB,"
                + "CONSTRAINT email_unique UNIQUE"
                + "(" + ACCOUNT_EMAIL +")"
                + ")";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME;
    }

    static class EthTable implements  BaseColumns {
        static final String ETH_TABLE_NAME = "eth";
        static final String ETH_ACCOUNT_ID = "account_id";
        static final String ETH_ADDRESS = "address";
        static final String ETH_ENC_PRIVATE_KEY = "enc_private_key";
        static final String ETH_IV = "iv";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ETH_TABLE_NAME +
            " (" + _ID + " INTEGER PRIMARY KEY,"
                + ETH_ACCOUNT_ID + " INTEGER,"
                + ETH_ADDRESS + " TEXT,"
                + ETH_ENC_PRIVATE_KEY + " BLOB,"
                + ETH_IV + " BLOB,"
                + "FOREIGN KEY (" + ETH_ACCOUNT_ID + ") REFERENCES "
                + ACCOUNT_TABLE_NAME + "(" + _ID + "))";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ETH_TABLE_NAME;
    }

}
