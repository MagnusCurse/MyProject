package com.example.demo.controller;

import com.example.demo.utils.AjaxResult;
import com.example.demo.utils.SessionUnit;
import com.example.demo.entity.Blog;
import com.example.demo.entity.User;
import com.example.demo.service.impl.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogServiceImpl service;

    @RequestMapping("/publish")
    @ResponseBody
    public Object publish(@RequestBody Map<String,String> body, HttpServletRequest request){
        String content = body.get("content");
        String title = body.get("title");
        if(!StringUtils.hasLength(content) || !StringUtils.hasLength(title)){
            return AjaxResult.fail(-1,"博客内容或者标题为空");
        }
        // 得到当前用户对象
        User curUser = SessionUnit.getLoginUser(request);
        if(curUser == null) {
            return AjaxResult.fail(-1,"当前用户对象为空");
        }
        // 操作数据库
        int res = service.publish(title,content,curUser.getId());
        if(res != 1) {
            return AjaxResult.fail(-1,"数据库插入博客失败");
        }
        return AjaxResult.success(res,"发布博客成功");
    }

    @RequestMapping("/search")
    @ResponseBody
    public Object search(String title) {
        if(!StringUtils.hasLength(title)) {
            return AjaxResult.fail(-1,"标题为空");
        }
        List<Blog> res = service.search(title);
        return res;
    }

    @RequestMapping("/init-blogs")
    @ResponseBody
    public Object initBlogs(Integer pageIndex, Integer pageSize) {
        List<Blog> ret = service.initBlogs(pageIndex, pageSize);
        if(ret == null) return AjaxResult.fail(-1,"页面参数出现问题");
        return ret;
    }

    @RequestMapping("/init-user-blogs")
    @ResponseBody
    public Object initUserBlogs(HttpServletRequest request) {
        // 得到当前用户对象
        User curUser = SessionUnit.getLoginUser(request);
        if(curUser == null) {
            return AjaxResult.fail(-1,"当前用户对象为空");
        }
        Integer user_id = curUser.getId();
        if(user_id == null) {
            return AjaxResult.fail(-1,"用户 id 为空");
        }
        return service.initUserBlogs(user_id);
    }

    @RequestMapping("/init-blog")
    @ResponseBody
    public Object intiBlog(Integer id) {
       Blog res = service.initBlog(id);
       if(res == null) {
           return AjaxResult.fail(-1,"找不到该 Blog");
       }
       return AjaxResult.success(res,"初始化博客内容详情成功");
    }

    /**
     * 初始化编辑页面博客内容
     * @param id
     * @return
     */
    @RequestMapping("/init-edit-blog")
    @ResponseBody
    public Object initEditBlog(Integer id) {
        Blog blog = service.getBlog(id);
        if(blog == null) {
            return AjaxResult.fail(-1,"没有找到id为" + id + "的博客");
        }
        return AjaxResult.success(blog,"初始化编辑页面成功");
    }

    /**
     * 初始化当前博客用户对象
     * @param blogId
     * @return
     */
    @RequestMapping("/init-blog-user")
    public Object initBlogUser(Integer blogId) {
        return service.initBlogUser(blogId);
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Object modify(@RequestBody Map<String,String> body,HttpServletRequest request) {
        String title =  body.get("title");
        String content =  body.get("content");
        Integer id = Integer.valueOf(body.get("id"));

        if(!StringUtils.hasLength(content) || !StringUtils.hasLength(title)){
            return AjaxResult.fail(-1,"博客内容或者标题为空");
        }
        // 获取当前用户对象
        User curUser = SessionUnit.getLoginUser(request);
        if(curUser == null) {
            return AjaxResult.fail(-1,"当前用户对象为空");
        }
        // 获取该篇博客对象
        Blog blog =  service.getBlog(id);
        if(blog == null) {
            return AjaxResult.fail(-1,"当前 id 博客不存在");
        }
        if(!blog.getUserId().equals(curUser.getId())) {
            return AjaxResult.fail(-2,"你没有权限修改该博客");
        }
        int res = service.modify(id,title,content);
        if(res != 1) {
            return AjaxResult.fail(-1,"修改数据库失败");
        }
        return AjaxResult.success(res,"修改博客成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Object deleteBlog(Integer id) {
        if(id == null) {
            return AjaxResult.fail(-1,"博客 id 为空");
        }
        int res = service.deleteBlog(id);
        if(res != 1) {
            return AjaxResult.fail(-1,"数据库删除博客失败");
        }
        return AjaxResult.success(res,"删除博客成功");
    }

    @RequestMapping("/view")
    public Object viewBlog(HttpServletRequest request, Integer blogId) {
       return service.viewBlog(request, blogId);
    }

    @RequestMapping("/init-views")
    public Object initBlogViews(Integer blogId) {
        return service.initViews(blogId);
    }

    @RequestMapping("/init-total-page")
    public Object initTotalPage(Integer pageSize) {
        return service.initTotalPage(pageSize);
    }
}
