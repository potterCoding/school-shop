package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.dao.AreaDao;
import cn.reminis.o2o.entity.Area;
import cn.reminis.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sun
 * @date 2020-07-12 10:55
 * @description
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
