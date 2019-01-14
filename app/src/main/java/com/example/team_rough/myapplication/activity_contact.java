package com.example.team_rough.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class activity_contact extends Activity {

    private TextView idText, numberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        numberText = findViewById(R.id.phone);
        idText = findViewById(R.id.email);

        idText.setPaintFlags(idText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        numberText.setPaintFlags(idText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        idText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "subject text" + "&body=" + "body text " + "&to=" + "snehadevi2016105@gmail.com");
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
            }
        });

        numberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean callResult = CallPermission.checkPermission(activity_contact.this);
                if (callResult) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:01521429899"));
                    startActivity(intent);
                }
            }
        });

    }
}
