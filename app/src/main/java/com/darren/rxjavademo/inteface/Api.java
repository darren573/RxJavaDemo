package com.darren.rxjavademo.inteface;

import com.darren.rxjavademo.entity.LoginRequest;
import com.darren.rxjavademo.entity.LoginResponse;
import com.darren.rxjavademo.entity.RegisterRequest;
import com.darren.rxjavademo.entity.RegisterResponse;
import com.darren.rxjavademo.entity.UserBaseInfoResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by lenovo on 2017/9/13.
 */

public interface Api {
    @GET
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET
    Observable<RegisterResponse> register(@Body RegisterRequest request);

    @GET
    Observable<UserBaseInfoResponse> getUserBaseInfo(@Body UserBaseInfoResponse request);

}
