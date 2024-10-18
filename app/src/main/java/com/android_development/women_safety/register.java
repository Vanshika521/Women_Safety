package com.android_development.women_safety;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    EditText editText;
    ImageButton add, view, edit, dlt;
    RecyclerView recyclerView;
    PhoneAdapter phoneAdapter;  // Custom RecyclerView adapter for phone numbers
    ArrayList<String> phoneList;  // List to hold phone numbers
    FirebaseFirestore db;  // Firestore database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        editText = findViewById(R.id.editText);
        add = findViewById(R.id.img1);
        view = findViewById(R.id.img2);
        edit = findViewById(R.id.img3);
        dlt = findViewById(R.id.img4);
        recyclerView = findViewById(R.id.recycleView);

        // Initialize RecyclerView and adapter
        phoneList = new ArrayList<>();
        phoneAdapter = new PhoneAdapter(phoneList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(phoneAdapter);

        // Load phone numbers from Firebase
        loadPhoneNumbers();

        // Add button click listener to add phone number to Firestore
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editText.getText().toString().trim();

                if (!TextUtils.isEmpty(phoneNumber)) {
                    addPhoneNumberToFirestore(phoneNumber);
                } else {
                    Toast.makeText(register.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete button click listener to delete the entered phone number
        dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editText.getText().toString().trim();

                if (!TextUtils.isEmpty(phoneNumber)) {
                    deletePhoneNumberFromFirestore(phoneNumber);
                } else {
                    Toast.makeText(register.this, "Please enter a phone number to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Function to add phone number to Firestore
    private void addPhoneNumberToFirestore(String phoneNumber) {
        CollectionReference phoneNumbersRef = db.collection("phoneNumbers");

        // Create a new document with the phone number
        Map<String, Object> phoneData = new HashMap<>();
        phoneData.put("number", phoneNumber);

        // This will now return a Task<DocumentReference>
        phoneNumbersRef.add(phoneData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(register.this, "Phone number added", Toast.LENGTH_SHORT).show();
                    phoneList.add(phoneNumber);  // Add to local list
                    phoneAdapter.notifyDataSetChanged();  // Notify adapter of data change
                    editText.setText("");  // Clear the input field
                } else {
                    Toast.makeText(register.this, "Failed to add phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Function to delete phone number from Firestore
    private void deletePhoneNumberFromFirestore(String phoneNumber) {
        CollectionReference phoneNumbersRef = db.collection("phoneNumbers");

        phoneNumbersRef.whereEqualTo("number", phoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            task.getResult().getDocuments().get(0).getReference().delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(register.this, "Phone number deleted", Toast.LENGTH_SHORT).show();
                                                phoneList.remove(phoneNumber);  // Remove from local list
                                                phoneAdapter.notifyDataSetChanged();  // Notify adapter of data change
                                            } else {
                                                Toast.makeText(register.this, "Failed to delete phone number", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(register.this, "Phone number not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Function to load phone numbers from Firestore and display in RecyclerView
    private void loadPhoneNumbers() {
        CollectionReference phoneNumbersRef = db.collection("phoneNumbers");

        phoneNumbersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String phoneNumber = document.getString("number");
                        phoneList.add(phoneNumber);  // Add to local list
                    }
                    phoneAdapter.notifyDataSetChanged();  // Notify adapter of data change
                } else {
                    Toast.makeText(register.this, "Failed to load phone numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
