package com.example.bookstoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private EditText userName, password;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.login);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
    }

    public void login(View V) {
        String inputUserName = userName.getText().toString();
        String inputPassword = password.getText().toString();
        myRef.child(inputUserName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String savedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (savedPassword.equals(inputPassword)) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("authorized", true);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    } else {
                        Toast.makeText(getApplicationContext(), "Password is wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "There is no such user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goSignUp(View V) {
        Intent i = new Intent(Login.this, SignUp.class);
        startActivity(i);
    }
}
