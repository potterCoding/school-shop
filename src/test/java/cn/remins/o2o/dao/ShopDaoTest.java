package cn.remins.o2o.dao;

import cn.reminis.o2o.dao.ShopDao;
import cn.reminis.o2o.entity.Area;
import cn.reminis.o2o.entity.PersonInfo;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.entity.ShopCategory;
import cn.remins.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author sun
 * @date 2020-07-12 14:56
 * @description
 */
public class ShopDaoTest extends BaseTest {

    @Autowired
    private ShopDao shopDao;

    @Test
    public void testInsertShop(){
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setPriority(1);
        shop.setShopAddr("test");
        shop.setAdvice("审核中");
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        int result = shopDao.insertShop(shop);
        assertEquals(1,result);
    }

    @Test
    public void testUpdateShop(){
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopId(1L);
        shop.setShopCategory(shopCategory);
        shop.setLastEditTime(new Date());
        shop.setShopDesc("测试描述");
        int result = shopDao.updateShop(shop);
        assertEquals(1,result);
    }

}
