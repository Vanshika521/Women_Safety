package com.android_development.women_safety;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ValueEventListener;

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

        // View all phone numbers from Firebase
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        StringBuilder data = new StringBuilder();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String phoneNumber = snapshot.getValue(String.class);
                            data.append(phoneNumber).append("\n");
                        }
                        editText.setText(data.toString());
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
                String newPhoneNumber = editText.getText().toString().trim();

                if (!oldPhoneNumber.isEmpty() && !newPhoneNumber.isEmpty()) {
                    getDatabaseReference().orderByValue().equalTo(oldPhoneNumber)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            snapshot.getRef().setValue(newPhoneNumber);
                                        }
                                        Toast.makeText(register.this, "Phone number updated successfully!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(register.this, "Please enter the phone number to update!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("phone_numbers");
    }
}
