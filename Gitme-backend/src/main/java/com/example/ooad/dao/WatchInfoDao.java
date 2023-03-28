package com.example.ooad.dao;

import com.example.ooad.bean.Watch;
import com.example.ooad.bean.WatchInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchInfoDao extends CommonDao<WatchInfo>{

    WatchInfo findById(long id);
    List<WatchInfo> findByWatch(Watch watch);
    void deleteByWatch(Watch watch);
}
