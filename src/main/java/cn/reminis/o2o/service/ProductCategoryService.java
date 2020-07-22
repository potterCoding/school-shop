package cn.reminis.o2o.service;

import cn.reminis.o2o.entity.ProductCategory;

import java.util.List;

/**
 * @author sun
 * @date 2020-07-22 20:20
 * @description
 */
public interface ProductCategoryService {

    /**
     * 查询指定某个店铺下的所有商品类别信息
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(Long shopId);
}
