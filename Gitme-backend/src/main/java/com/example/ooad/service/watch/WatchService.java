package com.example.ooad.service.watch;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
public interface WatchService {
    boolean getWatchByMe(String repoID);

    boolean addWatch(String repoID);
    boolean deleWatch(String repoID);
    ArrayList<WatchInfo_> getWatchInfoByMe();
    boolean addWatchInfo(String info, String RepoId);
    boolean setRead(String WatchInfoId);
}
