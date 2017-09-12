package com.darren.rxjavademo.entity;

/**
 * Created by lenovo on 2017/9/13.
 */

public class UserInfo {
    UserBaseInfoResponse mBaseInfo;
    UserExtraInfoResponse mExtraInfo;

    public UserInfo(UserBaseInfoResponse mBaseInfo, UserExtraInfoResponse mExtraInfo) {
        this.mBaseInfo = mBaseInfo;
        this.mExtraInfo = mExtraInfo;
    }
}
