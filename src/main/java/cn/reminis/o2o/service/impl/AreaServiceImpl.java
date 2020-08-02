package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.cache.JedisUtil;
import cn.reminis.o2o.dao.AreaDao;
import cn.reminis.o2o.entity.Area;
import cn.reminis.o2o.exceptions.AreaOperationException;
import cn.reminis.o2o.service.AreaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sun
 * @date 2020-07-12 10:55
 * @description
 */
@Service
public class AreaServiceImpl implements AreaService {
    private Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Override
    public List<Area> getAreaList() {
        String key = AREALISTKEY;
        List<Area> areaList = null;
        //定义jackson操作装换类
        ObjectMapper mapper = new ObjectMapper();
        //判断key是否存在
        if (!jedisKeys.exists(key)){
            areaList = areaDao.queryArea();
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        } else {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,Area.class);
            try {
                areaList = mapper.readValue(jsonString,javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        }
        return areaList;
    }
}
