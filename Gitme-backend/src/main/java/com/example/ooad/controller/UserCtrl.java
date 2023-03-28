package com.example.ooad.controller;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.UTil.Handler.ReturnHelper;
import com.example.ooad.service.User.EmailAndPwd;
import com.example.ooad.service.User.RegisterInfo;
import com.example.ooad.service.User.UserInfo;
import com.example.ooad.service.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserCtrl {
    private static final Logger logger = LoggerFactory.getLogger(UserCtrl.class);


    @Autowired
    private UserService userService;

    @RequestMapping(value ="doLoginEmail", method= RequestMethod.POST)
    @ResponseBody
    public SaResult doLoginEmail( @RequestBody EmailAndPwd emailAndPwd){
        return userService.LoginMail(emailAndPwd.getEmail(), emailAndPwd.getPwd());
    }

    @RequestMapping(value ="register", method= RequestMethod.POST)
    public SaResult register(@RequestBody RegisterInfo registerInfo){
        logger.info("register");
        return userService.addUser(registerInfo.getName(), registerInfo.getEmail(), registerInfo.getPwd());
    }


    @RequestMapping(value ="isLogin", method= RequestMethod.GET)
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    @RequestMapping(value ="logout", method= RequestMethod.POST)
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("注销成功");
    }

    /**
     * get info
     */

    @RequestMapping(value ="getUserInfo", method= RequestMethod.GET)
    public SaResult getUserInfo(String mail, String name){
        logger.info("getUserInfo "+mail+" "+name);
        UserInfo result = null;
        if(InputChecker.checkNullAndEmpty(mail))
            result = userService.getUserByMail(mail);
        else if(InputChecker.checkNullAndEmpty(name))
            result = userService.getUserByName(name);

        if (result == null) {
            return SaResult.error("no such user");
        }
        return SaResult.ok().setData(result);
    }

    @RequestMapping(value ="getAllUser", method= RequestMethod.GET)
    public SaResult getAllUser(){
        return ReturnHelper.returnObj(userService.getAllUser());

    }

    /**
     * edit info
     */

    @RequestMapping(value ="editMyInfo", method= RequestMethod.POST)
    public SaResult editMyInfo(String bio, String phoneNumber, String sex){
        return ReturnHelper.returnBool(userService.editMyInfo(new UserInfo(bio, phoneNumber, sex)));

    }

    @RequestMapping(value ="editMyPw", method= RequestMethod.POST)
    public SaResult editMyPw(@RequestBody String pw){
        return ReturnHelper.returnBool(userService.editMyPw(pw));

    }


    /**
     * invitation
     */
    @RequestMapping(value ="edit/invite", method= RequestMethod.POST)
    public SaResult invite(String userName, String repoID){
        return ReturnHelper.returnBool(userService.invite(userName, repoID));
    }
    @RequestMapping(value ="edit/unInvite", method= RequestMethod.POST)
    public SaResult unInvite(String userName, String repoID){
        return ReturnHelper.returnBool(userService.unInvite(userName, repoID));
    }

    @RequestMapping(value ="getAllCol", method= RequestMethod.GET)
    public SaResult getAllCol(String repoID){
        return ReturnHelper.returnObj(userService.getAllCollaByRepoID(repoID));
    }








}
