package com.example.notes.activities;

import static com.example.notes.activities.SettingActivity.FINGERPRINT_CHECKED;
import static com.example.notes.activities.SettingActivity.FINGERPRINT_KEY;
import static com.example.notes.activities.SettingActivity.HAVING_PASSWORD_KEY;
import static com.example.notes.activities.SettingActivity.IS_CHECKED;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.notes.App;
import com.example.notes.FingerprintHelper;
import com.example.notes.R;
import com.example.notes.Themes;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView pinFirstImage;
    private ImageView pinSecondImage;
    private ImageView pinThirdImage;
    private ImageView pinFourthImage;

    private String password = "";
    public boolean firstTime;
    boolean passwordOn;
    boolean fingerprintOn;

    private KeyStore keyStore;
    private Cipher cipher;

    private static final String KEY_NAME = "keyName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Themes.applyTheme(this);
        setContentView(R.layout.activity_password);

        init();
        onOffPassword();
    }

    public void init() {
        firstTime = App.getKeystore().notHasPincode();

        if (firstTime) {
            Intent intent = new Intent(PasswordActivity.this, NewPasswordActivity.class);
            startActivity(intent);
        } else {
            App.getKeystore().hasPincode();
        }

        initButtons();
    }

    public void onOffPassword() {
        SharedPreferences havingPasswordPreferences = getSharedPreferences(HAVING_PASSWORD_KEY,
                MODE_PRIVATE);
        passwordOn = havingPasswordPreferences.getBoolean(IS_CHECKED, false);

        if (passwordOn) {
            init();
            fingerprintAuthentication();
        } else {
            Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void initButtons() {
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                if (password.length() < 4) {
                    password += "1";
                    changePinImageColor();
                }
                break;
            case R.id.button2:
                if (password.length() < 4) {
                    password += "2";
                    changePinImageColor();
                }
                break;
            case R.id.button3:
                if (password.length() < 4) {
                    password += "3";
                    changePinImageColor();
                }
                break;
            case R.id.button4:
                if (password.length() < 4) {
                    password += "4";
                    changePinImageColor();
                }
                break;
            case R.id.button5:
                if (password.length() < 4) {
                    password += "5";
                    changePinImageColor();
                }
                break;
            case R.id.button6:
                if (password.length() < 4) {
                    password += "6";
                    changePinImageColor();
                }
                break;
            case R.id.button7:
                if (password.length() < 4) {
                    password += "7";
                    changePinImageColor();
                }
                break;
            case R.id.button8:
                if (password.length() < 4) {
                    password += "8";
                    changePinImageColor();
                }
                break;
            case R.id.button9:
                if (password.length() < 4) {
                    password += "9";
                    changePinImageColor();
                }
                break;
            case R.id.button0:
                if (password.length() < 4) {
                    password += "0";
                    changePinImageColor();
                }
                break;
            case R.id.buttonClear:
                if (password != null && password.length() > 0) {
                    password = password.substring(0, password.length() - 1);
                    changePinImageColor();
                }
                break;
        }

        checkPassword();
    }

    private void changePinImageColor() {
        pinFirstImage = findViewById(R.id.pinFirstImage);
        pinSecondImage = findViewById(R.id.pinSecondImage);
        pinThirdImage = findViewById(R.id.pinThirdImage);
        pinFourthImage = findViewById(R.id.pinFourthImage);

        switch (password.length()) {
            case 0:
                pinFirstImage.setImageResource(R.drawable.ic_circle_grey);
                break;
            case 1:
                pinFirstImage.setImageResource(R.drawable.ic_circle_orange);
                pinSecondImage.setImageResource(R.drawable.ic_circle_grey);
                break;
            case 2:
                pinFirstImage.setImageResource(R.drawable.ic_circle_orange);
                pinSecondImage.setImageResource(R.drawable.ic_circle_orange);
                pinThirdImage.setImageResource(R.drawable.ic_circle_grey);
                break;
            case 3:
                pinFirstImage.setImageResource(R.drawable.ic_circle_orange);
                pinSecondImage.setImageResource(R.drawable.ic_circle_orange);
                pinThirdImage.setImageResource(R.drawable.ic_circle_orange);
                pinFourthImage.setImageResource(R.drawable.ic_circle_grey);
                break;
            case 4:
                pinFirstImage.setImageResource(R.drawable.ic_circle_orange);
                pinSecondImage.setImageResource(R.drawable.ic_circle_orange);
                pinThirdImage.setImageResource(R.drawable.ic_circle_orange);
                pinFourthImage.setImageResource(R.drawable.ic_circle_orange);
                break;
        }
    }

    private void checkPassword() {
        if (password.length() == 4) {
            if (App.getKeystore().checkPincode(password)) {
                Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                password = "";
                pinFirstImage.setImageResource(R.drawable.ic_circle_grey);
                pinSecondImage.setImageResource(R.drawable.ic_circle_grey);
                pinThirdImage.setImageResource(R.drawable.ic_circle_grey);
                pinFourthImage.setImageResource(R.drawable.ic_circle_grey);
                Toast.makeText(PasswordActivity.this, R.string.toastPasswordIncorrect,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fingerprintAuthentication() {
        SharedPreferences fingerprintPreferences = getSharedPreferences(FINGERPRINT_KEY, MODE_PRIVATE);
        fingerprintOn =
                Objects.equals(fingerprintPreferences.getString(FINGERPRINT_CHECKED, null), "enable");

        if (fingerprintOn) {
            // аутентификация по отпечатку пальца возможна только с Android 6.0 (M)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                FingerprintManager fingerprintManager =
                        (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                // проверка наличия датчика отпечатка пальца на устройстве
                if (!fingerprintManager.isHardwareDetected()) {
                    Toast.makeText(PasswordActivity.this, R.string.toast_authentication_not_supported,
                            Toast.LENGTH_LONG).show();
                }
                // проверка предоставления пользователем разрешения приложению
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(PasswordActivity.this, R.string.toast_authentication_permission,
                            Toast.LENGTH_LONG).show();
                }
                // проверка настроек экрана блокировки на устройстве
                if (!keyguardManager.isKeyguardSecure()) {
                    Toast.makeText(PasswordActivity.this, R.string.toast_authentication_settings,
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        generateKey();
                    } catch (FingerprintException e) {
                        e.printStackTrace();
                    }

                    if (initCipher()) {
                        FingerprintManager.CryptoObject cryptoObject =
                                new FingerprintManager.CryptoObject(cipher);
                        FingerprintHelper helper = new FingerprintHelper(this);
                        helper.startAuthentication(fingerprintManager, cryptoObject);
                    }
                }
            }
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() throws FingerprintException {
        try {
            // "AndroidKeyStore" - стандартное название хранилища ключей
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
            keyStore.load(null);

            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException
                | InvalidAlgorithmParameterException | CertificateException | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private static class FingerprintException extends Exception {
        FingerprintException(Exception e) {
            super(e);
        }
    }
}