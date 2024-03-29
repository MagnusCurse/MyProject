package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Follow;
import com.example.demo.entity.User;
import com.example.demo.mapper.FollowMapper;
import com.example.demo.service.IFollowService;
import com.example.demo.utils.AjaxResult;
import com.example.demo.utils.RedisKeyUtils;
import com.example.demo.utils.SessionUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserServiceImpl userService;

    /**
     * 关注用户功能
     * @param request
     * @param followUserId
     * @param isFollow
     * @return
     */
    @Override
    @Transactional
    public Object followUser(HttpServletRequest request, Integer followUserId, Boolean isFollow) {
        // 获取到当前用户对象
        User curUser = SessionUnit.getLoginUser(request);
        if(curUser == null) {
            return AjaxResult.fail(-1,"当前用户不存在");
        }
        // 当前用户 id 和要关注的用户 id 相同
        if(Objects.equals(curUser.getId(), followUserId)) {
           return AjaxResult.fail(-2,"不能关注自己");
        }
        // 判断当前用户是否已经被关注
        if(!isFollow) {
            /* 操作数据库 */
            Follow follow = new Follow();
            follow.setUserId(curUser.getId());
            follow.setFollowedUserId(followUserId);
            Boolean isSuccess = save(follow);
            /* 操作 Redis */
            String key = RedisKeyUtils.USER_FOLLOWED_KEY + curUser.getId();
            if(isSuccess) {
                stringRedisTemplate.opsForZSet().add(key,followUserId.toString(),System.currentTimeMillis());
            }
        } else { // 用户已经关注，则取消关注
            // 移除数据库中该条关注记录
            boolean isSuccess = remove(new QueryWrapper<Follow>().eq("user_id",curUser.getId()).eq("followed_user_id",followUserId.toString()));
            String key = RedisKeyUtils.USER_FOLLOWED_KEY + curUser.getId();
            if(isSuccess) {
                stringRedisTemplate.opsForZSet().remove(key,followUserId.toString());
            }
        }
        return AjaxResult.success(1,"关注 / 取关成功");
    }

    /**
     * 判断当前用户是否有关注该用户
     * @param request
     * @param followUserId
     * @return
     */
    @Override
    public Object isFollow(HttpServletRequest request, String followUserId) {
        // 获取到当前用户对象
        User curUser = SessionUnit.getLoginUser(request);
        if(curUser == null) {
            return AjaxResult.fail(-1,"当前用户不存在");
        }
        /* 从 Redis 中查询 */
        String key = RedisKeyUtils.USER_FOLLOWED_KEY + curUser.getId();
        Double isFollow = stringRedisTemplate.opsForZSet().score(key,followUserId);
        // isFollow 为空即证明 Redis 中没有该条数据
        if(isFollow == null) {
            /* 从数据库中查询 */
            Integer count = query().eq("user_id",curUser.getId()).eq("followed_user_id", followUserId).count();
            // 获取到数据库该关注记录的创建时间
            Follow follow = query().eq("user_id",curUser.getId()).eq("followed_user_id", followUserId).one();
            if(count > 0 && follow != null) {
                // 将数据库的内容重新写回 Redis
                stringRedisTemplate.opsForZSet().add(key,followUserId,follow.getCreateTime().getTime());
                return true;
            }
            // 数据库数据也为空，证明该用户没有关注，返回 false
            return false;
        }
        return true;
    }

    /**
     * 显示用户的关注列表功能
     * @param request
     * @return
     */
    @Override
    public Object initFollowList(HttpServletRequest request) {
        // 获取到当前用户对象
        User curUser = SessionUnit.getLoginUser(request);
        if(curUser == null) {
            return AjaxResult.fail(-1,"当前用户不存在");
        }
        Integer userId = curUser.getId();
        /* 根据 key 从 Redis 中获取该用户所有的关注用户的 id */
        String key = RedisKeyUtils.USER_FOLLOWED_KEY + userId;
        Set<String> ids = stringRedisTemplate.opsForZSet().range(key,0,-1);
        // 如果 Redis 中数据为空则尝试去数据库中获取
        if(ids == null) {
            // 新建立一个 HashSet
            ids = new HashSet<>();
            // 根据 userId 从数据库中获取到记录
            List<Follow> follows = query().eq("user_id",userId).list();
            if(follows != null) {
                for(Follow follow : follows) {
                    // 给 ids 赋值，并将记录重新写回 Redis
                    ids.add(follow.getFollowedUserId().toString());
                    stringRedisTemplate.opsForZSet().add(key,follow.getFollowedUserId().toString(),follow.getCreateTime().getTime());
                }
            }
        }
        List<User> users = new ArrayList<>();
        for(String id : ids) {
            users.add(userService.initUserInfo(Integer.valueOf(id)));
        }
        return AjaxResult.success(users,"初始化关注列表成功");
    }
}
