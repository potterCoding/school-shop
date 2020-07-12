package cn.reminis.o2o.dao;

import cn.reminis.o2o.entity.Shop;

/**
 * @author sun
 * @date 2020-07-12 14:42
 * @description
 */
public interface ShopDao {

    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

}
