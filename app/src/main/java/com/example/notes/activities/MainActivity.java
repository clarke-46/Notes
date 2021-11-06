package com.example.notes.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.adapters.NoteAdapter;
import com.example.notes.NoteViewModel;
import com.example.notes.R;
import com.example.notes.Themes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerView list;
    private FloatingActionButton fab;
    private LinearLayout searchLayout;
    private EditText inputSearch;

    private NoteAdapter adapter;
    public static NoteViewModel viewModel;

    private final boolean[] changeSearchVisibility = {true};
    private int sortIndex = 1;   // default
    private int selectedSortIndex = 0;

    public static final String SORTING_PREFERENCES = "sorting_preferences";
    public static final String SORTING_KEY = "sort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Themes.applyTheme(this);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        list = findViewById(R.id.list);
        fab = findViewById(R.id.fab);
        searchLayout = findViewById(R.id.searchLayout);
        inputSearch = findViewById(R.id.inputSearch);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddingNewNoteActivity.class);
            startActivity(intent);
        });

        adapter = new NoteAdapter(this);
        list.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        SharedPreferences sortingPreferences = getSharedPreferences(SORTING_PREFERENCES, MODE_PRIVATE);
        if (sortingPreferences != null) {
            sortIndex = sortingPreferences.getInt(SORTING_KEY, 1);
        }

        applySortingNotes(MainActivity.this, sortIndex);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.searchNotes(editable.toString());
            }
        });
    }

    public void applySortingNotes(Context context, int sortIndex) {
        SharedPreferences preferences = context.getSharedPreferences(SORTING_PREFERENCES,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SORTING_KEY, sortIndex);
        editor.apply();

        switch (sortIndex) {
            case 1:
                viewModel.getListNotes().observe(this, notes -> adapter.setNotes(notes));
                break;
            case 2:
                viewModel.getListNotesSortUpdate().observe(this, notes ->
                        adapter.setNotes(notes));
                break;
            case 3:
                viewModel.getListNotesSortAlphabetically().observe(this, notes ->
                        adapter.setNotes(notes));
                break;
            default:
                break;
        }
    }

    public void changeSorting() {
        String[] sorting = {getString(R.string.by_default), getString(R.string.by_update),
                getString(R.string.alphabetically)};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.sorting_notes)
                .setSingleChoiceItems(sorting, -1, (dialog, item) -> {
                    switch (sorting[item]) {
                        case "по умолчанию":
                        case "by default":
                            selectedSortIndex = 1;
                            applySortingNotes(getApplicationContext(), selectedSortIndex);
                            dialog.cancel();
                            break;
                        case "по дате изменения":
                        case "by update date":
                            selectedSortIndex = 2;
                            applySortingNotes(getApplicationContext(), selectedSortIndex);
                            dialog.cancel();
                            break;
                        case "по алфавиту":
                        case "alphabetically":
                            selectedSortIndex = 3;
                            applySortingNotes(getApplicationContext(), selectedSortIndex);
                            dialog.cancel();
                            break;
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.toast_notes_sorted) +
                            " " + sorting[item], Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_search) {
            if (changeSearchVisibility[0]) {
                searchLayout.setVisibility(View.VISIBLE);
                changeSearchVisibility[0] = false;
            } else {
                searchLayout.setVisibility(View.GONE);
                changeSearchVisibility[0] = true;
            }
            return true;
        }
        if (id == R.id.action_sort) {
            changeSorting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}