package com.example.ooad.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.io.resource.InputStreamResource;
import com.example.ooad.OoadApplication;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.UTil.Handler.ReturnHelper;
import com.example.ooad.bean.Repo;
import com.example.ooad.bean.RepoState;
import com.example.ooad.service.Repo.*;
import com.example.ooad.service.User.EmailAndPwd;
import com.example.ooad.service.User.UserInfo;
import com.example.ooad.service.User.UserService;
import com.google.common.collect.Lists;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/repo/")
public class RepoCtrl {
    @Autowired
    private RepoService repoService;

    private static final Logger logger = LoggerFactory.getLogger(RepoCtrl.class);


    /**
     *
     * 获得repo基本信息
     */



    @RequestMapping(value ="view/getRepoByRepoID", method= RequestMethod.GET)
    public SaResult getRepoByRepoID(String repoID){
        // TODO
        // maybe other data will be added into the repo class
        // please use  repoInfo class
        if(!InputChecker.checkNullAndEmpty(repoID))
            return ReturnHelper.returnObj(null);
        return ReturnHelper.returnObj(repoService.getRepoByRepoID(Long.parseLong(repoID)));
    }

    @RequestMapping(value = "getRepoIDByCreator_NameAndRepo_Name", method = RequestMethod.GET)
    public SaResult getRepoIDByCreator_NameAndRepo_Name(String cName, String rName){
        return ReturnHelper.returnObj(repoService.getRepoIDByCreator_NameAndRepo_Name(cName, rName));
    }

    @RequestMapping(value ="getRepoByMyself", method= RequestMethod.GET)
    public SaResult getRepoByMyself(){
        // TODO: return the information of all the repo that the user has created
        //
        return ReturnHelper.returnObj(repoService.getRepoByMyself());
    }
    @RequestMapping(value = "getRepoByMyStar", method = RequestMethod.GET)
    public SaResult getRepoByMyStar(){
        System.out.println("0\n");
        return ReturnHelper.returnObj(repoService.getRepoByMyStar());
    }

    @RequestMapping(value ="getRepoByUser", method= RequestMethod.GET)
    public SaResult getRepoByUserID(String UserId){
        if(!InputChecker.checkNullAndEmpty(UserId))
            return ReturnHelper.returnObj(null);
        //返回该用户创建的所有public的repo
        return ReturnHelper.returnObj(repoService.getRepoByUserID(Long.parseLong(UserId)));
    }

    @RequestMapping(value ="getRepoByUserName", method= RequestMethod.GET)
    public SaResult getRepoByUserName(String userName){
        //返回该用户创建的所有public的repo
        return ReturnHelper.returnObj(repoService.getRepoByUserName(userName));
    }


    @RequestMapping(value ="permission/getRepoSateByName", method= RequestMethod.GET)
    public SaResult getRepoSateByName(String repoName, String creatorEmail){
//        return SaResult.ok();
        return repoService.getRepoSateByName(repoName, creatorEmail);
    }

    @RequestMapping(value ="getAllPublicRepo", method= RequestMethod.GET)
    public SaResult getAllPublicRepo(){

        return ReturnHelper.returnObj(repoService.getAllRepo());
    }

    @RequestMapping(value = "view/ls", method= RequestMethod.GET)
    public SaResult ls(String repoID ,String dir, String commitID){
        if (!InputChecker.checkNullAndEmpty(
                Lists.newArrayList(repoID,commitID)))
            return SaResult.error("repoID or commitID is empty or null");

        return ReturnHelper.returnObj(repoService.ls(repoID, dir, commitID));
    }

    @RequestMapping(value = "view/getFileByPath", method= RequestMethod.GET)
    public SaResult getFileByPath(String repoID,String filePath, String branchName){
        FileContent fileContent = null;
        boolean isText = repoService.isText(filePath);
        if(!isText) return ReturnHelper.returnObj(new FileContent(false));

        if (InputChecker.checkNullAndEmpty(branchName)) {
            fileContent = repoService.getContent(repoID, filePath, branchName);
        }else {
            fileContent = repoService.getContent(repoID, filePath);
        }
        if (fileContent==null) return SaResult.error("input error");
        fileContent.setText(repoService.isText(filePath));
        logger.info("get the file of "+filePath);
        return SaResult.ok().setData(fileContent);
    }

    @RequestMapping(value = "permission/isEditable", method= RequestMethod.POST)
    public SaResult isEditable(String repoName,String creatorEmail,@RequestBody EmailAndPwd emailAndPwd){
        return ReturnHelper.returnBool(repoService.isEditable(repoName, creatorEmail, emailAndPwd.getEmail(), emailAndPwd.getPwd()));
    }


    /**
     * 操作repo
     */

    @RequestMapping(value = "iniRepo", method= RequestMethod.POST)
    public SaResult iniRepo(String repoName, String state, String desc){
        //TODO init a repo and change below
        return ReturnHelper.returnBool(repoService.iniRepo(repoName, state, desc));
    }

    @RequestMapping(value = "edit/updateDescriById", method = RequestMethod.POST)
    public SaResult updateDescriById(String descri, String repoID){
        return ReturnHelper.returnBool(repoService.updateDescriById(descri, repoID));
    }

