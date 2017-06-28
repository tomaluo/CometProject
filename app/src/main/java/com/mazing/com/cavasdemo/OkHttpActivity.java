package com.mazing.com.cavasdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mazing.com.cavasdemo.hook.HookHelper;
import com.mazing.com.cavasdemo.model.City;
import com.mazing.com.cavasdemo.net.CometService;
import com.mazing.com.cavasdemo.net.RequestEncryptInterceptor;
import com.mazing.com.cavasdemo.net.TokenHeaderInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 17/6/27.
 */

public class OkHttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initData();
    }

    public void initData(){
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .addNetworkInterceptor(new RequestEncryptInterceptor())
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://www.baidu.com")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        List<City> city = new ArrayList<>();
//
//        CometService apiService = retrofit.create(CometService.class);
//        apiService.listRepos("user").enqueue(new Callback<List<City>>() {
//            @Override
//            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<List<City>> call, Throwable t) {
//
//            }
//        });

//        apiService.login("13523927564","1458").enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                 if(response.code() == RESULT_OK){
//
//                 }
//                 else{
//                     Log.i("RequestEncryptInterceptor","warn");
//                 }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.i("RequestEncryptInterceptor","error");
//            }
//        });

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("http://www.baidu.com"));

        // 注意这里使用的ApplicationContext 启动的Activity
        // 因为Activity对象的startActivity使用的并不是ContextImpl的mInstrumentation
        // 而是自己的mInstrumentation, 如果你需要这样, 可以自己Hook
        // 比较简单, 直接替换这个Activity的此字段即可.
        getApplicationContext().startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            // 在这里进行Hook
            HookHelper.HookActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

