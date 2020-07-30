package cn.reminis.o2o.web.frontend;

import cn.reminis.o2o.dto.ProductExecution;
import cn.reminis.o2o.entity.Product;
import cn.reminis.o2o.entity.ProductCategory;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.service.ProductCategoryService;
import cn.reminis.o2o.service.ProductService;
import cn.reminis.o2o.service.ShopService;
import cn.reminis.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sun
 * @date 2020-07-29 19:49
 * @description
 */
@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 获取店铺信息以及该店偶下得商品类别列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listshopdetailpageinfo",method = RequestMethod.GET)
    public Map<String,Object> listShopDetailPageInfo(HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop shop = null;
        List<ProductCategory> productCategoryList = null;
        if (shopId != -1L) {
            shop = shopService.getShopById(shopId);
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            data.put("shop",shop);
            data.put("productCategoryList",productCategoryList);
            data.put("success",true);
        } else {
            data.put("success",false);
            data.put("errMsg","empty shopId");
        }
        return data;
    }

    /**
     * 根据查询条件分页列出该店铺下面得所有商品
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listproductsbyshop",method = RequestMethod.GET)
    public Map<String,Object> listProductsByShop(HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (pageIndex > -1 && pageSize > -1 && shopId > -1L) {
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition3Search(shopId,productCategoryId,productName);
            ProductExecution pe = productService.getProductLsit(productCondition, pageIndex, pageSize);
            data.put("productList",pe.getProductList());
            data.put("count",pe.getCount());
            data.put("success",true);
        } else {
            data.put("success",false);
            data.put("errMsg","empty pageIndex or pageSize or shopId");
        }

        return data;
    }

    /**
     * 组合查询条件
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition3Search(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != -1L) {
            //查询某个商品类别下得商品列表
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (!StringUtils.isEmpty(productName)) {
            productCondition.setProductName(productName);
        }
        //只允许选出状态为已上架得商品
        productCondition.setEnableStatus(1);
        return productCondition;
    }

}
