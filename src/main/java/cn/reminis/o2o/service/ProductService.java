package cn.reminis.o2o.service;

import cn.reminis.o2o.dto.ImageHolder;
import cn.reminis.o2o.dto.ProductExecution;
import cn.reminis.o2o.entity.Product;
import cn.reminis.o2o.exceptions.ProductOperationException;

import java.awt.*;
import java.io.InputStream;
import java.util.List;

/**
 * @author sun
 * @date 2020-07-24 20:22
 * @description
 */
public interface ProductService {

    /**
     * 添加商品信息以及图片处理
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgList) throws ProductOperationException;

}
