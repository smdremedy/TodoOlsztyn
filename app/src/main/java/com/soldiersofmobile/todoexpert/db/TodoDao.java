package com.soldiersofmobile.todoexpert.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.soldiersofmobile.todoexpert.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoDao {

    /**
     * Nazwy kolumn w DB.
     */
    public static final String C_ID = "_id";
    public static final String C_CONTENT = "content";
    public static final String C_DONE = "done";
    public static final String C_USER_ID = "user_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_UPDATED_AT = "updated_at";

    /**
     * Nazwa tabeli, w której przechowywane będa obiekty
     */
    public static final String TABLE_NAME = "todos";

    private final DbHelper dbHelper;

    public TodoDao(DbHelper dbHelper) {

        this.dbHelper = dbHelper;
    }


    public void create(Todo todo) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(C_ID, todo.objectId);
        contentValues.put(C_CONTENT, todo.content);
        contentValues.put(C_DONE, todo.done);
        contentValues.put(C_USER_ID, todo.userId);

        database.insertWithOnConflict(TABLE_NAME, null, contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);

    }

    public List<Todo> getTodos(String userId) {
        Cursor cursor = getTodosCursor(userId);
        List<Todo> todos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Todo todo = new Todo();
            todo.userId = cursor.getString(cursor.getColumnIndex(C_USER_ID));
            todo.content = cursor.getString(cursor.getColumnIndex(C_CONTENT));
            todo.objectId = cursor.getString(cursor.getColumnIndex(C_ID));
            todo.done = cursor.getInt(cursor.getColumnIndex(C_DONE)) > 0;
            todos.add(todo);
        }
        return todos;
    }

    public Cursor getTodosCursor(String userId) {
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();

        return readableDatabase.query(TABLE_NAME, new String[]{C_CONTENT, C_DONE, C_ID, C_USER_ID},
                String.format("%s=?", C_USER_ID), new String[]{userId}, null, null,
                String.format("%s ASC", C_CONTENT));
    }

}
