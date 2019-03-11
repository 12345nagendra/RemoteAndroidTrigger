package com.example.android.remotesuitetrigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    Button button;
    Boolean flag = false;
    String currStatus = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        try {
            getStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Button button = (Button) findViewById(R.id.stop_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    new ConnectToDB().update();
                    Intent intent = new Intent(SecondActivity.this,ThirdActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getStatus() throws Exception {
        TextView status = findViewById(R.id.queue);
        currStatus = new ConnectToDB().showstatus();
        status.setText(currStatus);
    }



//      while (!currStatus.equalsIgnoreCase("Finished"))
//      {
//          status.setText(currStatus);
//          getStatus();
//      }





}