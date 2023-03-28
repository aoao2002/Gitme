package com.example.ooad.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Repo")
public class Repo extends BaseBean{

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "state")
    @NotNull
    private RepoState state;

    @ManyToOne
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="creator_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User creator;

    @Column(name = "descri")
    private String descri;


}
