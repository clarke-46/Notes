package com.example.notes.activities;

import static com.example.notes.activities.MainActivity.viewModel;
import static com.example.notes.adapters.NoteAdapter.EXTRA_COLOR;
import static com.example.notes.adapters.NoteAdapter.EXTRA_DEADLINE;
import static com.example.notes.adapters.NoteAdapter.EXTRA_DEADLINE_CHECKBOX;
import static com.example.notes.adapters.NoteAdapter.EXTRA_ID;
import static com.example.notes.adapters.NoteAdapter.EXTRA_SUBTITLE;
import static com.example.notes.adapters.NoteAdapter.EXTRA_TITLE;
import static com.example.notes.adapters.NoteAdapter.EXTRA_TODO_LIST;
import static com.example.notes.adapters.NoteAdapter.savingUpdate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Note;
import com.example.notes.R;
import com.example.notes.Themes;
import com.example.notes.TodoList;
import com.example.notes.adapters.TodoAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddingNewNoteActivity extends AppCompatActivity implements TodoAdapter.ItemClicked {

    private EditText titleAdding;
    private EditText subtitleAdding;
    private CheckBox deadlineCheckBox;
    private EditText deadlineEditText;
    private ImageButton deadlineCalendar;
    private RelativeLayout deadlineLayout;
    private RecyclerView todoListAdding;
    private ImageView addTodo;
    private LinearLayout addItemTodo;
    private ScrollView scrollable;
    private View colorsLayout;
    private ImageView imageColor1, imageColor2, imageColor3, imageColor4, imageColor5, imageColor6,
            imageColor7, imageColor8;

    private RecyclerView.LayoutManager layoutManager;

    private final Calendar calendar = Calendar.getInstance();
    private long deadlineDate;
    private final boolean[] changeDeadlineVisibility = {true};
    public int idUpdate;
    private ArrayList<TodoList> todoList;
    private TodoAdapter todoAdapter;
    private final boolean[] changeColorsVisibility = {true};
    private int selectedColor;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Themes.applyTheme(this);
        setContentView(R.layout.activity_adding_new_note);

        init();
        initColors();
        updateNote();
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void init() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        titleAdding = findViewById(R.id.titleAdding);
        subtitleAdding = findViewById(R.id.subtitleAdding);
        deadlineCheckBox = findViewById(R.id.deadlineCheckBox);
        deadlineEditText = findViewById(R.id.deadlineEditText);
        deadlineCalendar = findViewById(R.id.deadlineCalendar);
        deadlineLayout = findViewById(R.id.deadlineLayout);
        todoListAdding = findViewById(R.id.todoListAdding);
        addTodo = findViewById(R.id.addTodo);
        addItemTodo = findViewById(R.id.addItemTodo);

        TodoAdapter.f = 0;

        deadlineEditText.setEnabled(false);
        deadlineCalendar.setEnabled(false);

        deadlineCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (deadlineCheckBox.isChecked()) {
                setInitialDateTime();
                deadlineEditText.setEnabled(true);
                deadlineCalendar.setEnabled(true);
            } else {
                deadlineEditText.setText("");
                deadlineDate = 0;
                deadlineEditText.setEnabled(false);
                deadlineCalendar.setEnabled(false);
            }
        });

        todoListAdding.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        todoListAdding.setLayoutManager(layoutManager);
        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(this, todoList);
        todoListAdding.setAdapter(todoAdapter);

        if (todoList.size() > 0) {
            addTodo.setEnabled(false);
        }

        addTodo.setOnClickListener(v -> {
            todoList.add(new TodoList(null, false));
            TodoAdapter.f = 1;
            todoAdapter.notifyDataSetChanged();
            addItemTodo.setVisibility(View.VISIBLE);
            addTodo.setEnabled(false);
        });

        if (todoList.isEmpty()) {
            addItemTodo.setVisibility(View.GONE);
        }

        addItemTodo.setOnClickListener(v -> {
            todoList.add(new TodoList(null, false));
            TodoAdapter.f = 1;
            todoAdapter.notifyDataSetChanged();
            addTodo.setEnabled(false);
        });
    }

    public void setDate(View v) {
        new DatePickerDialog(AddingNewNoteActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener dateSetListener = ((view, year, month, dayOfMonth) -> {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTime();
    });

    public void setTime(View v) {
        new TimePickerDialog(AddingNewNoteActivity.this, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                true).show();
        setDate(v);
    }

    TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        setInitialDateTime();
    };

    private void setInitialDateTime() {
        deadlineEditText.setText(DateUtils.formatDateTime(this, calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE |
                        DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME |
                        DateUtils.FORMAT_24HOUR));
        deadlineDate = calendar.getTimeInMillis();
    }

    @SuppressLint("ResourceAsColor")
    private void initColors() {
        scrollable = findViewById(R.id.scrollable);
        colorsLayout = findViewById(R.id.colorsLayout);
        imageColor1 = findViewById(R.id.imageColor1);
        imageColor2 = findViewById(R.id.imageColor2);
        imageColor3 = findViewById(R.id.imageColor3);
        imageColor4 = findViewById(R.id.imageColor4);
        imageColor5 = findViewById(R.id.imageColor5);
        imageColor6 = findViewById(R.id.imageColor6);
        imageColor7 = findViewById(R.id.imageColor7);
        imageColor8 = findViewById(R.id.imageColor8);

        imageColor1.setOnClickListener(view -> setTransparentColor());

        imageColor2.setOnClickListener(view -> setPinkColor());

        imageColor3.setOnClickListener(view -> setDeepOrangeColor());

        imageColor4.setOnClickListener(view -> setYellowColor());

        imageColor5.setOnClickListener(view -> setGreenColor());

        imageColor6.setOnClickListener(view -> setSkyBlueColor());

        imageColor7.setOnClickListener(view -> setBlueColor());

        imageColor8.setOnClickListener(view -> setVioletColor());
    }

    private void setTransparentColor() {
        selectedColor = R.color.transparent;
        imageColor1.setImageResource(R.drawable.ic_baseline_check_24);
        imageColor2.setImageResource(0);
        imageColor3.setImageResource(0);
        imageColor4.setImageResource(0);
        imageColor5.setImageResource(0);
        imageColor6.setImageResource(0);
        imageColor7.setImageResource(0);
        imageColor8.setImageResource(0);
        setColor();
    }

    private void setPinkColor() {
        selectedColor = R.color.pinkPale;
        imageColor1.setImageResource(0);
        imageColor2.setImageResource(R.drawable.ic_baseline_check_24);
        imageColor3.setImageResource(0);
        imageColor4.setImageResource(0);
        imageColor5.setImageResource(0);
        imageColor6.setImageResource(0);
        imageColor7.setImageResource(0);
        imageColor8.setImageResource(0);
        setColor();
    }

    private void setDeepOrangeColor() {
        selectedColor = R.color.deepOrangePale;
        imageColor1.setImageResource(0);
        imageColor2.setImageResource(0);
        imageColor3.setImageResource(R.drawable.ic_baseline_check_24);
        imageColor4.setImageResource(0);
        imageColor5.setImageResource(0);
        imageColor6.setImageResource(0);
        imageColor7.setImageResource(0);
        imageColor8.setImageResource(0);
        setColor();
    }

    private void setYellowColor() {
        selectedColor = R.color.yellowPale;
        imageColor1.setImageResource(0);
        imageColor2.setImageResource(0);
        imageColor3.setImageResource(0);
        imageColor4.setImageResource(R.drawable.ic_baseline_check_24);
        imageColor5.setImageResource(0);
        imageColor6.setImageResource(0);
        imageColor7.setImageResource(0);
        imageColor8.setImageResource(0);
        setColor();
    }

    private void setGreenColor() {
        selectedColor = R.color.greenPale;
        imageColor1.setImageResource(0);
        imageColor2.setImageResource(0);
        imageColor3.setImageResource(0);
        imageColor4.setImageResource(0);
        imageColor5.setImageResource(R.drawable.ic_baseline_check_24);
        imageColor6.setImageResource(0);
        imageColor7.setImageResource(0);
        imageColor8.setImageResource(0);
        setColor();
    }

    private void setSkyBlueColor() {
        selectedColor = R.color.skyBluePale;
        imageColor1.setImageResource(0);
        imageColor2.setImageResource(0);
        imageColor3.setImageResource(0);
        imageColor4.setImageResource(0);
        imageColor5.setImageResource(0);
        imageColor6.setImageResource(R.drawable.ic_baseline_check_24);
        imageColor7.setImageResource(0);
        imageColor8.setImageResource(0);
        setColor();
    }

    private void setBlueColor() {
        selectedColor = R.color.bluePale;
        imageColor1.setImageResource(0);
        imageColor2.setImageResource(0);
        imageColor3.setImageResource(0);
        imageColor4.setImageResource(0);
        imageColor5.setImageResource(0);
        imageColor6.setImageResource(0);
        imageColor7.setImageResource(R.drawable.ic_baseline_check_24);
        imageColor8.setImageResource(0);
        setColor();
    }

    private void setVioletColor() {
        selectedColor = R.color.violetPale;
        imageColor1.setImageResource(0);
        imageColor2.setImageResource(0);
        imageColor3.setImageResource(0);
        imageColor4.setImageResource(0);
        imageColor5.setImageResource(0);
        imageColor6.setImageResource(0);
        imageColor7.setImageResource(0);
        imageColor8.setImageResource(R.drawable.ic_baseline_check_24);
        setColor();
    }

    private void setColor() {
        scrollable.setBackgroundColor(ContextCompat.getColor(this, selectedColor));
        colorsLayout.setVisibility(View.GONE);
        changeColorsVisibility[0] = true;
    }

    private void saveNote() {
        Note note = new Note(titleAdding.getText().toString(), subtitleAdding.getText().toString(),
                deadlineCheckBox.isChecked(), deadlineDate);
        long update = System.currentTimeMillis();
        note.setUpdate(update);
        note.setTodoLists(todoList);
        note.setColor(selectedColor);

        if (savingUpdate) {
            note.setId(idUpdate);
            viewModel.update(note);
            savingUpdate = false;
        } else {
            viewModel.insert(note);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void updateNote() {
        if (savingUpdate) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.editingNote);
            idUpdate = (int) Objects.requireNonNull(getIntent().getSerializableExtra(EXTRA_ID));
            String title =
                    Objects.requireNonNull(getIntent().getSerializableExtra(EXTRA_TITLE)).toString();
            String subtitle =
                    Objects.requireNonNull(getIntent().getSerializableExtra(EXTRA_SUBTITLE)).toString();
            boolean checkbox =
                    (boolean) Objects.requireNonNull(getIntent().getSerializableExtra(EXTRA_DEADLINE_CHECKBOX));
            long deadline =
                    (long) Objects.requireNonNull(getIntent().getSerializableExtra(EXTRA_DEADLINE));
            ArrayList<TodoList> listTodo =
                    (ArrayList<TodoList>) Objects.requireNonNull(getIntent().getSerializableExtra(EXTRA_TODO_LIST));
            int color = (int) Objects.requireNonNull(getIntent().getSerializableExtra(EXTRA_COLOR));

            titleAdding.setText(title);
            subtitleAdding.setText(subtitle);
            deadlineCheckBox.setChecked(checkbox);
            if (checkbox) {
                deadlineEditText.setText(DateUtils.formatDateTime(this, deadline,
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE |
                                DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME |
                                DateUtils.FORMAT_24HOUR));
                deadlineDate = deadline;
            }

            todoList.addAll(listTodo);
            if (listTodo.size() > 0) {
                addItemTodo.setVisibility(View.VISIBLE);
                addTodo.setEnabled(false);
            }

            switch (color) {
                case R.color.transparent:
                    setTransparentColor();
                    break;
                case R.color.pinkPale:
                    setPinkColor();
                    break;
                case R.color.deepOrangePale:
                    setDeepOrangeColor();
                    break;
                case R.color.yellowPale:
                    setYellowColor();
                    break;
                case R.color.greenPale:
                    setGreenColor();
                    break;
                case R.color.skyBluePale:
                    setSkyBlueColor();
                    break;
                case R.color.bluePale:
                    setBlueColor();
                    break;
                case R.color.violetPale:
                    setVioletColor();
                    break;
            }
            selectedColor = color;
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void shareNote() {
        final String mailTitle = titleAdding.getText().toString();
        final String mailSubtitle = subtitleAdding.getText().toString();
        final String mailDeadline = deadlineEditText.getText().toString();

        StringBuilder stringBuilder = new StringBuilder();
        if (!mailTitle.equals("")) {
            stringBuilder.append(mailTitle).append("\n\n");
        }
        if (todoList.size() > 0) {
            for (int i = 0; i < todoList.size();  i++) {
                String item = todoList.get(i).getTodo();
                if (todoList.get(i).isCancelled) {
                    stringBuilder.append("[+]  ");
                } else {
                    stringBuilder.append("[ ]  ");
                }
                stringBuilder.append(item).append("\n");
            }
            stringBuilder.append("\n");
        }
        if (!mailSubtitle.equals("")) {
            stringBuilder.append(mailSubtitle).append("\n\n");
        }
        if (!mailDeadline.equals("")) {
            stringBuilder.append(getString(R.string.deadline)).append(": ").append(mailDeadline);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, getString(R.string.toastNoApp), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent chosenIntent = Intent.createChooser(intent, getString(R.string.toastChooseApp));
        startActivity(chosenIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if (titleAdding.getText().length() > 0 | subtitleAdding.getText().length() > 0) {
                saveNote();
                Toast.makeText(AddingNewNoteActivity.this, R.string.toastNoteIsSaved,
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddingNewNoteActivity.this, R.string.toastNoteIsEmpty,
                        Toast.LENGTH_LONG).show();
            }
            finish();
            return true;
        }
        if (id == R.id.action_share) {
            shareNote();
            return true;
        }
        if (id == R.id.action_deadline) {
            if (changeDeadlineVisibility[0]) {
                deadlineLayout.setVisibility(View.VISIBLE);
                changeDeadlineVisibility[0] = false;
            } else {
                deadlineLayout.setVisibility(View.GONE);
                changeDeadlineVisibility[0] = true;
            }
            return true;
        }
        if (id == R.id.action_colors) {
            if (changeColorsVisibility[0]) {
                colorsLayout.setVisibility(View.VISIBLE);
                changeColorsVisibility[0] = false;
            } else {
                colorsLayout.setVisibility(View.GONE);
                changeColorsVisibility[0] = true;
            }
            return true;
        }
        if (id == android.R.id.home) {
            savingUpdate = false;
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        savingUpdate = false;
        finish();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCheckedClick(int index) {
        boolean cancelled = todoList.get(index).isCancelled;
        cancelled = !cancelled;
        todoList.get(index).setCancelled(cancelled);
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    public void itemText(int index, String text) {
        todoList.get(index).setTodo(text);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void clearItem(int index) {
        todoList.remove(index);
        TodoAdapter.f = 1;
        todoAdapter.notifyDataSetChanged();
        if (todoList.isEmpty()) {
            addItemTodo.setVisibility(View.GONE);
            addTodo.setEnabled(true);
        } else {
            addItemTodo.setVisibility(View.VISIBLE);
            addTodo.setEnabled(false);
        }
    }
}