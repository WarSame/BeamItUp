package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.NoSuchElementException;

class EthDbAdapter extends DbAdapter{
    private static final String TAG = "EthDbAdapter";

    EthDbAdapter(Context context) {
        super(context);
    }

    long createEth(Eth eth, String privateKey) {
        Encryption.Encryptor encryptor = encryptPrivateKey(eth.getAddress(), privateKey);

        long ethID = createEthInDB(eth, encryptor.getEncryption(), encryptor.getIv());
        Log.i(TAG, "Returning eth id: " + ethID);
        return ethID;
    }

    private long createEthInDB(Eth eth, byte[] encPrivateKey, byte[] iv){
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
                EthTable.ETH_ENC_PRIVATE_KEY,
                encPrivateKey
        );
        contentValues.put(
                EthTable.ETH_IV,
                iv
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
                res.getLong(res.getColumnIndex(EthTable._ID)),
                res.getInt(res.getColumnIndex(EthTable.ETH_ACCOUNT_ID))
        );
    }

    String retrieveSenderPrivateKey(long ethID, String senderAddress) throws Exception {
        Encryption.Encryptor encryptor = retrieveEncryptor(ethID);
        byte[] encPrivateKey = encryptor.getEncryption();
        byte[] iv = encryptor.getIv();
        Encryption.Decryptor decryptor = new Encryption.Decryptor();
        return decryptor.decryptPrivateKey(senderAddress, encPrivateKey, iv);
    }

    private Encryption.Encryptor retrieveEncryptor(long ethID){
        Cursor res = retrieveEncryptorCursor(ethID);
        res.moveToFirst();
        return putCursorIntoEncryptor(res);
    }

    private Cursor retrieveEncryptorCursor(long ethID){
        Cursor res = this.db.query(
                EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable.ETH_ENC_PRIVATE_KEY,
                        EthTable.ETH_IV
                },
                EthTable._ID + "=?",
                new String[]{Long.toString(ethID)},
                null,
                null,
                null
        );
        if (res.getCount() == 0){
            throw new NoSuchElementException();
        }
        return res;
    }

    private Encryption.Encryptor putCursorIntoEncryptor(Cursor res){
        byte[] encPrivateKey = res.getBlob(res.getColumnIndex(EthTable.ETH_ENC_PRIVATE_KEY));
        byte[] iv = res.getBlob(res.getColumnIndex(EthTable.ETH_IV));
        Encryption.Encryptor encryptor = new Encryption.Encryptor();
        encryptor.setEncryption(encPrivateKey);
        encryptor.setIv(iv);
        return encryptor;
    }

    boolean updateEth(Eth eth, String privateKey){
        Encryption.Encryptor encryptor = encryptPrivateKey(eth.getAddress(), privateKey);
        return updateEthInDB(eth, encryptor.getEncryption(), encryptor.getIv());
    }

    private boolean updateEthInDB(Eth eth, byte[] encPrivateKey, byte[] iv){
        ContentValues contentValues = new ContentValues();
        contentValues.put(
                EthTable.ETH_ADDRESS,
                eth.getAddress()
        );
        contentValues.put(
                EthTable.ETH_ENC_PRIVATE_KEY,
                encPrivateKey
        );
        contentValues.put(
                EthTable.ETH_IV,
                iv
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

    private Encryption.Encryptor encryptPrivateKey(String address, String privateKey){
        Encryption.Encryptor encryptor = new Encryption.Encryptor();
        try {
            encryptor.encryptPrivateKey(address, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptor;
    }
}
