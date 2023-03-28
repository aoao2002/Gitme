package com.example.ooad.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.ooad.UTil.Handler.ReturnHelper;
import com.example.ooad.service.ForkRS.ForkRSInfo;
import com.example.ooad.service.ForkRS.ForkRSService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/fork/")

public class forkCtrl {

    private static final Logger logger = LoggerFactory.getLogger(forkCtrl.class);
    @Autowired
    private ForkRSService forkRSService;
//github上可以更改名字
    @RequestMapping(value = "edit/fork", method= RequestMethod.POST)
    public SaResult fork(String repoID, String rename, String desc) throws IOException, GitAPIException {
        // TODO: 为当前登录的用户fork, rename 为null则为原名
        return ReturnHelper.returnBool(forkRSService.fork(repoID, rename, desc));
    }

//    通过输入被fork的仓库ID来unfork（github好像没有这个功能），删库的话需要在fork里检查一下也删去；
//    这里好像默认了只能fork一个，不太合理
    @RequestMapping(value = "edit/unFork", method = RequestMethod.DELETE)
    public SaResult unFork(String repoID) throws Exception {
        return ReturnHelper.returnBool(forkRSService.unFork(repoID));
    }

    @RequestMapping(value = "view/getForkByFromRepoID", method= RequestMethod.GET)
    public SaResult getForkByFromRepoID(String repoID){
        // TODO: 返回fork过这个repo的其他所有的repo的id --> List<String>
        return ReturnHelper.returnObj(forkRSService.getForkByFromRepoID(repoID)) ;
    }

    @RequestMapping(value = "view/getForkByToRepoID", method= RequestMethod.GET)
    public SaResult getForkByToRepoID(String repoID){
        // TODO: 返回这个repo是通过fork哪个repo得到的
        return ReturnHelper.returnObj(forkRSService.getForkByToRepoID(repoID)) ;
    }

    @RequestMapping(value = "view/getAllForks", method = RequestMethod.GET)
    public SaResult getAllForks(){
        return ReturnHelper.returnObj(forkRSService.getAllForks());
    }


}
