package com.mazing.com.cavasdemo.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 17/6/27.
 * 请求头增肌token拦截器
 */

public class TokenHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = "";
        Request newRequest = originalRequest.newBuilder()
                .header("token",token)
                .build();

        return chain.proceed(newRequest);
    }
}
