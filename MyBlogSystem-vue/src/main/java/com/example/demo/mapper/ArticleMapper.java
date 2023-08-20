package com.example.demo.mapper;

import com.example.demo.model.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    public int publish(@Param("title") String title,@Param("content") String content,@Param("user_id") Integer user_id);

    public List<Article> initBlogs();

    public Article initBlog(@Param("id") Integer id);
}
