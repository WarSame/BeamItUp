package com.example.graeme.beamitup.eth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.graeme.beamitup.DbAdapter;
import com.example.graeme.beamitup.Encryption;

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
                EthTable.ETH_ACCOUNT_ID,
                eth.getAccountId()
        );
        contentValues.put(
                EthTable.ETH_ADDRESS,
                eth.getAddress()
        );
        contentValues.put(
                EthTable.ETH_NICKNAME,
                eth.getNickname()
        );
        long ethID = this.db.insert(
                EthTable.ETH_TABLE_NAME,
                null,
                contentValues
        );
        Log.i(TAG, "Inserted Eth has id: " + ethID);
        return ethID;
    }

    Eth retrieveEthByEthID(long id){
        Cursor res = this.db.query(EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable.ETH_ADDRESS
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

    public List<Eth> retrieveEthsByAccountId(long accountId){
        List<Eth> eths = new ArrayList<>();
        Cursor res = retrieveEthCursorByAccountID(accountId);
        Log.i(TAG, "Eth cursor count: " + res.getCount());
        while (res.moveToNext()) {
            Log.i(TAG, "Eth id: " + res.getLong(res.getColumnIndex(EthTable._ID)));
            eths.add(retrieveEthFromCursor(res));
        }
        res.close();
        Log.i(TAG, "Returning number of eths by account id: " + eths.size());
        return eths;
    }

    private Cursor retrieveEthCursorByAccountID(long accountId){
        Log.i(TAG, "Retrieving eth cursor for eths associated with account id " + accountId);
        return this.db.query(
                EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable._ID,
                        EthTable.ETH_NICKNAME,
                        EthTable.ETH_ACCOUNT_ID,
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
                res.getString(res.getColumnIndex(EthTable.ETH_NICKNAME)),
                res.getString(res.getColumnIndex(EthTable.ETH_ADDRESS)),
                res.getLong(res.getColumnIndex(EthTable._ID)),
                res.getInt(res.getColumnIndex(EthTable.ETH_ACCOUNT_ID))
        );
    }

    boolean updateEth(Eth eth, String privateKey){
        Encryption.Encryptor encryptor = encryptPassword(eth.getAddress(), privateKey);
        return updateEthInDB(eth, encryptor.getEncryption(), encryptor.getIv());
    }

    private boolean updateEthInDB(Eth eth, byte[] encPrivateKey, byte[] iv){
        ContentValues contentValues = new ContentValues();
        contentValues.put(
                EthTable.ETH_ADDRESS,
                eth.getAddress()
        );
        contentValues.put(
                EthTable.ETH_ACCOUNT_ID,
                eth.getAccountId()
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

    private Encryption.Encryptor encryptPassword(String walletName, String password){
        Encryption.Encryptor encryptor = new Encryption.Encryptor();
        try {
            encryptor.encryptPassword(walletName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptor;
    }
}
