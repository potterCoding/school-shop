package cn.reminis.o2o.service;

import cn.reminis.o2o.entity.ShopCategory;

import java.util.List;

/**
 * @author sun
 * @date 2020-07-13 20:52
 * @description
 */
public interface ShopCategoryService {

    String SCLISTKEY = "shopcategorylist";

    List<ShopCategory> getCategories(ShopCategory condition);

}
