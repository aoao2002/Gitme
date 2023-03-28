package com.example.ooad.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;



@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "Issue")
public class Issue extends BaseBean{
    /**
     * 基础信息：
     * id:Long
     * title:String
     * idWithinRepo: Long
     * author:User
     * timestamp:Timestamp
     * repo:repo
     * state:boolean
     * @param o
     * @return
     */



    @NotNull
    private String title;

    @Column(name = "idWithinRepo", nullable = true)
    private Long idWithinRepo;

    @Column(name = "RepoID", nullable = true)
    private Long repoID;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name="owner_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User author;

    @Column(name = "state", nullable = true)
    private boolean state;

    @Column(name = "createTime", nullable = true)
    private String createTime;



}