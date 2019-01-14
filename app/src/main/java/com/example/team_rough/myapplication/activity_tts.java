package com.example.team_rough.myapplication;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class activity_tts extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private Button play;
    private Button stop;
    private TextView textView;

    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private int mStatus = 0;
    private boolean mProcessed = false;
    private final String FILENAME = "/tts_text.wav";

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

        textToSpeech = new TextToSpeech(this, this);

        mediaPlayer = new MediaPlayer();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait ...");

        View.OnClickListener playButttonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mStatus == TextToSpeech.SUCCESS) {

                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        runMediaPlayer(1);
                        play.setText("Play");
                        return;
                    }
                    play.setText("Pause");

                    progressDialog.show();

                    HashMap<String, String> myHashRender = new HashMap();
                    String utteranceID = "tts";
                    myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceID);

                    String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + FILENAME;

                    if (!mProcessed) {
                        int status = textToSpeech.synthesizeToFile(textView.getText().toString(), myHashRender, fileName);
                    } else {
                        runMediaPlayer(0);
                    }
                } else {
                    String message = "TextToSpeech Engine is not initialized";
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        };

        View.OnClickListener stopButttonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                play.setText("Play");
                runMediaPlayer(2);
            }
        };

        play.setOnClickListener(playButttonClickListener);
        stop.setOnClickListener(stopButttonClickListener);

        MediaPlayer.OnCompletionListener mediaPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                play.setText("Play");
            }
        };

        mediaPlayer.setOnCompletionListener(mediaPlayerCompletionListener);
    }

    @Override
    protected void onDestroy() {

        textToSpeech.stop();
        textToSpeech.shutdown();

        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();

        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        mStatus = status;
        ttsStart(textToSpeech);
    }

    private void ttsStart(TextToSpeech tts) {
        this.textToSpeech = tts;

        if (Build.VERSION.SDK_INT >= 15) {
            this.textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onDone(String utteranceId) {

                    mProcessed = true;
                    initializeMediaPlayer();
                    runMediaPlayer(0);
                }

                @Override
                public void onError(String utteranceId) {
                }

                @Override
                public void onStart(String utteranceId) {
                }
            });
        } else {
            this.textToSpeech.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                @Override
                public void onUtteranceCompleted(String utteranceId) {

                    mProcessed = true;
                    initializeMediaPlayer();
                    runMediaPlayer(0);
                }
            });
        }
    }

    private void initializeMediaPlayer() {
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + FILENAME;

        Uri uri = Uri.parse("file://" + fileName);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void runMediaPlayer(int status) {
        progressDialog.dismiss();

        // Start
        if (status == 0) {
            mediaPlayer.start();
        }
        // Pause
        if (status == 1) {
            mediaPlayer.pause();
        }
        //Stop
        if (status == 2) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            initializeMediaPlayer();
        }
    }
}

