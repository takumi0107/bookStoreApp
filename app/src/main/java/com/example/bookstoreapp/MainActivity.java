package com.example.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapp.provider.Book;
import com.example.bookstoreapp.provider.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private int st = 0;
    private int lp = 0;

    private EditText bookIdInfo, titleNameInfo, isbnInfo, authorInfo, descriptionInfo, priceInfo;
    DrawerLayout drawer;
    ArrayList<String> bookList = new ArrayList<String>();
    ArrayAdapter myAdapter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyRecyclerViewAdapter adapter;
    List<Book> bookArray = new ArrayList<>();
    private BookViewModel mBookViewModel;

    DatabaseReference myRef;

    View myFrame;

    int x_down;
    int y_down;

    boolean authorized = false;

    GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authorized = getIntent().getBooleanExtra("authorized", false);
        if (authorized == false) {
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
            return;
        }
        setContentView(R.layout.drawer);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        bookIdInfo = findViewById(R.id.bookIdInput);
        titleNameInfo = findViewById(R.id.titleInput);
        isbnInfo = findViewById(R.id.isbnInput);
        authorInfo = findViewById(R.id.authorInput);
        descriptionInfo = findViewById(R.id.descriptionInput);
        priceInfo = findViewById(R.id.priceInput);

        myFrame = findViewById(R.id.frame_id);

        IntentFilter intentFilter = new IntentFilter("MySMS");
        registerReceiver(myReceiver, intentFilter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

//        ListView listView = findViewById(R.id.lv);
//        myAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bookList);
//        listView.setAdapter(myAdapter);

//        recyclerView = findViewById(R.id.rv);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        adapter = new MyRecyclerViewAdapter();
//        recyclerView.setAdapter(adapter);

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        mBookViewModel.getAllBooks().observe(this, newData -> {
//            adapter.setData(newData);
//            adapter.notifyDataSetChanged();
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frame1, new RecyclerViewFragment()).commit();

        FloatingActionButton fabAddBook = findViewById(R.id.floatingActionButton2);
        fabAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBook();
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Book/myBook");

        gestureDetector = new GestureDetector(this, new MyGestureDetector());

        myFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lab3", "onStart");

        SharedPreferences bookData = getSharedPreferences("book1", 0);
        String savedBookId = bookData.getString("bookId", "");
        String savedTitleName = bookData.getString("titleName", "");
        String savedIsbn = bookData.getString("isbn", "");
        String savedAuthor = bookData.getString("author", "");
        String savedDescription = bookData.getString("description", "");
        String savedPrice = bookData.getString("price", "");

        bookIdInfo.setText(savedBookId);
        titleNameInfo.setText(savedTitleName);
        isbnInfo.setText(savedIsbn);
        authorInfo.setText(savedAuthor);
        descriptionInfo.setText(savedDescription);
        priceInfo.setText(savedPrice);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lab3", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lab3", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lab3", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lab3", "onDestroy");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
        Log.i("lab3", "onSaveInstanceState");

        String onSaveTitleName = titleNameInfo.getText().toString();
        outState.putString("title", onSaveTitleName);
        String onSaveIsbn = isbnInfo.getText().toString();
        outState.putString("isbn", onSaveIsbn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("lab3", "onRestoreInstanceState");

        String onSaveTitleName = savedInstanceState.getString("title");
        String onSaveIsbn = savedInstanceState.getString("isbn");

        titleNameInfo.setText(onSaveTitleName);
        isbnInfo.setText(onSaveIsbn);
    }

    public void addBook() {
        String bookId = bookIdInfo.getText().toString();
        String titleName = titleNameInfo.getText().toString();
        String isbn = isbnInfo.getText().toString();
        String author = authorInfo.getText().toString();
        String description = descriptionInfo.getText().toString();
        String price = priceInfo.getText().toString();

        Toast addBookMsg = Toast.makeText(this, "Book (" + titleName
                + ") and the price (" + price + ")", Toast.LENGTH_SHORT);
        addBookMsg.show();
        String cardPosition = String.valueOf(bookArray.size());

        Book book = new Book(titleName, isbn, author, description, price);
//        bookArray.add(book);
//        adapter.notifyDataSetChanged();
        mBookViewModel.insert(book);
        myRef.push().setValue(book);


        SharedPreferences bookData = getSharedPreferences("book1", 0);
        SharedPreferences.Editor bookEditor = bookData.edit();
        bookEditor.putString("bookId", bookId);
        bookEditor.putString("titleName", titleName);
        bookEditor.putString("isbn", isbn);
        bookEditor.putString("author", author);
        bookEditor.putString("description", description);
        bookEditor.putString("price", price);
        bookEditor.apply();
    }
    public void clearInputs(View V) {
        bookIdInfo.getText().clear();
        titleNameInfo.getText().clear();
        isbnInfo.getText().clear();
        authorInfo.getText().clear();
        descriptionInfo.getText().clear();
        priceInfo.getText().clear();
    }

    public void DoublePrice(View V) {
        double price = Double.parseDouble(priceInfo.getText().toString());
        double doublePrice = price * 2;
        priceInfo.setText(Double.toString(doublePrice));
    }

    public void reload(View V) {
        SharedPreferences bookData = getSharedPreferences("book1", 0);
        String savedBookId = bookData.getString("bookId", "");
        String savedTitleName = bookData.getString("titleName", "");
        String savedIsbn = bookData.getString("isbn", "");
        String savedAuthor = bookData.getString("author", "");
        String savedDescription = bookData.getString("description", "");
        String savedPrice = bookData.getString("price", "");

        bookIdInfo.setText(savedBookId);
        titleNameInfo.setText(savedTitleName);
        isbnInfo.setText(savedIsbn);
        authorInfo.setText(savedAuthor);
        descriptionInfo.setText(savedDescription);
        priceInfo.setText(savedPrice);
    }

    public void setIsbn(View V) {
        String newIsbn = "00112233";
        SharedPreferences bookData = getSharedPreferences("book1", 0);
        SharedPreferences.Editor bookEditor = bookData.edit();
        bookEditor.putString("isbn", newIsbn);
        bookEditor.apply();
    }

    public void clearField() {
        bookIdInfo.getText().clear();
        titleNameInfo.getText().clear();
        isbnInfo.getText().clear();
        authorInfo.getText().clear();
        descriptionInfo.getText().clear();
        priceInfo.getText().clear();
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("KEY1");

            StringTokenizer sT = new StringTokenizer(msg, "|");
            String receivedBookId = sT.nextToken();
            String receivedTitle = sT.nextToken();
            String receivedIsbn = sT.nextToken();
            String receivedAuthor = sT.nextToken();
            String receivedDescription = sT.nextToken();
            double receivedPrice = Double.parseDouble(sT.nextToken());
            boolean receivedBool = Boolean.parseBoolean(sT.nextToken());

            if (receivedBool == true) {
                receivedPrice += 100;
            } else {
                receivedPrice += 5;
            }

            bookIdInfo.setText(receivedBookId);
            titleNameInfo.setText(receivedTitle);
            isbnInfo.setText(receivedIsbn);
            authorInfo.setText(receivedAuthor);
            descriptionInfo.setText(receivedDescription);
            priceInfo.setText(String.valueOf(receivedPrice));

        }
    };

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.add_book) {
                addBook();
            } else if (id == R.id.remove_last) {
                mBookViewModel.deleteLastBook();
//                bookArray.remove(bookArray.size() - 1);
//                adapter.notifyDataSetChanged();
            } else if (id == R.id.remove_all) {
//                bookArray.removeAll(bookArray);
                mBookViewModel.deleteAll();
                myRef.removeValue();
            } else if (id == R.id.list_all) {
                Intent i = new Intent(MainActivity.this, MainActivity2.class);
                MainActivity.this.startActivity(i);
            }
            drawer.closeDrawers();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void loadData() {
        SharedPreferences bookData = getSharedPreferences("book1", 0);
        String savedBookId = bookData.getString("bookId", "");
        String savedTitleName = bookData.getString("titleName", "");
        String savedIsbn = bookData.getString("isbn", "");
        String savedAuthor = bookData.getString("author", "");
        String savedDescription = bookData.getString("description", "");
        String savedPrice = bookData.getString("price", "");

        bookIdInfo.setText(savedBookId);
        titleNameInfo.setText(savedTitleName);
        isbnInfo.setText(savedIsbn);
        authorInfo.setText(savedAuthor);
        descriptionInfo.setText(savedDescription);
        priceInfo.setText(savedPrice);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_fields) {
            clearField();
        } else if (id == R.id.load_data) {
            loadData();
        } else if (id == R.id.total_books) {
            int totalBooksNum = mBookViewModel.getTotalCount();
            Toast.makeText(getApplicationContext(), "The total number of books: " + totalBooksNum, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.delete_unknown_author_book) {
            mBookViewModel.deleteUnknownAuthorBook();
            Toast.makeText(this, "Unknown Author Books were deleted", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            String randomString = RandomString.generateNewRandomString(7);
            isbnInfo.setText(randomString);
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            clearField();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            if (distanceX > 0) {
                double price = Double.parseDouble(priceInfo.getText().toString());
                price -= (int) distanceX;
                priceInfo.setText(Double.toString(price));
            } else if (distanceX < 0) {
                double price = Double.parseDouble(priceInfo.getText().toString());
                price -= (int) distanceX;
                priceInfo.setText(Double.toString(price));
            } else if (distanceY > 0) {
                titleNameInfo.setText("");
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            moveTaskToBack(true);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            loadData();
            super.onLongPress(e);
        }
    }

}

