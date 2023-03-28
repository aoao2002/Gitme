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
//@NoArgsConstructor
@Table(name = "watch")
public class Watch extends BaseBean {

    @ManyToOne
    @NotFound(action= NotFoundAction.IGNORE)
    @JoinColumn(name="watcher_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="repo_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Repo repo;


}
