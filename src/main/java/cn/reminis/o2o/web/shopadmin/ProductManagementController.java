package cn.reminis.o2o.web.shopadmin;

import cn.reminis.o2o.dto.ImageHolder;
import cn.reminis.o2o.dto.ProductExecution;
import cn.reminis.o2o.entity.Product;
import cn.reminis.o2o.entity.ProductCategory;
import cn.reminis.o2o.entity.ProductImg;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.enums.ProductStateEnum;
import cn.reminis.o2o.exceptions.ProductOperationException;
import cn.reminis.o2o.service.ProductCategoryService;
import cn.reminis.o2o.service.ProductService;
import cn.reminis.o2o.util.CodeUtil;
import cn.reminis.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sun
 * @date 2020-07-24 21:16
 * @description
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    //支持上传商品详情图的最大数量
    private static final int IMAGE_MAX_COUNT = 6;

    @ResponseBody
    @RequestMapping(value = "/addproduct",method = RequestMethod.POST)
    public Map<String,Object> addProduct(HttpServletRequest request){
        Map<String,Object> data = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            data.put("success",false);
            data.put("errMsg","验证码错误");
            return data;
        }

        //接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        MultipartHttpServletRequest multipartRequest = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            //若请求中存在文件流，则取出相关文件（包括缩略图和详情图）
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImg(request, thumbnail, productImgList);
            } else {
                data.put("success",false);
                data.put("errMsg","图片上传不能为空");
                return data;
            }
        } catch (Exception e) {
            data.put("success",false);
            data.put("errMsg","图片上传不能为空");
            return data;
        }

        try {
            //尝试获取前端传过来的表单String流，并将其转换为Product实体类
            product = mapper.readValue(productStr,Product.class);
        } catch (Exception e) {
            data.put("success",false);
            data.put("errMsg",e.toString());
            return data;
        }

        //若Product信息，缩略图及详情图片列表为非空，则开始进行 商品添加操作
        if (product != null && thumbnail != null && productImgList.size() > 0) {
            try {
                //从session中获取当前店铺的id，并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                //执行添加操作
                ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    data.put("success",true);
                } else {
                    data.put("success",false);
                    data.put("errMsg",pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                data.put("success",false);
                data.put("errMsg",e.toString());
            }
        } else {
            data.put("success",false);
            data.put("errMsg","请输入商品信息");
        }

        return data;
    }

    private ImageHolder handleImg(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        //取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }                //取出详情图片并构建List<ImageHolder>列表对象，最多支持六张图片上传
        for (int i = 0; i < IMAGE_MAX_COUNT; i++) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
            if (productImgFile != null) {
                //若取出第i个详情图片文件流不为空，则将其加入详情图列表
                ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                productImgList.add(productImg);
            } else {
                //若取出第i个详情图片文件流为空,则终止循环
                break;
            }
        }
        return thumbnail;
    }

    @ResponseBody
    @RequestMapping(value = "/getproductbyid",method = RequestMethod.GET)
    public Map<String,Object> getProductById(@RequestParam("productId") Long productId) {
        Map<String,Object> data = new HashMap<>();
        if (productId > 1) {
            Product product = productService.getProductById(productId);
            //获取该店铺下的商品类别
            List<ProductCategory> productCategoryList = productCategoryService
                    .getProductCategoryList(product.getShop().getShopId());
            data.put("product",product);
            data.put("productCategoryList",productCategoryList);
            data.put("success",true);
        } else {
            data.put("success",false);
            data.put("errMsg","empty productId");
        }
        return data;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyproduct",method = RequestMethod.POST)
    public Map<String,Object> modifyProduct(HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        //验证码判断
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            data.put("success",false);
            data.put("errMsg","输入了错误的验证码");
            return data;
        }

        //接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            //若请求中存在文件流，则取出相关文件（包括缩略图和详情图）
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImg(request, thumbnail, productImgList);
            }
        } catch (Exception e) {
            data.put("success",false);
            data.put("errMsg","图片上传不能为空");
            return data;
        }

        try {
            //尝试获取前端传过来的表单String流，并将其转换为Product实体类
            product = mapper.readValue(productStr,Product.class);
        } catch (Exception e) {
            data.put("success",false);
            data.put("errMsg",e.toString());
            return data;
        }

        if (product != null) {
            try {
                //从session中获取当前店铺的id，并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                //开始进行商品信息变更操作
                ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    data.put("success",true);
                } else {
                    data.put("success",false);
                    data.put("errMsg",pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                data.put("success",false);
                data.put("errMsg",e.toString());
            }
        } else {
            data.put("success",false);
            data.put("errMsg","请输入商品信息");
        }
        return data;
    }

    @ResponseBody
    @RequestMapping(value = "getproductlistbyshop",method = RequestMethod.GET)
    public Map<String,Object> getProductListByShop(HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        //获取前台传过来的页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //获取前台传过来的每页要求返回的商品数量
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            //获取传入的需要检索的搜索条件
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            ProductExecution pe = productService.getProductLsit(productCondition, pageIndex, pageSize);
            data.put("productList",pe.getProductList());
            data.put("count",pe.getCount());
            data.put("success",true);
        } else {
            data.put("success",false);
            data.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return data;
    }

    /**
     * 组合列表查询条件
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        //若有指定类别的要求，则添加进去
        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        //若需要按照商品名称模糊查询，则添加进去
        if (!StringUtils.isEmpty(productName)) {
            productCondition.setProductName(productName);
        }
        return productCondition;
    }

}
