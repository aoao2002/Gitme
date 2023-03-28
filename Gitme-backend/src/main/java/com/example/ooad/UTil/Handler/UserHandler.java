package com.example.ooad.UTil.Handler;

import com.example.ooad.bean.User;

public class UserHandler implements StringHandler {
    @Override
    public boolean isLegal(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return true;
    }
    public boolean isLegal(User user) {
        if (user == null) {
            return false;
        }
        return true;
    }
}
