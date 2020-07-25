package cn.remins.o2o.dao;

import cn.reminis.o2o.dao.ProductDao;
import cn.reminis.o2o.entity.Product;
import cn.reminis.o2o.entity.ProductCategory;
import cn.reminis.o2o.entity.Shop;
import cn.remins.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author sun
 * @date 2020-07-24 22:26
 * @description
 */
public class ProductDaoTest extends BaseTest {

    @Autowired
    private ProductDao productDao;

    @Test
    public void testInsertProduct() {
        Product product = new Product();
        product.setImgAddr("test");
        product.setEnableStatus(1);
        product.setLastEditTime(new Date());
        product.setPromotionPrice("12");
        product.setProductName("商品测试");
        product.setProductDesc("test");
        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);
        ProductCategory category = new ProductCategory();
        category.setProductCategoryId(2l);
        product.setProductCategory(category);
        int i = productDao.insertProduct(product);
        System.out.println(i);
    }

}
