package cn.reminis.o2o.dao;

import cn.reminis.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sun
 * @date 2020-07-27 20:18
 * @description
 */
public interface HeadLineDao {

    /**
     * 头条查询列表
     * @param headLineCondition
     * @return
     */
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);

}
