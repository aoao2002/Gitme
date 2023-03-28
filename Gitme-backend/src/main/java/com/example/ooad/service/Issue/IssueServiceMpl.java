package com.example.ooad.service.Issue;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.bean.Comment;
import com.example.ooad.bean.Issue;
import com.example.ooad.bean.User;
import com.example.ooad.dao.CommentDao;
import com.example.ooad.dao.IssueDao;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.dao.UserDao;
import com.example.ooad.service.Comment.CommentServiceMpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceMpl implements IssueService {

    @Autowired
    private IssueDao issueDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RepoDao repoDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentServiceMpl commentServiceMpl;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //@Override
    //public int save(Issue issue) {
    //    issueDao.save(issue);
    //}

    public void supplementIssueInfo(IssueInfo issueInfo){
        long authorId = issueInfo.getAuthorID();
        long issueId = issueInfo.getID();
        String name = userDao.findById(authorId).get().getName();
        long comments = commentDao.countByReplyIssue_Id(issueId);
        issueInfo.setAuthorName(name);
        issueInfo.setCommentNum(comments);
    }
    public List<IssueInfo> getAllIssue(){
        List<Issue> issues = issueDao.findAll();
        List<IssueInfo> issueInfos = new ArrayList<>();
        for(Issue issue:issues){
//            System.out.printf("issue id %d, state %b\n", issue.getId(), issue.isState());
            IssueInfo issue1 = new IssueInfo(issue);
            supplementIssueInfo(issue1);
//            System.out.printf("issueInfo idd %d, state %b\n", issue1.getID(), issue1.isState());
            issueInfos.add(issue1);
//            System.out.printf("issueServiceMpl time %s\n", issue.getCreateTime());
        }
        return issueInfos;
    }
    @Override
    public IssueInfo getIssueByTitle(String title) {
        if(!InputChecker.checkNullAndEmpty(title)){
            return null;
        }
        Optional<Issue> issue2 = issueDao.findByTitle(title);
        Issue issue;
        if(issue2.isEmpty()){
            return null;
        }else{
            issue = issue2.get();
        }
        IssueInfo issueInfo = new IssueInfo(issue);
        supplementIssueInfo(issueInfo);
        return issueInfo;
    }


    public IssueInfo getIssueByIssueID(String issueID) {
//        System.out.println(issueID);
        Long isid;
        if(InputChecker.checkNullAndEmpty(issueID) && InputChecker.checkNum(issueID)){
            isid = Long.valueOf(issueID);
        }else{
            return null;
        }
        Optional<Issue> issue = issueDao.findById(isid);
//        System.out.println("success find by issueID");
        IssueInfo issueInfo = new IssueInfo(issue);
        supplementIssueInfo(issueInfo);
        return issueInfo;
    }

    @Override
    public List<IssueInfo> getIssueByRepoID(String repoID) {
        List<IssueInfo> issueInfos = new ArrayList<>();
        Long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.valueOf(repoID);
        }else{
            return null;
        }
        List<Issue> issues = issueDao.findByRepoID(repoid);
        for(Issue issue : issues){
            IssueInfo issueInfo = new IssueInfo(issue);
            supplementIssueInfo(issueInfo);
            issueInfos.add(issueInfo);
        }
        return issueInfos;
    }

    @Override
    public Long getMaxidWithinRepoByRepoID(String repoID) {
        Long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.valueOf(repoID);
        }else{
            return null;
        }
        Long is = issueDao.findMaxidWithinRepoByRepoID(repoid);
        if (is == null){
            return 0L;
        }
        return issueDao.findMaxidWithinRepoByRepoID(repoid);
    }


    @Override
    public Integer updateTitleById(String title, String id) {
        Long id2;
        List<String> str = new ArrayList<>();
        str.add(title);str.add(id);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(id)){
            id2 = Long.valueOf(id);
        }
        else{
            return null;
        }
        return issueDao.updateTitleById(title, id2);
    }

    @Override
    public Long addIssue(String title, String repoid2, String comment) {
//        long autherID =StpUtil.getLoginIdAsLong() ;
//        long idWR = getMaxidWithinRepoByRepoID(repoid)+1;
//        Date date = new Date();
//        return issueDao.save(new IssueInfo(title, idWR, autherID, date, repoid, true));
//        return issueDao.addIssue(getMaxidWithinRepoByRepoID(repoid)+1, true, title, StpUtil.getLoginIdAsLong(), repoid);
        Long repoid;
        List<String> str = new ArrayList<>();
        str.add(title);str.add(repoid2);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(repoid2)){
            repoid = Long.valueOf(repoid2);
        }
        else{
            return null;
        }
        if(repoDao.findById(repoid).isEmpty()){
            return null;
        }

        Date date= new Date();
