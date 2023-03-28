package com.example.ooad.dao;

import com.example.ooad.bean.Issue;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface IssueDao extends CommonDao<Issue>{

//    @Query("select '*' from Issue")
    @Nullable
    List<Issue> findAll();

    @Transactional
    @Modifying
    @Override
    void deleteById(Long aLong);

    @Modifying
    @Transactional
    long deleteByRepoID(Long repoID);


    Optional<Issue> findByTitle(String title);

    Optional<Issue> findById(Long IssueID);

    List<Issue> findByRepoID(@NonNull Long repoID);

    Issue findByIdWithinRepoAndRepoID(@NonNull Long idWithinRepo, @NonNull Long repoID);

    @Transactional
    @Query("select max(i.idWithinRepo) from Issue i where i.repoID = ?1")
    Long findMaxidWithinRepoByRepoID(@NonNull Long repoID);


    @Nullable
    Issue findByState(boolean state);

    /**
     * 改
     */
    @Transactional
    @Modifying
    @Query("update Issue i set i.title = ?1 where i.id = ?2")
    int updateTitleById(String title, Long id);

    @Transactional
    @Modifying
    @Query(value = "insert into issue (id_within_repo, state, title, owner_id, repoid, timestamp) values (?1, ?2, ?3, ?4, ?5, now())", nativeQuery = true)
    int addIssue(Long id_within_repo, boolean state, String title, Long owner_id, Long repoid);

    long countByRepoID(Long repoID);

    Issue findByRepoIDAndIdWithinRepo(Long repoID, Long idWithinRepo);

    @Transactional
    @Modifying
    @Query("update Issue i set i.state = ?1 where i.id = ?2")
    void updateStateById(boolean state, Long id);




}

