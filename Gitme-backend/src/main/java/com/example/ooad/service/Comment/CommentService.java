package com.example.ooad.service.Comment;

import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CommentService {

    CommentInfo getCommentByID(String commentID);
    List<CommentInfo> getCommentByIssueID(String issueID);
    List<CommentInfo> getCommentByRepoID(String repoID);
    List<CommentInfo> getAllComment();

    boolean addComment(String content, String issueID);

    Integer updateLikesNumById(String likesNum, String id);
    Integer updateContentById(String content, String id);

    boolean deleteCommentByID(String commentID);
    Integer deleteCommentsByIssueID(String issueID);
    Integer deleteCommentsByRepoId(String repoID);
    Integer deleteCommentsByOwnerId(String ownerID);
    Integer deleteAllComments();


}

