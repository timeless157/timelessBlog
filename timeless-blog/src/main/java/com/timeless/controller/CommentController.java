package com.timeless.controller;

import com.timeless.constants.SystemConstants;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddCommentDto;
import com.timeless.domain.entity.Comment;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.service.CommentService;
import com.timeless.utils.BeanCopyUtils;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author timeless
 * @create 2022-12-08 22:51
 */

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 分页查询文章的所有评论
     *
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.TYPE_ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
//        if(ObjectUtils.isEmpty(token)){
//            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
//        }
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.TYPE_LINK_COMMENT, null, pageNum, pageSize);
    }

}
