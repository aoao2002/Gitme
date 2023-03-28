package com.example.ooad.service.Comment;

import com.example.ooad.bean.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
public class CommentInfo {
     Long ID;

     String content;

//     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
//     Date date;
//     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String date;

     Long likesNum;

     Long ownerID;
     String authorName;

     Long replyIssueId;
     public CommentInfo(){

          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          this.date = simpleDateFormat.format(new Date());
     }

     public CommentInfo(Long ID,String content, Date date, Long likesNum, Long ownerID, Long replyIssueId, String authorName) {
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          this.ID = ID;
          this.content = content;
          this.setDate(simpleDateFormat.format(date));
          this.likesNum = likesNum;
          this.ownerID = ownerID;
          this.replyIssueId = replyIssueId;
          this.authorName = authorName;
     }
     public CommentInfo(Comment comment){
          this.ID = comment.getId();
//          this.date = Objects.requireNonNullElseGet(date, Date::new);
          this.date = comment.getCreateTime();
          this.ownerID = comment.getOwner().getId();
          this.replyIssueId = comment.getReplyIssue().getId();
          this.content = comment.getContent();
          this.likesNum = comment.getLikesNum();
          this.authorName = comment.getOwner().getName();
     }
     public CommentInfo(Optional<Comment> comment2){
          if(comment2.isPresent()) {
               Comment comment = comment2.get();
               this.ID = comment.getId();
               this.date = comment.getCreateTime();
//               this.date = Objects.requireNonNullElseGet(date, Date::new);
               this.ownerID = comment.getOwner().getId();
               this.replyIssueId = comment.getReplyIssue().getId();
               this.content = comment.getContent();
               this.likesNum = comment.getLikesNum();
               this.authorName = comment.getOwner().getName();
          }

     }



     public Long getID() {
          return ID;
     }

     public void setID(Long ID) {
          this.ID = ID;
     }

     public String getContent() {
          return content;
     }

     public void setContent(String content) {
          this.content = content;
     }

     public String getDate() {
          return date;
     }

     public void setDate(String date) {
          if(date == null){
               SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               this.date = simpleDateFormat.format(new Date());
          }else {
               this.date = date;
          }
     }

     public Long getLikesNum() {
          return likesNum;
     }

     public void setLikesNum(Long likesNum) {
          this.likesNum = likesNum;
     }

     public Long getOwnerID() {
          return ownerID;
     }

     public void setOwnerID(Long ownerID) {
          this.ownerID = ownerID;
     }

     public Long getReplyIssueId() {
          return replyIssueId;
     }

     public void setReplyIssueId(Long replyIssueId) {
          this.replyIssueId = replyIssueId;
     }
}
