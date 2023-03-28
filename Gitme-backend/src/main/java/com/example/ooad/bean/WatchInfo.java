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
@Table(name = "watchInfo")
public class WatchInfo extends BaseBean {
    @ManyToOne
    @NotFound(action= NotFoundAction.IGNORE)
    @JoinColumn(name="watch_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Watch watch;

    @NotNull
    private String info;

    @NotNull
    private boolean haveRead;
}
