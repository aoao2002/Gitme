package com.example.ooad.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/behavior/")
public class BehaviorCtrl {

    @RequestMapping(value ="starRepo", method= RequestMethod.POST)
    public boolean starRepo(long repoID){
        // TODO
        // 获取当前会话用户id, 如果未登录，则抛出异常：`NotLoginException`
        // StpUtil.getLoginId();
        // 在多对多关系表star中添加一行记录
        return false;

    }

    @RequestMapping(value ="undoStarRepo", method= RequestMethod.POST)
    public boolean undoStarRepo(long repoID){
        // TODO
        // please check if already star this repo
        return false;
    }
}
