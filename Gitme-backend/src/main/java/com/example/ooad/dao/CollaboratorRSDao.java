package com.example.ooad.dao;


import com.example.ooad.bean.CollaboratorRS;
import com.example.ooad.bean.Repo;
import com.example.ooad.bean.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollaboratorRSDao extends CommonDao<CollaboratorRS>{
    List<CollaboratorRS> findByUser(User user);

    List<CollaboratorRS> findByRepo(Repo repo );

    List<CollaboratorRS> findByRepo_Id(Long id);


    CollaboratorRS findByUser_IdAndRepo_Id(Long id, Long id1);

    long deleteByUserAndRepo(User user, Repo repo);

    void deleteByRepo(Repo repo);


}
