package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;

class AccountEthDbAdapter extends DbAdapter {
    private static final String TAG = "AccountEthDbAdapter";

    AccountEthDbAdapter(Context context){
        super(context);
    }

    long createAccountEth(String accountEmail, long ethId) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountEthTable.ACCOUNT_ETH_COLUMN_ACCOUNT_EMAIL, accountEmail);
        contentValues.put(AccountEthTable.ACCOUNT_ETH_COLUMN_ETH_ID, ethId);
        return this.db.insert(
                AccountEthTable.ACCOUNT_ETH_TABLE_NAME,
                null,
                contentValues
        );
    }

    private Cursor getAccountEthCursor(String email){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(
                AccountTable.ACCOUNT_TABLE_NAME
                + " INNER JOIN " + AccountEthTable.ACCOUNT_ETH_TABLE_NAME
                + " ON " + AccountTable.ACCOUNT_COLUMN_EMAIL + " = "
                + AccountEthTable.ACCOUNT_ETH_COLUMN_ACCOUNT_EMAIL
                + EthTable.ETH_TABLE_NAME
                + " INNER JOIN " + AccountEthTable.ACCOUNT_ETH_TABLE_NAME
                + " ON " + EthTable._ID + " = "
                + AccountEthTable.ACCOUNT_ETH_COLUMN_ETH_ID
        );
        return qb.query(
                this.db,
                null,
                AccountTable.ACCOUNT_COLUMN_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null
        );
    }

    ArrayList<Eth> retrieveAccountEths(String email){
        Cursor res = getAccountEthCursor(email);
        ArrayList<Eth> eths = new ArrayList<>();
        for (int i = 0; i < res.getCount() ; i++){
            String address = res.getString(i);
            byte[] encPrivateKey = res.getBlob(i);
            eths.add(new Eth(address, encPrivateKey));
        }
        return eths;
    }

    boolean updateAccountEth(String accountEmail, long ethId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountEthTable.ACCOUNT_ETH_COLUMN_ACCOUNT_EMAIL, accountEmail);
        contentValues.put(AccountEthTable.ACCOUNT_ETH_COLUMN_ETH_ID, ethId);
        return this.db.update(
                AccountEthTable.ACCOUNT_ETH_TABLE_NAME,
                contentValues,
                AccountEthTable.ACCOUNT_ETH_COLUMN_ACCOUNT_EMAIL + "=?" +
                AccountEthTable.ACCOUNT_ETH_COLUMN_ETH_ID + "=?",
                new String[]{accountEmail, Long.toString(ethId)}
        ) > 0;
    }

    boolean deleteAccountEth(String accountEmail, long ethId){
        return this.db.delete(
                AccountEthTable.ACCOUNT_ETH_TABLE_NAME,
                AccountEthTable.ACCOUNT_ETH_COLUMN_ACCOUNT_EMAIL +"=?" +
                AccountEthTable.ACCOUNT_ETH_COLUMN_ETH_ID + "=?",
                new String[]{accountEmail, Long.toString(ethId)}
        ) > 0;
    }

}
