package com.example.android.remotesuitetrigger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreference = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());

        final Button button = (Button) findViewById(R.id.button_id);
        speakButton = (Button) findViewById(R.id.btn_speak);
        speakButton.setOnClickListener(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
       // String[] items = new String[]{"1", "2", "three"};
        ArrayList<String> items = null;
        try {
            items = new ConnectToDB().selectData("PLATFORM");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,items);
        spinner.setAdapter(adapter);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner2);
        // String[] items = new String[]{"1", "2", "three"};
        ArrayList<String> items1 = null;
        try {
            items1 = new ConnectToDB().selectData("COMMANDS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,items1);
        spinner1.setAdapter(adapter1);


        voiceinputbuttons();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    new ConnectToDB().insertSpeech("API","Speech","Trigger smoke suite","TRIGGER");
                    Toast.makeText(getApplicationContext(), "Sent Data to Database", Toast.LENGTH_SHORT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

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
                if(((String)matches.get(i)).equalsIgnoreCase("Trigger Smoke Suite"))
                {
                    Toast.makeText(getApplicationContext(),"Finding a Match",Toast.LENGTH_SHORT);
                    try {
                        new ConnectToDB().insertSpeech("API","Speech",matches.get(i).toString(),"TRIGGER");
                        Toast.makeText(getApplicationContext(),"Sent Data to Database",Toast.LENGTH_SHORT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }


}