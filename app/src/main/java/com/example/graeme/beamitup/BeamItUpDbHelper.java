package com.example.graeme.beamitup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BeamItUpDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "BeamItUpDbHelper";

    BeamItUpDbHelper(Context context){
        super(context, BeamItUpContract.DATABASE_NAME, null, BeamItUpContract.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(BeamItUpContract.AccountTable.SQL_CREATE_TABLE);
        db.execSQL(BeamItUpContract.EthTable.SQL_CREATE_TABLE);
        db.execSQL(BeamItUpContract.AccountEthTable.SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(BeamItUpContract.AccountTable.SQL_DELETE_TABLE);
        db.execSQL(BeamItUpContract.EthTable.SQL_DELETE_TABLE);
        db.execSQL(BeamItUpContract.AccountEthTable.SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
