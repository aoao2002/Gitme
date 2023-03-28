package com.example.ooad.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.UTil.Handler.ReturnHelper;
import com.example.ooad.bean.PRRS;
import com.example.ooad.service.ForkRS.ForkRSInfo;
import com.example.ooad.service.ForkRS.ForkRSService;
import com.example.ooad.service.PRRS.PRRSInfo;
import com.example.ooad.service.PRRS.PRRSService;
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
@RequestMapping("/PR/")

public class PRCtrl {
    /**
     * 1. fork到本地
     * 2. 新建分支 想修改master分支，新建一个分支，clone所有
     * 3. 修改后会commit
     * 4. 提出pr时，检查被fork仓库的目标分支的最新commit，与本地最新commit是否一致，若不一致则merge该分支到本地
     * 5. diff为本地master分支与new_master分支的差异
     * 6. 若原repo主同意，便为其新建一个new_master分支。之后是否合并自己决定
     */

    private static final Logger logger = LoggerFactory.getLogger(PRCtrl.class);
    @Autowired
    private PRRSService prrsService;

    @RequestMapping(value = "view/makePR", method = RequestMethod.POST)
    public SaResult makePR(String  repoID, String title, String toBranch, String fromBranch){
        //判断是否是fork过来的
        //找到fork对象的repo作为被PR者，修改pr关系表
        return ReturnHelper.returnObj(prrsService.makePR(repoID, title, toBranch, fromBranch));
    }

    @RequestMapping(value = "deletePR", method = RequestMethod.DELETE)
    public SaResult deletePR(String forkRepoId, String originRepoId){
        return ReturnHelper.returnBool(prrsService.deletePR(forkRepoId, originRepoId));
    }

    /**
     *PR显示某个branch最新commit和fork时分支的差别，且在接受前随时更新（）
     * 接受PR之后merge两个branch
     */
    @RequestMapping(value = "view/getAllPR", method = RequestMethod.GET)
    public SaResult getAllPR(String  repoID){
        // 返回这个repo的所有PR信息
        // 每一个对象包含发起的repoID，对应的commitID
        return ReturnHelper.returnObj(prrsService.getAllPR(repoID));
    }
    @RequestMapping(value = "view/getPRByID", method = RequestMethod.GET)
    public SaResult getPRByID(String PRID){
        return ReturnHelper.returnObj(prrsService.getPRByID(PRID));
    }

    @RequestMapping(value = "edit/acceptPR", method = RequestMethod.POST)
    public SaResult acceptPR(String  PRID){
        // 先判断两者的fork关系是否成立
        // 把pr发起者的最新的commit，merge给接受pr的repo的特定branch
        if(prrsService.acceptPR(PRID)){
            return SaResult.ok("accept PR success");
        }else {
            return SaResult.error("Maybe merge conflict,please check the new PR branch if it exists");
        }
    }

    @RequestMapping(value = "edit/deleteAllPR", method = RequestMethod.DELETE)
    public void deleteAllPR(){
        prrsService.deleteAllPR();
    }

    @RequestMapping(value = "getALLPR_test", method = RequestMethod.GET)
    public SaResult getALLPR_test(){
        return ReturnHelper.returnObj(prrsService.getALLPR_test());
    }

    @RequestMapping(value = "edit/rejectPR", method = RequestMethod.POST)
    public SaResult rejectPR(String PRRSId){
        if(!InputChecker.checkNullAndEmpty(PRRSId))
            return ReturnHelper.returnBool(false);
        // 先判断两者的fork关系是否成立
        // 把pr发起者的最新的commit，merge给接受pr的repo的特定branch
        return ReturnHelper.returnBool(prrsService.rejectPR(Long.parseLong(PRRSId)));
    }

}
