package com.example.team_rough.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navView;
    private MediaPlayer tutorialmp, profilemp, faqmp, contactmp;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tutorialmp = MediaPlayer.create(this, R.raw.tts_tutorial);
        profilemp = MediaPlayer.create(this, R.raw.tts_profile);
        faqmp = MediaPlayer.create(this, R.raw.tts_faqs);
        contactmp = MediaPlayer.create(this, R.raw.tts_contact_us);

        navView = findViewById(R.id.navid);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);

                return false;
            }
        });

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OCR.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.tutorial:
                tutorialmp.start();
                Intent intentt = new Intent(MainActivity.this,activity_tutorial.class);
                startActivity(intentt);
                break;
            case R.id.profile:
                profilemp.start();
                break;
            case R.id.faq:
                faqmp.start();
                Intent intentf = new Intent(MainActivity.this,activity_faq.class);
                startActivity(intentf);
                break;
            case R.id.contact:
                contactmp.start();
               Intent intent1 = new Intent(MainActivity.this,activity_contact.class);
               startActivity(intent1);
               break;
        }
    }

    @Override
    protected void onDestroy() {
        if (tutorialmp.isPlaying()) {
            tutorialmp.stop();
            tutorialmp.release();
        }
        if (profilemp.isPlaying()) {
            profilemp.stop();
            profilemp.release();
        }
        if (contactmp.isPlaying()) {
            contactmp.stop();
            contactmp.release();
        }
        if (faqmp.isPlaying()) {
            faqmp.stop();
            faqmp.release();
        }
        super.onDestroy();
    }
}
