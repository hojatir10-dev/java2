.package com.example.smartstudent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.IntentFilter;


import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int NOTIFICATION_PERMISSION_CODE = 102;
    private static final String CHANNEL_ID = "motivation_channel";

    TextView tvWelcome, tvApiResult;
    Button btnCamera, btnApi, btnExitApp,btndollar;
    ImageView imgProfile;

    private RequestQueue requestQueue;





    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvApiResult = findViewById(R.id.tvApiResult);
        btnCamera = findViewById(R.id.btnCamera);
        btnApi = findViewById(R.id.btnApi);
        btnExitApp = findViewById(R.id.btnExitApp);
        imgProfile = findViewById(R.id.imgProfile);

        requestQueue = Volley.newRequestQueue(this);
        btndollar = findViewById(R.id.btndollar);
        btndollar.setOnClickListener(v -> fetchRandomFact());



        sp = getSharedPreferences("user_data", MODE_PRIVATE);
        tvWelcome.setText("خوش آمدی " + sp.getString("username", ""));

        createNotificationChannel();


        loadBottomFragment(new PdfFragment());


        btnCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_CODE
                );
            } else {
                openCamera();
            }
        });


        btnApi.setOnClickListener(v -> showMotivationalQuote());



        btnExitApp.setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });



    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_edit_username) {
            changeUsername();
        } else if (id == R.id.menu_edit_password) {
            changePassword();
        } else if (id == R.id.menu_delete_account) {
            deleteAccount();
        } else if (id == R.id.menu_logout_account) {

            sp.edit().putBoolean("isLoggedIn", false).apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return true;
    }


    private void changeUsername() {
        android.widget.EditText edt = new android.widget.EditText(this);
        edt.setText(sp.getString("username", ""));
        edt.setHint("نام کاربری جدید");

        new AlertDialog.Builder(this)
                .setTitle("ویرایش نام کاربری")
                .setView(edt)
                .setPositiveButton("ذخیره", (d, w) -> {
                    String name = edt.getText().toString().trim();
                    if (!name.isEmpty()) {
                        sp.edit().putString("username", name).apply();
                        tvWelcome.setText("خوش آمدید " + name);
                    }
                })
                .setNegativeButton("لغو", null)
                .show();
    }

    private void changePassword() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        android.widget.EditText oldP = new android.widget.EditText(this);
        oldP.setHint("رمز فعلی");
        oldP.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        android.widget.EditText newP = new android.widget.EditText(this);
        newP.setHint("رمز جدید");
        newP.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        android.widget.EditText repP = new android.widget.EditText(this);
        repP.setHint("تکرار رمز");
        repP.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        layout.addView(oldP);
        layout.addView(newP);
        layout.addView(repP);

        new AlertDialog.Builder(this)
                .setTitle("تغییر رمز عبور")
                .setView(layout)
                .setPositiveButton("ذخیره", (d, w) -> {
                    if (!oldP.getText().toString().equals(sp.getString("password", ""))) {
                        Toast.makeText(this, "رمز فعلی اشتباه است", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newP.getText().toString().equals(repP.getText().toString())) {
                        Toast.makeText(this, "رمزها یکسان نیستند", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sp.edit().putString("password", newP.getText().toString()).apply();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("لغو", null)
                .show();
    }

    private void deleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("حذف حساب")
                .setMessage("آیا مطمئن هستید؟")
                .setPositiveButton("بله", (d, w) -> {
                    sp.edit().clear().apply();
                    startActivity(new Intent(this, RegisterActivity.class));
                    finish();
                })
                .setNegativeButton("خیر", null)
                .show();
    }


    private void openCamera() {
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                CAMERA_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int code, int res, @Nullable Intent data) {
        super.onActivityResult(code, res, data);
        if (code == CAMERA_REQUEST_CODE && res == RESULT_OK && data != null) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            imgProfile.setImageBitmap(bmp);
            vibrate(100);
        }
    }

    private void vibrate(int ms) {
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v != null) v.vibrate(ms);
    }
    private NetworkReceiver networkReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Motivation",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    private void sendNotification(String text) {
        PendingIntent pi = PendingIntent.getActivity(
                this, 0, new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("جمله انگیزشی")
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setAutoCancel(true)
                .setContentIntent(pi);

        getSystemService(NotificationManager.class).notify(1, builder.build());
        vibrate(150);
    }


    private void showMotivationalQuote() {
        String[] quotes = {
                "هر روز یک قدم به جلو بردار",
                "موفقیت نتیجه پشتکار است",
                "به خودت ایمان داشته باش",
                "شروع، مهم‌تر از کامل بودن است"
        };

        String text = quotes[new Random().nextInt(quotes.length)];
        tvApiResult.setText(text);
        sendNotification(text);
    }

    private void fetchRandomFact() {
        String url = "https://uselessfacts.jsph.pl/random.json?language=en";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.has("text")) {
                            String fact = response.getString("text");
                            tvApiResult.setText("📌 Fact:\n" + fact);
                        } else {
                            tvApiResult.setText("پاسخ معتبر نیست");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        tvApiResult.setText("خطا در پردازش داده");
                    }
                },
                error -> {
                    error.printStackTrace();
                    tvApiResult.setText("اینترنت در دسترس نیست");
                }
        );

        Volley.newRequestQueue(this).add(request);
    }


    private void loadBottomFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottomFragment, fragment)
                .commit();
    }
                                  }
