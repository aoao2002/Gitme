package com.example.ooad.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "Comment")
public class Comment extends BaseBean {

    @Column(name = "content")
    @NotNull
    private String content;


    @Column(name = "likesNum")
    @NotNull
    private Long likesNum;

    @ManyToOne
    @NotFound(action= NotFoundAction.IGNORE)
    @JoinColumn(name="owner_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User owner;

    @ManyToOne
    @NotFound(action= NotFoundAction.IGNORE)
    @JoinColumn(name="reply_Issue_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Issue replyIssue;

    @Column(name = "createTime")
    private String createTime;

    private void setUser(User user){
        this.owner = user;
    }
}
