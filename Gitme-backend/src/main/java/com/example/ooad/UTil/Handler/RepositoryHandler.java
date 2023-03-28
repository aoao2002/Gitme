package com.example.ooad.UTil.Handler;

import com.example.ooad.bean.Repo;

public class RepositoryHandler implements StringHandler {
    @Override
    public boolean isLegal(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return true;
    }
    public boolean isLegal(Repo repository) {
        if (repository == null) {
            return false;
        }
        return true;
    }
}
