package cn.reminis.o2o.web.shopadmin;

import cn.reminis.o2o.dto.ImageHolder;
import cn.reminis.o2o.dto.ProductExecution;
import cn.reminis.o2o.entity.Product;
import cn.reminis.o2o.entity.ProductImg;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.enums.ProductStateEnum;
import cn.reminis.o2o.exceptions.ProductOperationException;
import cn.reminis.o2o.service.ProductService;
import cn.reminis.o2o.util.CodeUtil;
import cn.reminis.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
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
                multipartRequest = (MultipartHttpServletRequest) request;
                //取出缩略图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
                thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
                //取出详情图片并构建List<ImageHolder>列表对象，最多支持六张图片上传
                for (int i = 0; i < IMAGE_MAX_COUNT; i++) {
                    CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
                    if (productImgFile != null) {
                        //若取出第i个详情图片文件流不为空，则将其加入详情图列表
                        ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream());
                        productImgList.add(productImg);
                    } else {
                        //若取出第i个详情图片文件流为空,则终止循环
                        break;
                    }
                }
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

}
