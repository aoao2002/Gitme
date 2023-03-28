package com.example.ooad.service.User;

import java.util.Date;
import com.example.ooad.bean.User;


public class UserInfo {
    private String name;
    private String bio;
    private String mail;
    private String phoneNumber;
    private int sex;
    private Date createdData;

    public UserInfo(User user) {
        this.name = user.getName();
        this.bio = user.getBio();
        this.mail = user.getMail();
        this.phoneNumber = user.getPhoneNumber();
        this.sex = user.getSex();
        this.createdData = user.getCreatedDate();
    }

    public UserInfo(String bio, String name, String phoneNumber, String sex) {
        this.name = name;
        this.bio = bio;
        this.phoneNumber = phoneNumber;
        this.sex = Integer.parseInt(sex);
    }

    public UserInfo(String bio, String phoneNumber, String sex) {
        this.bio = bio;
        this.phoneNumber = phoneNumber;
        this.sex = Integer.parseInt(sex);
    }

    public User changeUser(User user){
        user.setBio(getBio());
        user.setPhoneNumber(getPhoneNumber());
        user.setSex(getSex());
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getCreatedData() {
        return createdData;
    }

    public void setCreatedData(Date createdData) {
        this.createdData = createdData;
    }
}
