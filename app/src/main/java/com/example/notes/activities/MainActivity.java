package com.example.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.NoteAdapter;
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
        viewModel.getListNotes().observe(this, notes -> adapter.setNotes(notes));

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
        return super.onOptionsItemSelected(item);
    }
}