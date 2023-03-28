package com.example.ooad.service.Repo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BranchInfo {
    String name;
    String objID;

    boolean isDefault;

//    List<VersionInfo> versions;
    public BranchInfo(){}
    public BranchInfo(String name){
        this.name = name;
    }
    public BranchInfo(String name, String objID){
        this.name = name;
        this.objID = objID;
        this.isDefault = false;
    }


}
