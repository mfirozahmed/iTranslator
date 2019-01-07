package com.example.team_rough.myapplication;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


public class TTS extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private Button play;
    private Button stop;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);

        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);

        String text = getIntent().getExtras().getString("Text");
        textView.setText(text);
        textView.setMovementMethod(new ScrollingMovementMethod());

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.getDefault());

                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS", "Language not supported");
                    }else{
                        play.setEnabled(true);
                        stop.setEnabled(true);
                    }
                }else{
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speak();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stop();
            }
        });
    }

    private void stop() {

        textToSpeech.stop();
    }

    private void speak() {

        String text = getIntent().getExtras().getString("Text");
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {

        if(textToSpeech == null){
            Log.d("TTS", "NULL");
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
