package com.android_development.women_safety;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.FirebaseApp;

public class register extends AppCompatActivity {

    EditText editText;
    ImageButton add, view, edit, dlt;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize UI elements
        editText = findViewById(R.id.editText);
        add = findViewById(R.id.img1);
        view = findViewById(R.id.img2);
        edit = findViewById(R.id.img3);
        dlt = findViewById(R.id.img4);
        list = findViewById(R.id.list);

        // Add button click listener to add phone number to Firebase Realtime Database
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editText.getText().toString().trim();
                Log.d("print","1");
                if (!phoneNumber.isEmpty()) {
                    // Use the method to get the database reference
                    Log.d("print","2");
                    String id = getDatabaseReference().push().getKey();
                    Log.d("print","3");
                    getDatabaseReference().child("Phone_no").setValue(phoneNumber).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("print","4");
                            Toast.makeText(register.this, "Phone number added to Realtime Database!", Toast.LENGTH_SHORT).show();
                            Log.d("print","5");
                            editText.setText("");  // Clear the input field after successful addition
                        } else {
                            Log.d("print","6");
                            Toast.makeText(register.this, "Failed to add phone number to Realtime Database.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(register.this, "Please enter a phone number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("phone_numbers");
    }
}

