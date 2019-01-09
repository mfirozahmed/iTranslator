package com.example.team_rough.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;

public class OCR extends AppCompatActivity {

    private ImageView imageView;
    private StringBuilder stringBuilder;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        imageView = findViewById(R.id.img1);
        button = findViewById(R.id.ocr);
//        byte[] byteArray = getIntent().getByteArrayExtra("Image");


        try {
//            Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("image2");
//            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chobi.jpeg";
            Bitmap bitmap = BitmapFactory.decodeFile(fileName);
            imageView.setImageBitmap(bitmap);
            TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

            if (!textRecognizer.isOperational()) {
                Toast.makeText(getApplicationContext(), "could not get the text", Toast.LENGTH_SHORT).show();

            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> items = textRecognizer.detect(frame);
                stringBuilder = new StringBuilder();
                for (int i = 0; i < items.size(); ++i) {
                    TextBlock myitem = items.valueAt(i);
                    stringBuilder.append(myitem.getValue());
                    stringBuilder.append('\n');
                }
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OCR.this, TTS.class);
                String text = stringBuilder.toString();
                intent.putExtra("Text", text);
                startActivity(intent);
                OCR.this.finish();
            }
        });
    }
}


