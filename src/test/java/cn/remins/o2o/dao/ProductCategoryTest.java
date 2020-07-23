package cn.remins.o2o.dao;

import cn.reminis.o2o.dao.ProductCategoryDao;
import cn.reminis.o2o.entity.ProductCategory;
import cn.remins.o2o.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sun
 * @date 2020-07-23 21:14
 * @description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryTest extends BaseTest {

    @Autowired
    private ProductCategoryDao categoryDao;

    @Test
    public void test() {
        ProductCategory category = new ProductCategory();
        category.setProductCategoryName("类别1");
        category.setCreateTime(new Date());
        category.setPriority(2);
        category.setShopId(1L);

        ProductCategory category1 = new ProductCategory();
        category1.setProductCategoryName("类别2");
        category1.setCreateTime(new Date());
        category1.setPriority(6);
        category1.setShopId(1L);

        List<ProductCategory> categories = new ArrayList<>();
        categories.add(category);
        categories.add(category1);

        int num = categoryDao.batchInsertProductCategory(categories);
        System.out.println(num);
    }
}
