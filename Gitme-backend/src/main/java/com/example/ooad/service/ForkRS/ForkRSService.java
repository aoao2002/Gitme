package com.example.ooad.service.ForkRS;

import cn.dev33.satoken.util.SaResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Transactional
public interface ForkRSService {
    boolean fork(String repoID) throws IOException, GitAPIException;
    boolean fork(String repoID, String rename, String desc) throws IOException, GitAPIException;
    boolean unFork(String repoID) throws Exception;
    List<String> getForkByFromRepoID(String repoID);
    String getForkByToRepoID(String repoID);
    List<ForkRSInfo> getAllForks();

}
