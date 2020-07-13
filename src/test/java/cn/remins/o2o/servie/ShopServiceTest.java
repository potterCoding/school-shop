package cn.remins.o2o.servie;

import cn.reminis.o2o.dto.ShopExecution;
import cn.reminis.o2o.entity.Area;
import cn.reminis.o2o.entity.PersonInfo;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.entity.ShopCategory;
import cn.reminis.o2o.enums.ShopStateEnum;
import cn.reminis.o2o.service.ShopService;
import cn.remins.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author sun
 * @date 2020-07-12 22:26
 * @description
 */
public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop() throws IOException{
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
        shop.setShopName("测试店铺3");
        shop.setPhone("test3");
        shop.setShopDesc("test3");
        shop.setShopAddr("test3");
        shop.setPriority(1);
        shop.setAdvice("审核中");
        File shopImg = new File("G:/images/yellow.jpeg");
        InputStream inputStream = new FileInputStream(shopImg);
        ShopExecution shopExecution = shopService.addShop(shop, inputStream,shopImg.getName());
        assertEquals(ShopStateEnum.CHECK.getState(),shopExecution.getState());
    }

}
