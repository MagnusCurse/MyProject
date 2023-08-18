package com.example.demo.controller;

import com.example.demo.common.AjaxResult;
import com.example.demo.common.SessionUnit;
import com.example.demo.model.User;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService service;

    @RequestMapping("/publish")
    @ResponseBody
    public Object publish(String title, String content, HttpServletRequest request){
        if(!StringUtils.hasLength(content) || !StringUtils.hasLength(title)){
            return AjaxResult.fail(-1,"文章内容或者标题为空");
        }
        // 得到当前用户对象
        User curUser = SessionUnit.getLoginUser(request);
        if(curUser == null) {
            return AjaxResult.fail(-1,"当前用户对象为空");
        }
        // 操作数据库
        int res = service.publish(title,content,curUser.getId());
        if(res != 1) {
            return AjaxResult.fail(-1,"数据库插入文章失败");
        }
        return AjaxResult.success(res,"发布文章成功");
    }
}