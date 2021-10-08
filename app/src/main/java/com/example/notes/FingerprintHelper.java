package com.example.notes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.notes.activities.MainActivity;
import com.example.notes.activities.PasswordActivity;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHelper extends FingerprintManager.AuthenticationCallback {

    private final Context context;

    public FingerprintHelper(Context context1) {
        context = context1;
    }

    public void startAuthentication(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        // CancellationSignal используется, когда приложение не может обрабатывать вводимые
        // пользователем данные. Если он не используется, другие приложения (включая экран
        // блокировки) не смогут получить доступ к сенсорному датчику

        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();

        Toast.makeText(context, context.getString(R.string.toast_authentication_failed),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);

        Toast.makeText(context, context.getString(R.string.toast_authentication_error) + "\n" +
                errString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);

        Toast.makeText(context, context.getString(R.string.toast_authentication_help) + "\n" +
                helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        ((PasswordActivity) context).finish();

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("check", 1);
        context.startActivity(intent);
    }
}