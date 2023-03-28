package com.example.ooad.service.ForkRS;

import cn.dev33.satoken.stp.StpUtil;
import com.example.ooad.bean.ForkRS;
import com.example.ooad.dao.RepoDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class ForkRSInfo {

    Long id;
    Long fromRepoID;
    Long toRepoID;
    Long forkUserID;
    String fromBranch;
    String fromCommit;
    public ForkRSInfo(){}
    public ForkRSInfo(Long fromRepoID, Long toRepoID, Long forkUserID, String fromBranch, String fromCommit){
        this.fromRepoID = fromRepoID;
        this.toRepoID = toRepoID;
        this.forkUserID = forkUserID;
        this.fromBranch = fromBranch;
        this.fromCommit = fromCommit;
    }
    public ForkRSInfo(ForkRS forkRS){
        this.id = forkRS.getId();
        this.forkUserID = StpUtil.getLoginIdAsLong();
        this.fromRepoID = forkRS.getFromRepo().getId();
        this.toRepoID = forkRS.getToRepo().getId();
        this.fromBranch = forkRS.getFromBranch();
        this.fromCommit = forkRS.getFromCommit();
    }
}
