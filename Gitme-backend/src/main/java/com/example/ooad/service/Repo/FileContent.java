package com.example.ooad.service.Repo;

public class FileContent {
    String content;
    boolean isText;
    long linesNum=0;
    long contentSize=0;



    public FileContent(String content, boolean isText) {
        this.content = content;
        this.isText = isText;
    }

    public FileContent(boolean isText) {
        this.isText = isText;
    }

    public FileContent(String content, boolean isText, long linesNum, long contentSize) {
        this.content = content;
        this.isText = isText;
        this.linesNum = linesNum;
        this.contentSize = contentSize;
    }

    public long getLinesNum() {
        return linesNum;
    }

    public void setLinesNum(long linesNum) {
        this.linesNum = linesNum;
    }

    public long getContentSize() {
        return contentSize;
    }

    public void setContentSize(long contentSize) {
        this.contentSize = contentSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isText() {
        return isText;
    }

    public void setText(boolean text) {
        isText = text;
    }
}
