package com.example.ooad.dao;

import com.example.ooad.bean.Repo;
import com.example.ooad.bean.User;
import com.example.ooad.bean.Watch;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface WatchDao extends CommonDao<Watch> {
    List<Watch> findByRepo(Repo repo);
    void deleteByRepo(Repo repo);
    Watch findByRepoAndUser(Repo repo, User user);
    ArrayList<Watch> findByUser(User user);
    void deleteWatchByRepoAndUser(Repo repo, User user);
}
