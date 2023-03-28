package com.example.ooad.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.ooad.UTil.Handler.ReturnHelper;
import com.example.ooad.bean.Issue;
import com.example.ooad.dao.IssueDao;
import com.example.ooad.service.Issue.IssueInfo;
import com.example.ooad.service.Issue.IssueService;
import com.example.ooad.service.Issue.NewIssueParam;
import com.sun.istack.NotNull;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
@RequestMapping("/issue/")

public class IssueCtrl {

    @Autowired
    private IssueService issueService;

    /**
     * Issue 的增删改查
     * title is not unique, so far it is temporary
     * use issueID
     */

    @RequestMapping(value="getAllIssueByRepoID", method = RequestMethod.GET)
    public SaResult getAllIssueByRepoID(String repoID){
        return ReturnHelper.returnObj(issueService.getIssueByRepoID(repoID));
    }

    @RequestMapping(value="getIssueInfo", method = RequestMethod.GET)
    public SaResult getIssueInfo(String IssueID){
        IssueInfo issueInfo = issueService.getIssueByIssueID(IssueID);
//        System.out.println(issueInfo.toString());
        return ReturnHelper.returnObj(issueInfo);
    }

    @RequestMapping(value="getIssueInfoByTitle", method = RequestMethod.GET)
    public SaResult getIssueInfoByTitle(String title){
        IssueInfo issueInfo = issueService.getIssueByTitle(title);
        return ReturnHelper.returnObj(issueInfo);
    }
    @RequestMapping(value="getAllIssue", method = RequestMethod.GET)
    public SaResult getAllIssue(){
        return ReturnHelper.returnObj(issueService.getAllIssue());
    }

    @RequestMapping(value="updateTitleById", method=RequestMethod.POST)
    public SaResult updateTitleById(String title, String id){

        return ReturnHelper.returnObj(issueService.updateTitleById(title, id));
    }

//    参数为 repoID, issue, comment
    @RequestMapping(value="addIssue", method = RequestMethod.POST)
    public SaResult addIssue(@RequestBody NewIssueParam newIssueParam){
        return ReturnHelper.returnObj(issueService.addIssue(newIssueParam.getIssue(), newIssueParam.getRepoID(), newIssueParam.getComment()));
    }

    /**
     * 没有删除issue的功能
     */
    @RequestMapping(value="deleteByRepoID", method = RequestMethod.DELETE)
    public SaResult deleteIssueByRepoID(String repoID){

        return ReturnHelper.returnObj(issueService.deleteIssueByRepoID(repoID));
    }
    @RequestMapping(value="deleteByID", method = RequestMethod.DELETE)
    public SaResult deleteIssueByID(String issueID){
        return ReturnHelper.returnBool(issueService.deleteIssueById(issueID));
    }

    @RequestMapping(value = "findByRepoIDAndIdWithinRepo", method = RequestMethod.GET)
    public SaResult findByRepoIDAndIdWithinRepo(String repoID, String issueId){
        return ReturnHelper.returnObj(issueService.findByRepoIDAndIdWithinRepo(repoID, issueId));
    }

    @RequestMapping(value = "edit/openIssue", method = RequestMethod.POST)
    public SaResult openIssue(String repoID, String idWithin){
        return ReturnHelper.returnBool(issueService.openIssue(repoID, idWithin));
    }
    @RequestMapping(value = "edit/closeIssue", method = RequestMethod.POST)
    public SaResult closeIssue(String repoID, String idWithin){
        return ReturnHelper.returnBool(issueService.closeIssue(repoID, idWithin));
    }


}
