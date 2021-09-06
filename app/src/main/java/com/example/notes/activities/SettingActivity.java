package com.example.notes.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.notes.R;
import com.example.notes.Themes;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {

    private TextView changePasswordText;
    private TextView darkModeText;
    private TextView themeText;
    private SwitchCompat requestPasswordSwitch;

    private int selectedModeIndex = 0;
    private int selectedThemeIndex = 0;

    public static final String HAVING_PASSWORD_KEY = "thereIsPassword";
    public static final String IS_CHECKED = "isChecked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Themes.applyTheme(this);
        setContentView(R.layout.activity_setting);

        init();
        havingPassword();
        darkLightMode();
        changeTheme();
    }

    private void init() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        changePasswordText = findViewById(R.id.changePasswordText);

        changePasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, NewPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void havingPassword() {
        requestPasswordSwitch = findViewById(R.id.requestPasswordSwitch);

        SharedPreferences havingPasswordPreferences = getSharedPreferences(HAVING_PASSWORD_KEY,
                MODE_PRIVATE);

        requestPasswordSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // при изменении состояния switch новое значение
            // будет сразу записано в соответствующее "поле" в SharedPreferences
            SharedPreferences.Editor editor = havingPasswordPreferences.edit();
            editor.putBoolean(IS_CHECKED, isChecked);
            editor.apply();
        });

        requestPasswordSwitch.setChecked(havingPasswordPreferences.getBoolean(IS_CHECKED, false));
    }

    private void darkLightMode() {
        darkModeText = findViewById(R.id.darkModeText);

        String[] modes = {getResources().getString(R.string.followSystem),
                getResources().getString(R.string.on), getResources().getString(R.string.off)};

        darkModeText.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.darkMode)
                    .setSingleChoiceItems(modes, -1, (dialog, item) -> {
                        switch (modes[item]) {
                            case "Follow system":
                            case "Системный":
                                selectedModeIndex = 1;
                                break;
                            case "On":
                            case "Вкл.":
                                selectedModeIndex = 2;
                                break;
                            case "Off":
                            case "Выкл.":
                                selectedModeIndex = 3;
                                break;
                        }
                        Toast.makeText(SettingActivity.this,
                                getResources().getString(R.string.darkMode) + ": " +
                                        modes[item], Toast.LENGTH_SHORT).show();
                    })
                    .setPositiveButton(R.string.ok, (dialog, i) -> {
                        Themes.applyDarkMode(getApplicationContext(), selectedModeIndex);
                        recreate();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void changeTheme() {
        themeText = findViewById(R.id.colorThemeText);

        String[] themes = {getResources().getString(R.string.maroonColor),
                getResources().getString(R.string.emeraldColor),
                getResources().getString(R.string.lightGreenColor),
                getResources().getString(R.string.blackColor)};

        themeText.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.themeSelection)
                    .setSingleChoiceItems(themes, -1, (dialog, item) -> {
                        switch (themes[item]) {
                            case "Maroon":
                            case "Бордовая":
                                selectedThemeIndex = 1;
                                break;
                            case "Emerald":
                            case "Изумрудная":
                                selectedThemeIndex = 2;
                                break;
                            case "Light green":
                            case "Светло-зелёная":
                                selectedThemeIndex = 4;
                                break;
                            case "Black":
                            case "Чёрная":
                                selectedThemeIndex = 3;
                                break;
                        }
                        Toast.makeText(SettingActivity.this,
                                getResources().getString(R.string.toastThemeSelected) + " " +
                                        themes[item], Toast.LENGTH_SHORT).show();
                    })
                    .setPositiveButton(R.string.ok, (dialog, id) -> {
                        Themes.applyColorTheme(getApplicationContext(), selectedThemeIndex);
                        recreate();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void backHome() {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            backHome();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backHome();
    }
}