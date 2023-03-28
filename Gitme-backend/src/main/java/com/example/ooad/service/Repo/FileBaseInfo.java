package com.example.ooad.service.Repo;

import java.util.Date;

public class FileBaseInfo {
    String name;
    String time;
    String type;
    String content;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FileBaseInfo(){}
    public FileBaseInfo(String name, String time, String type) {
        this.name = name;
        this.time = time;
        this.type = type;
    }

    public FileBaseInfo(String name, String time, String type, String content) {
        this.name = name;
        this.time = new Date().toString();
        this.type = type;
        this.content = content;
    }
}