    @RequestMapping(value = "edit/deleteRepo", method= RequestMethod.POST)
    public SaResult deleteRepo(String repoID){
        if(!InputChecker.checkNullAndEmpty(repoID))
            return ReturnHelper.returnBool(false);
        //TODO
        if(!InputChecker.checkNullAndEmpty(repoID))
            return ReturnHelper.returnBool(false);
        return ReturnHelper.returnBool(repoService.deleteRepo(Long.parseLong(repoID)));
    }

    @RequestMapping(value = "downloadRepo", method= RequestMethod.GET)
    public void downloadRepo(HttpServletResponse response,String repoID,String branch,String tokenValue){

        Object userID =  StpUtil.getLoginIdByToken(tokenValue);
        if(userID==null) {
           logger.error("downloadRepo with wrong token: do not login");
            return;
        }

        if(!InputChecker.checkNullAndEmpty(repoID)) return;
        if(repoService.downLoadAsZip(response, Long.parseLong(repoID), branch)){
            System.out.println("download success");
        }else {
            System.out.println("download failed");
        }
    }
    @RequestMapping(value = "edit/upFile", method= RequestMethod.POST)
    public SaResult upFile(String repoID, String branchName, String commitMsg,
                          MultipartFile uploadFile, String path){
        //在对应path下，add一个文件，如果文件存在就替换掉
        return ReturnHelper.returnBool(repoService.upFile(repoID, branchName, commitMsg, uploadFile, path));
    }

    @RequestMapping(value = "edit/deleFile", method= RequestMethod.POST)
    public SaResult deleFile(String repoID, String branchName, String commitMsg,String path){
        //在对应path下，delete一个文件
//        return ReturnHelper.returnObj(null);

        return ReturnHelper.returnBool(repoService.deleFile(repoID, branchName, commitMsg, path));
    }


    /**
     *  版本控制相关
     *  https://blog.csdn.net/qq_44299928/article/details/109577937
     *  getAllBranch 还不确定对于远端commit和本地commit有什么差别，目前代码应该是显示所有（这点没测试，主要没有远端commit）
     *  其次是还没有确定哪个是当前所在分支
     */


    @RequestMapping(value = "view/getAllBranchByRepoID", method= RequestMethod.GET)
    public SaResult getAllBranchByRepoID(String repoID) {
        return ReturnHelper.returnObj(repoService.getAllBranch(repoID));

    }

    /*
    https://gist.github.com/jasonrudolph/1810768 返回最新commit
     */
    @RequestMapping(value = "view/getAllVersionByRepoID", method= RequestMethod.GET)
    public SaResult getAllVersionByRepoID(String repoID, String branchName){
            return ReturnHelper.returnObj(repoService.getAllVersion(repoID, branchName));
    }
    @RequestMapping(value = "getVersionByRepoID", method = RequestMethod.GET)
    public SaResult getVersionByRepoID(String repoID, String commitID){
        return ReturnHelper.returnObj(repoService.getVersion(repoID, commitID));
    }

    @RequestMapping(value = "edit/addBranch", method= RequestMethod.POST)
    public SaResult addBranch(String repoID, String branchName){

        return ReturnHelper.returnBool(repoService.addBranch(repoID, branchName));
    }

    @RequestMapping(value = "edit/mergeBranch", method = RequestMethod.POST)
    public SaResult mergeBranch(String repoID, String branchNameFrom, String branchNameTo){
        return ReturnHelper.returnBool(repoService.MergeBranch(repoID, branchNameFrom, branchNameTo));
    }

    @RequestMapping(value = "view/getVersionDiff", method= RequestMethod.POST)
    public SaResult getVersionDiff(String repoID, String branchName1, String branchName2){
        // TODO: git diff HEAD HEAD^
        return ReturnHelper.returnObj(repoService.getVersionDiff(repoID,branchName1,branchName2));
    }

    @RequestMapping(value = "privateRepo", method = RequestMethod.POST)
    public SaResult privateRepo(String repoID){
        return ReturnHelper.returnBool(repoService.privateRepo(repoID));
    }
    @RequestMapping(value = "publicRepo", method = RequestMethod.POST)
    public SaResult publicRepo(String repoID){
        return ReturnHelper.returnBool(repoService.publicRepo(repoID));
    }
    @RequestMapping(value = "updateNameByRepoID", method = RequestMethod.POST)
    public SaResult updateNameByReopID(String repoId, String reName){
        return ReturnHelper.returnBool(repoService.updateNameByRepoID(reName, repoId));
    }

    @RequestMapping(value="FuzzySearch", method = RequestMethod.GET)
    public SaResult FuzzySearch(String keyword){
        return ReturnHelper.returnObj(repoService.fuzzySearch(keyword));
    }

    /**
     * <a href="https://blog.csdn.net/qq_36441027/article/details/124032290n">...</a>
     * info about revert and reset
     */

//    @RequestMapping(value = "edit/revert", method= RequestMethod.POST)
//    public boolean revert(String repoID, String commitId){
//        // TODO: git revert
//        return false;
//    }
//
//    @RequestMapping(value = "edit/reset", method= RequestMethod.POST)
//    public boolean reset(String repoID, String commitId){
//        // TODO: git reset
//        return false;
//    }
//    @RequestMapping(value = "view/diffOfCommits", method = RequestMethod.GET)
//    public void diffOfCommits(String repoId) throws GitAPIException, IOException {
//        repoService.diffOfCommits(repoId);
//    }



}
