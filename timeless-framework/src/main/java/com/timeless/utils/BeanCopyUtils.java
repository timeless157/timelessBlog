package com.timeless.utils;

import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author timeless
 * @create 2022-12-05 20:38
 */
public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    /**
     * 单个对象的拷贝
     *
     * @param source 拷贝源
     * @param clazz  被拷贝的类对象
     * @param <T>
     * @return
     */
    public static <T> T copyBean(Object source, Class<T> clazz) {
        T result = null;
        try {
            result = clazz.newInstance();
            //属性copy，属性的名字和类型要一一对应，result是source的子集
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * list集合对象的拷贝
     *
     * @param listSource
     * @param clazz
     * @param <O>
     * @param <T>
     * @return
     */
    public static <O, T> List<T> copyBeanList(List<O> listSource, Class<T> clazz) {
        return listSource.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }

}
