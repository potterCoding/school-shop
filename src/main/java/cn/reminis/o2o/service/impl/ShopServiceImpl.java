package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.dao.ShopDao;
import cn.reminis.o2o.dto.ShopExecution;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.enums.ShopStateEnum;
import cn.reminis.o2o.exceptions.ShopOperationException;
import cn.reminis.o2o.service.ShopService;
import cn.reminis.o2o.util.ImageUtil;
import cn.reminis.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Date;

/**
 * @author sun
 * @date 2020-07-12 22:01
 * @description
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, File shopImg) {
        //空值判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //给店铺信息赋初始值
            //0审核中
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectNum = shopDao.insertShop(shop);
            if (effectNum <= 0) {
                throw new ShopOperationException("店铺创建失败");
            }
            if (!StringUtils.isEmpty(shopImg)) {
                //存储图片 （基本类型传值：字面值常量的拷贝 ，引用类型传值：对象在堆中内存地址的拷贝）
                try {
                    addShopImg(shop,shopImg);
                } catch (Exception e) {
                    throw new ShopOperationException("addShopImg error: " + e.getMessage());
                }
                //更新店铺的图片地址
                effectNum = shopDao.updateShop(shop);
                if (effectNum <= 0) {
                    throw new ShopOperationException("更新图片地址失败");
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error: " + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    private void addShopImg(Shop shop, File shopImg) {
        //获取shop图片的相对路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);
        shop.setShopImg(shopImgAddr);
    }
}
