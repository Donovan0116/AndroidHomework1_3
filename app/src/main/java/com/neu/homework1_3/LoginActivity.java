package com.neu.homework1_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.neu.homework1_3.Retrofit.UserBeanService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EditText et_ID = findViewById(R.id.et_ID);
        EditText et_password = findViewById(R.id.et_password);

        Intent intentMain = new Intent(this, MainActivity.class);

        Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = et_ID.getText().toString();
                String password = et_password.getText().toString();

                SharedPreferences sp = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ID", ID);
                editor.putString("password", password);
                editor.apply();

                String BASE_URL = "http://10.0.2.2:8443/";
                Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();

                UserBeanService api = retrofit.create(UserBeanService.class);

                api.login(ID, password).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            if(response.body() != null){
                                String currentJson = response.body().string();
                                System.out.println(currentJson);
                                User user = new Gson().fromJson(currentJson,User.class);
                                startActivity(intentMain);
                            }else{
                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        System.out.println("fail");
                    }
                });

            }
        });



    }
}