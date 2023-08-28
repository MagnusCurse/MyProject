package com.example.demo.service;

import com.example.demo.mapper.CommentMapper;
import com.example.demo.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper mapper;

    /**
     * 发表评论功能
     * @param user_id
     * @param blog_id
     * @param username
     * @param comment
     * @return
     */
    public int post(Integer user_id,Integer blog_id,String username,String comment) {
      return mapper.post(user_id,blog_id,username,comment);
    }

    /**
     * 回复评论功能
     * @param parent_id
     * @param user_id
     * @param blog_id
     * @param username
     * @param comment
     * @return
     */
    public int reply(Integer parent_id,Integer user_id,Integer blog_id,String username,String comment) {
        return mapper.reply(parent_id,user_id,blog_id,username,comment);
    }

    /**
     * 初始化父评论元素
     * @return
     */
    public List<Comment> initParentComment() {
        return mapper.initParentComment();
    }

    /**
     * 初始化子评论元素
     * @param parent_id
     * @return
     */
    public List<Comment> initChildComment(Integer parent_id) {
        return mapper.initChildComment(parent_id);
    }
}
