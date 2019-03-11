package com.example.android.remotesuitetrigger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.android.remotesuitetrigger.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedpreference;
    int requestCode;
    public ListView mList;
    public Button speakButton;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreference = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());

        final Button button = (Button) findViewById(R.id.button_id);
        speakButton = (Button) findViewById(R.id.btn_speak);
        speakButton.setOnClickListener(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        View v = findViewById(R.id.spinner1);
        v.bringToFront();
        // String[] items = new String[]{"1", "2", "three"};
        ArrayList<String> items = null;
        try {
            items = new ConnectToDB().selectData("PLATFORM");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(adapter);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner2);
        View v2 = findViewById(R.id.spinner2);
        v2.bringToFront();
        // String[] items = new String[]{"1", "2", "three"};
        ArrayList<String> items1 = null;
        try {
            items1 = new ConnectToDB().selectData("COMMANDS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, items1);
        spinner1.setAdapter(adapter1);

        voiceinputbuttons();

        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {

                    String platform = String.valueOf(spinner.getSelectedItem());
                    String command = String.valueOf(spinner1.getSelectedItem());
                    System.out.println("text1 :" + platform);
                    System.out.println("text2 :" + command);
                    if ((platform.equals("Web")) && (command.equals("Trigger smoke suite"))) {
                        new ConnectToDB().insertSpeech(platform, "Button Click", command + " web", "TRIGGER");
                        Toast.makeText(getApplicationContext(), "Sent Data to Database", Toast.LENGTH_SHORT);
                        showStatus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void showStatus() {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    public void voiceinputbuttons() {
        speakButton = (Button) findViewById(R.id.btn_speak);
        mList = (ListView) findViewById(R.id.list);

    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void onClick(View v)
    {
        startVoiceRecognitionActivity();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
           
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, matches));

            for(int i=0;i<matches.size();i++)
            {
                Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT);
                String text3 = matches.get(i).toString();
                System.out.println("text3:"+text3);
                if(((String)matches.get(i)).equalsIgnoreCase("Trigger Smoke Suite Web"))
                {
                    try {
                        new ConnectToDB().insertSpeech("Web","Speech",matches.get(i).toString(),"TRIGGER");
                        showStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }


}