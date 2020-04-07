package cn.why.blog.basic.service;

import cn.why.blog.basic.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CommentService extends IService<Comment> {

    Boolean saveComment(Comment comment);

}
