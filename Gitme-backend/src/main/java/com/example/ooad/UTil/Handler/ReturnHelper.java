package com.example.ooad.UTil.Handler;


import cn.dev33.satoken.util.SaResult;
import org.springframework.stereotype.Service;

@Service
public class ReturnHelper {

    public static SaResult returnObj(Object obj){
        if(obj==null) return SaResult.error();
        return SaResult.ok().setData(obj);
    }

    public static SaResult returnBool(boolean bool){
        if(!bool) return SaResult.error().setData("Post fail");
        return SaResult.ok().setData("Post success");
    }
}
