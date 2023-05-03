package com.timeless.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Tag;
import com.timeless.domain.vo.TagVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-12-15 14:33:45
 */
public interface TagService extends IService<Tag> {
    ResponseResult tagList(String name, Integer pageNum, Integer pageSize);
    void deleteById(List<Long> id);

    List<TagVo> listAllTag();
}
