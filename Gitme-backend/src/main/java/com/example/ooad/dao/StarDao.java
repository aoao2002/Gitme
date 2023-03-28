package com.example.ooad.dao;

import com.example.ooad.bean.Repo;
import com.example.ooad.bean.Star;
import com.example.ooad.bean.User;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface StarDao extends CommonDao<Star>{


    @Override
    Optional<Star> findById(Long aLong);

    List<Star> findByUser_IdAndRepo_Id(Long id, Long id1);

    long countByUser_IdAndRepo_Id(Long id, Long id1);


//    Optional<Star> findByUser_IdAndRepo_Id(Long id, Long id1);


    List<Star> findByUser_Id(Long id);

    List<Star> findByRepo_Id(Long id);

    List<Star> findAllByRepo(Repo repo);


    @Override
    void deleteById(Long aLong);

    void deleteByRepo(Repo repo);

    long deleteByRepoAndUser(Repo repo, User user);


    long countByUser_Id(Long id);

    long countByRepo_Id(Long id);

    void deleteByIdAndUser(Long id, User user);






}
