package com.example.ooad.service.Repo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class VersionInfo {

    long repoID;
    long hashCode;
    String branchName;

    // hash for this version
    String hash;
    List<String> parentHash;

    // commit for submit this version
    String commit;

    // the files have been changed in this version
    List<String> changedFile;

    String submitDate;
    public VersionInfo(){}
    public VersionInfo(long repoID, long hashCode, String branchName, String hash, List<String> parentHash,String commit, String submitDate){
        this.repoID = repoID;
        this.hashCode = hashCode;
        this.branchName = branchName;
        this.hash = hash;
        this.parentHash = parentHash;
        this.commit = commit;
        this.submitDate = submitDate;
        this.changedFile = new ArrayList<>();
    }
}
