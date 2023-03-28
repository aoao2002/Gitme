package com.example.ooad.service.ForkRS;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.io.FileUtil;
import com.example.ooad.UTil.FileUtils;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.UTil.JGitUtils;
import com.example.ooad.bean.ForkRS;
import com.example.ooad.bean.Repo;
import com.example.ooad.bean.User;
import com.example.ooad.dao.ForkRSDao;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.dao.UserDao;
import com.example.ooad.service.Repo.RepoService;
import com.example.ooad.service.Repo.VersionInfo;
import com.example.ooad.service.User.UserService;
import io.swagger.models.auth.In;
import org.apache.commons.compress.utils.Lists;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ForkRSServiceMpl implements ForkRSService{
    @Autowired
    private ForkRSDao forkRSDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RepoService repoService;
    @Autowired
    private RepoDao repoDao;

    @Value("${file.upload.dir}")
    private String uploadFilePath;

    private String fromBranch;
    private String fromCommit;

    public void getFromBranchAndCommit(Git git) {
        try{
            this.fromBranch = git.getRepository().getBranch();
            RevCommit latestCommit = git.log().setMaxCount(1).call().iterator().next();
            this.fromCommit = latestCommit.getName();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    如果fork中途断了，没有fork成功，但是仓库已经创建了，之后再fork的话就会报错，因为仓库已经存在了，这个方面可能还要再考虑一下
    public boolean fork(String repoID) throws IOException, GitAPIException {
        long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.parseLong(repoID);
        }else{
            return false;
        }
        return forkMpl(repoid, null, null);
//注意输入路径要绝对
    }

    /**
     * https://www.eclipse.org/forums/index.php/t/1065371/ clone 特定branch的
     * @param repoID
     * @param rename
     * @param desc
     * @return
     * @throws IOException
     * @throws GitAPIException
     */
    public boolean fork(String repoID, String rename, String desc) throws IOException, GitAPIException{
        long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.parseLong(repoID);
        }else{
            return false;
        }
        return forkMpl(repoid, rename, desc);
    }
    public boolean forkMpl(long repoid, String rename, String desc) throws IOException, GitAPIException {
        User user = userService.findById(String.valueOf(StpUtil.getLoginId()));
        Repo repo = repoDao.findById(repoid);
        if(repo.getCreator().getId() == user.getId()){
            return false;
        }
        String new_dir = uploadFilePath+"/"+user.getMail()+"/";
        long repoNum = repoDao.countByNameAndCreator_Id(rename, StpUtil.getLoginIdAsLong());
        if(repoNum != 0){
            return false;
        }
        if(rename == null || rename.trim().equals("")){
            new_dir += repo.getName();
        }else{
            new_dir += rename;
        }
        String old_dir = uploadFilePath+"/"+repo.getCreator().getMail()+"/"+repo.getName();
        File directory = new File("");// 参数为空
        String localRepoDir = directory.getCanonicalPath();
//        System.out.printf("localRepoDir : %s\n", localRepoDir);
        old_dir = localRepoDir.replace("\\", "/") + "/" + old_dir;

        File new_file = new File(new_dir);
//        clone
        /*try(Git result = Git.cloneRepository()
                .setURI(old_dir)
                .setDirectory(new_file)
                .setCloneAllBranches(true)
//                .setProgressMonitor(new SimpleProgressMonitor())
                .call()){
        }*/
//        复制前者到后者
        old_dir += "/.git";

        String dir = JGitUtils.findDir(String.valueOf(repoid), uploadFilePath, repoDao);
        try(Git git = JGitUtils.OpenRepository(dir)){
            Iterable<RevCommit> logs = git.log().call();
            int cnt = 0;
            for(RevCommit revCommit : logs){
                cnt++;
            }
            if(cnt != 0){
                getFromBranchAndCommit(git);
            }
        }
        Repo repo2 = new Repo();
        Date date = new Date();
        repo2.setCreatedDate(date);
        if(rename == null || rename.trim().equals("")){
            repo2.setName(repo.getName());
        }else{
            repo2.setName(rename);
        }
        if(desc == null || desc.trim().equals("")){
            repo2.setDescri(repo.getDescri());
        }else{
            repo2.setDescri(desc);
        }
        repo2.setState(repo.getState());repo2.setCreator(user);
        repoDao.save(repo2);
        ForkRS forkRS = new ForkRS();
        forkRS.setFromRepo(repoDao.findById(repoid));
        forkRS.setToRepo(repo2);
        forkRS.setCreatedDate(date);
        forkRS.setForkBranch(this.fromBranch);
        forkRS.setFromBranch(this.fromBranch);
        forkRS.setFromCommit(this.fromCommit);
        if(forkRS.getFromCommit() != null){
            FileUtil.copy(Paths.get(old_dir), Paths.get(new_dir));
        }
        forkRSDao.save(forkRS);
        return true;
    }


    @Override
    public boolean unFork(String repoID) throws Exception {
        long repoId;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            return false;
        }
        String name = repoDao.findById(repoId).getName();
        if(userDao.findById(StpUtil.getLoginIdAsLong()).isEmpty()){
            return false;
        }
        User user = userDao.findById(StpUtil.getLoginIdAsLong()).get();
        Optional<ForkRS> forkRS = forkRSDao.findByToRepo_Id(repoId);
        if(forkRS.isEmpty()){
            return false;
        }
        Long forkId = forkRS.get().getId();
        Repo repoOptional = repoDao.findById(repoId);
//        删除三部曲：fork， repo， 本地仓库
        forkRSDao.deleteById(forkId);
        String dir = uploadFilePath + "/" + user.getMail() + "/" + repoOptional.getName();
        repoDao.delete(repoOptional);
        JGitUtils.CleanRepository(dir);//problem
        return true;
    }

    private static class SimpleProgressMonitor implements ProgressMonitor {
        @Override
        public void start(int totalTasks) {
            System.out.println("Starting work on " + totalTasks + " tasks");
        }

        @Override
        public void beginTask(String title, int totalWork) {
            System.out.println("Start " + title + ": " + totalWork);
        }

        @Override
        public void update(int completed) {
            System.out.print(completed + "-");
        }

        @Override
        public void endTask() {
            System.out.println("Done");
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }

    @Override
    public List<String> getForkByFromRepoID(String repoID) {
        long repoId;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            return null;
        }
        List<ForkRS> forkRS = forkRSDao.findByFromRepo_Id(repoId);
        List<String> forks = new ArrayList<>();
        for(ForkRS forkRS1 : forkRS){
            forks.add(String.valueOf(forkRS1.getToRepo().getId()));
        }
        return forks;
    }

    @Override
    public String getForkByToRepoID(String repoID) {
        long repoId;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            return null;
        }
        if(forkRSDao.findByToRepo_Id(repoId).isEmpty()){
            return null;
        }
        return String.valueOf(forkRSDao.findByToRepo_Id(repoId).get().getFromRepo().getId());
    }

    @Override
    public List<ForkRSInfo> getAllForks() {
        List<ForkRS> forkRSList = forkRSDao.findAll();
        List<ForkRSInfo> forkRSInfoList = new ArrayList<>();
        for(ForkRS forkRS : forkRSList){
            forkRSInfoList.add(new ForkRSInfo(forkRS));
        }
        return forkRSInfoList;
    }

}
