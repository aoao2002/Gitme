package com.example.ooad.service.Comment;

import cn.dev33.satoken.stp.StpUtil;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.bean.Comment;
import com.example.ooad.bean.User;
import com.example.ooad.dao.CommentDao;
import com.example.ooad.dao.IssueDao;
import com.example.ooad.dao.UserDao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceMpl implements CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private IssueDao issueDao;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    Date date;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    String date;

    public void suppleCommentInfo(CommentInfo commentInfo){
        Optional<User> user = userDao.findById(commentInfo.getOwnerID());
        if(user.isEmpty()){
            return;
        }
        commentInfo.setAuthorName(user.get().getName());
    }
    @Override
    public CommentInfo getCommentByID(String commentID) {
        Long commentid;
        if(InputChecker.checkNullAndEmpty(commentID) && InputChecker.checkNum(commentID)){
            commentid = Long.valueOf(commentID);
        }else{
            return null;
        }
        if(commentDao.findById(commentid).isEmpty()){
           return null;
        }
        return new CommentInfo(commentDao.findById(commentid).get());
    }

    @Override
    public List<CommentInfo> getCommentByIssueID(String issueID) {
        List<CommentInfo> commentInfoList = new ArrayList<>();
        Long issueid;
        if(InputChecker.checkNullAndEmpty(issueID) && InputChecker.checkNum(issueID)){
            issueid = Long.valueOf(issueID);
        }else{
            return null;
        }
        List<Comment> comments = commentDao.findByReplyIssue_Id(issueid);
        for(Comment comment : comments){
            commentInfoList.add(new CommentInfo(comment));
        }
        return commentInfoList;
    }

    @Override
    public List<CommentInfo> getCommentByRepoID(String repoID){
        Long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.valueOf(repoID);
        }else{
            return null;
        }
        List<CommentInfo> commentInfoList = new ArrayList<>();
        List<Comment> comments = commentDao.findByReplyIssue_RepoID(repoid);
        for(Comment comment : comments){
            commentInfoList.add(new CommentInfo(comment));
        }
        return commentInfoList;
    }

    @Override
    public List<CommentInfo> getAllComment() {
        List<Comment> comments = commentDao.findAll();
        List<CommentInfo> commentInfoList = new ArrayList<>();
        for(Comment comment:comments){
            commentInfoList.add(new CommentInfo(comment));
        }
        return commentInfoList;
    }

    @Override
    public boolean addComment(String content, String issueID) {
        Long issueid;
        List<String> str = new ArrayList<>();
        str.add(content);str.add(issueID);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(issueID)){
            issueid = Long.valueOf(issueID);
        }else{
            return false;
        }
        if(issueDao.findById(issueid).isEmpty()){
            return false;
        }
        Comment comment = new Comment();
        date = new Date();
        comment.setContent(content);
        Optional<User> user = userDao.findById(StpUtil.getLoginIdAsLong());
        if(user.isEmpty()){
            return false;
        }
        comment.setOwner(user.get());
        comment.setLikesNum(0L);
        if(issueDao.findById(issueid).isPresent()) {
            comment.setReplyIssue(issueDao.findById(issueid).get());
        }else{
            System.out.println("no such issue in CommentService");
        }
        comment.setCreatedDate(date);
        comment.setCreateTime(simpleDateFormat.format(new Date()));
        commentDao.save(comment);
        return true;
    }

    @Override
    public Integer updateLikesNumById(String likesNum, String id2) {
        Long id, likesnum;
        List<String> str = new ArrayList<>();
        str.add(likesNum);str.add(id2);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(likesNum) && InputChecker.checkNum(id2)){
            likesnum = Long.valueOf(likesNum);
            id = Long.valueOf(id2);
        }else{
            return null;
        }
        return commentDao.updateLikesNumById(likesnum, id);
    }

    @Override
    public Integer updateContentById(String content, String id2) {
        Long id;
        List<String> str = new ArrayList<>();
        str.add(content);str.add(id2);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(id2)){
            id = Long.valueOf(id2);
        }else{
            return null;
        }
        return commentDao.updateContentById(content, id);
    }

    @Override
    public boolean deleteCommentByID(String commentID) {
        Long commentid;
        List<String> str = new ArrayList<>();
        str.add(commentID);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(commentID)){
            commentid = Long.valueOf(commentID);
        }else{
            return false;
        }
        if(commentDao.findById(commentid).isEmpty()){
            return false;
        }
        commentDao.deleteById(commentid);
        return true;
    }

    @Override
    public Integer deleteCommentsByIssueID(String issueID) {
        Long issueid;
        if(InputChecker.checkNullAndEmpty(issueID) && InputChecker.checkNum(issueID)){
            issueid = Long.valueOf(issueID);
        }else{
            return null;
        }
        List<Comment> comments = commentDao.findByReplyIssue_Id(issueid);
        int cnt = comments.size();
        for(Comment comment:comments){
            commentDao.deleteById(comment.getId());
        }
        return cnt;
    }

    @Override
    public Integer deleteCommentsByRepoId(String repoID) {
        Long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.valueOf(repoID);
        }else{
            return null;
        }
        List<Comment> comments = commentDao.findByReplyIssue_RepoID(repoid);
        int cnt = comments.size();
        for(Comment comment:comments){
            commentDao.deleteById(comment.getId());
        }
        return cnt;
    }

    @Override
    public Integer deleteCommentsByOwnerId(String ownerID) {
        Long ownerid;
        if(InputChecker.checkNullAndEmpty(ownerID) && InputChecker.checkNum(ownerID)){
            ownerid = Long.valueOf(ownerID);
        }else{
            return null;
        }
        List<Comment> comments = commentDao.findByOwner_Id(ownerid);
        int cnt = comments.size();
        for(Comment comment:comments){
            commentDao.deleteById(comment.getId());
        }
        return cnt;
    }

    @Override
    public Integer deleteAllComments() {
        int num = commentDao.findAll().size();
        commentDao.deleteAll();
        int num2 = commentDao.findAll().size();
        return num - num2;
    }


}
