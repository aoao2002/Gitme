package com.example.ooad.bean;

import com.example.ooad.bean.Issue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "git_user")
public class User extends BaseBean {

//    @Column(unique = true)
    @NotNull
    private String name;
    // password
    @NotNull
    private String pw;

    @Column(name = "bio", nullable = true)
    private String bio;

    @Column(name = "mail", nullable = true)
    private String mail;

    @Column(name = "phone_Number", nullable = true)
    private String phoneNumber;

    /**
     * 账号状态（0 normal 1 abort）
     */
    @Column(name = "status", nullable = true)
    private String status;

    /**
     * 用户性别（0 male，1 female，2 unknown）
     */
    @Column(name = "sex", nullable = true)
    private int sex;



//    @JsonIgnore
//    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
//    private Set<Issue> issues = new HashSet<>();
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        Issue issue = (Issue) o;
//        return getId() != null && Objects.equals(getId(), issue.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPw() {
//        return pw;
//    }
//
//    public void setPw(String pw) {
//        this.pw = pw;
//    }
//
//    public String getBio() {
//        return bio;
//    }
//
//    public void setBio(String bio) {
//        this.bio = bio;
//    }
//
//    public String getMail() {
//        return mail;
//    }
//
//    public void setMail(String mail) {
//        this.mail = mail;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getSex() {
//        return sex;
//    }
//
//    public void setSex(String sex) {
//        this.sex = sex;
//    }
//
//    public Date getCreatedData() {
//        return createdData;
//    }
//
//    public void setCreatedData(Date createdData) {
//        this.createdData = createdData;
//    }
}
