package cn.reminis.o2o.web.shopadmin;

import cn.reminis.o2o.dto.ShopExecution;
import cn.reminis.o2o.entity.Area;
import cn.reminis.o2o.entity.PersonInfo;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.entity.ShopCategory;
import cn.reminis.o2o.enums.ShopStateEnum;
import cn.reminis.o2o.exceptions.ShopOperationException;
import cn.reminis.o2o.service.AreaService;
import cn.reminis.o2o.service.ShopCategoryService;
import cn.reminis.o2o.service.ShopService;
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
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sun
 * @date 2020-07-12 22:48
 * @description
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagermentController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    /**
     * 店铺详情信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    public Map<String,Object> getShopById(HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getShopById(shopId);
                List<Area> areaList = areaService.getAreaList();
                data.put("shop",shop);
                data.put("areaList",areaList);
                data.put("success",true);
            } catch (Exception e) {
                data.put("success",false);
                data.put("errMsg", e.getMessage());
            }
        } else {
            data.put("success",false);
            data.put("errMsg", "empty  shopId");
        }
        return data;
    }

    /**
     * 店铺初始化信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    public Map<String,Object> getShopInitInfo(){
        Map<String,Object> data = new HashMap<>();
        List<ShopCategory> categories;
        List<Area> areas;
        try {
            categories = shopCategoryService.getCategories(new ShopCategory());
            areas = areaService.getAreaList();
            data.put("shopCategoryList",categories);
            data.put("areaList",areas);
            data.put("success",true);
        } catch (Exception e) {
            data.put("success",false);
            data.put("errMsg", e.getMessage());
        }
        return data;
    }

    /**
     * 店铺注册
     * @param request
     * @return
     */
    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> registerShop(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            result.put("success", false);
            result.put("errMsg", "输入了错误的验证码");
            return result;
        }
        //1.接收并转化相应的参数，包括店铺信息及图片信息 shopStr是前台约定好的
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr,Shop.class);
        } catch (IOException e) {
            result.put("success",false);
            result.put("errMsg", e.getMessage());
            return result;
        }

        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //判断这个请求中是否有上传的文件流
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //shopImg是前台约定好的
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            result.put("success",false);
            result.put("errMsg", "上传图片不能为空");
            return result;
        }

        //2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution se;
            try {
                se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
                    result.put("success", true);
                    //该用户可以操作的店铺列表
                    List<Shop> shops = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shops == null || shops.size() == 0) {
                        shops = new ArrayList<>();
                    }
                    shops.add(se.getShop());
                    request.getSession().setAttribute("shopList",shops);
                } else {
                    result.put("success",false);
                    result.put("errMsg",se.getStateInfo());
                }
            } catch (ShopOperationException e) {
                result.put("success",false);
                result.put("errMsg",e.getMessage());
            } catch (IOException e) {
                result.put("success",false);
                result.put("errMsg",e.getMessage());
            }
        } else {
            result.put("success",false);
            result.put("errMsg", "请输入店铺信息");
        }

        //3.返回结果
        return result;
    }

    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modifyShop(HttpServletRequest request){
        Map<String,Object> data = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            data.put("success", false);
            data.put("errMsg", "输入了错误的验证码");
            return data;
        }
        //1.接收并转化相应的参数，包括店铺信息及图片信息 shopStr是前台约定好的
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr,Shop.class);
        } catch (IOException e) {
            data.put("success",false);
            data.put("errMsg", e.getMessage());
            return data;
        }

        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //判断这个请求中是否有上传的文件流
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //shopImg是前台约定好的
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //2.修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution se;
            try {
                if ( shopImg == null) {
                    se = shopService.modifyShop(shop,null,null);
                } else {
                    se = shopService.modifyShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                }
                if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                    data.put("success", true);
                } else {
                    data.put("success",false);
                    data.put("errMsg",se.getStateInfo());
                }
            } catch (ShopOperationException e) {
                data.put("success",false);
                data.put("errMsg",e.getMessage());
            } catch (IOException e) {
                data.put("success",false);
                data.put("errMsg",e.getMessage());
            }
        } else {
            data.put("success",false);
            data.put("errMsg", "请输入shopId");
        }

        //3.返回结果
        return data;
    }

    /**
     * CommonsMultipartFile 转为 File，直接无法转换，通过转成inputStream间接转换
     * @param inputStream
     * @param file
     */
//    private static void inputStreamToFile(InputStream inputStream, File file) {
//        OutputStream outputStream = null;
//        try {
//            outputStream = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer,0,bytesRead);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("调用inputStreamToFile产生异常： " + e.getMessage());
//        } finally {
//            try {
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//            } catch (IOException e) {
//                throw new RuntimeException("inputStreamToFile关闭IO产生异常： " + e.getMessage());
//            }
//        }
//    }

}
