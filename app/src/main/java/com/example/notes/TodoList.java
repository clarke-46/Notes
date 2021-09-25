package com.example.notes;

import java.io.Serializable;

public class TodoList implements Serializable {

    String todo;
    public boolean isCancelled;

    public TodoList(String todo, boolean isCancelled) {
        this.todo = todo;
        this.isCancelled = isCancelled;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}