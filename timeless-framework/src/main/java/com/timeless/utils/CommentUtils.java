package com.timeless.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.timeless.domain.entity.Comment;
import com.timeless.domain.vo.CommentVo;
import com.timeless.service.CommentService;
import com.timeless.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-09 17:54
 */
@Component
public class CommentUtils {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    public List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for(CommentVo commentVo : commentVos){
            //查询自己的昵称
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setAvatar(userService.getById(commentVo.getCreateBy()).getAvatar());
            commentVo.setUsername(nickName);
            //查询所回复的用户昵称并赋值
            if(commentVo.getToCommentId() != -1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }

    public List<CommentVo> getChildren(Long id){

        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getRootId,id);
        commentLambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = commentService.list(commentLambdaQueryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

}
