package cn.reminis.o2o.service;

import cn.reminis.o2o.dto.ShopExecution;
import cn.reminis.o2o.entity.Shop;

import java.io.File;

/**
 * @author sun
 * @date 2020-07-12 22:01
 * @description
 */
public interface ShopService {
    ShopExecution addShop(Shop shop, File shopImg);
}
