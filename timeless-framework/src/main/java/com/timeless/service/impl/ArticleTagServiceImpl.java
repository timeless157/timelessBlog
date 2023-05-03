package com.timeless.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.domain.entity.ArticleTag;
import com.timeless.mapper.ArticleTagMapper;
import com.timeless.service.ArticleTagService;
import org.springframework.stereotype.Service;
/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-12-16 17:54:54
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}
