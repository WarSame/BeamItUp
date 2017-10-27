package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class BeamItUpDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "BeamItUpDbHelper";

    BeamItUpDbHelper(Context context){
        super(context, BeamItUpContract.DATABASE_NAME, null, BeamItUpContract.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(BeamItUpContract.Account.SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(BeamItUpContract.Account.SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    void insertAccount(String email, String password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL, email);
        contentValues.put(BeamItUpContract.Account.ACCOUNT_COLUMN_PASSWORD_HASH, convertPasswordToHash(password));
        db.insert(BeamItUpContract.Account.ACCOUNT_TABLE_NAME, null, contentValues);
        db.close();
    }

    boolean isAuthentic(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(BeamItUpContract.Account.ACCOUNT_TABLE_NAME,
                new String[]{
                        BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL,
                        BeamItUpContract.Account.ACCOUNT_COLUMN_PASSWORD_HASH
                },
                BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{email},
                null, null, null);
        res.moveToFirst();
        byte[] storedHash = (res.getBlob(res.getColumnIndex(BeamItUpContract.Account.ACCOUNT_COLUMN_PASSWORD_HASH)));
        res.close();
        return Arrays.equals(storedHash, convertPasswordToHash(password));
    }

    boolean isEmailInUse(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(BeamItUpContract.Account.ACCOUNT_TABLE_NAME,
                new String[]{
                        BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL,
                        BeamItUpContract.Account.ACCOUNT_COLUMN_PASSWORD_HASH},
                BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{email},
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
