package com.example.ooad.service;

import cn.dev33.satoken.stp.StpInterface;
import com.example.ooad.bean.User;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RepoDao repoDao;


    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<String>();
        list.add("userAdd");
        list.add("userDelete");
        list.add("userUpdate");
        list.add("userGet");

        list.add("repoGet");
        list.add("repoDelete");
        list.add("repoUpdate");
        list.add("repoGet");
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<String>();
        list.add("repoOwner");
        list.add("super-admin");
        return list;
    }

    private List<String> getRepoPermissionList(long userID){
        Optional<User> userOptional = userDao.findById(userID);
        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();
        if (user == null) return new ArrayList<>();
        // find all the repo belong to him (change/delete/query)


        return null;
    }
}
