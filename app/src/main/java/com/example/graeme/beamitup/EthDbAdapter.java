package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class EthDbAdapter extends DbAdapter{
    private static final String TAG = "EthDbAdapter";

    EthDbAdapter(Context context) {
        super(context);
    }

    long createEth(Eth eth) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EthTable.ETH_ADDRESS,
                eth.getAddress());
        contentValues.put(EthTable.ETH_ENC_PRIVATE_KEY, eth.getEncPrivateKey());
        return this.db.insert(EthTable.ETH_TABLE_NAME, null, contentValues);
    }

    Cursor retrieveEth(long id){
        Cursor res = this.db.query(EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable.ETH_ADDRESS,
                        EthTable.ETH_ENC_PRIVATE_KEY
                },
                EthTable._ID + "=?", new String[]{Long.toString(id)},
                null, null, null);
        if (res != null){
            res.moveToFirst();
        }
        return res;
    }

    boolean updateEth(long id, Eth eth){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EthTable.ETH_ADDRESS, eth.getAddress());
        contentValues.put(EthTable.ETH_ENC_PRIVATE_KEY, eth.getEncPrivateKey());
        return this.db.update(EthTable.ETH_TABLE_NAME, contentValues, EthTable._ID + "=" + id, null) > 0;
    }

    boolean deleteAccount(long id){
        return this.db.delete(AccountTable.ACCOUNT_TABLE_NAME, AccountTable._ID + "=" + id, null) > 0;
    }

    void updateEth(int id, Eth eth){

    }

    void deleteEth(int id){

    }
}
