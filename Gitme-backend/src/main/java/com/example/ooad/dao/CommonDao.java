package com.example.ooad.dao;
/**
 * Dao(Data Access Object)层用于和数据库交互-->利用Dao对象访问数据库
 * 继承JpaRepository
 * 在一个项目中，我们往往会创建一个公共接口来处理到数据库的请求，然后每个接口去继承它即可。
 * */


import com.example.ooad.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.ooad.bean.BaseBean;
import org.springframework.transaction.annotation.Transactional;


//两个参数，分别表示 —— 实体类型、主键类型
@Repository
public interface CommonDao<T extends BaseBean> extends JpaRepository<T, Long> {

    @Transactional
    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS = 0", nativeQuery = true)
    void cancelForeignKeyConstraint();

    @Transactional
    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS = 1", nativeQuery = true)
    void enableForeignKeyConstraint();

//    @Transactional
//    T findById(long ID);

}