//        String format = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Issue issue = new Issue();
        issue.setTitle(title);
        if(getMaxidWithinRepoByRepoID(repoid2) >= 0){
            issue.setIdWithinRepo(getMaxidWithinRepoByRepoID(repoid2)+1);
        }else{
            issue.setIdWithinRepo(0L);
        }
        issue.setRepoID(repoid);
        Optional<User> user = userDao.findById(StpUtil.getLoginIdAsLong());
        if(user.isEmpty()){
            return null;
        }
        issue.setAuthor(user.get());
        issue.setCreatedDate(date);
        issue.setCreateTime(simpleDateFormat.format(date));
        System.out.println(simpleDateFormat.format(date));
        issue.setState(true);
        issueDao.save(issue);
        if(InputChecker.checkNullAndEmpty(comment)){
            commentServiceMpl.addComment(comment, String.valueOf(issue.getId()));
        }
//        System.out.println(issue.getCreatedDate());
        return issue.getIdWithinRepo();

    }

    @Override
    public boolean deleteIssueById(String IssueID) {
        Long issueid;
        if(InputChecker.checkNullAndEmpty(IssueID) && InputChecker.checkNum(IssueID)){
            issueid = Long.valueOf(IssueID);
        }else{
            return false;
        }
        if(issueDao.findById(issueid).isEmpty()){
            return false;
        }
        deleteByID(issueid);
        return true;
    }

    @Override
    public Long deleteIssueByRepoID(String repoID) {
        Long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.valueOf(repoID);
        }
        else{
            return null;
        }
        List<Issue> issues = issueDao.findByRepoID(repoid);
        issues.forEach(issue -> deleteByID(issue.getId()));
        return (long) issues.size();
    }

    @Override
    public IssueInfo findByRepoIDAndIdWithinRepo(String repoID, String issueIdWI){
        Long repoId, Issueid;
        List<String> str = new ArrayList<>();
        str.add(repoID);str.add(issueIdWI);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(str)){
            repoId = Long.valueOf(repoID);
            Issueid = Long.valueOf(issueIdWI);
        }else{
            return null;
        }
        Issue issue = issueDao.findByRepoIDAndIdWithinRepo(repoId, Issueid);
        if(issue == null){
            return null;
        }
        IssueInfo issueInfo = new IssueInfo(issue);
        supplementIssueInfo(issueInfo);
        return issueInfo;
    }

    public boolean openIssue(String repoID, String idWithin){
        long repoId, IdWithin;
        List<String> str = new ArrayList<>();
        str.add(repoID);str.add(idWithin);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(str)){
            repoId = Long.parseLong(repoID);
            IdWithin = Long.parseLong(idWithin);
        }else{
            return false;
        }
        long userId = StpUtil.getLoginIdAsLong();
        Issue issue = issueDao.findByRepoIDAndIdWithinRepo(repoId, IdWithin);
        if(issue == null){
            return false;
        }
//        if(userId == issue.getAuthor().getId()){
//            issueDao.updateStateById(true, issue.getId());
//            return true;
//        }else{
//            return false;
//        }
        issueDao.updateStateById(true, issue.getId());
        return true;
    }
    public boolean closeIssue(String repoID, String idWithin){
        long repoId, IdWithin;
        List<String> str = new ArrayList<>();
        str.add(repoID);str.add(idWithin);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(str)){
            repoId = Long.parseLong(repoID);
            IdWithin = Long.parseLong(idWithin);
        }else{
            return false;
        }
        long userId = StpUtil.getLoginIdAsLong();
        Issue issue = issueDao.findByRepoIDAndIdWithinRepo(repoId, IdWithin);
        if(issue == null){
            return false;
        }
//        if(userId == issue.getAuthor().getId()){
//            issueDao.updateStateById(false, issue.getId());
//            return true;
//        }
        issueDao.updateStateById(false, issue.getId());
        return true;
    }

    @Override
    public boolean deleteByID(long id) {
        List<Comment> comments = commentDao.findByReplyIssue_Id(id);
        for(Comment comment : comments){
            commentDao.deleteById(comment.getId());
        }
        issueDao.deleteById(id);
        return true;
    }


}
