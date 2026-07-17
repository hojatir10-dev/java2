package com.example.smartstudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtRepeat;
    Button btnRegister;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtRepeat   = findViewById(R.id.edtRepeat);
        btnRegister = findViewById(R.id.btnLogin);


        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtRepeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        sp = getSharedPreferences("user_data", MODE_PRIVATE);

        btnRegister.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String repeat = edtRepeat.getText().toString().trim();


            if(username.isEmpty() || password.isEmpty() || repeat.isEmpty()){
                Toast.makeText(this,"تمام فیلدها را پر کنید", Toast.LENGTH_SHORT).show();
                return;
            }


            if (username.equals(sp.getString("username", ""))) {
                Toast.makeText(this, "کاربر با این نام کاربری قبلاً ثبت‌نام کرده است", Toast.LENGTH_SHORT).show();
                return;
            }


            if(password.length() < 6){
                Toast.makeText(this,"رمز باید حداقل ۶ کاراکتر باشد", Toast.LENGTH_SHORT).show();
                return;
            }


            if(!password.matches(".*[0-9].*") || !password.matches(".*[A-Za-z].*")){
                Toast.makeText(this,"رمز باید شامل حداقل یک عدد و یک حرف باشد", Toast.LENGTH_SHORT).show();
                return;
            }


            if(!password.equals(repeat)){
                Toast.makeText(this,"رمزها یکسان نیستند", Toast.LENGTH_SHORT).show();
                return;
            }


            sp.edit()
                    .putString("username", username)
                    .putString("password", password)
                    .putBoolean("isLoggedIn", true)
                    .apply();

            Toast.makeText(this,"ثبت نام با موفقیت انجام شد", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
