package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class BeamItUpDbHelper extends SQLiteOpenHelper implements BaseColumns {
    private static final String TAG = "BeamItUpDbHelper";
    //If changing schema, must update db version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BeamItUp.db";

    private static final String ACCOUNT_TABLE_NAME = "account";
    private static final String ACCOUNT_COLUMN_EMAIL = "email";
    private static final String ACCOUNT_COLUMN_PASSWORD_HASH = "password";


    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ACCOUNT_TABLE_NAME +
            " (" + _ID + " INTEGER PRIMARY KEY," + ACCOUNT_COLUMN_EMAIL + " TEXT," +
            ACCOUNT_COLUMN_PASSWORD_HASH + " BLOB)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME;

    BeamItUpDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    void insertAccount(Account account){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_COLUMN_EMAIL, account.getEmail());
        contentValues.put(ACCOUNT_COLUMN_PASSWORD_HASH, convertPasswordToHash(account.getPassword()));
        db.insert(ACCOUNT_TABLE_NAME, null, contentValues);
        db.close();
    }

    boolean isAuthentic(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(ACCOUNT_TABLE_NAME, new String[]{ACCOUNT_COLUMN_EMAIL, ACCOUNT_COLUMN_PASSWORD_HASH},
                ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{email},
                null, null, null);
        res.moveToFirst();
        byte[] storedHash = (res.getBlob(res.getColumnIndex(ACCOUNT_COLUMN_PASSWORD_HASH)));
        res.close();
        return Arrays.equals(storedHash, convertPasswordToHash(password));
    }

    boolean isEmailInUse(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(ACCOUNT_TABLE_NAME, new String[]{ACCOUNT_COLUMN_EMAIL, ACCOUNT_COLUMN_PASSWORD_HASH},
                ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{email},
                null, null, null);
        if (res.getCount() > 0){
            res.close();
            return true;
        }
        res.close();
        return false;
    }

    private byte[] convertPasswordToHash(String password){
        MessageDigest md;
        byte[] passwordHash = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        if (md != null) {
            passwordHash = md.digest(password.getBytes());
        }
        if (passwordHash == null){
            return null;
        }
        return passwordHash;
    }
}
