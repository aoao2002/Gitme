package com.example.ooad.dao;

import com.example.ooad.bean.CollaboratorRS;
import com.example.ooad.bean.ForkRS;
import com.example.ooad.bean.Repo;
import com.example.ooad.bean.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForkRSDao extends CommonDao<ForkRS>{
    List<ForkRS> findByFromRepo(Repo fromRepo);

    List<ForkRS> findByToRepo(Repo toRepo);

    long countByFromRepo_Id(Long id);

    long countByFromRepo_IdAndToRepo_Creator_Id(Long id, Long id1);



    ForkRS findByFromRepoAndToRepo(Repo fromRepo, Repo toRepo);


    List<ForkRS> findByFromRepo_Id(Long id);

    Optional<ForkRS> findByToRepo_Id(Long id);

    ForkRS findByFromRepo_IdAndToRepo_Creator_Id(Long id, Long id1);

    ForkRS findByFromRepo_IdAndToRepo_Id(Long id, Long id1);

    long deleteByFromRepoAndToRepo(Repo fromRepo, Repo toRepo);

    void deleteByToRepo(Repo toRepo);
    void deleteByFromRepo(Repo fromRepo);

    @Override
    void deleteById(Long aLong);
}


