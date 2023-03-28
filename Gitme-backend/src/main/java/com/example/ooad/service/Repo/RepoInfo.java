package com.example.ooad.service.Repo;

import com.example.ooad.bean.Repo;
import com.example.ooad.bean.RepoState;
import com.example.ooad.bean.User;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RepoInfo {
     String name;
     long ID;
     RepoState state;
     String creatorName;
     Long creatorID;
     Date createdData;
     Long star;
//     是否star该仓库
     Long starOwn;
     Long fork;
//     是否fork该仓库
     long forkIt;
     Long watchIt;
     Long issue;
     Long pr;
     String lastUpdate;
     String desc="";
     String forkFromUserName;
     String forkFromRepoName;
     List<String> collaborators;

     public RepoInfo(){}
     public RepoInfo(Repo temp){
          this.setID(temp.getId());
          this.setName(temp.getName());
          this.setState(temp.getState());
          this.setCreatorName(temp.getCreator().getName());
          this.setCreatorID(temp.getCreator().getId());
          this.setCreatedData(temp.getCreatedDate());
//          this.setDesc(temp.getDesc());
     }
     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public long getID() {
          return ID;
     }

     public void setID(long ID) {
          this.ID = ID;
     }

     public RepoState getState() {
          return state;
     }

     public void setState(RepoState state) {
          this.state = state;
     }

     public String getCreatorName() {
          return creatorName;
     }

     public void setCreatorName(String creatorName) {
          this.creatorName = creatorName;
     }

     public Long getCreatorID() {
          return creatorID;
     }

     public void setCreatorID(Long creatorID) {
          this.creatorID = creatorID;
     }

     public Date getCreatedData() {
          return createdData;
     }

     public void setCreatedData(Date createdData) {
          this.createdData = createdData;
     }
}
