package com.android_development.women_safety;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Location;

public class Second extends AppCompatActivity {

    ImageButton btn1, btn2, btn3, btn4, btn5;
    MediaPlayer mediaPlayer;
    private FusedLocationProviderClient fusedLocationClient; // Declare the FusedLocationProviderClient

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this); // Initialize FusedLocationProviderClient

        // Emergency call button functionality
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Phone number for emergency call
                String emergencyNumber = "tel:9729943453"; // Replace with actual emergency number

                // Check for call permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {

                    // Start the phone call
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(emergencyNumber));
                    startActivity(callIntent);
                } else {
                    // Request permission if not granted
                    ActivityCompat.requestPermissions(Second.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

        // Send SMS button functionality
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "9729943453"; // Replace with the recipient's phone number
                String message = "This is a safety alert! I need help at my current location."; // Customize the message

                // Check for SMS permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {

                    // Send the SMS
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Request SMS permission if not granted
                    ActivityCompat.requestPermissions(Second.this, new String[]{android.Manifest.permission.SEND_SMS}, 2);
                }
            }
        });

        // Initialize the MediaPlayer with the sound resource
        mediaPlayer = MediaPlayer.create(this, R.raw.siren); // Replace with your sound file name

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("enter");
                if (mediaPlayer != null) {
                    mediaPlayer.start(); // Play the sound
                }
            }
        });

        // Share location button functionality
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for location permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    // Get the last known location
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(Second.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Check if location is not null
                                    if (location != null) {
                                        double latitude = location.getLatitude();
                                        double longitude = location.getLongitude();
                                        String phoneNumber = "9729943453"; // Replace with the recipient's phone number

                                        // Create the message with the location link
                                        String message = "I need help! My current location: https://www.google.com/maps?q=" + latitude + "," + longitude;

                                        // Send the SMS
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                                        Toast.makeText(getApplicationContext(), "Location shared successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Request location permission if not granted
                    ActivityCompat.requestPermissions(Second.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 3);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted for phone call
                String emergencyNumber = "tel:9729943453";
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(emergencyNumber));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
            } else {
                Toast.makeText(this, "Permission to make phone calls denied.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted for SMS
                String phoneNumber = "9729943453";
                String message = "This is a safety alert! I need help at my current location.";

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission to send SMS denied.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 3) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted for location
                // You may want to trigger the location sharing here if necessary
            } else {
                Toast.makeText(this, "Permission to access location denied.", Toast.LENGTH_SHORT).show();
            }
        }
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
