package com.example.ooad.dao;

import com.example.ooad.bean.Comment;
import com.example.ooad.bean.Issue;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CommentDao extends CommonDao<Comment>{



    Comment findById(long aLong);

    List<Comment> findByReplyIssue_Id(@NonNull long id);

    List<Comment> findByReplyIssue_RepoID(@NonNull long repoID);

    List<Comment> findByOwner_Id(Long id);

    long countByReplyIssue_Id(Long id);



    @Transactional
    @Modifying
    @Query(value = "insert into comment (content, likes_num, owner_id, timestamp,reply_issue_id) values (?1, ?2, ?3, now(),?4)", nativeQuery = true)
    int addComment(String content, long likes_num, long owner_id, long reply_issue_id);

    @Transactional
    @Modifying
    @Query("update Comment c set c.likesNum = ?1 where c.id = ?2")
    int updateLikesNumById(long likesNum, @NonNull long id);

    @Transactional
    @Modifying
    @Query("update Comment c set c.content = ?1 where c.id = ?2")
    int updateContentById(@NonNull String content, @NonNull Long id);


    @Override
    @Modifying
    @Transactional
    void deleteById(Long aLong);
    void deleteByReplyIssue(Issue issue);

}