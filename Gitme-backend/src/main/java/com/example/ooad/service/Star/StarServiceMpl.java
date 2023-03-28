package com.example.ooad.service.Star;

import com.example.ooad.bean.Repo;
import com.example.ooad.bean.Star;
import com.example.ooad.bean.User;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.dao.StarDao;
import com.example.ooad.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cn.dev33.satoken.stp.StpUtil;
import com.example.ooad.UTil.Handler.InputChecker;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

@Service
public class StarServiceMpl implements StarService{
    @Autowired
    private StarDao starDao;

    @Autowired
    private RepoDao repoDao;

    @Autowired
    private UserDao userDao;

    @Override
    public StarInfo getStarByID(String ID) {
        Long id;
        if(InputChecker.checkNullAndEmpty(ID) && InputChecker.checkNum(ID)){
            id = Long.valueOf(ID);
        }else{
            return null;
        }
        Optional<Star> star = starDao.findById(id);
        if(star.isEmpty()){
            return null;
        }
        return new StarInfo(star);
    }

    @Override
    public StarInfo getStarByUser_IDAndRepo_ID(String userID, String repoID) {
        List<String> str = new ArrayList<>();
        str.add(userID);str.add(repoID);
        Long userid, repoid;
        if(InputChecker.checkNullAndEmpty(str) && InputChecker.checkNum(str)){
            userid = Long.valueOf(userID);
            repoid = Long.valueOf(repoID);
        }else{
            return null;
        }
        List<Star> star = starDao.findByUser_IdAndRepo_Id(userid, repoid);
        if(star.isEmpty()){
            return null;
        }
        return new StarInfo(star.get(0));
    }

    @Override
    public List<StarInfo> getStarsByRepoID(String repoID) {
        Long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.valueOf(repoID);
        }else{
            return null;
        }
        List<Star> stars = starDao.findByRepo_Id(repoid);
        List<StarInfo> starInfos = new ArrayList<>();
        for(Star star : stars){
            starInfos.add(new StarInfo(star));
        }
        return starInfos;
    }

    @Override
    public Long getStarsNumByRepoID(String repoID) {
        Long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.valueOf(repoID);
        }else{
            return null;
        }
        List<Star> stars = starDao.findByRepo_Id(repoid);
        if(stars.size()==0){
            return null;
        }
        return Long.parseLong(String.valueOf(stars.size()));
    }

    @Override
    public List<StarInfo> getStarsByUserID(String userID) {
        Long userid;
        if(InputChecker.checkNullAndEmpty(userID) && InputChecker.checkNum(userID)){
            userid = Long.valueOf(userID);
        }else{
            return null;
        }
        List<Star> stars = starDao.findByUser_Id(userid);
        List<StarInfo> starInfos = new ArrayList<>();
        for(Star star : stars){
            starInfos.add(new StarInfo(star));
        }
        return starInfos;
    }

    @Override
    public Long getStarsNumByUserID(String userID) {
        Long userid;
        if(InputChecker.checkNullAndEmpty(userID) && InputChecker.checkNum(userID)){
            userid = Long.valueOf(userID);
        }else{
            return null;
        }
        List<Star> stars = starDao.findByUser_Id(userid);
        if(stars.size()==0){
            return null;
        }
        return Long.parseLong(String.valueOf(stars.size()));
    }

    @Override
    public List<StarInfo> getAllStars() {
        List<Star> stars = starDao.findAll();
        List<StarInfo> starInfos = new ArrayList<>();
        for(Star star : stars){
            starInfos.add(new StarInfo(star));
        }
        return starInfos;
    }

    @Override
    public boolean addStar(String repoID) {
        Long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.valueOf(repoID);
        }else{
            return false;
        }
        List<Star> s = starDao.findByUser_IdAndRepo_Id(StpUtil.getLoginIdAsLong(), repoid);
        if(!s.isEmpty()){
            return true;
        }
        Star star = new Star();
        Optional<Repo> repo = repoDao.findById(repoid);
        Optional<User> user = userDao.findById(StpUtil.getLoginIdAsLong());
        if(user.isEmpty() || repo.isEmpty()){
            return false;
        }
        star.setRepo(repo.get());
        star.setUser(user.get());
        starDao.save(star);
        return true;
    }

    @Override
    public boolean deleteByID(String ID) {
        Long id;
        if(InputChecker.checkNullAndEmpty(ID) && InputChecker.checkNum(ID)){
            id = Long.valueOf(ID);
        }else{
            return true;
        }

        starDao.deleteById(id);
        return true;
    }

    @Override
    public Long deleteAllStar() {
        long num1 = starDao.count();
        starDao.deleteAll();
        long num2 = starDao.count();
        if(num1-num2==0){
            return null;
        }
        return num1-num2;
    }

    @Override
    public boolean deleteByRepoID(String repoID) {
        Long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.valueOf(repoID);
        }else{
            return false;
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()){
            return false;
        }
        Optional<Repo> repo = repoDao.findById(repoid);
        if(repo.isEmpty()){
            return false;
        }
        starDao.deleteByRepoAndUser(repo.get(), user.get());
        return true;
    }

    @Override
    public Long deleteByUserID(String userID) {
        Long userid;
        if(InputChecker.checkNullAndEmpty(userID) && InputChecker.checkNum(userID)){
            userid = Long.valueOf(userID);
        }else{
            return null;
        }
        long num1 = starDao.countByUser_Id(userid);
        List<Star> stars = starDao.findByUser_Id(userid);
        for(Star star:stars){
            starDao.deleteById(star.getId());
        }
        long num2 = starDao.countByUser_Id(userid);
        return num1-num2;
    }

    @Override
    public boolean deleteByID(long id) {
        return false;
    }
}
