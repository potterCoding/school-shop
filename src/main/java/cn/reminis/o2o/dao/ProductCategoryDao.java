package cn.reminis.o2o.dao;

import cn.reminis.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 批量新增商品类别
     * @param categoryList
     * @return
     */
    int batchInsertProductCategory(@Param("categoryList") List<ProductCategory> categoryList);

    /**
     * 删除指定商品类别
     * @param productCategoryId
     * @return
     */
    int deleteProductCategoryDao(@Param("productCategoryId") Long productCategoryId, @Param("shopId") Long shopId);
}
