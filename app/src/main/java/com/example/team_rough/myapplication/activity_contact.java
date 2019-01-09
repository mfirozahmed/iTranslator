package com.example.team_rough.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class activity_contact extends Activity {
   TextView idtxt,numbertext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        numbertext = (TextView) findViewById(R.id.button2);
        idtxt = (TextView) findViewById(R.id.button);
        idtxt.setPaintFlags(idtxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        numbertext.setPaintFlags(idtxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

     getWindow().setLayout((int)(width*.8),(int)(height*.6));

        idtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "subject text"+ "&body=" + "body text " + "&to=" + "snehadevi2016105@gmail.com");
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
            }
        });

        numbertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean callResult = CallPermission.checkPermission(activity_contact.this);
                if(callResult){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:01521429899"));
                    startActivity(intent);
                }
            }
        });

    }
}
