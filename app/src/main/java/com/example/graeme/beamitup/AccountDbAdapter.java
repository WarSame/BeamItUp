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

    long createAccount(String email, byte[] passwordHash, byte[] salt) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.ACCOUNT_EMAIL,
                email);
        contentValues.put(AccountTable.ACCOUNT_PASSWORD_HASH,
                passwordHash);
        contentValues.put(AccountTable.ACCOUNT_SALT,
                salt);
        return this.db.insert(AccountTable.ACCOUNT_TABLE_NAME, null, contentValues);
    }

    private Cursor getAccountCursor(String email){
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_EMAIL,
                        AccountTable.ACCOUNT_PASSWORD_HASH,
                        AccountTable.ACCOUNT_SALT,
                        AccountTable._ID
                },
                AccountTable.ACCOUNT_EMAIL + "=?", new String[]{email},
                null,
                null,
                null);
        if (res != null){
            res.moveToFirst();
        }
        return res;
    }

    Account retrieveAccount(String email){
        Cursor res = getAccountCursor(email);
        long id = res.getLong(
                res.getColumnIndex(AccountTable._ID)
        );
        return new Account(email, id);
    }

    boolean updateAccount(String email, Account account){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.ACCOUNT_EMAIL, account.getEmail());
        contentValues.put(AccountTable._ID, account.getId());
        return this.db.update(
                AccountTable.ACCOUNT_TABLE_NAME,
                contentValues,
                AccountTable.ACCOUNT_EMAIL + "=" + email,
                null
        ) > 0;
    }

    boolean deleteAccount(String email){
        return this.db.delete(
                AccountTable.ACCOUNT_TABLE_NAME,
                AccountTable.ACCOUNT_EMAIL + "=" + email,
                null
        ) > 0;
    }

    boolean isAuthentic(String email, byte[] passwordHash){
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_EMAIL,
                        AccountTable.ACCOUNT_PASSWORD_HASH
                },
                AccountTable.ACCOUNT_EMAIL + " like ?", new String[]{email},
                null, null, null);
        res.moveToFirst();
        byte[] storedHash = (res.getBlob(res.getColumnIndex(AccountTable.ACCOUNT_PASSWORD_HASH)));
        res.close();
        return Arrays.equals(storedHash, passwordHash);
    }

    byte[] retrieveSalt(String email){
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_EMAIL,
                        AccountTable.ACCOUNT_SALT
                },
                AccountTable.ACCOUNT_EMAIL + " like ?", new String[]{email},
                null, null, null);
        res.moveToFirst();
        byte[] storedSalt = (res.getBlob(res.getColumnIndex(AccountTable.ACCOUNT_SALT)));
        res.close();

        return storedSalt;
    }

    boolean isEmailInUse(String email){
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_EMAIL
                },
                AccountTable.ACCOUNT_EMAIL + " like ?", new String[]{email},
                null, null, null);
        if (res.getCount() > 0){
            res.close();
            return true;
        }
        res.close();
        return false;
    }
}
