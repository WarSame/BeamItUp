package com.example.graeme.beamitup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.web3j.utils.Assertions;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class AccountDbAdapterTest {
    private AccountDbAdapter accountDB;

    private String insertedEmail;
    private byte[] insertedSalt;
    private byte[] insertedPasswordHash;

    private String notInsertedEmail;
    private byte[] notInsertedSalt;
    private byte[] notInsertedPasswordHash;

    Account newAccount;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        accountDB = new AccountDbAdapter(appContext);

        DbAdapter.DatabaseHelper dbHelper = new DbAdapter.DatabaseHelper(appContext);
        dbHelper.onUpgrade(accountDB.db, 0, 1);//Wipe db tables

        insertedEmail = "someinsertedEmail@thisplace.com";
        String insertedPassword = "someinsertedPassword";
        insertedSalt = Encryption.generateSalt();
        insertedPasswordHash = Encryption.hashPassword(insertedPassword, insertedSalt);

        accountDB.createAccount(insertedEmail, insertedPasswordHash, insertedSalt);

        notInsertedEmail = "somenotInsertedemail@place.com";
        String notInsertedPassword = "somenotInsertedpassword";
        notInsertedSalt = Encryption.generateSalt();
        notInsertedPasswordHash = Encryption.hashPassword(notInsertedPassword, notInsertedSalt);

        newAccount = new Account("someotheremail@someotherplace.com", 17);

    }

    @After
    public void tearDown() throws Exception {
        accountDB.close();
    }

    @Test
    public void createAccount_NotInsertedAccountCreated_ShouldBePositive() throws Exception {
        assertTrue(accountDB.createAccount(notInsertedEmail, notInsertedPasswordHash, notInsertedSalt) > 0);
    }

    @Test
    public void createAccount_InsertedAccountCreated_ShouldBeNegativeOne() throws Exception {
        assertTrue(accountDB.createAccount(insertedEmail, insertedPasswordHash, insertedSalt) == -1);
    }

    @Test
    public void retrieveAccount_InsertedAccountRetrieved_ShouldBeTrue() throws Exception {
        Account account = accountDB.retrieveAccount(insertedEmail);
        assertTrue(account.getEmail().equals(insertedEmail));
    }

    @Test(expected = NoSuchElementException.class)
    public void retrieveAccount_NotInsertedAccountRetrieved_ShouldBeNoSuchElement() throws Exception {
        accountDB.retrieveAccount(notInsertedEmail);
    }

    @Test
    public void updateAccount_InsertedAccountUpdated_ShouldBeTrue() throws Exception {
        assertTrue(accountDB.updateAccount(insertedEmail, newAccount));
    }

    @Test
    public void updateAccount_NotInsertedAccountUpdated_ShouldBeFalse() throws Exception {
        assertFalse(accountDB.updateAccount(notInsertedEmail, newAccount));
    }

    @Test
    public void deleteAccount_InsertedAccountDeleted_ShouldBeTrue() throws Exception {
        assertTrue(accountDB.deleteAccount(insertedEmail));
    }

    @Test
    public void deleteAccount_NotInsertedAccountDeleted_ShouldBeFalse() throws Exception {
        assertFalse(accountDB.deleteAccount(notInsertedEmail));
    }

    @Test
    public void isAuthentic_InsertedAccountCheckedWithCorrectValues_ShouldBeTrue() throws Exception {
        assertTrue(accountDB.isAuthentic(insertedEmail, insertedPasswordHash));
    }

    @Test
    public void isAuthentic_InsertedAccountCheckedWithIncorrectValues_ShouldBeFalse() throws Exception {
        assertFalse(accountDB.isAuthentic(insertedEmail, notInsertedPasswordHash));
    }
    
    @Test
    public void isAuthentic_NotInsertedAccountChecked_ShouldBeFalse() throws Exception {
        assertFalse(accountDB.isAuthentic(notInsertedEmail, notInsertedPasswordHash));
    }

    @Test
    public void isEmailInUse_InsertedAccountChecked_ShouldBeTrue() throws Exception {
        assertTrue(accountDB.isEmailInUse(insertedEmail));
    }

    @Test
    public void isEmailInUse_NotInsertedAccountChecked_ShouldBeFalse() throws Exception {
        assertFalse(accountDB.isEmailInUse(notInsertedEmail));
    }

}