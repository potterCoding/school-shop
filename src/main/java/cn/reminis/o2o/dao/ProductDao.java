package cn.reminis.o2o.dao;

import cn.reminis.o2o.entity.Product;

/**
 * @author sun
 * @date 2020-07-24 20:06
 * @description
 */
public interface ProductDao {

    /**
     * 插入商品
     * @param product
     * @return
     */
    int insertProduct(Product product);

}
