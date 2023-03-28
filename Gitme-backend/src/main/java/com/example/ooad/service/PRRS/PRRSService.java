package com.example.ooad.service.PRRS;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PRRSService {
    Long makePR(String  repoID, String title, String toBranch, String fromBranch);
    boolean deletePR(String forkRepo, String originRepo);
    List<PRRSInfo> getAllPR(String  repoID);
    List<PRRSInfo> getALLPR_test();
    void deleteAllPR();
   boolean acceptPR(String PRID);
   PRRSInfo getPRByID(String PRID);

   boolean rejectPR(long PRID);

}
