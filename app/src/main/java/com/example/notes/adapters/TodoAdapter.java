package com.example.notes.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.TodoList;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    ItemClicked activity;
    Context context;
    public ArrayList<TodoList> todoList;
    public static int f = 0;

    public interface ItemClicked {
        void onCheckedClick(int index);
        void itemText(int index, String text);
        void clearItem(int index);
    }

    public TodoAdapter(Context context, ArrayList<TodoList> todoList) {
        this.context = context;
        this.todoList = todoList;
        activity = (ItemClicked) context;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_list,
                parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(todoList.get(position));
        String string = todoList.get(position).getTodo();
        holder.todoText.setText(string);

        boolean isCancelled = todoList.get(position).isCancelled;
        if (isCancelled) {
            holder.checkButton.setChecked(true);
            holder.checkButton.setButtonDrawable(R.drawable.check_ok);
            holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.checkButton.setChecked(false);
            holder.checkButton.setButtonDrawable(R.drawable.check);
            holder.todoText.setPaintFlags(0);
        }

        if (todoList.size() > 0 && position == (todoList.size() - 1) && f == 1) {
            holder.todoText.requestFocus(1);
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RadioButton checkButton;
        EditText todoText;
        ImageView clearButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkButton = itemView.findViewById(R.id.checkButton);
            todoText = itemView.findViewById(R.id.todoText);
            clearButton = itemView.findViewById(R.id.clearButton);

            checkButton.setOnClickListener(v -> {
                int index = todoList.indexOf(itemView.getTag());
                activity.onCheckedClick(index);
            });

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    todoList.get(getAdapterPosition()).setTodo(todoText.getText().toString());
                    int index = todoList.indexOf(itemView.getTag());
                    activity.itemText(index, todoText.getText().toString());
                }
            };

            todoText.addTextChangedListener(textWatcher);

            todoText.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    clearButton.setVisibility(View.VISIBLE);
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
                    }
                } else {
                    clearButton.setVisibility(View.GONE);
                }
            });

            clearButton.setOnClickListener(v -> {
                int index = todoList.indexOf(itemView.getTag());
                activity.clearItem(index);
            });
        }
    }
}