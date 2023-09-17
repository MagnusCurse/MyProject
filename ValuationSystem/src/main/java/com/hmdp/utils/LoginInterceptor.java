package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO 获取到请求头中的 token
        String token = request.getHeader(SystemConstants.USER_LOGIN_TOKEN);
        System.out.println("token" + token);
        if(StrUtil.isBlank(token)) {
            // token 不存在, 证明该用户没有登录, 进行拦截
            response.setStatus(401);
            return false;
        }
        // TODO 基于 token 获取 redis 中的用户
        String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object,Object> userMap = stringRedisTemplate.opsForHash()
                .entries(tokenKey);
        // TODO 判断用户是否存在
        if(userMap.isEmpty()) {
            // 用户不存在
            response.setStatus(401);
            return false;
        }
        // TODO 将查询到的 Hash 数据转化为 UserDTO 对象
        UserDTO dto = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);

        // TODO 将用户信息保存到 ThreadLocal
        UserHolder.saveUser(dto);

        // TODO 刷新 token 有效期
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        // TODO 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       // TODO 将用户从 ThreadLocal 中移除
       UserHolder.removeUser();
    }
}
