package com.example.ooad.bean;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "forkRs")
public class ForkRS extends BaseBean{

    @ManyToOne
    @NotNull
    @JoinColumn(name="from_repo_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Repo fromRepo;

    @ManyToOne
    @NotNull
    @JoinColumn(name="to_repo_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Repo toRepo;

    @NotNull
    @Column(name="fromBranch")
    private String fromBranch;

    @NotNull
    @Column(name="fromCommit")
    private String fromCommit;

    @Column(name = "forkBranch")
    private String forkBranch;
}
