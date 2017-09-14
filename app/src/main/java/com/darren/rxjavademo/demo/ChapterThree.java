package com.darren.rxjavademo.demo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.darren.rxjavademo.entity.LoginRequest;
import com.darren.rxjavademo.entity.LoginResponse;
import com.darren.rxjavademo.entity.RegisterRequest;
import com.darren.rxjavademo.entity.RegisterResponse;
import com.darren.rxjavademo.inteface.Api;
import com.darren.rxjavademo.retrofit.RetrofitCreate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.darren.rxjavademo.MainActivity.TAG;

/**
 * Created by lenovo on 2017/9/13.
 */

public class ChapterThree {
    public static void demo1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
            }
        }).map(new Function<Integer, String>() {//上游发送的Integer类型转换为String类型
            @Override
            public String apply(Integer integer) throws Exception {
                return "result is :"+integer;
            }
        }).subscribe(new Consumer<String>() {//Consumer只关注onNext事件
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }
    public static void demo2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            //将一个发送事件的上游Observable变换为多个发送事件的Observables，
            // 然后将它们发射的事件合并后放进一个单独的Observable里,flatMap并不保证事件的顺序.
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {//无序转换符
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }
    public static void demo3() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            //将一个发送事件的上游Observable变换为多个发送事件的Observables，
            // 然后将它们发射的事件合并后放进一个单独的Observable里,concatMap可以保证事件的顺序.
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {//有序转换符
                final List<String> list=new ArrayList<String>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG,s);
            }
        });
    }
    public static void practice(final Context context){
        //登录注册嵌套
        final Api api= RetrofitCreate.get().create(Api.class);
        api.register(new RegisterRequest())//发起注册请求
                .subscribeOn(Schedulers.io())//在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())//回到主线程去处理请求注册结果
                .doOnNext(new Consumer<RegisterResponse>() {

                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        //先根据注册的响应结果去做一些操作
                    }
                }).observeOn(Schedulers.io())
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
            @Override
            public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                return api.login(new LoginRequest());
            }
        }).observeOn(AndroidSchedulers.mainThread())//回到主线程去处理请求登录的结果
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
