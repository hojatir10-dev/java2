package com.example.smartstudent;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvRegisterHint, tvForgotPassword;
    SharedPreferences sp;

    private static final String CHANNEL_ID = "reset_pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterHint = findViewById(R.id.tvRegisterHint);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        sp = getSharedPreferences("user_data", MODE_PRIVATE);


        if (sp.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }


        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "همه فیلدها را پر کنید", Toast.LENGTH_SHORT).show();
                return;
            }

            String savedUser = sp.getString("username", null);
            String savedPass = sp.getString("password", null);

            if (savedUser == null || savedPass == null) {
                Toast.makeText(this, "کاربری یافت نشد", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.equals(savedUser) && password.equals(savedPass)) {
                sp.edit().putBoolean("isLoggedIn", true).apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this,
                        "نام کاربری یا رمز عبور اشتباه است",
                        Toast.LENGTH_SHORT).show();
            }
        });


        tvRegisterHint.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );


        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
    }


    private void resetPassword() {

        if (!sp.contains("username")) {
            Toast.makeText(this, "کاربری یافت نشد", Toast.LENGTH_SHORT).show();
            return;
        }

        String newPass = "P" + (1000 + new Random().nextInt(9000));
        sp.edit().putString("password", newPass).apply();

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Reset Password",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("رمز جدید شما")
                        .setContentText("رمز جدید: " + newPass)
                        .setAutoCancel(true);


        if (Build.VERSION.SDK_INT < 33 ||
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED) {

            manager.notify(10, builder.build());
        }

        Toast.makeText(this,
                "رمز جدید با نوتیفیکیشن ارسال شد",
                Toast.LENGTH_LONG).show();
    }
}
