package com.timeless.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.constants.SystemConstants;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Category;
import com.timeless.domain.entity.Link;
import com.timeless.domain.vo.LinkVo;
import com.timeless.domain.vo.PageVo;
import com.timeless.mapper.LinkMapper;
import com.timeless.service.LinkService;
import com.timeless.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-12-07 10:37:49
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    /**
     * 查询所有审核通过的友链
     *
     * @return
     */
    @Override
    public ResponseResult getAllLink() {
        //1.查询所有审核通过的友链
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> linkList = list(linkLambdaQueryWrapper);
        //2.转换成Vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);

        return ResponseResult.okResult(linkVos);
    }

    @Override
    public PageVo listBackLink(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.like(StringUtils.hasText(name), Link::getName, name);
        linkLambdaQueryWrapper.like(StringUtils.hasText(status), Link::getStatus, status);
        Page<Link> linkPage = new Page<>(pageNum, pageSize);
        page(linkPage, linkLambdaQueryWrapper);
        return new PageVo(linkPage.getRecords(), linkPage.getTotal());
    }

}
