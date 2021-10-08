package com.example.notes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
    private SwitchCompat fingerprintSwitch;

    private int selectedModeIndex = 0;
    private int selectedThemeIndex = 0;

    public static final String HAVING_PASSWORD_KEY = "thereIsPassword";
    public static final String IS_CHECKED = "isChecked";
    public static final String FINGERPRINT_KEY = "fingerprint";
    public static final String FINGERPRINT_CHECKED = "fingerprintChecked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Themes.applyTheme(this);
        setContentView(R.layout.activity_setting);

        init();
        havingPassword();
        enableFingerprintAuthentication();
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

    private void enableFingerprintAuthentication() {
        fingerprintSwitch = findViewById(R.id.fingerprintSwitch);

        SharedPreferences fingerprintPreferences = getSharedPreferences(FINGERPRINT_KEY, MODE_PRIVATE);
        SharedPreferences.Editor fingerprintEditor = fingerprintPreferences.edit();

        fingerprintSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (fingerprintSwitch.isChecked()) {
                fingerprintEditor.putString(FINGERPRINT_CHECKED, "enable");
                fingerprintEditor.apply();
            } else {
                fingerprintEditor.putString(FINGERPRINT_CHECKED, "disable");
                fingerprintEditor.apply();
            }
        });

        fingerprintSwitch.setChecked(Objects.equals(fingerprintPreferences.getString(FINGERPRINT_CHECKED,
                null), "enable"));

        // аутентификация по отпечатку пальца возможна только с Android 6.0 (M)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager =
                    (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager != null) {
                if (!fingerprintManager.isHardwareDetected()) {
                    fingerprintSwitch.setVisibility(View.GONE);
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    fingerprintSwitch.setVisibility(View.GONE);
                }
            }
        } else {
            fingerprintSwitch.setVisibility(View.GONE);
        }
    }

    private void darkLightMode() {
        darkModeText = findViewById(R.id.darkModeText);

        String[] modes = {getString(R.string.followSystem), getString(R.string.on), getString(R.string.off)};

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
                        Toast.makeText(SettingActivity.this, getString(R.string.darkMode) +
                                ": " + modes[item], Toast.LENGTH_SHORT).show();
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

        String[] themes = {getString(R.string.maroonColor), getString(R.string.emeraldColor),
                getString(R.string.lightGreenColor), getString(R.string.blackColor)};

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
                        Toast.makeText(SettingActivity.this, getString(R.string.toastThemeSelected) +
                                " " + themes[item], Toast.LENGTH_SHORT).show();
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