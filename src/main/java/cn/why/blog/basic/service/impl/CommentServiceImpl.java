package cn.why.blog.basic.service.impl;

import cn.why.blog.basic.entity.Comment;
import cn.why.blog.basic.mapper.CommentMapper;
import cn.why.blog.basic.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper,Comment> implements CommentService {

    @Override
    @Transactional
    public Boolean saveComment(Comment comment) {
        return baseMapper.insert(comment) == 1;
    }


}
