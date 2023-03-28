package com.example.ooad.service.Issue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewIssueParam {
    String repoID;
    String issue;
    String comment;

}
