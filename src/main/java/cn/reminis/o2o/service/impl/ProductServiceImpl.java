package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.dao.ProductDao;
import cn.reminis.o2o.dao.ProductImgDao;
import cn.reminis.o2o.dto.ImageHolder;
import cn.reminis.o2o.dto.ProductExecution;
import cn.reminis.o2o.entity.Product;
import cn.reminis.o2o.entity.ProductImg;
import cn.reminis.o2o.enums.ProductStateEnum;
import cn.reminis.o2o.exceptions.ProductOperationException;
import cn.reminis.o2o.service.ProductService;
import cn.reminis.o2o.util.ImageUtil;
import cn.reminis.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sun
 * @date 2020-07-24 20:28
 * @description
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductImgDao productImgDao;
    @Autowired
    private ProductDao productDao;

    /**
     * 1.处理缩略图，获取缩略图相对路径并赋值给product
     * 2.向tb_product写入商品信息，获取productId
     * 3.结合productId批量处理商品详情图片
     * 4.将商品详情图列表批量插入tb_product_img中
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    @Transactional
    @Override
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            //给商品设置上默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //默认为上架的状态
            product.setEnableStatus(1);
            //若商品缩略图不为空则添加
            if (thumbnail != null) {
                addThumbnail(product,thumbnail);
            }
            try {
                //创建商品信息
                int num = productDao.insertProduct(product);
                if (num <= 0) {
                    throw new ProductOperationException("创建商品失败");
                }
            }catch (Exception e) {
                throw new ProductOperationException("创建商品失败: " + e.getMessage());
            }
            //若商品详情图片不为空则添加
            if (productImgList != null && productImgList.size() > 0) {
                addProductImgList(product,productImgList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS,product);
        } else {
            //传参为空则返回错误信息
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 批量添加图片
     * @param product
     * @param productImgList
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgList) {
        //获取图片存储路径，这里直接存放到相应店铺的文件夹下
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgs = new ArrayList<>();
        //遍历图片流，添加进prductImg实体类中
        for (ImageHolder imageHolder : productImgList) {
            String addr = ImageUtil.generateNormalThumbnail(imageHolder, dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(addr);
            productImg.setCreateTime(new Date());
            productImg.setProductId(product.getProductId());
            productImgs.add(productImg);
        }

        if (productImgs.size() > 0) {
            try {
                int num = productImgDao.batchInsertProductImg(productImgs);
                if (num <= 0) {
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图片失败：" + e.getMessage());
            }
        }
    }

    /**
     * 添加商品缩略图
     * @param product
     * @param thumbnail
     */
    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String addr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(addr);
    }
}
