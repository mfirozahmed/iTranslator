package com.example.team_rough.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class activity_main extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private MediaPlayer tutorialMp, faqMp, contactMp, creditMp;
    private ImageView cameraIcon;
    private Uri imageUri;
    private String text;

    private static final int REQUEST_CAMERA = 111;
    private static final int MY_PERMISSIONS_REQUESTS = 222;
    private static final String TAG = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request runtime permission for storage and camera
        requestPermissions();

        //Setting Menu bar
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting media files
        tutorialMp = MediaPlayer.create(this, R.raw.tts_tutorial);
        faqMp = MediaPlayer.create(this, R.raw.tts_faqs);
        contactMp = MediaPlayer.create(this, R.raw.tts_contact_us);
        creditMp = MediaPlayer.create(this, R.raw.tts_credits);

        //Action listener of Navigation
        navigationView = findViewById(R.id.navid);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);

                return false;
            }
        });

        //Action listener of CameraIcon
        cameraIcon = findViewById(R.id.imageView);
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Saving the image in the storage
                String filename = System.currentTimeMillis() + ".jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_CAMERA);

            }
        });
    }

    private void requestPermissions() {

        List<String> requiredPermissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.CAMERA);
        }

        if (!requiredPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, requiredPermissions.toArray(new String[]{}), MY_PERMISSIONS_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUESTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    requestPermissions();
                }
                break;
            }
            default:
                requestPermissions();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (imageUri != null) {
                inspect(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void inspect(Uri uri) {

        InputStream is = null;
        Bitmap bitmap = null;

        try {

            is = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            options.inScreenDensity = DisplayMetrics.DENSITY_LOW;
            bitmap = BitmapFactory.decodeStream(is, null, options);
            inspectFromBitmap(bitmap);

        } catch (FileNotFoundException e) {
            Log.w(TAG, "Failed to find the file: " + uri, e);
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.w(TAG, "Failed to close InputStream", e);
                }
            }
        }
    }

    private void inspectFromBitmap(Bitmap bitmap) {

        //OCR part
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        try {
            if (!textRecognizer.isOperational()) {
                new AlertDialog.
                        Builder(this).
                        setMessage("Text recognizer could not be set up on your device").show();
                return;
            }

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> origTextBlocks = textRecognizer.detect(frame);
            List<TextBlock> textBlocks = new ArrayList<>();
            for (int i = 0; i < origTextBlocks.size(); i++) {
                TextBlock textBlock = origTextBlocks.valueAt(i);
                textBlocks.add(textBlock);
            }

            Collections.sort(textBlocks, new Comparator<TextBlock>() {
                @Override
                public int compare(TextBlock o1, TextBlock o2) {
                    int diffOfTops = o1.getBoundingBox().top - o2.getBoundingBox().top;
                    int diffOfLefts = o1.getBoundingBox().left - o2.getBoundingBox().left;
                    if (diffOfTops != 0) {
                        return diffOfTops;
                    }
                    return diffOfLefts;
                }
            });

            StringBuilder detectedText = new StringBuilder();
            for (TextBlock textBlock : textBlocks) {
                if (textBlock != null && textBlock.getValue() != null) {
                    detectedText.append(textBlock.getValue());
                    detectedText.append("\n");
                }
            }

            text = detectedText.toString();

        } finally {

            textRecognizer.release();

            //Sending text to TTS
            Intent intent = new Intent(activity_main.this, activity_tts.class);
            intent.putExtra("Text", text);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {

        //Menu items
        switch (item.getItemId()) {
            case R.id.tutorial:
                tutorialMp.start();
                Intent tutorialIntent = new Intent(activity_main.this, activity_tutorial.class);
                startActivity(tutorialIntent);
                break;
            case R.id.faq:
                faqMp.start();
                Intent faqIntent = new Intent(activity_main.this, activity_faq.class);
                startActivity(faqIntent);
                break;
            case R.id.contact:
                contactMp.start();
                Intent contactIntent = new Intent(activity_main.this, activity_contact.class);
                startActivity(contactIntent);
                break;
            case R.id.credit:
                creditMp.start();
                Intent creditIntent = new Intent(activity_main.this, activity_credit.class);
                startActivity(creditIntent);
                break;
            case R.id.logout:
                Intent logoutIntent = new Intent(activity_main.this, activity_login.class);
                startActivity(logoutIntent);
                activity_main.this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {

        if (tutorialMp.isPlaying()) {
            tutorialMp.stop();
            tutorialMp.release();
        }

        if (contactMp.isPlaying()) {
            contactMp.stop();
            contactMp.release();
        }
        if (faqMp.isPlaying()) {
            faqMp.stop();
            faqMp.release();
        }
        if (creditMp.isPlaying()) {
            creditMp.stop();
            creditMp.release();
        }
        super.onDestroy();
    }
}
