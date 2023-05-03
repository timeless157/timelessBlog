package com.timeless.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.constants.SystemConstants;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Article;
import com.timeless.domain.entity.Comment;
import com.timeless.domain.vo.CommentVo;
import com.timeless.domain.vo.PageVo;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.mapper.CommentMapper;
import com.timeless.service.ArticleService;
import com.timeless.service.CommentService;
import com.timeless.service.UserService;
import com.timeless.utils.CommentUtils;
import com.timeless.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-12-08 21:46:04
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentUtils commentUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //条件查询评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();

        commentLambdaQueryWrapper.eq(SystemConstants.TYPE_ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        commentLambdaQueryWrapper.eq(Comment::getRootId,-1);
        //评论类型
        commentLambdaQueryWrapper.eq(Comment::getType,commentType);
        //分页查询
        Page<Comment> commentPage = new Page<>(pageNum, pageSize);
        page(commentPage,commentLambdaQueryWrapper);
        //转换成Vo
        List<CommentVo> commentVoList = commentUtils.toCommentVoList(commentPage.getRecords());

        //查询子评论
        for (CommentVo commentVo : commentVoList){
//            父评论的头像地址
//            commentVo.setAvatar(userService.getById(commentVo.getCreateBy()).getAvatar());
            List<CommentVo> children = commentUtils.getChildren(commentVo.getId());
            //子评论的头像地址
//            for (CommentVo child : children) {
//                child.setAvatar(userService.getById(child.getCreateBy()).getAvatar());
//            }
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList, commentPage.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_CANNOT_BE_NULL);
        }
        Long articleId = comment.getArticleId();
        Article article = articleService.getById(articleId);
        System.out.println(article);
        if(article.getIsComment().equals("1")){
            throw new SystemException(AppHttpCodeEnum.NOT_COMMENT);
        }
        save(comment);
        return ResponseResult.okResult();
    }

}
