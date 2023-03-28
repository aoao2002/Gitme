package com.example.ooad.controller;


import cn.dev33.satoken.util.SaResult;
import com.example.ooad.UTil.Handler.ReturnHelper;
import com.example.ooad.service.Star.StarInfo;
import com.example.ooad.service.Star.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Controller
@RestController
@RequestMapping("/star/")
public class StarCtrl {
    @Autowired
    private StarService starService;

    @RequestMapping(value="getAllStars", method = RequestMethod.GET)
    public SaResult getAllStars(){
        return ReturnHelper.returnObj(starService.getAllStars());
    }

    @RequestMapping(value = "getStarByID", method = RequestMethod.GET)
    public SaResult getStarByID(String ID){
        return ReturnHelper.returnObj(starService.getStarByID(ID));
    }

    @RequestMapping(value = "getStarByUser_IDAndRepo_ID", method = RequestMethod.GET)
    public SaResult getStarByUser_IDAndRepo_ID(String userID, String repoID){
        return ReturnHelper.returnObj(starService.getStarByUser_IDAndRepo_ID(userID, repoID));
    }

    @RequestMapping(value = "getStarsByUserID", method = RequestMethod.GET)
    public SaResult getStarsByUserID(String userID){
        return ReturnHelper.returnObj(starService.getStarsByUserID(userID));
    }

    @RequestMapping(value = "getStarsByRepoID", method = RequestMethod.GET)
    public SaResult getStarsByRepoID(String repoID){
        return ReturnHelper.returnObj(starService.getStarsByRepoID(repoID));
    }

    @RequestMapping(value = "getStarsNumByUserID", method = RequestMethod.GET)
    public SaResult getStarsNumByUserID(String userID){
        return ReturnHelper.returnObj(starService.getStarsNumByUserID(userID));
    }

    @RequestMapping(value = "getStarsNumByRepoID", method = RequestMethod.GET)
    public SaResult getStarsNumByRepoID(String repoID){
        return ReturnHelper.returnObj(starService.getStarsNumByRepoID(repoID));
    }

    @RequestMapping(value = "addStar", method = RequestMethod.POST)
    public SaResult addStar(String repoID){
        return ReturnHelper.returnBool(starService.addStar(repoID));
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
    public SaResult deleteById(String ID){
        return ReturnHelper.returnBool(starService.deleteByID(ID));
    }

    @RequestMapping(value = "deleteAllStars", method = RequestMethod.DELETE)
    public SaResult deleteAllStars(){
        return ReturnHelper.returnObj(starService.deleteAllStar());
    }

    @RequestMapping(value = "deleteStarsByRepo", method = RequestMethod.DELETE)
    public SaResult deleteStarsByRepo(String repoID){
        return ReturnHelper.returnBool(starService.deleteByRepoID(repoID));
    }

    @RequestMapping(value = "deleteByUserID", method = RequestMethod.DELETE)
    public SaResult deleteStarsByUser(String userID){
        return ReturnHelper.returnObj(starService.deleteByUserID(userID));
    }

}
