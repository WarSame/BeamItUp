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
        contentValues.put(AccountTable.ACCOUNT_EMAIL,
                account.getEmail());
        contentValues.put(AccountTable.ACCOUNT_PASSWORD_HASH,
                account.getPasswordHash());
        contentValues.put(AccountTable.ACCOUNT_SALT,
                account.getSalt());
        contentValues.put(AccountTable.ACCOUNT_ID,
                account.getId());
        return this.db.insert(AccountTable.ACCOUNT_TABLE_NAME, null, contentValues);
    }

    private Cursor getAccountCursor(String email){
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_EMAIL,
                        AccountTable.ACCOUNT_PASSWORD_HASH,
                        AccountTable.ACCOUNT_SALT,
                        AccountTable.ACCOUNT_ID
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
        byte[] passwordHash = res.getBlob(
                res.getColumnIndex(AccountTable.ACCOUNT_PASSWORD_HASH)
        );
        byte[] salt = res.getBlob(
                res.getColumnIndex(AccountTable.ACCOUNT_SALT)
        );
        long id = res.getLong(
                res.getColumnIndex(AccountTable.ACCOUNT_ID)
        );
        return new Account(email, passwordHash, salt, id);
    }

    boolean updateAccount(String email, Account account){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.ACCOUNT_EMAIL, account.getEmail());
        contentValues.put(AccountTable.ACCOUNT_PASSWORD_HASH, account.getPasswordHash());
        contentValues.put(AccountTable.ACCOUNT_SALT, account.getSalt());
        contentValues.put(AccountTable.ACCOUNT_ID, account.getId());
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

    boolean isAuthentic(Account account){
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_EMAIL,
                        AccountTable.ACCOUNT_PASSWORD_HASH
                },
                AccountTable.ACCOUNT_EMAIL + " like ?", new String[]{account.getEmail()},
                null, null, null);
        res.moveToFirst();
        byte[] storedHash = (res.getBlob(res.getColumnIndex(AccountTable.ACCOUNT_PASSWORD_HASH)));
        res.close();
        return Arrays.equals(storedHash, account.getPasswordHash());
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
