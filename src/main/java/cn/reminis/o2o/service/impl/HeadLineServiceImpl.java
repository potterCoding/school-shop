package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.cache.JedisUtil;
import cn.reminis.o2o.dao.HeadLineDao;
import cn.reminis.o2o.entity.HeadLine;
import cn.reminis.o2o.exceptions.AreaOperationException;
import cn.reminis.o2o.exceptions.HeadLineOperationException;
import cn.reminis.o2o.service.HeadLineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sun
 * @date 2020-07-27 20:29
 * @description
 */
@Service
public class HeadLineServiceImpl implements HeadLineService {
    private Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
        //定义redis中key的前缀
        String key = HLLISTKEY;
        //定义就收对象
        List<HeadLine> headLineList = null;
        //定义jackson的数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        //拼接出redis的key
        if (headLineCondition.getEnableStatus() != null) {
            key = key + "_" + headLineCondition.getEnableStatus();
        }
        //判断key是否存在
        if (!jedisKeys.exists(key)){
            //若不存在，则从数据库取出相应信息
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(headLineList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        } else {
            //若存在，则从redis中获取
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,HeadLine.class);
            try {
                headLineList = mapper.readValue(jsonString,javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
        }
        return headLineList;
    }
}
