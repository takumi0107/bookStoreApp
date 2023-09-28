package com.example.bookstoreapp.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao mBookDao;
    private LiveData<List<Book>> mAllBooks;

    BookRepository(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.BookDao();
        mAllBooks = mBookDao.getAllBooks();
    }
    LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }
    void insert(Book book) {
        BookDatabase.databaseWriteExecutor.execute(() -> mBookDao.addBook(book));
    }

    void deleteAll(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteAllBook();
        });
    }

    void deleteLastBook(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteLastBook();
        });
    }

    int getTotalCount(){
        return mAllBooks.getValue().size();
    }

    void deleteUnknownAuthorBook() {
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteUnknownAuthorBook();
        });
    }
}
