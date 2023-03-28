package com.example.ooad.service.watch;

import cn.dev33.satoken.stp.StpUtil;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.bean.Repo;
import com.example.ooad.bean.User;
import com.example.ooad.bean.Watch;
import com.example.ooad.bean.WatchInfo;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.dao.UserDao;
import com.example.ooad.dao.WatchDao;
import com.example.ooad.dao.WatchInfoDao;
import com.example.ooad.service.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Watch service mpl.
 */
@Service
public class WatchServiceMpl implements WatchService{
    @Autowired
    private RepoDao repoDao;
    @Autowired
    private WatchDao watchDao;
    @Autowired
    private UserService userService;
    @Autowired
    private WatchInfoDao watchInfoDao;

    private static final Logger logger = LoggerFactory.getLogger(WatchServiceMpl.class);


    /** Check if the repo has been watched by current user
     * @param repoID ID of repo needed to check
     * @return true if has been watched false instead
     */
    @Override
    public boolean getWatchByMe(String repoID)  {
        // check if input is valid
        long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.parseLong(repoID);
        }else{
            return false;
        }
        // get repo by id and get current user
        Repo repo =  repoDao.findById(repoid);
        User user = userService.findById(StpUtil.getLoginIdAsLong());
        // check if the user has watched this repo
        Watch watchDaoByUser_idAndRepo_id = watchDao.findByRepoAndUser(repo, user);
        return watchDaoByUser_idAndRepo_id != null;
    }

    /**
     * Add Watch
     * @param repoID: ID of repo needed to add
     * @return successful or not
     */
    @Override
    public boolean addWatch(String repoID) {
        // check if input is valid
        long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.parseLong(repoID);
        }else{
            return false;
        }
        // get repo by id and get current user
        Repo repo =  repoDao.findById(repoid);
        User user = userService.findById(StpUtil.getLoginIdAsLong());
        if (repo==null || user==null){
            return false;
        }
        // check if the user has watched this repo
        Watch watchDaoByUser_idAndRepo_id = watchDao.findByRepoAndUser(repo, user);
        if (watchDaoByUser_idAndRepo_id != null){
            // Has Watched! Unable to re-watch
            return true;
        }
        // if not, add watch
        Watch watch = new Watch();
        watch.setCreatedDate(new Date());
        watch.setRepo(repo);
        watch.setUser(user);
        watchDao.save(watch);

        return true;
    }

    /** Delete Watch
     * @param repoID ID of repo needed to delete
     * @return successful or not
     */
    @Override
    public boolean deleWatch(String repoID) {
        // check if input is valid
        long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.parseLong(repoID);
        }else{
            return false;
        }
        // get repo by id and get current user
        Repo repo =  repoDao.findById(repoid);
        User user = userService.findById(StpUtil.getLoginIdAsLong());
        // check if the user has watched this repo
        Watch watchDaoByUser_idAndRepo_id = watchDao.findByRepoAndUser(repo, user);
        if (watchDaoByUser_idAndRepo_id == null){
            // Not watch! Unable to delete
            return true;
        }
        // if yes, delete watch
        watchDao.deleteWatchByRepoAndUser(repo, user);
        return true;
    }

    /** Get all Watch info of current user
     * @return ArrayList of WatchInfo
     */
    @Override
    public ArrayList<WatchInfo_> getWatchInfoByMe() {
        // get repo by id and get current user
        User user = userService.findById(StpUtil.getLoginIdAsLong());
        if (user==null){
            return null;
        }
        return (ArrayList<WatchInfo_>) watchDao.findByUser(user).stream()
                .map(watchInfoDao::findByWatch)
                .flatMap(Collection::stream)
                .map(m -> new WatchInfo_(
                        m.getCreatedDate(),
                        m.getId(),
                        m.getInfo(),
                        m.getWatch().getRepo().getId(),
                        m.getWatch().getUser().getId(),
                        m.getWatch().getRepo().getCreator().getName(),
                        m.getWatch().getRepo().getName(),
                        m.isHaveRead()))
                .filter(m -> !m.isRead)
                .collect(Collectors.toList());

    }

    @Override
    public boolean addWatchInfo(String info, String RepoId){
        long repoid;
        if(InputChecker.checkNullAndEmpty(RepoId) && InputChecker.checkNum(RepoId)){
            repoid = Long.parseLong(RepoId);
        }else{
            return false;
        }

        Repo repo = repoDao.findById(repoid);
        if(repo==null) return false;


        List<Watch> watches = watchDao.findByRepo(repo);
        if(watches==null || watches.size()==0) return false;

        for (Watch watch : watches){
            WatchInfo watchInfo = new WatchInfo();
            watchInfo.setWatch(watch);
            watchInfo.setInfo(info);
            watchInfo.setCreatedDate(new Date());
            watchInfo.setHaveRead(false);
            watchInfo.setId(watch.getId());
            watchInfoDao.save(watchInfo);
        }

        return true;
    }

    @Override
    public boolean setRead(String WatchInfoId) {
        long watchinfoid;
        logger.info("setRead: "+ WatchInfoId);
        if(InputChecker.checkNullAndEmpty(WatchInfoId) && InputChecker.checkNum(WatchInfoId)){
            watchinfoid = Long.parseLong(WatchInfoId);
        }else{
            logger.error("setRead: input error");
            return false;
        }
        WatchInfo watchInfo = watchInfoDao.findById(watchinfoid);
        if (watchInfo==null){
            logger.error("setRead: watchInfo not find");
            return false;
        }
        else {
            watchInfo.setHaveRead(true);
        }
        return true;
    }
}
