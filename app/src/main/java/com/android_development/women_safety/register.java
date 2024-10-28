package com.android_development.women_safety;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class register extends AppCompatActivity {

    EditText editText;
    ImageButton add, view, edit, dlt;
    ListView list;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> phoneNumbers = new ArrayList<>();
    private String selectedPhoneNumber; // Variable to track the selected number

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

        // Setup ArrayAdapter and ListView
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phoneNumbers);
        list.setAdapter(arrayAdapter);

        // ListView item click listener to copy number to EditText
        list.setOnItemClickListener((parent, view, position, id) -> {
            selectedPhoneNumber = phoneNumbers.get(position); // Store the selected number
            editText.setText(selectedPhoneNumber);
            Toast.makeText(register.this, "Number copied to EditText", Toast.LENGTH_SHORT).show();
        });

        // Add phone number to Firebase
        add.setOnClickListener(view -> {
            String phoneNumber = editText.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                String id = getDatabaseReference().push().getKey();
                getDatabaseReference().child(id).setValue(phoneNumber).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(register.this, "Phone number added to Realtime Database!", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                    } else {
                        Toast.makeText(register.this, "Failed to add phone number.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(register.this, "Please enter a phone number!", Toast.LENGTH_SHORT).show();
            }
        });

        // Edit button functionality to update the selected phone number
        edit.setOnClickListener(view -> {
            String updatedNumber = editText.getText().toString().trim();
            if (!updatedNumber.isEmpty() && selectedPhoneNumber != null) {
                // Update the number in Firebase
                getDatabaseReference().orderByValue().equalTo(selectedPhoneNumber)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        snapshot.getRef().setValue(updatedNumber).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                // Update in the local ArrayList
                                                int position = phoneNumbers.indexOf(selectedPhoneNumber);
                                                phoneNumbers.set(position, updatedNumber); // Update the local list
                                                arrayAdapter.notifyDataSetChanged(); // Notify the adapter
                                                Toast.makeText(register.this, "Number updated successfully", Toast.LENGTH_SHORT).show();
                                                selectedPhoneNumber = null; // Reset the selected number
                                                editText.setText(""); // Clear the EditText after updating
                                            } else {
                                                Toast.makeText(register.this, "Failed to update number in database", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(register.this, "Phone number not found for update.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(register.this, "Error updating data.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Please select a number to edit", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete phone number from Firebase
        dlt.setOnClickListener(view -> {
            String phoneNumberToDelete = editText.getText().toString().trim();
            if (!phoneNumberToDelete.isEmpty()) {
                getDatabaseReference().orderByValue().equalTo(phoneNumberToDelete)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        snapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(register.this, "Phone number deleted successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(register.this, "Phone number not found.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(register.this, "Error deleting data.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(register.this, "Please enter the phone number to delete!", Toast.LENGTH_SHORT).show();
            }
        });

        // View button functionality to load data from Firebase
        view.setOnClickListener(view -> {
            getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    phoneNumbers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String phoneNumber = snapshot.getValue(String.class);
                        if (phoneNumber != null) {
                            phoneNumbers.add(phoneNumber);
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(register.this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(register.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Firebase database reference
    private DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("phone_numbers");
    }
}
