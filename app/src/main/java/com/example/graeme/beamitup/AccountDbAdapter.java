package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.Arrays;

class AccountDbAdapter extends DbAdapter {
    private static final String TAG = "AccountDbAdapter";

    AccountDbAdapter(Context context) {
        super(context);
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

    boolean updateAccount(long id, Account account){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.ACCOUNT_COLUMN_EMAIL, account.getEmail());
        contentValues.put(AccountTable.ACCOUNT_COLUMN_PASSWORD_HASH, account.getPasswordHash());
        contentValues.put(AccountTable.ACCOUNT_COLUMN_SALT, account.getSalt());
        return this.db.update(AccountTable.ACCOUNT_TABLE_NAME, contentValues, AccountTable._ID + "=" + id, null) > 0;
    }

    boolean deleteAccount(long id){
        return this.db.delete(EthTable.ETH_TABLE_NAME, EthTable._ID + "=" + id, null) > 0;
    }

    boolean isAuthentic(Account account){
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
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
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
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
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
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
}
