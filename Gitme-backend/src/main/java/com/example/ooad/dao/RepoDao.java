package com.example.ooad.dao;

import com.example.ooad.bean.Repo;
import com.example.ooad.bean.RepoState;
import com.example.ooad.bean.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RepoDao extends CommonDao<Repo> {

    Repo findById(long id);

    List<Repo> findByName(String name);

    List<Repo> findByState(RepoState state);

    Repo findByNameAndCreator(String name, User user);

    Repo findByNameAndCreator_Id(String name, Long id);

    long countByNameAndCreator_Id(String name, Long id);

    //模糊搜索title包含不区分大小写的keyword的repo
    @Query(value = "select * from repo where lower(name) like %?1%", nativeQuery = true)
    List<Repo> findRepoByTitle(String keyword);

    //模糊搜索describe包含不区分大小写的keyword的repoID
    @Query(value = "select * from repo where lower(descri) like %?1%", nativeQuery = true)
    List<Repo> findRepoByDescribe(String keyword);

    List<Repo> findByCreator(User user);

    @Transactional
    @Modifying
    @Query("update Repo r set r.name = ?1 where r.id = ?2")
    int updateNameById(String name, Long id);


    @Transactional
    @Modifying
    @Query("update Repo r set r.descri = ?1 where r.id = ?2")
    int updateDescriById(String descri, Long id);

    @Transactional
    @Modifying
    @Query("update Repo r set r.state = ?1 where r.id = ?2")
    int updateStateById(RepoState state, Long id);


}
