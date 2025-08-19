package com.s23010664.reminderapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todoManager";
    private static final String TABLE_TODOS = "todos";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_LOCATION = "location";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_LOCATION + " TEXT" + ")";
        db.execSQL(CREATE_TODOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);
    }

    // Add new ToDo
    void addTodo(ToDoItem todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, todo.getTitle());
        values.put(KEY_DATE, todo.getDate());
        values.put(KEY_TIME, todo.getTime());
        values.put(KEY_LOCATION, todo.getLocation());

        db.insert(TABLE_TODOS, null, values);
        db.close();
    }

    // Get all ToDos
    public List<ToDoItem> getAllTodos() {
        List<ToDoItem> todoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TODOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ToDoItem todo = new ToDoItem();
                todo.setId(cursor.getInt(0));
                todo.setTitle(cursor.getString(1));
                todo.setDate(cursor.getString(2));
                todo.setTime(cursor.getString(3));
                todo.setLocation(cursor.getString(4));
                todoList.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return todoList;
    }

    // Update a ToDo
    public int updateTodo(ToDoItem todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, todo.getTitle());
        values.put(KEY_DATE, todo.getDate());
        values.put(KEY_TIME, todo.getTime());
        values.put(KEY_LOCATION, todo.getLocation());

        return db.update(TABLE_TODOS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
    }

    // Delete a ToDo
    public void deleteTodo(ToDoItem todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOS, KEY_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
        db.close();
    }
}