package com.example.notes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String subtitle;

    @ColumnInfo(name = "deadline_checkbox")
    private boolean deadlineCheckbox;

    private long deadline;

    @ColumnInfo(name = "last_update")
    private long update;

    private ArrayList<TodoList> todoLists;

    @ColumnInfo(name = "background_color")
    private int color;

    public Note(String title, String subtitle, boolean deadlineCheckbox, long deadline) {
        this.title = title;
        this.subtitle = subtitle;
        this.deadlineCheckbox = deadlineCheckbox;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public boolean isDeadlineCheckbox() {
        return deadlineCheckbox;
    }

    public long getDeadline() {
        return deadline;
    }

    public long getUpdate() {
        return update;
    }

    public void setUpdate(long update) {
        this.update = update;
    }

    public ArrayList<TodoList> getTodoLists() {
        return todoLists;
    }

    public void setTodoLists(ArrayList<TodoList> todoLists) {
        this.todoLists = todoLists;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}