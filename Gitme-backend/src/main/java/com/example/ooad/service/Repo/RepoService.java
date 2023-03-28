package com.example.ooad.service.Repo;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.io.resource.InputStreamResource;
import com.example.ooad.bean.Repo;
import com.example.ooad.bean.RepoState;
import com.example.ooad.bean.User;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.service.User.UserInfo;
//import com.github.pagehelper.PageInfo;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Transactional
public interface RepoService {
    List<Repo> getRepoByName(String name);

    List<Repo> getRepoByState(RepoState state);

    List<String> getRepoFile(String dir);
    List<RepoInfo> getAllRepo();

    boolean iniRepo(String repoName, String state, String desc);
    boolean updateNameByRepoID(String reName, String repoID);
    boolean privateRepo(String repoId);
    boolean publicRepo(String repoID);

    boolean deleteRepo(long repoID);

    String getRepoDir(String repoID);
    String getRepoDir(Repo repo);
    List<BranchInfo> getAllBranch(String repoID);
    Object getAllVersion(String repoID, String branchName);
    Object getVersion(String repoID, String commitID);

    SaResult getRepoSateByName(String repoName, String CreatorEmail);

    Repo getRepoByID(String repoID);

    //power

    boolean isEditable(Repo repo,User user);

    boolean isEditableByID(String repoID,String userID);

    boolean isEditable(String repoName,String creatorEmail,String userEmail, String pw);

    boolean isViewable(Repo repo,User user);

    boolean isViewable(String repoName,String creatorEmail,String userEmail, String pw);

    boolean isViewableByID(String repoID,String userID);


    //get file
    FileContent getContent(String repoId,String path, String commitID);
    FileContent getContent(String repoId,String path);

    boolean upFile(String repoID, String branchID, String commitMsg,
                   MultipartFile uploadFile, String path);

    boolean downLoadAsZip(HttpServletResponse response,long repoID, String branch);

    List<FileBaseInfo> ls(String repoID ,String dir, String branch);

    boolean addBranch(String repoID, String branchName);

    RepoInfo getRepoByRepoID(long RepoId);

    List<RepoInfo> getRepoByMyself();
    List<RepoInfo> getRepoByMyStar();

    List<RepoInfo> getRepoByUserID(long UserId);

    List<RepoInfo> getRepoByUserName(String userName);

    void diffOfCommits(String repoID)throws GitAPIException, IOException;
    List<String> lastUpdateByRepo(Repo repo);

    String getPRDiff(String repoID, String branchName);
    String getVersionDiff(String repoID, String Commit1, String Commit2);
    boolean deleFile(String repoID, String branchName,
                     String commitMsg,String path);

    boolean updateDescriById(String descri, String id);

    Long getRepoIDByCreator_NameAndRepo_Name(String cName, String rName);

    boolean isText(String filePath);

    List<RepoInfo> fuzzySearch(String word);

    boolean MergeBranch(String repoID, String branchFromName, String branchToName);

    boolean deleteBranch(String repoID, String branchName);
}
