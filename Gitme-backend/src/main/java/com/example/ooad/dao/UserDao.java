package com.example.ooad.dao;
import com.example.ooad.bean.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * --> @repository是用来注解接口
 * 特殊的函数命名（findByName）, 可以自动生成根据主键查找对应数据的方法
 * 接口不需要手写实现，SPA可以帮你实现他
 * 也可以支持手写sql完成数据库操作
 */

@Repository
public interface UserDao extends CommonDao<User> {


    Optional<User> findByMail(String Mail);

    Optional<User> findById(long ID);

    User findByName(String name);

}
