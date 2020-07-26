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

    /**
     * 查询商品列表分页，可根据商品名称模糊查询，商品状态，商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductLsit(Product productCondition,int pageIndex,int pageSize);

    /**
     * 通过商品id查询唯一的 商品信息
     * @param productId
     * @return
     */
    Product getProductById(Long productId);

    /**
     * 修改商品信息及图片处理
     * @param thumbnail
     * @param productImageHolderList
     * @return
     */
    ProductExecution modifyProduct(Product product,ImageHolder thumbnail,
                                   List<ImageHolder> productImageHolderList) throws ProductOperationException;

}
