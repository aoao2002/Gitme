/**
 * 实现对应的业务逻辑
 * 利用Dao对象来和数据库交互
 * controller利用service层的对象来响应前端（这就是为啥给前端的手册只有controller的）
 * 一般的规范是先写接口再写实现
 * */

package com.example.ooad.service.User;

import cn.dev33.satoken.util.SaResult;
import com.example.ooad.bean.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//添加注解@Transactional进行事务的处理
@Transactional
public interface UserService {
    void save(User user);

    UserInfo getUserByMail(String mail);

    UserInfo getUserByName(String name);


    List<UserInfo> getAllUser();

    SaResult LoginMail(String Mail, String pwd);

    SaResult addUser(String name, String mail,String pwd);

    User findById(String UserID);

    User findById(long UserID);

    User findByMail(String userEmail);

    boolean editMyInfo(UserInfo userInfo);

    boolean editMyPw(String pw);

    boolean invite(String userName, String repoID);
    boolean unInvite(String userName, String repoID);

    List<UserInfo> getAllCollaByRepoID(String repoID);

}
