package com.example.graeme.beamitup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.graeme.beamitup.BeamItUpContract.EthTable;

public class EthDbAdapter extends SQLiteOpenHelper{
    private static final String TAG = "EthDbAdapter";
    private static final String CREATE_TABLE = EthTable.SQL_CREATE_TABLE;
    private static final String DELETE_TABLE = EthTable.SQL_DELETE_TABLE;

    EthDbAdapter(Context context){
        super(context, BeamItUpContract.DATABASE_NAME, null, BeamItUpContract.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    void createEth(Eth eth){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EthTable.ETH_PUBLIC_KEY,
                eth.getAddress());
        contentValues.put(EthTable.ETH_ENC_PRIVATE_KEY,
                eth.getEncPrivateKey());
        if (db.insert(EthTable.ETH_TABLE_NAME, null, contentValues) == -1){
            throw new SQLException();
        }
        db.close();
    }

    Eth retrieveEth(String address){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(EthTable.ETH_TABLE_NAME,
                new String[]{
                        EthTable.ETH_PUBLIC_KEY,
                        EthTable.ETH_ENC_PRIVATE_KEY
                },
                EthTable.ETH_PUBLIC_KEY + " like ?", new String[]{address},
                null, null, null);
        res.moveToFirst();
        byte[] encPrivateKey = res.getBlob(res.getColumnIndex(EthTable.ETH_ENC_PRIVATE_KEY));
        res.close();
        return new Eth(address, encPrivateKey);
    }

    void updateEth(int id, Eth eth){

    }

    void deleteEth(int id){

    }
}
