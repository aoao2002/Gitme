package com.example.ooad.UTil.Handler;

import com.example.ooad.bean.Repo;
import com.example.ooad.dao.RepoDao;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InputChecker {


    public static boolean checkNullAndEmpty(List<String> inputs){
        for (String i:inputs) {
            if(i==null) return false;
            if(i.equals("")) return false;
            if(i.trim().equals("")) return false;
        }
        return true;
    }
    public static boolean checkNullAndEmpty(String i){
        if(i==null) return false;
        if(i.trim().equals("")) return false;
        return !i.equals("");
    }
    public static boolean checkNum(List<String> inputs){
        for (String i:
             inputs) {
            Pattern pattern = Pattern.compile("[0-9]+");
            Matcher matcher = pattern.matcher((CharSequence) i);
            boolean result = matcher.matches();
            if(!result){
                return false;
            }
        }
        return true;
    }
    public static boolean checkNum(String i){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher((CharSequence) i);
        return matcher.matches();
    }


}
