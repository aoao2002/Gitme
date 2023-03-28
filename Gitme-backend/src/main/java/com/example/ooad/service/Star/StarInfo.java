package com.example.ooad.service.Star;

import com.example.ooad.bean.Star;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
public class StarInfo {
    Long ID;
    Long userID;
    Long repoID;
    public StarInfo(){}
    public StarInfo(Long userID, Long repoID){
        this.userID = userID;
        this.repoID = repoID;
    }
    public StarInfo(Optional<Star> star2){
        if(star2.isPresent()){
            Star star = star2.get();
            this.ID = star.getId();
            this.repoID = star.getRepo().getId();
            this.userID = star.getUser().getId();
        }else{
            System.out.println("star is null at starInfo 27");
        }
    }
    public StarInfo(Star star){
        this.ID = star.getId();
        this.repoID = star.getRepo().getId();
        this.userID = star.getUser().getId();
    }

}
