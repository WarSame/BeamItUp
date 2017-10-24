package com.example.graeme.beamitup;

import android.os.Bundle;
import android.app.Activity;

public class CreateAccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
