package com.example.graeme.beamitup.wallet;

import android.content.ContentValues;
import android.content.Context;
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
        return null;
    }
}
