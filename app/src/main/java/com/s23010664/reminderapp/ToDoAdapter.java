package com.s23010664.reminderapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoItem> todoList;
    private Context context;

    public ToDoAdapter(List<ToDoItem> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoItem todoItem = todoList.get(position);
        holder.title.setText(todoItem.getTitle());
        holder.dateTime.setText(todoItem.getDate() + " - " + todoItem.getTime());
        holder.location.setText(todoItem.getLocation());

        // Handle item click to edit
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditActivity.class);
            intent.putExtra("TODO_ID", todoItem.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, dateTime, location;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.textViewTitle);
            dateTime = view.findViewById(R.id.textViewDateTime);
            location = view.findViewById(R.id.textViewLocation);
        }
    }

    public ToDoItem getItem(int position) {
        return todoList.get(position);
    }

    public void removeItem(int position) {
        todoList.remove(position);
        notifyItemRemoved(position);
    }
}