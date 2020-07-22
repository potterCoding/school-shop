package cn.reminis.o2o.dao;

import cn.reminis.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查找店铺信息
     * @param shopId
     * @return
     */
    Shop queryShopById(Long shopId);

    /**
     * 分页查询店铺
     * @param shopCondition 店铺名称模糊查询，店铺状态，店铺类别，，区域ID，owner
     * @param rowIndex 当前页码
     * @param pageSize 页大小
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);

    /**
     * 返回queryShopList总数
     * @param shopCondition
     * @return
     */
    int queryShopCount( @Param("shopCondition") Shop shopCondition);
}
