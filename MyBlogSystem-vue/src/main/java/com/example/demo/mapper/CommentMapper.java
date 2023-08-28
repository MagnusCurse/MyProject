package com.example.demo.mapper;

import com.example.demo.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    public int post(@Param("user_id") Integer user_id,
                    @Param("blog_id") Integer blog_id,
                    @Param("username") String username,
                    @Param("comment") String comment);

    public int reply(@Param("parent_id") Integer parent_id,
                     @Param("user_id") Integer user_id,
                     @Param("blog_id") Integer blog_id,
                     @Param("username") String username,
                     @Param("comment") String comment);


    public List<Comment> initParentComment();

    public List<Comment> initChildComment(@Param("parent_id") Integer parent_id);
}
