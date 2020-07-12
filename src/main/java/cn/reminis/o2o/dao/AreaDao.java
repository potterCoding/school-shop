package cn.reminis.o2o.dao;

import cn.reminis.o2o.entity.Area;

import java.util.List;

/**
 * @author sun
 * @date 2020-07-12 10:38
 * @description
 */
public interface AreaDao {

    /**
     * 列出区域列表
     */
    List<Area> queryArea();
}
