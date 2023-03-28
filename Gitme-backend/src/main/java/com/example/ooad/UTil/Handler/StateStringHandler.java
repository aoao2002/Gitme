package com.example.ooad.UTil.Handler;

public class StateStringHandler implements StringHandler {
    @Override
    public boolean isLegal(String state) {
        if (state == null || state.length() == 0) {
            return false;
        }
        if (state.equals("PUBLIC") || state.equals("PRIVATE")) {
            return true;
        }
        return false;
    }
}
