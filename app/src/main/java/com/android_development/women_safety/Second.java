//package com.android_development.women_safety;
//
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.StyleableRes;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class Second extends AppCompatActivity {
//
//    ImageButton btn1,btn2,btn3,btn4,btn5;
//MediaPlayer mediaPlayer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_second);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//
//        btn1 =findViewById(R.id.img1);
//        btn2 =findViewById(R.id.img2);
//        btn3 =findViewById(R.id.img3);
//        btn4 =findViewById(R.id.img4);
//        btn5 =findViewById(R.id.img5);
//
//        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.siren);
//
//
//        btn3.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                System.out.println("1");
//                Toast.makeText(Second.this, "Alarm clicked", Toast.LENGTH_SHORT).show();
//
//                if (mediaPlayer != null) {
//                    mediaPlayer.stop();
//                    mediaPlayer.reset(); // Reset to prepare for playback again
//                    mediaPlayer.release();
//                    Toast.makeText(Second.this, "Alarm ", Toast.LENGTH_SHORT).show();
//                    // Release resources
//                }
//                return true;
//            }
//        });
//
//    }
//}

package com.android_development.women_safety;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;

public class Second extends AppCompatActivity {

    ImageButton btn1, btn2, btn3, btn4, btn5;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn1 = findViewById(R.id.img1);
        btn2 = findViewById(R.id.img2);
        btn3 = findViewById(R.id.img3);
        btn4 = findViewById(R.id.img4);
        btn5 = findViewById(R.id.img5);

        // Initialize the MediaPlayer with the sound resource
        mediaPlayer = MediaPlayer.create(this, R.raw.siren); // replace with your sound file name

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("enter");
                if (mediaPlayer != null) {
                    System.out.print("if");

                    mediaPlayer.start(); // Play the sound
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release the MediaPlayer resources
            mediaPlayer = null;
        }
    }
}