package com.example.graeme.beamitup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.sql.SQLException;

class DbAdapter {
    //If changing schema, must update db version
    static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "BeamItUp.db";

    DatabaseHelper DbHelper;
    SQLiteDatabase db;

    DbAdapter(Context context){
        this.DbHelper = new DatabaseHelper(context);
    }

    static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(AccountTable.SQL_CREATE_TABLE);
            db.execSQL(AccountEthTable.SQL_CREATE_TABLE);
            db.execSQL(AccountEthTable.SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            // Adding any table mods to this guy here
        }
    }

    DbAdapter open() throws SQLException
    {
        this.db = this.DbHelper.getWritableDatabase();
        return this;
    }

    void close()
    {
        this.DbHelper.close();
    }

    static class AccountTable implements BaseColumns {
        static final String ACCOUNT_TABLE_NAME = "account";
        static final String ACCOUNT_COLUMN_EMAIL = "email";
        static final String ACCOUNT_COLUMN_PASSWORD_HASH = "password";
        static final String ACCOUNT_COLUMN_SALT = "salt";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ACCOUNT_TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," + ACCOUNT_COLUMN_EMAIL + " TEXT," +
                ACCOUNT_COLUMN_PASSWORD_HASH + " BLOB," + ACCOUNT_COLUMN_SALT + " BLOB)";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME;
    }

    static class AccountEthTable implements BaseColumns {
        static final String ACCOUNT_ETH_TABLE_NAME = "account_eth";
        static final String ACCOUNT_ETH_COLUMN_ACCOUNT_ID = "account_id";
        static final String ACCOUNT_ETH_COLUMN_ETH_ID = "eth_id";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ACCOUNT_ETH_TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," + ACCOUNT_ETH_COLUMN_ACCOUNT_ID + " INTEGER," +
                ACCOUNT_ETH_COLUMN_ETH_ID + " INTEGER)";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ACCOUNT_ETH_TABLE_NAME;
    }

    static class EthTable implements  BaseColumns {
        static final String ETH_TABLE_NAME = "eth";
        static final String ETH_ADDRESS = "address";
        static final String ETH_ENC_PRIVATE_KEY = "enc_private_key";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ETH_TABLE_NAME +
            " (" + _ID + " INTEGER PRIMARY KEY," + ETH_ADDRESS + " TEXT," +
            ETH_ENC_PRIVATE_KEY + " BLOB)";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ETH_TABLE_NAME;

    }

}