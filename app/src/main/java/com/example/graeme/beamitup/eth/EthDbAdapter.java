package com.example.graeme.beamitup.eth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.graeme.beamitup.DbAdapter;

import java.util.ArrayList;
import java.util.List;

public class EthDbAdapter extends DbAdapter {
    private static final String TAG = "EthDbAdapter";

    public EthDbAdapter(Context context) {
        super(context);
    }

    public long createEth(Eth eth) {
        long ethID = createEthInDB(eth);
        Log.i(TAG, "Returning eth id: " + ethID);
        return ethID;
    }

    private long createEthInDB(Eth eth){
        Log.i(TAG, "Inserting eth into db of address: " + eth.getAddress());
        ContentValues contentValues = new ContentValues();
        contentValues.put(
                EthTable.ETH_NICKNAME,
                eth.getNickname()
        );
        contentValues.put(
                EthTable.ETH_ADDRESS,
                eth.getAddress()
        );
        contentValues.put(
                EthTable.ETH_WALLET_NAME,
                eth.getWalletName()
        );
        contentValues.put(
                EthTable.ETH_ENC_LONG_PASSWORD,
                eth.getEncryptedLongPassword()
        );
        contentValues.put(
                EthTable.ETH_IV,
                eth.getIV()
        );
        long ethID = this.db.insert(
                EthTable.ETH_TABLE_NAME,
                null,
                contentValues
        );
        Log.i(TAG, "Inserted Eth has id: " + ethID);
        return ethID;
    }

    public Eth retrieveEthByEthID(long id){
        Cursor res = this.db.query(EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable.ETH_NICKNAME,
                        EthTable.ETH_ADDRESS,
                        EthTable.ETH_WALLET_NAME,
                        EthTable.ETH_ENC_LONG_PASSWORD,
                        EthTable.ETH_IV,
                        EthTable._ID
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

    public Eth retrieveEthByEthAddress(String address){
        Log.i(TAG, "Retrieving eth with address " + address);
        Cursor res = retrieveEthCursorByAddress(address);
        Log.i(TAG, "Retrieved cursor of length " + res.getCount());
        res.moveToFirst();
        Eth eth = retrieveEthFromCursor(res);
        Log.i(TAG, "Retrieved eth with address " + eth.getAddress()
        + " and nickname " + eth.getNickname());
        return eth;
    }

    public List<Eth> retrieveEths(){
        List<Eth> eths = new ArrayList<>();
        Cursor res = retrieveEthCursor();
        Log.i(TAG, "Eth cursor count: " + res.getCount());
        while (res.moveToNext()) {
            Log.i(TAG, "Eth id: " + res.getLong(res.getColumnIndex(EthTable._ID)));
            eths.add(retrieveEthFromCursor(res));
        }
        res.close();
        Log.i(TAG, "Returning number of eths by account id: " + eths.size());
        return eths;
    }

    private Eth retrieveEthFromCursor(Cursor res){
        return new Eth(
                res.getString(res.getColumnIndex(EthTable.ETH_NICKNAME)),
                res.getString(res.getColumnIndex(EthTable.ETH_ADDRESS)),
                res.getString(res.getColumnIndex(EthTable.ETH_WALLET_NAME)),
                res.getBlob(res.getColumnIndex(EthTable.ETH_ENC_LONG_PASSWORD)),
                res.getBlob(res.getColumnIndex(EthTable.ETH_IV)),
                res.getLong(res.getColumnIndex(EthTable._ID))
        );
    }

    private Cursor retrieveEthCursor(){
        Log.i(TAG, "Retrieving eth cursor ");
        return this.db.query(
                EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable._ID,
                        EthTable.ETH_NICKNAME,
                        EthTable.ETH_ADDRESS,
                        EthTable.ETH_IV,
                        EthTable.ETH_ENC_LONG_PASSWORD,
                        EthTable.ETH_WALLET_NAME
                },
                null,
                null,
                null,
                null,
                null
        );
    }

    private Cursor retrieveEthCursorByAddress(String address){
        return this.db.query(
                EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable._ID,
                        EthTable.ETH_NICKNAME,
                        EthTable.ETH_ADDRESS,
                        EthTable.ETH_IV,
                        EthTable.ETH_ENC_LONG_PASSWORD,
                        EthTable.ETH_WALLET_NAME
                },
                EthTable.ETH_ADDRESS + "=?",
                new String[]{address},
                null,
                null,
                null
        );
    }

    private boolean updateEthInDB(Eth eth, byte[] encPrivateKey, byte[] iv){
        ContentValues contentValues = new ContentValues();
        contentValues.put(
                EthTable.ETH_ADDRESS,
                eth.getAddress()
        );
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
