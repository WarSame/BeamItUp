package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

class AccountEthDbAdapter extends DbAdapter {
    private static final String TAG = "AccountEthDbAdapter";

    AccountEthDbAdapter(Context context){
        super(context);
    }

    long createAccountEth(long accountId, long ethId) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountEthTable.ACCOUNT_ETH_COLUMN_ACCOUNT_ID, accountId);
        contentValues.put(AccountEthTable.ACCOUNT_ETH_COLUMN_ETH_ID, ethId);
        return this.db.insert(AccountEthTable.ACCOUNT_ETH_TABLE_NAME, null, contentValues);
    }

    Cursor retrieveAccountEth(long id){
        Cursor res = this.db.query(AccountEthTable.ACCOUNT_ETH_TABLE_NAME,
                new String[]{
                        AccountEthTable.ACCOUNT_ETH_COLUMN_ACCOUNT_ID,
                        AccountEthTable.ACCOUNT_ETH_COLUMN_ETH_ID
                },
                AccountEthTable._ID + "=?", new String[]{Long.toString(id)},
                null, null, null);
        if (res != null){
            res.moveToFirst();
        }
        return res;
    }

    boolean updateAccountEth(long id, long accountId, long ethId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountEthTable.ACCOUNT_ETH_COLUMN_ACCOUNT_ID, accountId);
        contentValues.put(AccountEthTable.ACCOUNT_ETH_COLUMN_ETH_ID, ethId);
        return this.db.update(AccountEthTable.ACCOUNT_ETH_TABLE_NAME, contentValues, AccountEthTable._ID + "=" + id, null) > 0;
    }

    boolean deleteAccountEth(long id){
        return this.db.delete(AccountEthTable.ACCOUNT_ETH_TABLE_NAME, AccountEthTable._ID + "=" + id, null) > 0;
    }

}
