package com.example.notes;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static ArrayList<TodoList> fromString(String value) {
        Type listType = new TypeToken<ArrayList<TodoList>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<TodoList> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}