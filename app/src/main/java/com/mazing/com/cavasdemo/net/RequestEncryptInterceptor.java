package com.mazing.com.cavasdemo.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 17/6/27.
 * 将请求体加密并打包post的拦截器
 */

public class RequestEncryptInterceptor implements Interceptor {

    private static final String FORM_NAME = "content";
    private static final String CHARSET = "UTF-8";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();

        //判断是否为请求体
        if(requestBody instanceof FormBody){
            FormBody fromBody = (FormBody) requestBody;
            Map<String,String> fromMap = new Hashtable<>();
            // 从 formBody 中拿到请求参数，放入 formMap 中
            for(int i=0; i< fromBody.size();i++){
                fromMap.put(fromBody.name(i),fromBody.value(i));
            }

            // 将 formMap 转化为 json 然后 AES 加密
            Gson gson = new Gson();
            String jsonParams = gson.toJson(fromMap);
//            String encryptParams = AESCryptUtils.encrypt(jsonParams.getBytes(CHARSET), AppConstant.getAESKey());
            requestBody = new FormBody.Builder()
                    .add(FORM_NAME,jsonParams)
                    .build();

            if(requestBody != null){
                request =  request.newBuilder().post(requestBody).build();
            }
        }

        return chain.proceed(request);
    }
}
