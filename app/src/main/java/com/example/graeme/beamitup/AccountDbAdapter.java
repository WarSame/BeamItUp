package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.graeme.beamitup.BeamItUpContract.AccountTable;

import java.util.Arrays;

class AccountDbAdapter {
    private static final String TAG = "AccountDbAdapter";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, AccountTable.ACCOUNT_TABLE_NAME, null, BeamItUpContract.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    AccountDbAdapter(Context context){
        this.context = context;
    }

    AccountDbAdapter open() {
        this.dbHelper = new DatabaseHelper(this.context);
        this.db = this.dbHelper.getWritableDatabase();
        return this;
    }

    void close(){
        this.dbHelper.close();
    }

    long createAccount(Account account) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.ACCOUNT_COLUMN_EMAIL,
                account.getEmail());
        contentValues.put(AccountTable.ACCOUNT_COLUMN_PASSWORD_HASH,
                account.getPasswordHash());
        contentValues.put(AccountTable.ACCOUNT_COLUMN_SALT,
                account.getSalt());
        return this.db.insert(AccountTable.ACCOUNT_TABLE_NAME, null, contentValues);
    }

    Cursor retrieveAccount(long id){
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_COLUMN_EMAIL,
                        AccountTable.ACCOUNT_COLUMN_PASSWORD_HASH,
                        AccountTable.ACCOUNT_COLUMN_SALT
                },
                 AccountTable._ID + "=?", new String[]{Long.toString(id)},
                null, null, null);
        if (res != null){
            res.moveToFirst();
        }
        return res;
    }

    boolean updateAccount(int id, Account account){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.ACCOUNT_COLUMN_EMAIL, account.getEmail());
        contentValues.put(AccountTable.ACCOUNT_COLUMN_PASSWORD_HASH, account.getPasswordHash());
        contentValues.put(AccountTable.ACCOUNT_COLUMN_SALT, account.getSalt());
        return this.db.update(AccountTable.ACCOUNT_TABLE_NAME, contentValues, AccountTable._ID + "=" + id, null) > 0;
    }

    boolean deleteAccount(long id){
        return this.db.delete(AccountTable.ACCOUNT_TABLE_NAME, AccountTable._ID + "=" + id, null) > 0;
    }

    boolean isAuthentic(Account account){
        Cursor res = db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_COLUMN_EMAIL,
                        AccountTable.ACCOUNT_COLUMN_PASSWORD_HASH
                },
                AccountTable.ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{account.getEmail()},
                null, null, null);
        res.moveToFirst();
        byte[] storedHash = (res.getBlob(res.getColumnIndex(AccountTable.ACCOUNT_COLUMN_PASSWORD_HASH)));
        res.close();
        return Arrays.equals(storedHash, account.getPasswordHash());
    }

    byte[] retrieveSalt(String email){
        Cursor res = db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_COLUMN_EMAIL,
                        AccountTable.ACCOUNT_COLUMN_SALT
                },
                AccountTable.ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{email},
                null, null, null);
        res.moveToFirst();
        byte[] storedSalt = (res.getBlob(res.getColumnIndex(AccountTable.ACCOUNT_COLUMN_SALT)));
        res.close();

        return storedSalt;
    }

    boolean isEmailInUse(String email){
        Cursor res = db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_COLUMN_EMAIL
                },
                AccountTable.ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{email},
                null, null, null);
        if (res.getCount() > 0){
            res.close();
            return true;
        }
        res.close();
        return false;
    }

    int getAccountId(Account account){
        Cursor res = db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_COLUMN_EMAIL
                },
                AccountTable.ACCOUNT_COLUMN_EMAIL + " like ?", new String[]{account.getEmail()},
                null, null, null);
        res.moveToFirst();
        int id = res.getInt(res.getColumnIndex("_ID"));
        res.close();
        return id;
    }
}
