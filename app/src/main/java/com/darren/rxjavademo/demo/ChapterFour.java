package com.darren.rxjavademo.demo;

import android.content.Context;
import android.util.Log;

import com.darren.rxjavademo.entity.UserBaseInfoResponse;
import com.darren.rxjavademo.entity.UserExtraInfoResponse;
import com.darren.rxjavademo.entity.UserInfo;
import com.darren.rxjavademo.inteface.Api;
import com.darren.rxjavademo.retrofit.RetrofitCreate;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.darren.rxjavademo.MainActivity.TAG;

/**
 * Created by lenovo on 2017/9/14.
 */

public class ChapterFour {
    public static void demo1() {
        Observable observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "emitter 1");
                e.onNext(1);
                Thread.sleep(1000);
                Log.d(TAG, "emitter 2");
                e.onNext(2);
                Thread.sleep(1000);
                Log.d(TAG, "emitter 3");
                e.onNext(3);
                Thread.sleep(1000);
                Log.d(TAG, "emitter 4");
                e.onNext(4);
                //RxJava的一个坑
//                Thread.sleep(1000);
                Log.d(TAG, "onComplete1");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.d(TAG, "emitter A");
                e.onNext("A");
                Thread.sleep(1000);
                Log.d(TAG, "emitter B");
                e.onNext("B");
                Thread.sleep(1000);
                Log.d(TAG, "emitter C");
                e.onNext("C");
                Thread.sleep(1000);
                Log.d(TAG, "onComplete2");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {

            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
    }

    public static void practice(final Context context) {
        Api api = RetrofitCreate.get().create(Api.class);
        Observable<UserBaseInfoResponse> observable1
                = api.getUserBaseInfo(new UserBaseInfoResponse()).subscribeOn(Schedulers.io());
        Observable<UserExtraInfoResponse> observable2
                = api.getUserExtrInfo(new UserExtraInfoResponse()).subscribeOn(Schedulers.io());
        Observable.zip(observable1, observable2, new BiFunction<UserBaseInfoResponse, UserExtraInfoResponse, UserInfo>() {
            //zip的作用：合并两个Observable
            @Override
            public UserInfo apply(UserBaseInfoResponse userBaseInfoResponse, UserExtraInfoResponse userExtraInfoResponse) throws Exception {
                return new UserInfo(userBaseInfoResponse, userExtraInfoResponse);
            }
        }).observeOn(AndroidSchedulers.mainThread())//切换到主线程操作处理返回数据
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo userInfo) throws Exception {
                        //做一些操作
                    }
                });
    }
}
