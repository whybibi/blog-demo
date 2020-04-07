package cn.why.blog.basic.controller;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.Comment;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.enums.IsDeleteEnum;
import cn.why.blog.basic.entity.enums.IsEnableEnum;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.CommentService;
import cn.why.blog.config.shiro.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 评论控制接口
 */
@Controller
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BasicUserService userService;


    /**
     * 提交评论
     * @param comment
     * @return
     */
    @PostMapping("submit")
    @ResponseBody
    public R submitComment(@Validated Comment comment) {
        if (comment != null) {
            comment.setUserId(ShiroUtils.getCurrentUser().getId());
            comment.setCreateUser(ShiroUtils.getCurrentUser().getId());
            comment.setCreateDate(new Date());
            if (commentService.saveComment(comment))
                return R.ok(GlobalMessage.TIP_SAVE_SUCCESS);
        }
        return R.failed(SimpleError.COMMENT_ERROR);
    }


    /**
     * 当前登陆用户评论查询
     * @param pageNum 当前页（默认第1页）
     * @param pageSize 每页条数（默认10条）
     * @return
     */
    @GetMapping("comment_list")
    @ResponseBody
    public Map commentList(@RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum, @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(pageNum <= 0 ? 1 : pageNum,pageSize);
        BasicUser user = ShiroUtils.getCurrentUser();
        if(user != null){
            List<Comment> list = commentService.list(new QueryWrapper<Comment>().lambda()
                    .eq(Comment::getUserId,user.getId())
                    .eq(Comment::getIsDelete, IsDeleteEnum.ONE.getValue()));
            if(list != null && list.size() > 0){
                PageInfo<Comment> pageInfo = new PageInfo<>(list);
                map.put("success",true);
                map.put("pageInfo",pageInfo);
            }else{
                map.put("success",false);
                map.put("msg","当前用户暂无评论");
            }
        }else{
            map.put("success",false);
            map.put("msg","只能加载当前登陆用户评论");
        }
        return map;
    }


    /**
     * 获取当前文章下的评论
     * @return
     */
    @PostMapping("get_comment_by_dynamic_id")
    @ResponseBody
    public List<Comment> getCommentByDynamicId(Long dynamicId){
        List<Comment> commentList = commentService.list(new QueryWrapper<Comment>().lambda()
                .eq(Comment::getDynamicsId,dynamicId)
                .eq(Comment::getIsEnable, IsEnableEnum.ONE.getValue())
                .eq(Comment::getIsDelete,IsDeleteEnum.ONE.getValue()));
        if(commentList.size() > 0){//如果有评论就循环查询当前评论的用户实体
            List<Comment> list = new ArrayList<>();
            for(Comment comment : commentList){
                BasicUser user = userService.getById(comment.getUserId());
                if(user != null){//转换用户的头像地址
                    user.setSalt("");
                    user.setPassWord("");
                    if(user.getHeadImg() != null && user.getHeadImg().indexOf("/image/") == -1)
                        user.setHeadImg("/image/"+ user.getHeadImg());
                    comment.setUser(user);
                }
                list.add(comment);
            }
            return list;
        }
        return commentList;
    }



}
