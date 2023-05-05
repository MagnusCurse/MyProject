package com.example.demo.common;

import com.example.demo.model.Constant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false); // false 表示如果有 session 才创建一个 session 对象,没有则不创建
        if(session != null && session.getAttribute(Constant.SESSION_USERINFO_KEY) != null){
            return true;
        } // 当前用户已经登录
        response.setStatus(401);
        return false; // 当前用户未登录
    }
}
