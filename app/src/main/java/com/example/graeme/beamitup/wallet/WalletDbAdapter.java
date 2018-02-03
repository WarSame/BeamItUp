package com.example.graeme.beamitup.wallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.graeme.beamitup.DbAdapter;

public class WalletDbAdapter extends DbAdapter {
    private static final String TAG = "WalletDbAdapter";
    private Context context;

    public WalletDbAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public long createEncryptedWallet(EncryptedWallet encryptedWallet, long ethID){
        ContentValues contentValues = new ContentValues();
        contentValues.put(WalletTable.ETH_ID, ethID);
        contentValues.put(WalletTable.WALLET_ENCRYPTED_LONG_PASSWORD, encryptedWallet.getEncryptedLongPassword());
        contentValues.put(WalletTable.WALLET_IV, encryptedWallet.getIV());
        contentValues.put(WalletTable.WALLET_NAME, encryptedWallet.getWalletName());
        long walletID = db.insert(
                WalletTable.WALLET_TABLE_NAME,
                null,
                contentValues
        );
        Log.i(TAG, "Inserted wallet with ID: " + walletID);
        return walletID;
    }

    public EncryptedWallet retrieveEncryptedWalletByEthID(long ethID) {
        Cursor res = this.db.query(
                WalletTable.WALLET_TABLE_NAME,
                new String[]{
                        WalletTable.ETH_ID,
                        WalletTable.WALLET_ENCRYPTED_LONG_PASSWORD,
                        WalletTable.WALLET_IV,
                        WalletTable.WALLET_NAME,
                        WalletTable._ID
                },
                WalletTable.ETH_ID+"=?",
                new String[]{Long.toString(ethID)},
                null,
                null,
                null
        );
        res.moveToFirst();
        byte[] encryptedLongPassword = res.getBlob(res.getColumnIndex(WalletTable.WALLET_ENCRYPTED_LONG_PASSWORD));
        byte[] IV = res.getBlob(res.getColumnIndex(WalletTable.WALLET_IV));
        String walletName = res.getString(res.getColumnIndex(WalletTable.WALLET_NAME));
        long ID = res.getLong(res.getColumnIndex(WalletTable._ID));
        res.close();
        return new EncryptedWallet(encryptedLongPassword, IV, walletName, ID);
    }
}
