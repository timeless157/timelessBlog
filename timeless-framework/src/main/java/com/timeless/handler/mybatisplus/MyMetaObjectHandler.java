package com.timeless.handler.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.utils.SecurityUtils;
import org.apache.catalina.security.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

import static com.timeless.utils.SecurityUtils.getAuthentication;

/**
 * @author timeless
 * @create 2022-12-09 23:14
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 下面注释掉的代码，即使捕获异常，也处理了，但还是不行。因为这个SecurityUtils.getUserId()，是登录之后
     * 才能有的，不然会报空指针异常，干脆一不做二不休直接注释掉了。
     * <p>
     * 我又想了想，不注释掉也是可以的，因为就算空指针了，反正也是-1了，只不过我把错误信息打印出来了，根本不碍着功能。
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
//            e.printStackTrace();
            userId = -1L;//表示是自己创建
        }
//        try {
//            userId = SecurityUtils.getUserId();
//        } catch (Exception e){
//            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
//        }

        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
//            e.printStackTrace();
            userId = -1L;//表示是自己修改
        }

        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }
}
