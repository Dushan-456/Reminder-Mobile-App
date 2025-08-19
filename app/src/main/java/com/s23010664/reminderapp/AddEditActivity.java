package com.s23010664.reminderapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDate, editTextTime, editTextLocation;
    private Button buttonSave;
    private DatabaseHelper db;
    private ToDoItem currentToDoItem;
    private int toDoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        db = new DatabaseHelper(this);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonSave = findViewById(R.id.buttonSave);

        // Check if we are editing an existing item
        toDoId = getIntent().getIntExtra("TODO_ID", -1);

        if (toDoId != -1) {
            setTitle("Edit To-Do");
            loadToDoItem();
        } else {
            setTitle("Add To-Do");
        }

        // Date Picker
        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        // Time Picker
        editTextTime.setOnClickListener(v -> showTimePickerDialog());

        buttonSave.setOnClickListener(v -> saveToDo());
    }

    private void loadToDoItem() {
        // This is a simplified way. A better way would be a getTodoById method in DatabaseHelper
        for(ToDoItem item : db.getAllTodos()){
            if(item.getId() == toDoId){
                currentToDoItem = item;
                break;
            }
        }

        if (currentToDoItem != null) {
            editTextTitle.setText(currentToDoItem.getTitle());
            editTextDate.setText(currentToDoItem.getDate());
            editTextTime.setText(currentToDoItem.getTime());
            editTextLocation.setText(currentToDoItem.getLocation());
        }
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                    editTextDate.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
                    editTextTime.setText(formattedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void saveToDo() {
        String title = editTextTitle.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill out Title, Date and Time", Toast.LENGTH_SHORT).show();
            return;
        }

        ToDoItem todo = new ToDoItem();
        todo.setTitle(title);
        todo.setDate(date);
        todo.setTime(time);
        todo.setLocation(location);

        int alarmId;
        if (toDoId != -1) {
            // Updating existing item
            todo.setId(toDoId);
            db.updateTodo(todo);
            alarmId = toDoId; // Use the existing ID
        } else {
            // Adding new item and getting its new ID
            long newId = db.addTodo(todo);
            alarmId = (int) newId; // Use the new ID returned by the database
        }

// Set the alarm with the correct ID
        AlarmHelper.setAlarm(this, alarmId, title, date, time);

        finish(); // Go back to MainActivity
    }
}