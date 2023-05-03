package com.timeless.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-12-08 21:46:03
 */
public interface CommentService extends IService<Comment> {
    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
