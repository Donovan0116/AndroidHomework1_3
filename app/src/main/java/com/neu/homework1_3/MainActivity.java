package com.neu.homework1_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.neu.homework1_3.Retrofit.UserBeanService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intentLogin = new Intent(this, LoginActivity.class);

        String BASE_URL = "http://10.0.2.2:8443/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();

        UserBeanService api = retrofit.create(UserBeanService.class);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String ID = sp.getString("ID", "");
        String password = sp.getString("password", "");

        TextView userName =findViewById(R.id.userName);
        TextView mainName = findViewById(R.id.mainName);
        TextView mainID = findViewById(R.id.mainID);
        TextView mainAge = findViewById(R.id.mainAge);
        TextView mainGender = findViewById(R.id.mainGender);

        if(ID.equals("") || password.equals("")){
            Toast.makeText(MainActivity.this, "登录信息过期，请重新登录", Toast.LENGTH_SHORT).show();
            System.out.println("=========================================");
            startActivity(intentLogin);
        }else {

            api.login(ID, password).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        if(response.body() != null){
                            String currentJson = response.body().string();
                            System.out.println(currentJson);
                            User user = new Gson().fromJson(currentJson,User.class);
                            userName.setText(user.getName());
                            mainName.setText(user.getName());
                            mainID.setText(user.getId());
                            mainAge.setText(String.valueOf(user.getAge()));
                            mainGender.setText(user.getGender());
                        }else{
                            Toast.makeText(MainActivity.this, "登录信息过期，请重新登录", Toast.LENGTH_SHORT).show();
                            startActivity(intentLogin);
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




    }
}