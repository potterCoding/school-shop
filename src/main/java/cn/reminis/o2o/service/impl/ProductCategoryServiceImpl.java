package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.dao.ProductCategoryDao;
import cn.reminis.o2o.entity.ProductCategory;
import cn.reminis.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }
}
