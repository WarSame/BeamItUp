package com.example.graeme.beamitup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BeamItUpDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "BeamItUpDbHelper";

    BeamItUpDbHelper(Context context){
        super(context, DbAdapter.DATABASE_NAME, null, DbAdapter.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(DbAdapter.AccountTable.SQL_CREATE_TABLE);
        db.execSQL(DbAdapter.EthTable.SQL_CREATE_TABLE);
        db.execSQL(DbAdapter.AccountEthTable.SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DbAdapter.AccountTable.SQL_DELETE_TABLE);
        db.execSQL(DbAdapter.EthTable.SQL_DELETE_TABLE);
        db.execSQL(DbAdapter.AccountEthTable.SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
