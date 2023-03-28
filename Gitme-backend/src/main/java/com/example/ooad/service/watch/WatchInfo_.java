package com.example.ooad.service.watch;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Watch info.
 */
@Setter
@Getter
public class WatchInfo_ {
    long watch_id;
    Date date;
    String info;
    long repoid;
    long userid;
    String username;
    String reponame;
    boolean isRead;

    public WatchInfo_(Date date, long watch_id, String info, long repoid, long userid, String username, String reponame, boolean isRead) {
        this.date = date;
        this.watch_id = watch_id;
        this.info = info;
        this.repoid = repoid;
        this.userid = userid;
        this.username = username;
        this.reponame = reponame;
        this.isRead = isRead;
    }



}
