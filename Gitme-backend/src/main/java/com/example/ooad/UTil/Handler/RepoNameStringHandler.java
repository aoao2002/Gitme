package com.example.ooad.UTil.Handler;

public class RepoNameStringHandler implements StringHandler {
    @Override
    public boolean isLegal(String repoName) {
        if (repoName == null || repoName.length() == 0) {
            return false;
        }
        return true;
    }
}
