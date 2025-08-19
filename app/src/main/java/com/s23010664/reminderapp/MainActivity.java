package com.s23010664.reminderapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ToDoAdapter adapter;
    private List<ToDoItem> todoList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for Android 14's full-screen intent permission
        checkFullScreenIntentPermission();

        // Check for standard notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        db = new DatabaseHelper(this);
        todoList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivity(intent);
        });

        // Add swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void checkFullScreenIntentPermission() {
        // This check is for Android 14 (API 34) and higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // Check if the permission is granted. canScheduleExactAlarms is the new check.
            if (!alarmManager.canScheduleExactAlarms()) {
                // If not granted, show a dialog to the user explaining why we need it.
                new AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("To ensure alarms and reminders appear on your screen correctly, please enable the 'Alarms & reminders' permission for this app in the next screen.")
                        .setPositiveButton("Go to Settings", (dialog, which) -> {
                            // Open the settings screen for the user to grant the permission.
                            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadToDoItems();
    }

    private void loadToDoItems() {
        todoList = db.getAllTodos();
        adapter = new ToDoAdapter(todoList, this);
        recyclerView.setAdapter(adapter);
    }

    // Swipe to delete functionality
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            ToDoItem itemToDelete = adapter.getItem(position);

            // Delete from DB
            db.deleteTodo(itemToDelete);

            // Remove from list and update adapter
            adapter.removeItem(position);

            // Cancel alarm for the deleted item
            AlarmHelper.cancelAlarm(MainActivity.this, itemToDelete.getId());
        }
    };
}