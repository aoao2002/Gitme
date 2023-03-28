package com.example.ooad.service.Star;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StarService {
    /*
    * find
    * */
    StarInfo getStarByID(String ID);
    StarInfo getStarByUser_IDAndRepo_ID(String userID, String repoID);
    List<StarInfo> getStarsByRepoID(String repoID);
    Long getStarsNumByRepoID(String repoID);
    List<StarInfo> getStarsByUserID(String userID);
    Long getStarsNumByUserID(String userID);
    List<StarInfo> getAllStars();
    /*
    add
     */
    boolean addStar(String repoID);

    boolean deleteByID(String ID);
    Long deleteAllStar();
    boolean deleteByRepoID(String repoID);
    Long deleteByUserID(String userID);


    boolean deleteByID(long id);

}
