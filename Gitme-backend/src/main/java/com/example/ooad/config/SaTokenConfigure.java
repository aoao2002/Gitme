package com.example.ooad.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.example.ooad.OoadApplication;
import com.example.ooad.bean.Repo;
import com.example.ooad.controller.UserCtrl;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.service.Handler.EditException;
import com.example.ooad.service.Repo.RepoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    @Autowired
    private RepoService repoService;

    private static final Logger logger = LoggerFactory.getLogger(SaTokenConfigure.class);


    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        // 基于路由的权限认证，适合只有少数接口开放（dologin）,其他全需要登录
        registry.addInterceptor(new SaInterceptor(handle ->{
            SaRouter.match("/**")
//                    .notMatch("/user/doLoginEmail")
//                    .notMatch("/user/isLogin")
//                    .notMatch("/swagger-ui.html#/")
//                    .notMatch("/user/register")
                    .notMatch("/repo/permission/**")
                    .check(r -> {
                        OoadApplication.getLogger().info(SaHolder.getRequest().getUrl());
                        StpUtil.checkLogin();
                    });

            SaRouter.match("/repo/edit/**")
                    .check(r -> checkEditPower());

            SaRouter.match("/repo/view/**")
                    .check(r -> checkViewPower());

        }))
//                StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/user/doLoginEmail")
                .excludePathPatterns("/user/isLogin")
                .excludePathPatterns("/swagger-ui.html#/")
                .excludePathPatterns("/repo/getRepoSateByName")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/repo/downloadRepo")
                .excludePathPatterns("/repo/permission/**");




        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
//        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    public void checkEditPower(){

        if (!repoService.isEditableByID(SaHolder.getRequest().getParam("repoID"),
                StpUtil.getLoginIdAsString()))
            throw new EditException("No access to edit this repo");
    }

    public void checkViewPower(){
        if (!repoService.isViewableByID(SaHolder.getRequest().getParam("repoID"),
                StpUtil.getLoginIdAsString()))
            throw new EditException("No access to view this repo");
    }
}