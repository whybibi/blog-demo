package cn.why.blog.core.controller;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.Comment;
import cn.why.blog.basic.entity.Dynamics;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.enums.IsDeleteEnum;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.CommentService;
import cn.why.blog.basic.service.DynamicsService;
import cn.why.blog.dto.LayuiPage;
import cn.why.blog.dto.LayuiRequest;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @version: V1.0
 * @auther: whybibi
 * @className:
 * @date: 2019-06-20 09:31
 * @description:
 */
@Controller
@RequestMapping("admin")
public class CommentAdminController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BasicUserService userService;
    @Autowired
    private DynamicsService dynamicsService;

    /**
     * 获取评论分页列表
     * @param layuiRequest
     * @return
     */
    @PostMapping("comment_list")
    @ResponseBody
    public LayuiPage<Comment> getCommentList(LayuiRequest layuiRequest){
        PageHelper.startPage(layuiRequest.getPage(),layuiRequest.getLimit());
        List<Comment> list = commentService.list();
        PageInfo<Comment> pageInfo;
        if(list.size() > 0){
            List<Comment> comments = new ArrayList<>();
            for(Comment comment : list){
                BasicUser user = userService.getById(comment.getUserId());
                Dynamics dynamics = dynamicsService.getById(comment.getDynamicsId());
                comment.setUser(user);
                comment.setDynamics(dynamics);
                comments.add(comment);
            }
            pageInfo = new PageInfo<>(comments);
        }else{
            pageInfo = new PageInfo<>(list);
        }
        return new LayuiPage<Comment>(pageInfo.getTotal(),pageInfo.getList());
    }


    /**
     * 更新评论是否可见
     * @return R
     */
    @PostMapping("update_comment_status")
    @ResponseBody
    @Transactional
    public R changeCommentStatus( Comment comment){
        if(!commentService.updateById(comment))
            Assert.fail(SimpleError.OPERATION_ERROR);
        return R.ok(GlobalMessage.TIP_OPERATION_SUCCESS);
    }


    /**
     * 逻辑删除评论
     * @param comment
     * @return
     */
    @PostMapping("del_comment")
    @ResponseBody
    @Transactional
    public R delComment(Comment comment){
        comment.setIsDelete(IsDeleteEnum.ZERO.getValue());
        if(!commentService.updateById(comment))
            Assert.fail(SimpleError.DELETE_ERROR);
        return R.ok(GlobalMessage.TIP_DELETE_SUCCESS);
    }

    /**
     * 逻恢复评论
     * @param comment
     * @return
     */
    @PostMapping("recover_comment")
    @ResponseBody
    @Transactional
    public R recoverComment(Comment comment){
        comment.setIsDelete(IsDeleteEnum.ONE.getValue());
        if(!commentService.updateById(comment))
            Assert.fail(SimpleError.OPERATION_ERROR);
        return R.ok(GlobalMessage.TIP_OPERATION_SUCCESS);
    }


    /**
     * 更新评论
     * @param comment
     * @return
     */
    @PostMapping("update_comment")
    @ResponseBody
    @Transactional
    public R updateComment(Comment comment){
        if(!commentService.updateById(comment))
            Assert.fail(SimpleError.OPERATION_ERROR);
        return R.ok(GlobalMessage.TIP_OPERATION_SUCCESS);
    }





}
