package com.example.team_rough.myapplication;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash2 extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        textView = findViewById(R.id.tv);
        imageView = findViewById(R.id.iv);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        textView.startAnimation(myanim);
        imageView.startAnimation(myanim);

        final Intent intent = new Intent(this,MainActivity.class);
        Thread timer = new Thread(){
            public void run(){
               try{
                   sleep(3000);
               } catch (InterruptedException e){
                   e.printStackTrace();
               }
               finally {
                   Splash2.this.finish();
                   startActivity(intent);
               }
            }
        };

        timer.start();
    }
}
