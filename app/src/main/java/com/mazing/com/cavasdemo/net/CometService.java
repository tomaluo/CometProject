package com.mazing.com.cavasdemo.net;

import com.mazing.com.cavasdemo.model.City;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by user on 17/6/27.
 */

public interface CometService {
    @GET("users/{user}/getComet")
    Call<List<City>> listRepos(@Path("user") String user);

    @FormUrlEncoded
    @POST("/mobile/login.htm")
    Call<ResponseBody> login(@Field("mobile") String phoneNumber, @Field("smsCode") String smsCode);

}
