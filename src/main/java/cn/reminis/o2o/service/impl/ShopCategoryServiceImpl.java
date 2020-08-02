package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.cache.JedisUtil;
import cn.reminis.o2o.dao.ShopCategoryDao;
import cn.reminis.o2o.entity.HeadLine;
import cn.reminis.o2o.entity.ShopCategory;
import cn.reminis.o2o.exceptions.AreaOperationException;
import cn.reminis.o2o.exceptions.ShopCategoryOperationException;
import cn.reminis.o2o.service.ShopCategoryService;
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
 * @date 2020-07-13 20:53
 * @description
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    private Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    /**
     * 根据查询条件获取店铺分类列表
     * @param condition
     * @return
     */
    @Override
    public List<ShopCategory> getCategories(ShopCategory condition) {
        //定义redis中key的前缀
        String key = SCLISTKEY;
        //定义接收对象
        List<ShopCategory> shopCategoryList = null;
        ObjectMapper mapper = new ObjectMapper();
        //拼接redis的key
        if (condition == null) {
            //若条件为空，则列出所有的首页大类，及parentId为空的店铺列表
            key = key + "_allfirstlevel";
        } else if (condition != null && condition.getParent() != null
                && condition.getParent().getShopCategoryId() != null) {
            //若parentId为非空，则列出parentId下的所有子类别
            key = key + "_parent" + condition.getParent().getShopCategoryId();
        } else if (condition != null) {
            //列出所有子类别，不管其属于哪个类，都列出来
            key = key + "_allsecondlevel";
        }

        //判断key是否存在
        if (!jedisKeys.exists(key)){
            //若不存在，则从数据库取出相应信息
            shopCategoryList = shopCategoryDao.queryShopCategory(condition);
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(shopCategoryList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        } else {
            //若存在，则从redis中获取
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,ShopCategory.class);
            try {
                shopCategoryList = mapper.readValue(jsonString,javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
        }

        return shopCategoryList;
    }
}
