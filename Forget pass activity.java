package com.example.smartstudent;

import android.content.Intent;
import android.os.Bundle;


import android.content.SharedPreferences;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edtUsername, edtVerificationCode;
    Button btnSubmit, btnVerify;
    SharedPreferences sp;
    String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtUsername = findViewById(R.id.edtUsername);
        edtVerificationCode = findViewById(R.id.edtVerificationCode);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnVerify = findViewById(R.id.btnVerify);

        sp = getSharedPreferences("user_data", MODE_PRIVATE);

        btnSubmit.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(this, "نام کاربری را وارد کنید", Toast.LENGTH_SHORT).show();
                return;
            }


            if (username.equals(sp.getString("username", ""))) {

                verificationCode = generateRandomCode();

                sendNotification(verificationCode);
                Toast.makeText(this, "کد تایید به ایمیل شما ارسال شد", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "کاربر یافت نشد", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerify.setOnClickListener(v -> {
            String enteredCode = edtVerificationCode.getText().toString().trim();

            if (enteredCode.equals(verificationCode)) {

                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "کد وارد شده اشتباه است", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String generateRandomCode() {
        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000);
        return String.valueOf(randomCode);
    }


    private void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "motivation_channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("فراموشی رمز عبور")
                .setContentText("کد تایید شما: " + message)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}
