package com.example.ooad.bean;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "collaboratorRS")
public class CollaboratorRS extends BaseBean{

    @ManyToOne
    @NotNull
    @JoinColumn(name="user_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name="repo_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Repo repo;
}
