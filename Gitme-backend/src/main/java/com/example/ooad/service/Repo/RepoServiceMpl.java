package com.example.ooad.service.Repo;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.InputStreamResource;
import com.example.ooad.OoadApplication;
import com.example.ooad.UTil.FileUtils;
import com.example.ooad.UTil.Handler.*;
import com.example.ooad.UTil.JGitAPI.LsFileList;
import com.example.ooad.bean.*;
import com.example.ooad.dao.*;
import com.example.ooad.service.ForkRS.ForkRSService;
import com.example.ooad.service.Handler.BeanNotExistException;
import com.example.ooad.service.Handler.EditException;
import com.example.ooad.service.Star.StarInfo;
import com.example.ooad.service.Star.StarService;
import com.example.ooad.service.User.UserService;
import com.example.ooad.service.User.UserServiceMpl;
import com.example.ooad.service.watch.WatchServiceMpl;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.Lists;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.example.ooad.UTil.JGitUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.AbstractDocument;

@Service
public class RepoServiceMpl implements RepoService{

    @Autowired
    private RepoDao repoDao;

    @Autowired
    private WatchInfoDao watchInfoDao;
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StarDao starDao;
    @Autowired
    private WatchDao watchDao;

    @Autowired
    private WatchServiceMpl watchServiceMpl;

    @Autowired
    private IssueDao issueDao;

    @Autowired
    private CollaboratorRSDao collaboratorRSDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ForkRSDao forkRSDao;

    @Autowired
    private PRRSDao prrsDao;


    private Date date;

    @Value("${file.upload.dir}")
    private String uploadFilePath;

    private static final Logger logger = LoggerFactory.getLogger(RepoService.class);


    RepoNameStringHandler repoNameStringHandler = new RepoNameStringHandler();
    StateStringHandler stateStringHandler = new StateStringHandler();
    UserHandler userHandler = new UserHandler();
    RepositoryHandler repositoryHandler = new RepositoryHandler();

    public void save(Repo repo)
    {
        repoDao.save(repo);
    }

    public List<Repo> getRepoByName(String name){
        return repoDao.findByName(name);
    }

    public List<Repo> getRepoByState(RepoState state){
        return repoDao.findByState(state);
    }

    public List<RepoInfo> getAllRepo(){
        List<RepoInfo> repoInfos = new ArrayList<>();
        List<Repo> repos = repoDao.findAll();
        for(Repo repo:repos){
            if(repo == null){
                continue;
            }
            repoInfos.add(getFullInfoOfRepo(repo));
        }
        return repoInfos;
    }

    public List<String> getRepoFile(String dir) {
        // 权限检查
        FileUtil fileUtil = new FileUtil();

        File[] nowFiles = fileUtil.ls(uploadFilePath+dir);
        List<String> result = new ArrayList<>();

        for (File file:nowFiles) {
            result.add(dir+file.getName());
        }

        return result;
    }

