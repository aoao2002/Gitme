package com.example.ooad.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.ooad.UTil.Handler.ReturnHelper;
import com.example.ooad.service.Comment.CommentInfo;
import com.example.ooad.service.Comment.CommentService;
import com.example.ooad.service.Comment.NewComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.activation.CommandInfo;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment/")
@Slf4j
public class CommentCtrl {
    @Autowired
    private CommentService commentService;

    @RequestMapping(value="add", method = RequestMethod.POST)
    public SaResult addComment(@RequestBody NewComment newComment){

        return ReturnHelper.returnBool(commentService.addComment(newComment.getContent(), newComment.getIssueID()));
    }
    @RequestMapping(value="updateLikesNumByID", method = RequestMethod.POST)
    SaResult updateLikesNumById(String likesNum, String id){
        return ReturnHelper.returnObj(commentService.updateLikesNumById(likesNum, id));
    }

    @RequestMapping(value="updateContentByID", method = RequestMethod.POST)
    SaResult updateContentById(String content, String id){
        return ReturnHelper.returnObj(commentService.updateContentById(content, id));
    }

    @RequestMapping(value="deleteCommentByID", method = RequestMethod.DELETE)
    public SaResult deleteCommentByID(String commentId){
        return ReturnHelper.returnBool(commentService.deleteCommentByID(commentId));
    }

    @RequestMapping(value="deleteCommentsByIssueID", method = RequestMethod.DELETE)
    public SaResult deleteCommentsByIssueID(String issueID){
        return ReturnHelper.returnObj(commentService.deleteCommentsByIssueID(issueID));
    }
    @RequestMapping(value="deleteCommentByRepoID", method = RequestMethod.DELETE)
    public SaResult deleteCommentByRepoID(String repoId){
        return ReturnHelper.returnObj(commentService.deleteCommentsByRepoId(repoId));
    }

    @RequestMapping(value="deleteCommentsByOwnerID", method = RequestMethod.DELETE)
    public SaResult deleteCommentsByOwnerID(String OwnerID){
        return ReturnHelper.returnObj(commentService.deleteCommentsByOwnerId(OwnerID));
    }

    @RequestMapping(value="getAllCommentByRepoID", method = RequestMethod.GET)
    public SaResult getAllCommentByRepoID(String repoID){
        return ReturnHelper.returnObj(commentService.getCommentByRepoID(repoID));
    }
    @RequestMapping(value="getCommentByID", method = RequestMethod.GET)
    public SaResult getCommentByID(String commentID){
        return ReturnHelper.returnObj(commentService.getCommentByID(commentID));
    }
    @RequestMapping(value="getCommentByIssueID", method = RequestMethod.GET)
    public SaResult getCommentByIssueID(String issueID){
        return ReturnHelper.returnObj(commentService.getCommentByIssueID(issueID));
    }

    @RequestMapping(value="getAllComment", method = RequestMethod.GET)
    public SaResult getAllComment(){
        return ReturnHelper.returnObj(commentService.getAllComment());
    }
}

