package com.example.ooad.service.Issue;

import com.example.ooad.bean.Issue;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.netty.util.internal.ObjectPool;
import lombok.Getter;
import lombok.Setter;

import javax.print.attribute.standard.JobKOctets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
public class IssueInfo {
    Long ID;
    String title;
    Long idWithinRepo;
    Long authorID;
    String authorName;
    Long commentNum;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date;
//    String createTime;
    Long repoID;
    boolean state;

    public IssueInfo(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = simpleDateFormat.format(new Date());
    }

    public IssueInfo(String title, Long idWithinRepo, Long authorID,
                     Long repoID, boolean state) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.title = title;
//        this.ID = ID;
        this.idWithinRepo = idWithinRepo;
        this.authorID = authorID;
        this.date = simpleDateFormat.format(new Date());
        /*等价于上面这句
        if (date == null) {
            this.date = new Date();
        }else {
            this.date = date;
        }
        */
        this.repoID = repoID;
        this.state = state;
    }

    public IssueInfo(Optional<Issue> issue){
        if(issue.isPresent()) {
            Issue issue2 = issue.get();
            this.ID = issue2.getId();
            this.title = issue2.getTitle();
            this.idWithinRepo = issue2.getIdWithinRepo();
            this.authorID = issue2.getAuthor().getId();
            this.state = issue2.isState();
            this.repoID = issue2.getRepoID();
            this.date = issue2.getCreateTime();
//            this.createTime = issue2.getCreateTime();

        }else{
            System.out.println("Issue is null at IssueInfo");
        }
    }
    public IssueInfo(Issue issue2){
        this.ID = issue2.getId();
        this.title = issue2.getTitle();
        this.idWithinRepo = issue2.getIdWithinRepo();
        this.authorID = issue2.getAuthor().getId();
        this.state = issue2.isState();
        this.repoID = issue2.getRepoID();
        this.date = issue2.getCreateTime();
//        this.createTime = issue2.getCreateTime();
    }


    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getIdWithinRepo() {
        return idWithinRepo;
    }

    public void setIdWithinRepo(Long idWithinRepo) {
        this.idWithinRepo = idWithinRepo;
    }

    public Long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Long authorID) {
        this.authorID = authorID;
    }
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getRepoID() {
        return repoID;
    }

    public void setRepoID(Long repoID) {
        this.repoID = repoID;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
