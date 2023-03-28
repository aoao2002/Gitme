package com.example.ooad.service.PRRS;

import cn.dev33.satoken.stp.StpUtil;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.UTil.JGitUtils;
import com.example.ooad.bean.*;
import com.example.ooad.dao.ForkRSDao;
import com.example.ooad.dao.PRRSDao;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.dao.UserDao;
import com.example.ooad.service.Repo.BranchInfo;
import com.example.ooad.service.Repo.RepoService;
import com.example.ooad.service.watch.WatchService;
import com.example.ooad.service.watch.WatchServiceMpl;
import io.swagger.models.auth.In;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PRRSServiceMpl implements PRRSService {
    @Autowired
    private ForkRSDao forkRSDao;

    @Autowired
    private RepoDao repoDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PRRSDao prrsDao;

    @Autowired
    private RepoService repoService;

    @Autowired
    private WatchService watchService;


    @Value("${file.upload.dir}")
    private String uploadFilePath;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public Long makePR(String  repoID, String title, String toBranch, String fromBranch){
        long repoId;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            return null;
        }
        Optional<ForkRS> forkRSOptional = forkRSDao.findByToRepo_Id(repoId);
        if(forkRSOptional.isEmpty()){
            return null;
        }
        Optional<User> user = userDao.findById(StpUtil.getLoginIdAsLong());
        Repo fromRepo = forkRSOptional.get().getFromRepo(), toRepo = repoDao.findById(repoId);
        PRRS prrs = new PRRS();
        prrs.setCreateTime(simpleDateFormat.format(new Date()));
        prrs.setCreatedDate(new Date());
        prrs.setTitle(title);
        prrs.setFromRepo(toRepo);
        prrs.setToRepo(fromRepo);
        prrs.setState(PRState.UNPROCESS);
//        prrs.setBranchName(repoService.lastUpdateByRepo(toRepo).get(1));
        prrs.setBranchName(toBranch);
        prrs.setFromBranch(fromBranch);
        prrs.setUserName(user.get().getName());
        prrs.setForkBranch(forkRSOptional.get().getForkBranch());
        prrsDao.save(prrs);
        return prrs.getId();
    }

    public boolean deletePR(String forkRepoId, String originRepoId){
        long forkRepoID, originRepoID;
        List<String> str = new ArrayList<>();
        str.add(forkRepoId);str.add(originRepoId);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(str)){
            forkRepoID = Long.parseLong(forkRepoId);
            originRepoID = Long.parseLong(originRepoId);
        }else{
            return false;
        }
        Repo forkRepo = repoDao.findById(forkRepoID), originRepo = repoDao.findById(originRepoID);
        if(forkRepo == null || originRepo == null){
            return false;
        }
        long delete = prrsDao.deleteByFromRepoAndToRepo(forkRepo, originRepo);
        return delete > 0;
    }

    public PRRSInfo getPRByID(String PRID){
        long prID;
        if(InputChecker.checkNullAndEmpty(PRID) && InputChecker.checkNum(PRID)){
            prID = Long.parseLong(PRID);
        }else{
            return null;
        }
        Optional<PRRS> prrsOptional = prrsDao.findById(prID);
        if(prrsOptional.isEmpty()){
            return null;
        }
        PRRS prrs = prrsOptional.get();
//        if(prrs.getState() != PRState.UNPROCESS)return null;
//            System.out.printf("pr id %d, pr to %d. from %d", prrs.getId(), prrs.getToRepo().getId(), prrs.getFromRepo().getId());
        PRRSInfo prrsInfo = new PRRSInfo(prrs);
        ForkRS forkRS = forkRSDao.findByFromRepo_IdAndToRepo_Id(prrs.getToRepo().getId(), prrs.getFromRepo().getId());
        if(forkRS == null){
            return null;
        }
        if(prrs.getTitle() == null){
            prrsInfo.setTitle("no provide");
        }
        supplePRRSInfo(prrsInfo, prrs);
        prrsInfo.setDiff(repoService.getPRDiff(String.valueOf(prrs.getFromRepo().getId()), forkRS.getForkBranch()));
//        prrsInfo.setToBranch(forkRS.getFromBranch());
        return prrsInfo;
    }
    public List<PRRSInfo> getAllPR(String  repoID){
        // 返回这个repo的所有PR信息
        // 每一个对象包含发起的repoID，对应的commitID
        long repoId;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            System.out.println("no repoID at PRRSServiceMpl.126");
            return null;
        }

        List<PRRS> prrsList = prrsDao.findByToRepo_Id(repoId);
        List<PRRSInfo> prrsInfoList = new ArrayList<>();
        for(PRRS prrs : prrsList){
//            if(prrs.getState() != PRState.UNPROCESS)continue;
//            System.out.printf("pr id %d, pr to %d. from %d", prrs.getId(), prrs.getToRepo().getId(), prrs.getFromRepo().getId());
            PRRSInfo prrsInfo = new PRRSInfo(prrs);
            ForkRS forkRS = forkRSDao.findByFromRepo_IdAndToRepo_Id(repoId, prrs.getFromRepo().getId());
            if(forkRS == null){
                continue;
            }
            if(prrs.getTitle() == null){
                prrsInfo.setTitle("no provide");
            }
            supplePRRSInfo(prrsInfo, prrs);
//            prrsInfo.setToBranch(forkRS.getFromBranch());
            prrsInfo.setDiff(repoService.getPRDiff(String.valueOf(prrs.getFromRepo().getId()), forkRS.getForkBranch()));
//            System.out.println(forkRS.getFromBranch());
            prrsInfoList.add(prrsInfo);
        }
        return prrsInfoList;
    }
    public void supplePRRSInfo(PRRSInfo prrsInfo, PRRS prrs){
        prrsInfo.setFromRepoName(prrs.getFromRepo().getName());
        prrsInfo.setFromBranch(prrs.getFromBranch());
        prrsInfo.setToBranch(prrs.getBranchName());
        prrsInfo.setFromRepoId(prrs.getFromRepo().getId());
        prrsInfo.setToRepoId(prrs.getToRepo().getId());
    }
    public List<PRRSInfo> getALLPR_test(){
        List<PRRS> prrsList = prrsDao.findAll();
        List<PRRSInfo> prrsInfoList = new ArrayList<>();
        for(PRRS prrs : prrsList){
            long repoId = prrs.getFromRepo().getId();
            PRRSInfo prrsInfo = new PRRSInfo(prrs);
            Optional<ForkRS> forkRS = forkRSDao.findByToRepo_Id(repoId);
            if(forkRS.isEmpty()){
                continue;
            }
//            if(prrs.getState() != PRState.UNPROCESS && prrs.getState() != PRState.ACCEPT && prrs.getState() != PRState.REJECT){
//                prrsDao.updateStateById(PRState.UNPROCESS, prrs.getId());
//            }
//            prrsDao.updateStateById(PRState.UNPROCESS, prrs.getId());
            if(prrs.getCreateTime() == null){
                prrsDao.updateCreateTimeById(simpleDateFormat.format(prrs.getCreatedDate()),prrs.getId());
            }
            String name = prrs.getFromRepo().getCreator().getName();
            prrsDao.updateUserNameById(name, prrs.getId());
            prrsInfo.setFromBranch(prrs.getFromBranch());
            prrsInfo.setFromRepoId(repoId);
//            prrsInfo.setToBranch(forkRS.get().getFromBranch());
            prrsInfo.setToBranch(prrs.getBranchName());
            prrsInfo.setToRepoId(prrs.getToRepo().getId());
//            这里会发现有的repo打不开，所以会出现问题，数据库好像就不会执行操作
            prrsInfo.setDiff(repoService.getPRDiff(String.valueOf(repoId), forkRS.get().getFromBranch()));
            System.out.println(forkRS.get().getFromBranch());
            prrsInfoList.add(prrsInfo);
        }
        return prrsInfoList;
    }

    public void deleteAllPR(){
        prrsDao.deleteAll();
    }

    public boolean acceptPR(String PRID){
        long prId;
        if(InputChecker.checkNullAndEmpty(PRID) && InputChecker.checkNum(PRID)){
            prId = Long.parseLong(PRID);
        }else{
            return false;
        }
        Optional<PRRS> prrs = prrsDao.findById(prId);
        if(prrs.isEmpty()){
            return false;
        }
        String PRTitle=prrs.get().getTitle();
        String repoID = String.valueOf(prrs.get().getToRepo().getId());
        long prMakerRepoId = prrs.get().getFromRepo().getId(), prAcceptRepoId = prrs.get().getToRepo().getId();
        ForkRS forkRS = forkRSDao.findByFromRepo_IdAndToRepo_Id(prAcceptRepoId, prMakerRepoId);
        if(forkRS == null){
            return false;
        }
        Repo PRToRepo= forkRS.getFromRepo(), PRFromRepo  = forkRS.getToRepo();
        String fromRepoPath = repoService.getRepoDir(PRFromRepo);
        String toRepoPath = repoService.getRepoDir(PRToRepo);
//        PRRS prrs = prrsDao.findByFromRepo_IdAndToRepo_Id(prMakerRepoId, prAcceptRepoId);
//        if (prrs == null) {
//            return false;
//        }
        boolean PRIsSuccess =JGitUtils.PRMerge(toRepoPath, fromRepoPath, prrs.get().getFromBranch(),PRTitle,PRID);//to+from
        if(PRIsSuccess){
            String newBranch = prrs.get().getFromBranch()+"_"+PRID+"_feature";
            if (repoService.MergeBranch(repoID, newBranch, prrs.get().getFromBranch())) {
                prrsDao.updateStateById(PRState.ACCEPT, prrs.get().getId());
                watchService.addWatchInfo(PRToRepo.getName()+" accept a new PR", PRToRepo.getId()+"");
                repoService.deleteBranch(repoID, newBranch);
            } 
            else {
                return false;
            }
        }
        return PRIsSuccess;
    }

    public boolean rejectPR(long PRRSId){
        prrsDao.updateStateById(PRState.REJECT, PRRSId);
        return true;
    }
}
