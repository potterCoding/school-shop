package cn.reminis.o2o.service;

import cn.reminis.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

/**
 * @author sun
 * @date 2020-07-27 20:27
 * @description
 */
public interface HeadLineService {

    /**
     * 根据传入的条件查寻指定的条件列表
     * @param headLineCondition
     * @return
     * @throws IOException
     */
    List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;

}
