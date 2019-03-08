package com.example.android.remotesuitetrigger;

import android.support.v7.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    int status ;

    protected void sendStatus() {

        if (status == 0)
        setContentView(R.layout.second_activity);
        else
            setContentView(R.layout.third_activity);
    }
}