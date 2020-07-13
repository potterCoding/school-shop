package cn.remins.o2o.dao;

import cn.reminis.o2o.dao.ShopCategoryDao;
import cn.reminis.o2o.entity.ShopCategory;
import cn.remins.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author sun
 * @date 2020-07-13 20:45
 * @description
 */
public class ShopCategoryDaoTest extends BaseTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory(){
        List<ShopCategory> categories = shopCategoryDao.queryShopCategory(new ShopCategory());
        assertEquals(2,categories.size());

        ShopCategory scp = new ShopCategory();
        scp.setShopCategoryId(1L);
        ShopCategory sc = new ShopCategory();
        sc.setParent(scp);
        List<ShopCategory> categorParent = shopCategoryDao.queryShopCategory(sc);
        assertEquals(1,categorParent.size());
    }

}
