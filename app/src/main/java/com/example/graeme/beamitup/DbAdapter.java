package com.example.graeme.beamitup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.sql.SQLException;

public class DbAdapter {
    //If changing schema, must update db version
    static final int DATABASE_VERSION = 16;
    static final String DATABASE_NAME = "BeamItUp.db";

    private DatabaseHelper DbHelper;
    protected SQLiteDatabase db;

    public DbAdapter(Context context){
        this.DbHelper = new DatabaseHelper(context);
        open();
    }

    static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(EthTable.SQL_CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            db.execSQL(EthTable.SQL_DELETE_TABLE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private void open()
    {
        this.db = this.DbHelper.getWritableDatabase();
    }

    public void close()
    {
        this.DbHelper.close();
    }

    protected static class EthTable implements  BaseColumns {
        public static final String ETH_TABLE_NAME = "eth";
        public static final String ETH_ADDRESS = "address";
        public static final String ETH_NICKNAME = "nickname";
        public static final String ETH_ENC_LONG_PASSWORD = "enc_long_password";
        public static final String ETH_IV = "iv";
        public static final String ETH_WALLET_NAME = "wallet_name";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ETH_TABLE_NAME +
            " (" + _ID + " INTEGER PRIMARY KEY,"
                + ETH_ADDRESS + " TEXT,"
                + ETH_NICKNAME + " TEXT,"
                + ETH_ENC_LONG_PASSWORD + " BLOB,"
                + ETH_IV + " BLOB,"
                + ETH_WALLET_NAME + " TEXT)";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ETH_TABLE_NAME;
    }
}
