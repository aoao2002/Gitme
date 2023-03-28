package com.example.ooad.service.PRRS;

import com.example.ooad.bean.PRRS;
import com.example.ooad.bean.PRState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PRRSInfo {
    Long fromRepoId;
    Long toRepoId;
    String fromBranch;
    String toBranch;
    String diff;
    PRState state;
    String title;
    Long id;
    String date;
    String userName;
    String fromRepoName;

    public PRRSInfo(){}
    public PRRSInfo(PRRS prrs){
        this.fromRepoId = prrs.getFromRepo().getId();
        this.toRepoId = prrs.getToRepo().getId();
        this.title = prrs.getTitle();
        this.state = prrs.getState();
        this.id = prrs.getId();
        this.date = prrs.getCreateTime();
        this.userName = prrs.getUserName();
    }

}
