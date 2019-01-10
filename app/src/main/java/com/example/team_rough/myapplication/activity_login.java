package com.example.team_rough.myapplication;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_login extends AppCompatActivity {

    private EditText loginmail, loginpass;
    private Button signUpbtn;

    RelativeLayout relativeLayout1, relativeLayout2, myLayout;
    AnimationDrawable animationDrawable;
    Button next;
    FirebaseAuth mAuth;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relativeLayout1.setVisibility(View.VISIBLE);
            relativeLayout2.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        myLayout = findViewById(R.id.myLayout);

        mAuth = FirebaseAuth.getInstance();

        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        relativeLayout1 = findViewById(R.id.relativeLayout1);
        relativeLayout2 = findViewById(R.id.relativeLayout2);

        loginmail = findViewById(R.id.mailAdress) ;
        loginpass = findViewById(R.id.passWord) ;
        signUpbtn = findViewById(R.id.login);

        handler.postDelayed(runnable, 2000);
        next = findViewById(R.id.forget);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_login.this, activity_main.class);
                startActivity(intent);
                activity_login.this.finish();
            }
        });

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowingUserToLogin();
            }
        });

    }

    private void allowingUserToLogin() {

        String email = loginmail.getText().toString();
        String pass = loginpass.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(activity_login.this,"Please enter your email", Toast.LENGTH_LONG).show();
            return;
        }

        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(activity_login.this,"Please enter your password", Toast.LENGTH_LONG).show();
            return;
        }

        else{
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendUserToMainActivity();
                            }
                            else{
                                Toast.makeText(activity_login.this, "Wrong email or password!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(activity_login.this,activity_main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        activity_login.this.finish();
    }
}
