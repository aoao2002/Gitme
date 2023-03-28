package com.example.ooad.service.User;

import cn.dev33.satoken.stp.SaTokenInfo;

public class LoginInfo {


    String tokenName;
    String tokenValue;
    boolean isLogin;
    Object loginId;
    String loginType;
    long tokenTimeout;
    long sessionTimeout;
    long tokenSessionTimeout;
    long tokenActivityTimeout;
    String loginDevice;
    String tag;

    String userName;


    public LoginInfo(SaTokenInfo tokenInfo, String userName) {
        this.tokenName = tokenInfo.tokenName;
        this.tokenValue = tokenInfo.tokenValue;
        this.isLogin = tokenInfo.isLogin;
        this.loginId = tokenInfo.loginId;
        this.loginType = tokenInfo.loginType;
        this.tokenTimeout = tokenInfo.tokenTimeout;
        this.sessionTimeout = tokenInfo.sessionTimeout;
        this.tokenSessionTimeout = tokenInfo.tokenSessionTimeout;
        this.tokenActivityTimeout = tokenInfo.tokenActivityTimeout;
        this.loginDevice = tokenInfo.loginDevice;
        this.tag = tokenInfo.tag;
        this.userName = userName;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public Object getLoginId() {
        return loginId;
    }

    public void setLoginId(Object loginId) {
        this.loginId = loginId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public long getTokenTimeout() {
        return tokenTimeout;
    }

    public void setTokenTimeout(long tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public long getTokenSessionTimeout() {
        return tokenSessionTimeout;
    }

    public void setTokenSessionTimeout(long tokenSessionTimeout) {
        this.tokenSessionTimeout = tokenSessionTimeout;
    }

    public long getTokenActivityTimeout() {
        return tokenActivityTimeout;
    }

    public void setTokenActivityTimeout(long tokenActivityTimeout) {
        this.tokenActivityTimeout = tokenActivityTimeout;
    }

    public String getLoginDevice() {
        return loginDevice;
    }

    public void setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
