package com.example.ooad.service.Issue;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
public interface IssueService {

    //int save(Issue issue);

    List<IssueInfo> getAllIssue();
    IssueInfo getIssueByTitle(String title);
    IssueInfo getIssueByIssueID(String issueID);

    List<IssueInfo> getIssueByRepoID(String repoID);

    Long getMaxidWithinRepoByRepoID(String repoID);


    Integer updateTitleById(String title, String id);

    Long addIssue(String title, String repoid, String comment);
    boolean deleteIssueById(String IssueID);
    Long deleteIssueByRepoID(String repoID);

    IssueInfo findByRepoIDAndIdWithinRepo(String repoID, String issueIDWI);
    boolean openIssue(String repoID, String idWithin);
    boolean closeIssue(String repoID, String idWithin);
    boolean deleteByID(long id);


}
