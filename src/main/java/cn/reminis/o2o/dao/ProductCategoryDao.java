package cn.reminis.o2o.dao;

import cn.reminis.o2o.entity.ProductCategory;

import java.util.List;

/**
 * @author sun
 * @date 2020-07-22 20:14
 * @description
 */
public interface ProductCategoryDao {

    /**
     * 通过shopId查询店铺类别
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(Long shopId);

}
