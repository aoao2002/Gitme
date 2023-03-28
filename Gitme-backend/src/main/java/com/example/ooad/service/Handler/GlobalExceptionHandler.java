package com.example.ooad.service.Handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import com.example.ooad.service.watch.WatchServiceMpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常拦截
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public SaResult handlerException(Exception e) {
//        e.printStackTrace();
        logger.error(e.getMessage());
        return SaResult.error(e.getMessage());
    }

    // 全局异常拦截（拦截项目中的NotLoginException异常）
    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerNotLoginException(NotLoginException nle)
            throws Exception {

        // 打印堆栈，以供调试
//        nle.printStackTrace();
        System.out.println("-----------------------------------------------------------");
        System.out.println("user have not login");
        System.out.println("-----------------------------------------------------------");


        // 判断场景值，定制化异常信息
        String message = "";
        if(nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供token";
        }
        else if(nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token无效";
        }
        else if(nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token已过期";
        }
        else if(nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token已被顶下线";
        }
        else if(nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token已被踢下线";
        }
        else {
            message = "当前会话未登录";
        }

        // 返回给前端
        return SaResult.error(message);
    }

    @ExceptionHandler(EditException.class)
    public SaResult handlerEditException(EditException editException)
            throws Exception {

        System.out.println("-----------------------------------------------------------");
        System.out.println("user without edit power");
        System.out.println("-----------------------------------------------------------");

        // 返回给前端
        return SaResult.error(editException.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public SaResult handlerNumberFormatException(NumberFormatException numberFormatException)
            throws Exception {
        System.out.println("-----------------------------------------------------------");
        System.out.println(numberFormatException.getMessage());
        System.out.println("-----------------------------------------------------------");

        // 返回给前端
        return SaResult.error("input str is not a long");
    }


}
