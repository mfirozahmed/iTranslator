package com.example.team_rough.myapplication;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private DrawerLayout dl;
     private ActionBarDrawerToggle toggle;
     private NavigationView navView;
     MediaPlayer tutorialmp,profilemp,faqmp,contactmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dl = (DrawerLayout)findViewById(R.id.drlayout);
        toggle = new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         tutorialmp = MediaPlayer.create(this,R.raw.tts_tutorial);
        profilemp = MediaPlayer.create(this,R.raw.tts_profile);
        faqmp = MediaPlayer.create(this,R.raw.tts_faqs);
        contactmp = MediaPlayer.create(this,R.raw.tts_contact_us);



        navView = findViewById(R.id.navid);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);

                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.tutorial:
                tutorialmp.start();
                Toast.makeText(MainActivity.this, "Tutorial Selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.profile:
                profilemp.start();
                Toast.makeText(MainActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.faq:
                faqmp.start();
                Toast.makeText(MainActivity.this, "Faq's Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact:
                contactmp.start();
                Toast.makeText(MainActivity.this, "Contact us Selected", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
