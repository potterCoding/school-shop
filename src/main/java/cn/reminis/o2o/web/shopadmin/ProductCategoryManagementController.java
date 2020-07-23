package cn.reminis.o2o.web.shopadmin;

import cn.reminis.o2o.dto.ProductCategoryExecution;
import cn.reminis.o2o.dto.Result;
import cn.reminis.o2o.entity.ProductCategory;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.enums.ProductCategoryStateEnum;
import cn.reminis.o2o.exceptions.ProductCategoryOperationException;
import cn.reminis.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sun
 * @date 2020-07-22 20:22
 * @description
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @ResponseBody
    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    public Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
        Shop shop = new Shop();
        shop.setShopId(1L);
        request.getSession().setAttribute("currentShop",shop);

        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> productCategories = null;

        if (currentShop != null && currentShop.getShopId() > 0) {
            productCategories = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<>(true,productCategories);
        } else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<>(false, ps.getState(), ps.getStateInfo());
        }

    }

    @ResponseBody
    @RequestMapping(value = "addproductcategorys",method = RequestMethod.POST)
    public Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory> productCategories, HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory pc : productCategories) {
            pc.setShopId(currentShop.getShopId());
        }

        if (productCategories == null || productCategories.isEmpty()) {
            data.put("success",false);
            data.put("errMsg","请至少输入一个商品类别");
        } else {
            try {
                ProductCategoryExecution pce = productCategoryService.batchAddProductCategory(productCategories);
                if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    data.put("success",true);
                } else {
                    data.put("success",false);
                    data.put("errMsg",pce.getStateInfo());
                }
            } catch (ProductCategoryOperationException e) {
                data.put("success",false);
                data.put("errMsg",e.toString());
            }
        }
        return data;
    }

    @ResponseBody
    @RequestMapping(value = "removeproductcategory",method = RequestMethod.POST)
    public Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        if (productCategoryId != null && productCategoryId > 0) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution pce = productCategoryService.deleteProductCategory(
                        productCategoryId, currentShop.getShopId());
                if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    data.put("success",true);
                } else {
                    data.put("success",false);
                    data.put("errMsg",pce.getStateInfo());
                }
            } catch (ProductCategoryOperationException e) {
                data.put("success",false);
                data.put("errMsg",e.toString());
            }
        } else {
            data.put("success",false);
            data.put("errMsg","请至少输入一个商品类别");
        }
        return data;
    }

}
