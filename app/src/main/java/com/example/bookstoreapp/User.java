package com.example.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "userName")
    String userName;
    @ColumnInfo(name = "password")
    String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
