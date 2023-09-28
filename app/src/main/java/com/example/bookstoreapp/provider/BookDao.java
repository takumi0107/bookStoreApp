package com.example.bookstoreapp.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {
    @Query("select * from books")
    LiveData<List<Book>> getAllBooks();

    @Query("select * from books where bookTitle=:name")
    List<Book> getBook(String name);

    @Insert
    void addBook(Book book);

    @Query("delete from books where bookTitle= :name")
    void deleteBook(String name);

    @Query("delete FROM books")
    void deleteAllBook();

    @Query("delete from books where bookId = (SELECT MAX(bookId) FROM books)")
    void deleteLastBook();

    @Query("select count(*) from books")
    int getTotalCount();

    @Query("DELETE FROM books WHERE bookAuthor = 'unknown'")
    void deleteUnknownAuthorBook();
}
