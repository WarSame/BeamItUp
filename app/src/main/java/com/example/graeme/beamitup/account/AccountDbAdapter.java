package com.example.graeme.beamitup.account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.graeme.beamitup.DbAdapter;
import com.example.graeme.beamitup.Encryption;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDbAdapter;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class AccountDbAdapter extends DbAdapter {
    private static final String TAG = "AccountDbAdapter";
    private Context context;

    public AccountDbAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public long createAccount(String email, char[] password) throws SQLException, NoSuchAlgorithmException {
        byte[] salt = Encryption.generateSalt();
        byte[] passwordHash = Encryption.hashPassword(password, salt);
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.ACCOUNT_EMAIL,
                email);
        contentValues.put(AccountTable.ACCOUNT_PASSWORD_HASH,
                passwordHash);
        contentValues.put(AccountTable.ACCOUNT_SALT,
                salt);
        Log.i(TAG,
                "Creating account with email: " + email
        );
        long accountID = this.db.insert(AccountTable.ACCOUNT_TABLE_NAME, null, contentValues);
        Log.i(TAG, "accountID: " + accountID);
        return accountID;
    }

    private Cursor getAccountCursor(String email){
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_EMAIL,
                        AccountTable.ACCOUNT_PASSWORD_HASH,
                        AccountTable.ACCOUNT_SALT,
                        AccountTable._ID
                },
                AccountTable.ACCOUNT_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null
        );
        res.moveToFirst();
        Log.i(TAG, "Returning account cursor of count: " + res.getCount());
        return res;
    }

    public Account retrieveAccount(String email){
        Log.i(TAG, "Retrieving account associated with " + email);
        Cursor res = getAccountCursor(email);
        if (res.getCount() == 0){
            throw new NoSuchElementException();
        }
        long id = res.getLong(
                res.getColumnIndex(AccountTable._ID)
        );
        Log.i(TAG,
            "Returning account email: " + email
            + " id: " + id
        );
        List<Eth> eths = getEthsByAccountID(id);
        Log.i(TAG, "Number of eths: " + eths.size());
        return new Account(email, id, eths);
    }

    private List<Eth> getEthsByAccountID(long id){
        EthDbAdapter ethDb = new EthDbAdapter(context);
        List<Eth> eths = ethDb.retrieveEthsByAccountId(id);
        ethDb.close();
        return eths;
    }

    public void updateAccount(Account account) throws SQLiteConstraintException {
        if (!updateAccountInAccountTable(account)){
            throw new NoSuchElementException();
        }
    }

    private boolean updateAccountInAccountTable(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.ACCOUNT_EMAIL, account.getEmail());
        return this.db.update(
                AccountTable.ACCOUNT_TABLE_NAME,
                contentValues,
                AccountTable._ID + "=?",
                new String[]{String.valueOf(account.getId())}
        ) > 0;
    }

    public boolean deleteAccount(Account account){
        Log.i(TAG, "Deleting account: " + account.getEmail());
        return this.db.delete(
                AccountTable.ACCOUNT_TABLE_NAME,
                AccountTable.ACCOUNT_EMAIL + "=?",
                new String[]{account.getEmail()}
        ) > 0;
    }

    public boolean isAuthentic(String email, char[] password) throws NoSuchAlgorithmException {
        Log.i(TAG, "Checking if email " + email + " is authentic.");
        Cursor res = this.db.query(
                AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_EMAIL,
                        AccountTable.ACCOUNT_PASSWORD_HASH,
                        AccountTable.ACCOUNT_SALT
                },
                AccountTable.ACCOUNT_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null
        );
        if (res.getCount() == 0){
            res.close();
            return false;
        }
        res.moveToFirst();
        byte[] storedHash = (res.getBlob(res.getColumnIndex(AccountTable.ACCOUNT_PASSWORD_HASH)));
        byte[] storedSalt = (res.getBlob(res.getColumnIndex(AccountTable.ACCOUNT_SALT)));
        res.close();
        byte[] passwordHash = Encryption.hashPassword(password, storedSalt);
        boolean isAuthentic = Arrays.equals(storedHash, passwordHash);
        Log.i(TAG, "Account is authentic: " + isAuthentic);
        return isAuthentic;
    }

    public boolean isEmailInUse(String email){
        Log.i(TAG, "Determining whether the email " + email + " is in use");
        Cursor res = this.db.query(AccountTable.ACCOUNT_TABLE_NAME,
                new String[]{
                        AccountTable.ACCOUNT_EMAIL
                },
                AccountTable.ACCOUNT_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null
        );
        if (res.getCount() > 0){
            res.close();
            Log.i(TAG, "Email is in use");
            return true;
        }
        res.close();
        Log.i(TAG, "Email is not in use");
        return false;
    }

}
