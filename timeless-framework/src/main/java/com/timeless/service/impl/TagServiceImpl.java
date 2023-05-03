package com.timeless.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Tag;
import com.timeless.domain.vo.PageVo;
import com.timeless.domain.vo.TagVo;
import com.timeless.mapper.TagMapper;
import com.timeless.service.TagService;
import com.timeless.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-12-15 14:33:46
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Override
    public ResponseResult tagList(String name, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.like(StringUtils.hasText(name),Tag::getName,name);
        tagLambdaQueryWrapper.orderByAsc(Tag::getId);
        Page<Tag> tagPage = new Page<>(pageNum, pageSize);
        page(tagPage,tagLambdaQueryWrapper);
        return ResponseResult.okResult(new PageVo(tagPage.getRecords(),tagPage.getTotal()));
    }

    @Override
    public void deleteById(List<Long> id) {
        update(new UpdateWrapper<Tag>().in("id",id).set("del_flag",1));
    }

    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> tagList = list(tagLambdaQueryWrapper);
        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return tagVoList;
    }

}
