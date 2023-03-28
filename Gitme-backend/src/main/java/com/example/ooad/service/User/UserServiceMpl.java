package com.example.ooad.service.User;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.bean.CollaboratorRS;
import com.example.ooad.bean.Repo;
import com.example.ooad.bean.User;
import com.example.ooad.controller.UserCtrl;
import com.example.ooad.dao.CollaboratorRSDao;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.dao.UserDao;
import com.example.ooad.service.Repo.RepoService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceMpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private RepoDao repoDao;

    @Autowired
    private CollaboratorRSDao collaboratorRSDao;
    public void save(User user)
    {
        userDao.save(user);
    }

    @Override
    public UserInfo getUserByMail(String mail) {
        // check input
        if(!InputChecker.checkNullAndEmpty(mail)) return null;
        Optional<User> userOptional = userDao.findByMail(mail);
        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();
        return new UserInfo(user);
    }

    public UserInfo getUserByName(String name) {
        // check input
        if (name==null){
            return null;
        }
        if ("".equals(name)){
            return null;

        }

        User user = userDao.findByName(name);
        if(user==null) return null;

        return new UserInfo(user);
    }

    @Override
    public SaResult LoginMail(String mail, String pwd) {
        // check input
        if (mail==null  || pwd==null){
            return SaResult.error("Login error: input null");
        }
        if ("".equals(mail) || "".equals(pwd)){
            return SaResult.error("Login error: input is empty");
        }
        Optional<User> userOptional = userDao.findByMail(mail);
        if (userOptional.isEmpty()) return SaResult.error("login fail: no such user");
        //check pw
        User user = userOptional.get();
        UserInfo userInfo = new UserInfo(user);
        if (user.getMail().equals(mail) && user.getPw().equals(pwd)){
            // login
            StpUtil.login(user.getId());
            // get token
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

            return SaResult.ok().setData(new LoginInfo(tokenInfo,userInfo.getName()));
        }else {
            return SaResult.error("login fail: pwd error");
        }

    }

    public SaResult addUser(String name, String mail, String pwd) {
        // check input
        if (name==null || mail==null  || pwd==null){
            return SaResult.error("register error: input null");
        }
        if ("".equals(name) || "".equals(mail) || "".equals(pwd)){
            return SaResult.error("register error: input is empty");

        }
        // check mail
        if (!mail.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            return SaResult.error("register error: mail format error");

        }

        // check if user mail exist
        Optional<User> userOptional = userDao.findByMail(mail);


        if (userOptional.isEmpty() && addUserNameChecker(name)) {
            User user = new User();
            user.setName(name);
            user.setPw(pwd);
            user.setMail(mail);

            userDao.save(user);
            return SaResult.ok("register success");
        }
        return SaResult.error("register error: user already exist");
    }

    public boolean addUserNameChecker(String name){
        if(!InputChecker.checkNullAndEmpty(name)) return false;

        // check if user mail exist
        User user = userDao.findByName(name);

        return user == null;

    }

    public List<UserInfo> getAllUser(){
        List<User> allUser = userDao.findAll();
        ArrayList<UserInfo> allUserInfo = new ArrayList<>();
        allUser.forEach(user -> {
            allUserInfo.add(new UserInfo(user));
        });
        return allUserInfo;
    }

    public User findById(String userID){
        if (!InputChecker.checkNullAndEmpty(Lists.newArrayList(userID)))
            return null;

        return findById(Long.parseLong(userID));
    }

    public User findByMail(String Mail){
        Optional<User> us = userDao.findByMail(Mail);
        if(us.isEmpty()){
            return null;
        }
        return us.get();
    }

    public User findById(long userID){
        Optional<User> us = userDao.findById(userID);
        if(us.isEmpty()){
            return null;
        }
        return us.get();
    }

    public boolean editMyInfo(UserInfo userInfo){
        if (userInfo==null) return false;
        User me = getMe();
        if(me==null) return false;
        userDao.save(userInfo.changeUser(me));
        return true;
    }

    public boolean editMyPw(String pw){
        if (!InputChecker.checkNullAndEmpty(pw)) return false;
        User me = getMe();
        if(me==null) return false;
        me.setPw(pw);
        userDao.save(me);
        return true;

    }

    @Override
    public boolean invite(String userName, String repoID) {
        Long repoId;
        List<String> str = new ArrayList<>();
        str.add(userName);str.add(repoID);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(repoID)){
            repoId = Long.valueOf(repoID);
        }else{
            return false;
        }
        User user = userDao.findByName(userName);
        Optional<Repo> repo = repoDao.findById(repoId);
        if(user == null || repo.isEmpty()){
            return false;
        }
        CollaboratorRS collaboratorRS = new CollaboratorRS();
        collaboratorRS.setUser(user);
        collaboratorRS.setRepo(repo.get());
        collaboratorRSDao.save(collaboratorRS);
        return true;
    }

    @Override
    public boolean unInvite(String userName, String repoID) {
        Long repoId;
        List<String> str = new ArrayList<>();
        str.add(userName);str.add(repoID);
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(repoID)){
            repoId = Long.valueOf(repoID);
        }else{
            return false;
        }
        User user = userDao.findByName(userName);
        Optional<Repo> repo = repoDao.findById(repoId);
        if(user == null || repo.isEmpty()){
            return false;
        }
        return collaboratorRSDao.deleteByUserAndRepo(user, repo.get()) > 0;
    }

    public List<UserInfo> getAllCollaByRepoID(String repoID){
        Long repoId;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoId = Long.valueOf(repoID);
        }else{
            return null;
        }
        Optional<Repo> repo = repoDao.findById(repoId);
        if(repo.isEmpty()){
            return null;
        }
        List<CollaboratorRS> collaboratorRSList = collaboratorRSDao.findByRepo(repo.get());
        List<UserInfo> userInfoList = new ArrayList<>();
        for(CollaboratorRS collaboratorRS : collaboratorRSList){
            User user = collaboratorRS.getUser();
            userInfoList.add(new UserInfo(user));
        }
        userInfoList.add(new UserInfo(userDao.findById(StpUtil.getLoginIdAsLong()).get()));
        return userInfoList;
    }



    private User getMe(){
        logger.info("getMe");
        return findById(StpUtil.getLoginIdAsLong());
    }

}
