package com.neu.homework1_3.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserBeanService {

    @GET("/login/{id}/{password}")
    Call<ResponseBody> login(@Path("id") String ID, @Path("password") String password);

}