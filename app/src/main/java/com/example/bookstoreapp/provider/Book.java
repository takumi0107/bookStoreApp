package com.example.bookstoreapp.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "bookId")
    int id;
    @ColumnInfo(name = "bookTitle")
    String title;
    @ColumnInfo(name = "bookIsbn")
    String isbn;
    @ColumnInfo(name = "bookAuthor")
    String author;
    @ColumnInfo(name = "bookDescription")
    String description;
    @ColumnInfo(name = "bookPrice")
    String price;

    public Book(String title, String isbn, String author, String description, String price) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
}
