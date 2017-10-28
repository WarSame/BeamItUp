package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    void createAccount(Account account) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL,
                account.getEmail());
        contentValues.put(BeamItUpContract.Account.ACCOUNT_COLUMN_PASSWORD_HASH,
                account.getPasswordHash());
        contentValues.put(BeamItUpContract.Account.ACCOUNT_COLUMN_SALT,
                account.getSalt());
        if (db.insert(BeamItUpContract.Account.ACCOUNT_TABLE_NAME, null, contentValues) == -1){
            throw new SQLException();
        }
        db.close();
    }

    boolean isAuthentic(Account account){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(BeamItUpContract.Account.ACCOUNT_TABLE_NAME,
                new String[]{
                        BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL,
                        BeamItUpContract.Account.ACCOUNT_COLUMN_PASSWORD_HASH
                },
                BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{account.getEmail()},
                null, null, null);
        res.moveToFirst();
        byte[] storedHash = (res.getBlob(res.getColumnIndex(BeamItUpContract.Account.ACCOUNT_COLUMN_PASSWORD_HASH)));
        res.close();
        return Arrays.equals(storedHash, account.getPasswordHash());
    }

    byte[] retrieveSalt(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(BeamItUpContract.Account.ACCOUNT_TABLE_NAME,
                new String[]{
                        BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL,
                        BeamItUpContract.Account.ACCOUNT_COLUMN_SALT
                },
                BeamItUpContract.Account.ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{email},
                null, null, null);
        res.moveToFirst();
        byte[] storedSalt = (res.getBlob(res.getColumnIndex(BeamItUpContract.Account.ACCOUNT_COLUMN_SALT)));
        res.close();

        return storedSalt;
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
}
