package cn.reminis.o2o.service;

import cn.reminis.o2o.dto.ProductCategoryExecution;
import cn.reminis.o2o.entity.ProductCategory;
import cn.reminis.o2o.exceptions.ProductCategoryOperationException;

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

    /**
     * 批量新增店铺信息
     * @param categoryList
     * @return
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> categoryList) throws ProductCategoryOperationException;

    /**
     * 将此商品类别下的商品的类别id置为空后，再删除该商品类别
     * @param produceCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution deleteProductCategory(Long produceCategoryId,Long shopId) throws ProductCategoryOperationException;

}
