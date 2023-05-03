package com.timeless.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Link;
import com.timeless.domain.vo.PageVo;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-12-07 10:37:49
 */
public interface LinkService extends IService<Link> {
    ResponseResult getAllLink();

    PageVo listBackLink(Integer pageNum, Integer pageSize, String name, String status);
}
