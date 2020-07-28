package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.dao.ShopCategoryDao;
import cn.reminis.o2o.entity.ShopCategory;
import cn.reminis.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sun
 * @date 2020-07-13 20:53
 * @description
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    /**
     * 根据查询条件获取店铺分类列表
     * @param condition
     * @return
     */
    @Override
    public List<ShopCategory> getCategories(ShopCategory condition) {
        return shopCategoryDao.queryShopCategory(condition);
    }
}
