package com.android_development.women_safety;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    Animation top,bottom;
    ImageView img;
    TextView textView1,textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        top = AnimationUtils.loadAnimation(this,R.anim.topanim);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottomanim);

        img = findViewById(R.id.img);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);

        img.setAnimation(top);
        textView1.setAnimation(bottom);
        textView2.setAnimation(bottom);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(MainActivity.this,Second.class);
                startActivity(intent);
                finish();
            }
        },2000);


    }



}