    public Repo getRepoByNameAndMail(String repoName, String creatorEmail){
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoName,creatorEmail)))
            return null;
        User creator = userService.findByMail(creatorEmail);
        if (creator==null) return null;

        Repo repo = repoDao.findByNameAndCreator(repoName, creator);
        return repo;
    }

    public boolean iniRepo(String repoName, String state, String desc){

        try {
            Optional<User> userOptional = userDao.findById(StpUtil.getLoginIdAsLong());
            if(userOptional.isEmpty()){
                return false;
            }
            User user = userOptional.get();
            if (!repoNameStringHandler.isLegal(repoName) || !stateStringHandler.isLegal(state)||!userHandler.isLegal(user)) {
                return false;
            }
            Repo repo = repoDao.findByNameAndCreator(repoName, user);
            if(repo!=null) return false;

            RepoState repoState = RepoState.toEnum(state);
            String mail = user.getMail();
            JGitUtils.CreateNewRepository(uploadFilePath + "/" + mail + "/" + repoName);
            repo = new Repo();
            date = new Date();
            repo.setCreatedDate(date);
            repo.setName(repoName);repo.setState(repoState);repo.setCreator(user);
            if(InputChecker.checkNullAndEmpty(desc)){
                repo.setDescri(desc);
            }else{
                repo.setDescri("");
            }
            repoDao.save(repo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRepo(long repoID){
        try {
            Repo repo = repoDao.findById(repoID);
            if (!repositoryHandler.isLegal(repo)) {
                return false;
            }
            String repoName = repoDao.findById(repoID).getName();
            User user = repoDao.findById(repoID).getCreator();
            if (!repoNameStringHandler.isLegal(repoName) || !userHandler.isLegal(user)) {
                return false;
            }
            if(forkRSDao.findByToRepo_Id(repoID).isPresent()){
                forkRSDao.deleteById(forkRSDao.findByToRepo_Id(repoID).get().getId());
            }
            String mail = user.getMail();
            JGitUtils.CleanRepository(uploadFilePath + "/" + mail + "/" + repoName);
            List<Watch> watches = watchDao.findByRepo(repo);
            for(Watch watch:watches){
                watchInfoDao.deleteByWatch(watch);
            }
            watchDao.deleteByRepo(repo);
            starDao.deleteByRepo(repo);
            List<Issue> issues = issueDao.findByRepoID(repoID);
            for(Issue issue:issues){
                commentDao.deleteByReplyIssue(issue);
            }
            issueDao.deleteByRepoID(repoID);
            collaboratorRSDao.deleteByRepo(repo);
            forkRSDao.deleteByFromRepo(repo);
            forkRSDao.deleteByToRepo(repo);
            prrsDao.deleteByToRepo(repo);
            prrsDao.deleteByFromRepo(repo);
            repoDao.deleteById(repoID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean forkRepo(String repoID) throws IOException {
        long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.parseLong(repoID);
        }else{
            return false;
        }
        User user = userService.findById(String.valueOf(StpUtil.getLoginId()));
        Repo repo = repoDao.findById(repoid);
        String new_dir = uploadFilePath+"/"+user.getMail()+"/"+repo.getName();
        String old_dir = uploadFilePath+"/"+repo.getCreator().getMail()+"/"+repo.getName();
        boolean init = iniRepo(repo.getName(), "PUBLIC", repo.getDescri());
        if(!init){
            return false;
        }
        File new_file = new File(new_dir), old_file = new File(old_dir);
        FileUtils.copyFileUsingFileChannels(old_file, new_file);
        return true;
    }

    public String getRepoDir(String repoID){
        long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.parseLong(repoID);
        }else{
            return null;
        }
        Repo repo = repoDao.findById(repoid);
        return uploadFilePath + "/" + repo.getCreator().getMail()
                + "/" + repo.getName();
    }

    public List<BranchInfo> getAllBranch(String repoID) {
        String dir = getRepoDir(repoID);
        if(dir == null){
            logger.error("getAllBranch:: no repo dir");
            return null;
        }
//        System.out.println(dir);
        Git git = JGitUtils.OpenRepository(dir);
        if(git == null){
            logger.error("getAllBranch:: no repo dir for git");
            return null;
        }
        return JGitUtils.GetBranchList(git, repoID, uploadFilePath, repoDao);
    }

    public Object getAllVersion(String repoID, String branchName){
        return JGitUtils.getAllVersion(repoID, branchName, uploadFilePath, repoDao);
    }
    public Object getVersion(String repoID, String commitID){
        return JGitUtils.getVersion(repoID, commitID, uploadFilePath, repoDao);
    }

    public void diffOfCommits(String repoID) throws GitAPIException, IOException {
        JGitUtils.diffOfCommits(repoID, uploadFilePath, repoDao);
    }

    @Override
    public SaResult getRepoSateByName(String repoName, String creatorEmail) {

        if (!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoName, creatorEmail)))
            return SaResult.error("input error");

        Optional<User> userOptional = userDao.findByMail(creatorEmail);

        if(userOptional.isEmpty()){
            return SaResult.error("no such user");
        }

        User user = userOptional.get();

        Repo repo =  repoDao.findByNameAndCreator(repoName, user);
        if (repo == null) return SaResult.error("no such repo");
        return SaResult.ok().setData(repo.getState().toString());
    }

    public Repo getRepoByID(String repoID){
        if (!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoID)))
            return null;

        return repoDao.findById(Long.parseLong(repoID));
    }

    public boolean isEditable(Repo repo,User user){
        if (user==null || repo==null)
            return false;

        if (repo.getCreator().getId().equals(user.getId())) return true;
        List<CollaboratorRS> collaborator = collaboratorRSDao.findByRepo(repo);

        for (CollaboratorRS c :
                collaborator) {
            if(c.getUser().getId().equals(user.getId())) return true;
        }
        return false;
    }

    public boolean isEditableByID(String repoID,String userID){
        return isEditable(getRepoByID(repoID), userService.findById(userID));
    }

    public boolean isEditable(String repoName,String creatorEmail,String userEmail,String pw){
        if (!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoName,userEmail,creatorEmail,pw)))
            return false;
        User creator = userService.findByMail(creatorEmail);
        if(creator == null) return false;

        Repo repo = repoDao.findByNameAndCreator(repoName, creator);
        if(repo == null) return false;

        SaResult loginResult = userService.LoginMail(userEmail,pw);
        if (loginResult.getCode()!=200) return false;

        User user = userService.findByMail(userEmail);
        if(user == null) return false;


        return isEditable(repo, user);
    }

    @Override
    public boolean isViewable(Repo repo, User user) {
        if(repo==null || user==null) return false;
        if(repo.getState().equals(RepoState.PUBLIC)) return true;
        return isEditable(repo, user);
    }

    @Override
    public boolean isViewable(String repoName, String creatorEmail, String userEmail, String pw) {
        if (!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoName,userEmail,creatorEmail,pw)))
            return false;
        User creator = userService.findByMail(creatorEmail);
        if(creator == null) return false;

        Repo repo = repoDao.findByNameAndCreator(repoName, creator);
        if(repo == null) return false;

        if(repo.getState()==RepoState.PUBLIC) return true;

        SaResult loginResult = userService.LoginMail(userEmail,pw);
        if (loginResult.getCode()!=200) return false;

        User user = userService.findByMail(userEmail);
        if(user == null) return false;

        return isEditable(repo, user);
    }

    @Override
    public boolean isViewableByID(String repoID,String userID){
        if (!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoID,userID)))
            return false;

        return isViewable(repoDao.findById(Long.parseLong(repoID)),
                userService.findById(userID));
    }


    public FileContent getContent(String repoId, String path){
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoId,path)))
            return null;
        Repo repo = getRepoByID(repoId);
        if(repo==null) return null;
        return getContent(repo, path);
    }

    public FileContent getContent(Repo repo, String path) {
        return JGitUtils.getContent(uploadFilePath + "/" + repo.getCreator().getMail()
                + "/" + repo.getName(), path,null);
    }

    public FileContent getContent(String repoID, String path, String commitID) {
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoID,path,commitID)))
            return null;
        Repo repo = getRepoByID(repoID);
        if(repo==null) return null;

        return getContent(repo,path);
