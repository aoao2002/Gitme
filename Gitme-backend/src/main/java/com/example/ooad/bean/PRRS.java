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
@Table(name = "PRRS")
public class PRRS extends BaseBean{

    @ManyToOne
    @NotNull
    @JoinColumn(name="from_repo_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    // 这里的from是提出pr的repo，也就是fork那的to
    private Repo fromRepo;

    @ManyToOne
    @NotNull
    @JoinColumn(name="to_repo_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Repo toRepo;

    @Column(name = "state")
    @NotNull
    private PRState state;

//    @NotNull
//    private String diff;
    @NotNull
    private String branchName;

    @Column(name="fromBranch")
    private String fromBranch;

    @Column(name="title")
    private String title;

    @Column(name="createTime")
    private String createTime;

    @Column(name = "makePR")
    private String userName;

    @Column(name = "forkBranch")
    private String forkBranch;

    // Accept or Reject or Unprocess
//    @NotNull
//    private String state;
}
