package com.example.ooad.dao;

import com.example.ooad.bean.ForkRS;
import com.example.ooad.bean.PRRS;
import com.example.ooad.bean.PRState;
import com.example.ooad.bean.Repo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PRRSDao extends CommonDao<PRRS>{
    List<PRRS> findByFromRepo_Id(Long id);

    List<PRRS> findByToRepo_Id(Long id);


    long deleteByFromRepoAndToRepo(Repo fromRepo, Repo toRepo);

    PRRS findByFromRepo_IdAndToRepo_Id(Long id, Long id1);

    @Transactional
    @Modifying
    @Query("update PRRS p set p.state = ?1 where p.id = ?2")
    int updateStateById(PRState state, Long id);

    @Transactional
    @Modifying
    @Query("update PRRS p set p.userName = ?1 where p.id = ?2")
    int updateUserNameById(String userName, Long id);

    @Transactional
    @Modifying
    @Query("update PRRS p set p.createTime = ?1 where p.id = ?2")
    int updateCreateTimeById(String createTime, Long id);



    void deleteByToRepo(Repo toRepo);
    void deleteByFromRepo(Repo fromRepo);


    long countByToRepo_Id(Long id);

    @Override
    Optional<PRRS> findById(Long aLong);
}
