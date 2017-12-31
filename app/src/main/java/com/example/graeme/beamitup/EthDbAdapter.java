package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

class EthDbAdapter extends DbAdapter{
    private static final String TAG = "EthDbAdapter";

    EthDbAdapter(Context context) {
        super(context);
    }

    long createEth(Eth eth) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(
                EthTable.ETH_ACCOUNT_ID,
                eth.getAccountId()
        );
        contentValues.put(
                EthTable.ETH_ADDRESS,
                eth.getAddress()
        );
        contentValues.put(
                EthTable.ETH_ENC_PRIVATE_KEY,
                eth.getEncPrivateKey()
        );
        contentValues.put(
                EthTable.ETH_IV,
                eth.getIv()
        );
        return this.db.insert(
                EthTable.ETH_TABLE_NAME,
                null,
                contentValues
        );
    }

    Eth retrieveEthByEthID(long id){
        Cursor res = this.db.query(EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable.ETH_ADDRESS,
                        EthTable.ETH_ENC_PRIVATE_KEY
                },
                EthTable._ID + "=?",
                new String[]{Long.toString(id)},
                null,
                null,
                null
        );
        res.moveToFirst();
        Eth eth = retrieveEthFromCursor(res);
        res.close();
        return eth;
    }

    ArrayList<Eth> retrieveEthsByAccountId(long accountId){
        ArrayList<Eth> eths = new ArrayList<>();
        Cursor res = retrieveEthCursorByAccountID(accountId);
        Log.i(TAG, "Eth cursor count: " + res.getCount());
        while (res.moveToNext()) {
            eths.add(retrieveEthFromCursor(res));
        }
        res.close();
        Log.i(TAG, "Returning number of eths by account id: " + eths.size());
        return eths;
    }

    private Cursor retrieveEthCursorByAccountID(long accountId){
        return this.db.query(
                EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable._ID,
                        EthTable.ETH_ACCOUNT_ID,
                        EthTable.ETH_ADDRESS,
                        EthTable.ETH_ENC_PRIVATE_KEY,
                        EthTable.ETH_IV
                },
                EthTable.ETH_ACCOUNT_ID + "=?",
                new String[]{Long.toString(accountId)},
                null,
                null,
                null
        );
    }

    private Eth retrieveEthFromCursor(Cursor res){
        return new Eth(
                res.getString(res.getColumnIndex(EthTable.ETH_ADDRESS)),
                res.getBlob(res.getColumnIndex(EthTable.ETH_ENC_PRIVATE_KEY)),
                res.getBlob(res.getColumnIndex(EthTable.ETH_IV)),
                res.getColumnIndex(EthTable._ID),
                res.getInt(res.getColumnIndex(EthTable.ETH_ACCOUNT_ID))
        );
    }

    int updateEths(ArrayList<Eth> eths) throws NoSuchElementException {
        int numEths = 0;
        for (Eth eth : eths){
            Log.i(TAG, "Creating or updating eth with address " + eth.getAddress());
            createOrUpdateEth(eth);
            numEths++;
        }
        return numEths;
    }

    private void createOrUpdateEth(Eth eth) throws NoSuchElementException {
        if (eth.getId() == -1){
            Log.i(TAG, "Creating eth");
            createEth(eth);
        }
        else {
            Log.i(TAG, "Updating eth");
            if (!updateEth(eth)) {
                Log.e(TAG, "Failed to update eth");
                throw new NoSuchElementException();
            }
        }
    }

    private boolean updateEth(Eth eth){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EthTable.ETH_ADDRESS, eth.getAddress());
        contentValues.put(EthTable.ETH_ENC_PRIVATE_KEY, eth.getEncPrivateKey());
        long numRowsUpdated = this.db.update(
                EthTable.ETH_TABLE_NAME,
                contentValues,
                EthTable._ID + "=?",
                new String[]{String.valueOf(eth.getId())}
        );
        Log.i(TAG, "Updated " + numRowsUpdated + " in updateEth");
        return numRowsUpdated > 0;
    }

    void deleteEth(long id){

    }
}