//        return JGitUtils.getContent(uploadFilePath + "/" + repo.getCreator().getMail()
//                + "/" + repo.getName(), path, commitID);
    }

    public String getRepoDir(Repo repo)  {
        if (repo==null) return null;
        return uploadFilePath + "/" + repo.getCreator().getMail() + "/" + repo.getName();
    }

    public List<FileBaseInfo> ls(String repoID ,String dir, String branch){
        // 获取当前仓库当前路径下管理的所有文件
        List<FileBaseInfo> result = new ArrayList<>();
        try {
            Repo repo = repoDao.findById(Long.parseLong(repoID));
            if (!repositoryHandler.isLegal(repo)) {
                return result;
            }
            User user = repo.getCreator();
            if (!userHandler.isLegal(user)) {
                return result;
            }
            String mail = user.getMail();
            String repoName = repo.getName();
            if (!repoNameStringHandler.isLegal(repoName)) {
                return result;
            }
            Git git = JGitUtils.OpenRepository(uploadFilePath + "/" + mail + "/" + repoName);
            result = JGitUtils.lsByBranch(git,dir,branch);
            ArrayList<FileBaseInfo> resultDir = new ArrayList<>();
            ArrayList<FileBaseInfo> resultFile = new ArrayList<>();
            for (FileBaseInfo fileBaseInfo : result) {
                String type = LsFileList.GetFileAttribute(fileBaseInfo.getName(),git,dir,branch);
                fileBaseInfo.setType(type);
                // sort the result
                if(type.equals("Directory")) resultDir.add(fileBaseInfo);
                else resultFile.add(fileBaseInfo);
            }
            resultDir.addAll(resultFile);
            return resultDir;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    public boolean upFile(String repoID, String branchName, String commitMsg,
                          MultipartFile uploadFile, String path)  {
        if (!InputChecker.checkNullAndEmpty(
                Lists.newArrayList(repoID,branchName,commitMsg, path))){
            logger.error("upFile: 参数为空");
            return false;
        }

        if (uploadFile.isEmpty()) {
            logger.error("upFile: file is empty");
            return false;
        }

        Repo repo = getRepoByID(repoID);
        if(repo==null) {
            logger.error("upFile: no such repo");
            return false;
        }

        if(!JGitUtils.checkout(getRepoDir(repo),branchName)){
            logger.error("upFile: checkout error");
            return false;
        }


        String[] pathSpilt = path.split("/");
        String fileName = pathSpilt[pathSpilt.length-1];

        String repoPath = getRepoDir(repo);
        if(repoPath == null) return false;
        File folder = new File(repoPath, path.replace(fileName,""));

        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        File targetFile = new File(folder, fileName);
        String targetAbsFilePath =targetFile.getAbsolutePath();

        String targetFilePath = targetFile.getPath();

        try {
            uploadFile.transferTo(new File(folder, fileName).getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("make file error: "+path);
            return false;
        }

        // commit this file
        logger.info("create a file to "+folder+" name: "+fileName);
        Git git = JGitUtils.OpenRepository(repoPath);
        if(!JGitUtils.AddFileToRepository(git, targetFilePath))
            return false;
        JGitUtils.CommitFileToRepository(git, commitMsg);

        //delete this file

        FileUtil.del(targetFile);
        return true;

    }

    public boolean addBranch(String repoID, String branchName){
        if (!InputChecker.checkNullAndEmpty(
                Lists.newArrayList(repoID,branchName)))
            return false;

        if (getAllBranch(repoID).stream().anyMatch(branchInfo
                -> branchName.equals(branchInfo.getName())))
            return false;

        Repo repo = getRepoByID(repoID);
        if(repo==null) return false;

        Git git = JGitUtils.OpenRepository(getRepoDir(repo));
        if(git==null) return false;
        return JGitUtils.CreateBranch(git, branchName);
    }

//    public boolean checkout(Repo repo, String branchName){
//        if (repo==null) return false;
//
//        Git git = JGitUtils.OpenRepository(getRepoDir(repo));
//
//        try {
//            git.checkout()
//                    .setName(branchName)
//                    .call();
//            git.checkout()
//                    .setAllPaths(true)
//                    .call();
//        } catch (GitAPIException e) {
//            e.printStackTrace();
//            return false;
//        }
//        logger.info("checkout");
//        return true;
//    }

    public boolean downLoadAsZip(HttpServletResponse response,long repoID, String branch){
        try{
            Repo repo = repoDao.findById(repoID);
            if (!repositoryHandler.isLegal(repo)) {
                return false;
            }
            String repoName = repoDao.findById(repoID).getName();
            User user = repoDao.findById(repoID).getCreator();
            if (!repoNameStringHandler.isLegal(repoName) || !userHandler.isLegal(user)) {
                return false;
            }
            String mail = user.getMail();
            JGitUtils.downLoadRepository(uploadFilePath + "/" + mail + "/" + repoName, branch);
            JGitUtils.downLoadZipToFront(response, uploadFilePath + "/" + mail + "/" + repoName);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleFile(String repoID, String branchName,
                            String commitMsg,String path){
        if (!InputChecker.checkNullAndEmpty(
                Lists.newArrayList(repoID,branchName,commitMsg, path))){
            return false;
        }

        Repo repo = getRepoByID(repoID);
        if(repo==null) return false;

        if(!JGitUtils.checkout(getRepoDir(repo),branchName))
            return false;

        String repoPath = getRepoDir(repo);
        if(repoPath == null) return false;

        File targetFile = new File(repoPath, path);
        String targetFilePath = targetFile.getPath();


        // commit this file
//        System.out.println("delete file: "+targetFilePath);
//        Git git = JGitUtils.OpenRepository(repoPath);
//        JGitUtils.MoveFileToRepository(git, targetFilePath);
//        JGitUtils.CommitFileToRepository(git, commitMsg);

        Git git = JGitUtils.OpenRepository(repoPath);
        FileUtil.del(targetFile);
        try {
            git.add()
                    .addFilepattern(".")
                    .setUpdate(true)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
//        if(!JGitUtils.AddFileToRepository(git, targetFilePath))
//            return false;
        JGitUtils.CommitFileToRepository(git, commitMsg);

        return true;
    }

    public RepoInfo getRepoByRepoID(long RepoId){
        Repo repo = repoDao.findById(RepoId);
        if(repo==null) return null;

        return getFullInfoOfRepo(repo);
    }

    public List<RepoInfo> getRepoByMyself(){
        Optional<User> userOptional = userDao.findById(StpUtil.getLoginIdAsLong());
        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();
        List<Repo> repos = repoDao.findByCreator(user);
        List<RepoInfo> repoInfos = new ArrayList<>();
        for(Repo repo:repos){
            repoInfos.add(getFullInfoOfRepo(repo));
        }
        return repoInfos;
    }

    public List<RepoInfo> getRepoByMyStar(){
        Optional<User> userOptional = userDao.findById(StpUtil.getLoginIdAsLong());
        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();
        List<Star> stars = starDao.findByUser_Id(user.getId());
        List<Repo> repos = new ArrayList<>();
        for(Star star:stars){
            repos.add(star.getRepo());
        }
        List<RepoInfo> repoInfos = new ArrayList<>();
        for(Repo repo:repos){
            repoInfos.add(getFullInfoOfRepo(repo));
        }
        return repoInfos;
    }

//    repo 90 查询的时候会有问题
    public RepoInfo getFullInfoOfRepo(Repo repo){
        RepoInfo repoInfo = new RepoInfo(repo);
        long star = starDao.countByRepo_Id(repo.getId());
        long fork = forkRSDao.countByFromRepo_Id(repo.getId());
        long issue = issueDao.countByRepoID(repo.getId());
        long pr = prrsDao.countByToRepo_Id(repo.getId());
        boolean watch = watchServiceMpl.getWatchByMe(String.valueOf(repo.getId()));
        String last_update = null;
        if(lastUpdateByRepo(repo) != null) {
            last_update = lastUpdateByRepo(repo).get(0);
        }
        if(watch){
            repoInfo.setWatchIt(1L);
        }else{
            repoInfo.setWatchIt(0L);
        }
        repoInfo.setStar(star);
        repoInfo.setFork(fork);
        repoInfo.setIssue(issue);
        repoInfo.setPr(pr);
        repoInfo.setLastUpdate(last_update);
        if(repo.getDescri() == null || repo.getDescri().trim().equals("")){
            repoInfo.setDesc("owner provide no description");
        }else{
            repoInfo.setDesc(repo.getDescri());
        }
        long starIt = starDao.countByUser_IdAndRepo_Id(StpUtil.getLoginIdAsLong(), repo.getId());
        if(starIt == 0){
            repoInfo.setStarOwn(0L);
        }else{
            repoInfo.setStarOwn(1L);
        }
        long forkIt = forkRSDao.countByFromRepo_IdAndToRepo_Creator_Id(repo.getId(), StpUtil.getLoginIdAsLong());
        if(forkIt == 0){
            repoInfo.setForkIt(0L);
        }else{
            repoInfo.setForkIt(1L);
        }
        Optional<ForkRS> forkRS = forkRSDao.findByToRepo_Id(repo.getId());
        if(forkRS.isPresent()){
            repoInfo.setForkFromUserName(forkRS.get().getFromRepo().getCreator().getName());
            repoInfo.setForkFromRepoName(forkRS.get().getFromRepo().getName());
        }
        List<CollaboratorRS> collaboratorRSList = collaboratorRSDao.findByRepo_Id(repo.getId());
        repoInfo.collaborators = new ArrayList<>();
        for(CollaboratorRS collaboratorRS : collaboratorRSList){
            repoInfo.collaborators.add(collaboratorRS.getUser().getName());
        }

//        if(repo.getDesc().equals("")){
//            repoInfo.setDesc("temp desc");
//        }
        return repoInfo;
    }
//    有一些不是本地的仓库，打不开
    public List<String> lastUpdateByRepo(Repo repo){
        String dir = uploadFilePath + "/" + repo.getCreator().getMail() + "/" + repo.getName();
        String lastUpdate = "", branch = "";
        Date date1=null;
        try(Git git = JGitUtils.OpenRepository(dir)) {
            /*if(git == null){
                return null;
            }
            List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            try(RevWalk walk = new RevWalk(git.getRepository())){
                for (Ref bra : branches){
                    RevCommit commit = walk.parseCommit(bra.getObjectId());
                    Date date2 = commit.getCommitterIdent().getWhen();
                    if(date1 == null){
                        date1 = date2;
                        branch = bra.getName();
                    }else if(date1.compareTo(date2) < 0){
                        date1 = date2;
                        branch = bra.getName();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
            List<BranchInfo> branchInfos = JGitUtils.GetBranchList(git, String.valueOf(repo.getId()), uploadFilePath, repoDao);
            for (BranchInfo branchInfo : branchInfos) {
                List<VersionInfo> versionInfos = JGitUtils.getAllVersion(String.valueOf(repo.getId()), branchInfo.getName(), uploadFilePath, repoDao);
                if (versionInfos.size() > 0) {
                    if (lastUpdate.equals("")) {
                        lastUpdate = versionInfos.get(0).getSubmitDate();
                        branch = versionInfos.get(0).getBranchName();
                    } else if (versionInfos.get(0).getSubmitDate().compareTo(lastUpdate) > 0) {
                        lastUpdate = versionInfos.get(0).getSubmitDate();
                        branch = versionInfos.get(0).getBranchName();
                    }
                }
            }
        }
        List<String> str = new ArrayList<>();
//        System.out.println(lastUpdate+" "+branch);
        str.add(lastUpdate);str.add(branch);
        return str;
    }

    public List<RepoInfo> getRepoByUserID(long UserId) {
        Optional<User> userOptional = userDao.findById(UserId);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        List<Repo> repos = repoDao.findByCreator(user);
        List<RepoInfo> repoInfos = new ArrayList<>();
        for (Repo repo : repos) {
            if (repo.getState() == RepoState.PUBLIC) {
                repoInfos.add(getFullInfoOfRepo(repo));
            }
        }
        return repoInfos;
    }

    public List<RepoInfo> getRepoByUserName(String userName) {

        if(!InputChecker.checkNullAndEmpty(userName)) return null;
        User user = userDao.findByName(userName);
        if(user==null) return null;

        List<Repo> repos = repoDao.findByCreator(user);
        List<RepoInfo> repoInfos = new ArrayList<>();
        for (Repo repo : repos) {
            if (repo.getState() == RepoState.PUBLIC) {
                repoInfos.add(getFullInfoOfRepo(repo));
            }
        }
        return repoInfos;
    }
//    输入提出者的repoID, 以及提出者的branchname（然后搜该branch的最新commit）
    public String getPRDiff(String repoID, String branchName){
        long repoId;
        List<String> str = new ArrayList<>();
        str.add(repoID);str.add(branchName);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            return null;
        }
        Optional<ForkRS> forkRS = forkRSDao.findByToRepo_Id(repoId);
        if(forkRS.isEmpty()){
            return null;
        }
        String dir = getRepoDir(repoID);
        try(Git git = JGitUtils.OpenRepository(dir)){
            RevCommit latestCommit = git.log().setMaxCount(1).call().iterator().next();
            branchName = latestCommit.getName();
        }catch (Exception e){
            e.printStackTrace();
        }
        String old_commit = forkRS.get().getFromCommit();
//        System.out.printf("repoID %s, branchName : %s, old_commit : %s at RepoServiceMpl 707", repoID, old_commit, branchName);
        return getVersionDiff(repoID, old_commit, branchName);
    }


    public String getVersionDiff(String repoID, String Commit1, String Commit2){
        try{
            Repo repo = getRepoByID(repoID);
            if(repo==null) return null;
            String repoPath = getRepoDir(repo);
            if(repoPath == null) return null;
            Git git = JGitUtils.OpenRepository(repoPath);
            if(git==null) return null;
            List<DiffEntry> diffEntries = JGitUtils.getVersionDiff(git, Commit1, Commit2);
            if(diffEntries==null) return null;
            return JGitUtils.DiffToString(diffEntries,git);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateDescriById(String descri, String id){
        long repoId;
        if(InputChecker.checkNullAndEmpty(id) && InputChecker.checkNum(id)){
            repoId = Long.parseLong(id);
        }else{
            return false;
        }
        Repo repo = repoDao.findById(repoId);
        if(repo.getCreator().getId() != StpUtil.getLoginIdAsLong()){
            return false;
        }
        return repoDao.updateDescriById(descri, repoId)>0;
    }

    public Long getRepoIDByCreator_NameAndRepo_Name(String cName, String rName){
        List<String> str = new ArrayList<>();
        str.add(cName);str.add(rName);
        if(!InputChecker.checkNullAndEmpty(str)){
            return null;
        }
        User user = userDao.findByName(cName);
        if(user==null){
            logger.error("user not found");
            return null;
        }
        Repo repo = repoDao.findByNameAndCreator_Id(rName, user.getId());
        if(repo == null){
            return null;
        }
        return repo.getId();
    }

    public boolean updateNameByRepoID(String reName, String repoID){
        long repoId;
        List<String> str = new ArrayList<>();
        str.add(reName);str.add(repoID);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            return false;
        }
        Repo repo = repoDao.findById(repoId);
        if(repo.getCreator().getId() != StpUtil.getLoginIdAsLong()){
            return false;
        }
        int update = repoDao.updateNameById(reName, repoId);
        return update!= 0;
    }

    public boolean privateRepo(String repoID){
        long repoId;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            return false;
        }
        Repo repo = repoDao.findById(repoId);
        if(repo.getCreator().getId() != StpUtil.getLoginIdAsLong()){
            return false;
        }
        repoDao.updateStateById(RepoState.PRIVATE, repoId);
        return true;
    }

    public boolean publicRepo(String repoID){
        long repoId;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            return false;
        }
        Repo repo = repoDao.findById(repoId);
        if(repo.getCreator().getId() != StpUtil.getLoginIdAsLong()){
            return false;
        }
        repoDao.updateStateById(RepoState.PUBLIC, repoId);
        return true;
    }

    public boolean isText(String filePath){
        if(!InputChecker.checkNullAndEmpty(filePath)) return false;
        return FileUtils.isText(filePath);
    }

    public List<RepoInfo> fuzzySearch(String keyword){
        if(!InputChecker.checkNullAndEmpty(keyword)) return null;
        //空格切割keyword到一个arraylist
        String[] keywords = keyword.split(" ");
        List<Repo> repos = new ArrayList<>();
        //找出title/describe所有包含关键字的repo
        for(String key : keywords){
            key = key.trim().toLowerCase();
            repos.addAll(repoDao.findRepoByTitle(key));
            repos.addAll(repoDao.findRepoByDescribe(key));
        }
        //去重
        repos = repos.stream().distinct().collect(Collectors.toList());
        List<RepoInfo> repoInfos = new ArrayList<>();
        for (Repo repo:repos){
            if(repo.getState() == RepoState.PRIVATE){
                continue;
            }
            repoInfos.add(getFullInfoOfRepo(repo));
        }
        return repoInfos;
    }

    public boolean MergeBranch(String repoID, String branchFromName, String branchToName){
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoID,branchFromName,branchToName)))return false;
        String repoDir = getRepoDir(repoID);
        if(repoDir == null) return false;
        try(Git git = JGitUtils.OpenRepository(repoDir)){
            if(git == null) return false;
            return JGitUtils.Merge2Branch(git,branchFromName,branchToName);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBranch(String repoID, String branchName){
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(repoID,branchName)))return false;
        String repoDir = getRepoDir(repoID);
        if(repoDir == null) return false;
        try(Git git = JGitUtils.OpenRepository(repoDir)){
            if(git == null) return false;
            return JGitUtils.DeleteBranch(git,branchName);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
