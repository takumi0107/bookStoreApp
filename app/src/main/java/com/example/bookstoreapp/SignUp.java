package com.example.bookstoreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private EditText userName, password;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.signup);

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

    }

    public void SignUp(View V) {
        String inputUserName = userName.getText().toString();
        String inputPassword = password.getText().toString();

        User user = new User(inputUserName, inputPassword);
        myRef.child(inputUserName).setValue(user);
        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
        finish();
    }
    public void goLogin(View V) {
        finish();
    }
}
