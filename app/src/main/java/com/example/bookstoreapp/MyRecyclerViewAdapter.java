package com.example.bookstoreapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapp.provider.Book;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    List<Book> data = new ArrayList<>();
    public MyRecyclerViewAdapter() {}

    public void setData(List<Book> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bookId.setText("ID: " + data.get(position).getId());
        holder.title.setText("Title: " + data.get(position).getTitle());
        holder.isbn.setText("Author: " + data.get(position).getAuthor());
        holder.author.setText("ISBN: " + data.get(position).getIsbn());
        holder.description.setText("DESC: " + data.get(position).getDescription());
        holder.price.setText("Price: " + data.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView bookId;
        public TextView title;
        public TextView isbn;
        public TextView author;
        public TextView description;
        public TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            bookId = itemView.findViewById(R.id.bookIdCard);
            title = itemView.findViewById(R.id.titleCard);
            isbn = itemView.findViewById(R.id.isbnCard);
            author = itemView.findViewById(R.id.authorCard);
            description = itemView.findViewById(R.id.descriptionCard);
            price = itemView.findViewById(R.id.priceCard);
        }

    }
}
