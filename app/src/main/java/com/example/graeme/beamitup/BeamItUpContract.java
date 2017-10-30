package com.example.graeme.beamitup;

import android.provider.BaseColumns;

final class BeamItUpContract {
    //If changing schema, must update db version
    static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "BeamItUp.db";

    private BeamItUpContract(){}

    static class AccountTable implements BaseColumns {
        static final String ACCOUNT_TABLE_NAME = "account";
        static final String ACCOUNT_COLUMN_EMAIL = "email";
        static final String ACCOUNT_COLUMN_PASSWORD_HASH = "password";
        static final String ACCOUNT_COLUMN_SALT = "salt";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ACCOUNT_TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," + ACCOUNT_COLUMN_EMAIL + " TEXT," +
                ACCOUNT_COLUMN_PASSWORD_HASH + " BLOB," + ACCOUNT_COLUMN_SALT + " BLOB)";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME;
    }

    static class AccountEthTable implements BaseColumns {
        static final String ACCOUNT_ETH_TABLE_NAME = "account_eth";
        static final String ACCOUNT_COLUMN_ACCOUNT_ID = "account_id";
        static final String ACCOUNT_COLUMN_ETH_ID = "eth_id";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ACCOUNT_ETH_TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," + ACCOUNT_COLUMN_ACCOUNT_ID + " INTEGER," +
                ACCOUNT_COLUMN_ETH_ID + " INTEGER)";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ACCOUNT_ETH_TABLE_NAME;
    }

    static class EthTable implements  BaseColumns {
        static final String ETH_TABLE_NAME = "eth";
        static final String ETH_PUBLIC_KEY = "public_key";
        static final String ETH_ENC_PRIVATE_KEY = "enc_private_key";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + ETH_TABLE_NAME +
            " (" + _ID + " INTEGER PRIMARY KEY," + ETH_PUBLIC_KEY + " TEXT," +
            ETH_ENC_PRIVATE_KEY + " BLOB)";

        static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ETH_TABLE_NAME;

    }

}
