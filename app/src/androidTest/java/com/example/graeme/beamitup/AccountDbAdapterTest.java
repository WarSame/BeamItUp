package com.example.graeme.beamitup;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.account.Account;
import com.example.graeme.beamitup.account.AccountDbAdapter;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDbAdapter;
import com.example.graeme.beamitup.wallet.WalletHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccountDbAdapterTest {
    private static final String TAG = "AccountDbAdapterTest";

    private static AccountDbAdapter accountDB;
    private static EthDbAdapter ethDb;
    private static Context appContext;

    private String insertedEmail = "someinsertedEmail@thisplace.com";
    private String insertedPassword = "someinsertedPassword";
    private char[] insertedPasswordCharArray = insertedPassword.toCharArray();//Gets cleared after insertion
    private Account insertedAccount;

    private Account otherInsertedAccount;
    private long otherInsertedAccountID;

    private String notInsertedEmail = "somenotInsertedemail@place.com";
    private char[] notInsertedPassword = "somenotInsertedpassword".toCharArray();
    private Account notInsertedAccount;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        accountDB = new AccountDbAdapter(appContext);
        ethDb = new EthDbAdapter(appContext);
    }

    @Before
    public void setUp() throws Exception {
        DbAdapter.DatabaseHelper dbHelper = new DbAdapter.DatabaseHelper(appContext);
        dbHelper.onUpgrade(accountDB.db, 0, 1);//Wipe db tables
        dbHelper.onUpgrade(ethDb.db, 0, 1);

        long insertedAccountID = accountDB.createAccount(insertedEmail, insertedPasswordCharArray);
        insertedAccount = new Account(insertedEmail, insertedAccountID);

        String otherInsertedEmail = "someotherinsertedemail@thisplace.com";
        char[] otherInsertedPassword = "someotherinsertedpassword".toCharArray();
        otherInsertedAccountID = accountDB.createAccount(otherInsertedEmail, otherInsertedPassword);
        otherInsertedAccount = new Account(otherInsertedEmail, otherInsertedAccountID);

        notInsertedAccount = new Account(notInsertedEmail, 17);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        accountDB.close();
        ethDb.close();
    }

    @Test
    public void createAccount_NotInsertedAccountCreated_ShouldBePositive() throws Exception {
        long accountID = accountDB.createAccount(notInsertedEmail, notInsertedPassword);
        assertTrue( accountID > 0);
    }

    @Test
    public void createAccount_InsertedAccountCreated_ShouldBeNegativeOne() throws Exception {
        assertTrue(accountDB.createAccount(insertedEmail, insertedPasswordCharArray) == -1);
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
    public void retrieveAccount_InsertedAccountEthCount_ShouldBeOne() throws Exception {
        WalletHelper.generateWallet(appContext, "someethnickname", insertedAccount.getId());
        Account retrievedAccount = accountDB.retrieveAccount(insertedAccount.getEmail());
        assertTrue(retrievedAccount.getEths().size() == 1);
    }

    @Test
    public void updateAccount_InsertedAccountUpdatedEmail_ShouldBeTrue() throws Exception {
        String updatedEmail = "updatedemail@someplace.com";
        insertedAccount.setEmail(updatedEmail);
        accountDB.updateAccount(insertedAccount);
        assertTrue(updatedEmail.equals(insertedAccount.getEmail()));
    }

    @Test(expected = SQLiteConstraintException.class)
    public void updateAccount_InsertedAccountDuplicated_ShouldBeSQLiteConstraintException() throws Exception {
        otherInsertedAccount.setEmail(insertedEmail);
        accountDB.updateAccount(otherInsertedAccount);
    }

    @Test(expected = NoSuchElementException.class)
    public void updateAccount_NotInsertedAccountUpdated_ShouldBeNoSuchElementException() throws Exception {
        accountDB.updateAccount(notInsertedAccount);
    }

    @Test
    public void deleteAccount_InsertedAccountDeleted_ShouldBeTrue() throws Exception {
        assertTrue(accountDB.deleteAccount(insertedAccount));
    }

    @Test
    public void deleteAccount_NotInsertedAccountDeleted_ShouldBeFalse() throws Exception {
        assertFalse(accountDB.deleteAccount(notInsertedAccount));
    }

    @Test
    public void isAuthentic_InsertedAccountCheckedWithCorrectValues_ShouldBeTrue() throws Exception {
        assertTrue(accountDB.isAuthentic(insertedEmail, insertedPassword.toCharArray()));
    }

    @Test
    public void isAuthentic_InsertedAccountCheckedWithIncorrectValues_ShouldBeFalse() throws Exception {
        assertFalse(accountDB.isAuthentic(insertedEmail, notInsertedPassword));
    }
    
    @Test
    public void isAuthentic_NotInsertedAccountChecked_ShouldBeFalse() throws Exception {
        assertFalse(accountDB.isAuthentic(notInsertedEmail, notInsertedPassword));
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