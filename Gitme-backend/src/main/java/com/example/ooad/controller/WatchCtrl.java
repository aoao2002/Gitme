package com.example.ooad.controller;


import cn.dev33.satoken.util.SaResult;
import com.example.ooad.UTil.Handler.ReturnHelper;
import com.example.ooad.service.watch.WatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 当pr获批后，
 */
@RestController
@RequestMapping("/watch/")
public class    WatchCtrl {

    private static final Logger logger = LoggerFactory.getLogger(WatchCtrl.class);

    @Autowired
    private WatchService watchService;

    @RequestMapping(value ="getWatchInfoByMe", method= RequestMethod.GET)
    public SaResult getWatchInfoByMe(){
        return ReturnHelper.returnObj(watchService.getWatchInfoByMe());
    }

    @RequestMapping(value ="getWatchByMe", method= RequestMethod.GET)
    public SaResult getWatchByMe(String repoID){
        // return if this repo has been watch by me
        //StpUtil.getLoginIdAsLong();      // 获取当前会话账号id, 并转化为`long`类型
        return ReturnHelper.returnBool(watchService.getWatchByMe(repoID));
    }

    @RequestMapping(value ="addWatch", method= RequestMethod.POST)
    public SaResult addWatch(String repoID){
        return ReturnHelper.returnBool(watchService.addWatch(repoID));
    }

    @RequestMapping(value ="deleWatch", method= RequestMethod.POST)
    public SaResult deleWatch(String repoID){
        return ReturnHelper.returnBool(watchService.deleWatch(repoID));
    }

//    @RequestMapping(value = "addWatchInfo", method = RequestMethod.POST)
//    public SaResult addWatchInfo(String RepoID, String UserID){
//        return ReturnHelper.returnBool(watchService.addWatchInfo("test", UserID, RepoID));
//    }
    @RequestMapping(value ="SetRead", method= RequestMethod.GET)
    public SaResult SetRead(String WatchInfoID){

        return ReturnHelper.returnBool(watchService.setRead(WatchInfoID));
    }

}
