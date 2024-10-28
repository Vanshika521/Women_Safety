package com.android_development.women_safety;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class register extends AppCompatActivity {

    EditText editText;
    ImageButton add, view, edit, dlt;
    ListView list;
    ArrayAdapter<String> arrayAdapter;  // ArrayAdapter for the ListView
    ArrayList<String> phoneNumbers = new ArrayList<>();  // ArrayList to store phone numbers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize UI elements
        editText = findViewById(R.id.editText); // for phone number
        add = findViewById(R.id.img1);
        view = findViewById(R.id.img2);
        edit = findViewById(R.id.img3);
        dlt = findViewById(R.id.img4);
        list = findViewById(R.id.list);

        // Add phone number to Firebase
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editText.getText().toString().trim();
                if (!phoneNumber.isEmpty()) {
                    String id = getDatabaseReference().push().getKey();
                    getDatabaseReference().child(id).setValue(phoneNumber).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(register.this, "Phone number added to Realtime Database!", Toast.LENGTH_SHORT).show();
                            editText.setText("");  // Clear input field
                        } else {
                            Toast.makeText(register.this, "Failed to add phone number.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(register.this, "Please enter a phone number!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete phone number from Firebase
        dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });


        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phoneNumbers);
        list.setAdapter(arrayAdapter);

        // Set an item click listener for the ListView
        list.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPhoneNumber = phoneNumbers.get(position);  // Get the selected phone number
            editText.setText(selectedPhoneNumber);  // Copy it to the EditText field
            Toast.makeText(register.this, "Number copied to EditText ", Toast.LENGTH_SHORT).show();
        });


        // View all phone numbers from Firebase
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        phoneNumbers.clear();  // Clear the ArrayList before adding new data
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String phoneNumber = snapshot.getValue(String.class);
                            if (phoneNumber != null) {
                                phoneNumbers.add(phoneNumber);  // Add each phone number to the ArrayList
                            }
                        }
                        arrayAdapter.notifyDataSetChanged();  // Notify the adapter of data changes
                        Toast.makeText(register.this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(register.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


// Edit phone number in Firebase
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPhoneNumber = editText.getText().toString().trim();

                if (!oldPhoneNumber.isEmpty()) {
                    // Show dialog to input new phone number
                    EditText newPhoneInput = new EditText(register.this);
                    newPhoneInput.setHint("Enter New Phone Number");

                    new AlertDialog.Builder(register.this)
                            .setTitle("Edit Phone Number")
                            .setView(newPhoneInput)
                            .setPositiveButton("Update", (dialog, which) -> {
                                String newPhoneNumber = newPhoneInput.getText().toString().trim();

                                if (!newPhoneNumber.isEmpty()) {
                                    // Find and update phone number in Firebase
                                    getDatabaseReference().orderByValue().equalTo(oldPhoneNumber)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            snapshot.getRef().setValue(newPhoneNumber);
                                                        }
                                                        Toast.makeText(register.this, "Phone number updated successfully!", Toast.LENGTH_SHORT).show();
                                                        editText.setText("");  // Clear input after update
                                                    } else {
                                                        Toast.makeText(register.this, "Phone number not found.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Toast.makeText(register.this, "Error updating data.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(register.this, "Please enter the new phone number!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    Toast.makeText(register.this, "Please enter the phone number to update!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

        private DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("phone_numbers");
    }
}
