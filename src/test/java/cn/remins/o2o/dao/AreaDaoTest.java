package cn.remins.o2o.dao;

import cn.reminis.o2o.dao.AreaDao;
import cn.reminis.o2o.entity.Area;
import cn.remins.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author sun
 * @date 2020-07-12 10:45
 * @description 测试AreaDao
 */
public class AreaDaoTest extends BaseTest {

    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea(){
        List<Area> list = areaDao.queryArea();
        assertEquals(2,list.size());
    }

}
