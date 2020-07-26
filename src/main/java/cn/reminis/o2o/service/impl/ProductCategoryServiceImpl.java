package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.dao.ProductCategoryDao;
import cn.reminis.o2o.dao.ProductDao;
import cn.reminis.o2o.dto.ProductCategoryExecution;
import cn.reminis.o2o.entity.Product;
import cn.reminis.o2o.entity.ProductCategory;
import cn.reminis.o2o.enums.ProductCategoryStateEnum;
import cn.reminis.o2o.exceptions.ProductCategoryOperationException;
import cn.reminis.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author sun
 * @date 2020-07-22 20:21
 * @description
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Transactional
    @Override
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> categoryList) throws ProductCategoryOperationException {
        if (categoryList == null || categoryList.isEmpty()) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
        for (ProductCategory productCategory : categoryList) {
            productCategory.setCreateTime(new Date());
        }
        try {
            int num = productCategoryDao.batchInsertProductCategory(categoryList);
            if (num <= 0)
                throw new ProductCategoryOperationException("店铺类别创建失败");
            else
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
        } catch (Exception e) {
            throw new ProductCategoryOperationException("batchAddProductCategory error: " + e.getMessage());
        }

    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(Long produceCategoryId, Long shopId) throws ProductCategoryOperationException {
        //将此商品类别下的商品的类别id置为空
        try {
            int num = productDao.updateProductCategoryIdToNull(produceCategoryId);
            if (num < 0) {
                throw new ProductCategoryOperationException("商品类别更新失败");
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("deleteProductCategory error: " + e.getMessage());
        }
        //删除该商品类别
        try {
            int num = productCategoryDao.deleteProductCategoryDao(produceCategoryId, shopId);
            if (num <= 0) {
                throw new ProductCategoryOperationException("商品类别删除失败");
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("deleteProductCategory error: " + e.getMessage());
        }
    }
}
