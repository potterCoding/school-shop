package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.dao.HeadLineDao;
import cn.reminis.o2o.entity.HeadLine;
import cn.reminis.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author sun
 * @date 2020-07-27 20:29
 * @description
 */
@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    private HeadLineDao headLineDao;

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
        return headLineDao.queryHeadLine(headLineCondition);
    }
}
