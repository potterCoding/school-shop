package cn.reminis.o2o.web;

import cn.reminis.o2o.dto.ShopExecution;
import cn.reminis.o2o.entity.PersonInfo;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.enums.ShopStateEnum;
import cn.reminis.o2o.service.ShopService;
import cn.reminis.o2o.util.HttpServletRequestUtil;
import cn.reminis.o2o.util.ImageUtil;
import cn.reminis.o2o.util.PathUtil;
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
import java.util.HashMap;
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

    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> registerShop(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        //1.接收并转化相应的参数，包括店铺信息及图片信息 shopStr是前台约定好的
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
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
            PersonInfo owner = new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
            File shopImgFile = new File(PathUtil.getImgBasePath() + ImageUtil.getRandomFileName());
            try {
                shopImgFile.createNewFile();
            } catch (IOException e) {
                result.put("success",false);
                result.put("errMsg", e.getMessage());
                return result;
            }
            try {
                inputStreamToFile(shopImg.getInputStream(),shopImgFile);
            } catch (IOException e) {
                result.put("success",false);
                result.put("errMsg", e.getMessage());
                return result;
            }
            ShopExecution se = shopService.addShop(shop,shopImgFile);
            if (se.getState() == ShopStateEnum.CHECK.getState()) {
                result.put("success", true);
            } else {
                result.put("success",false);
                result.put("errMsg",se.getStateInfo());
            }
        } else {
            result.put("success",false);
            result.put("errMsg", "请输入店铺信息");
            return result;
        }

        //3.返回结果
        return result;
    }

    private static void inputStreamToFile(InputStream inputStream, File file) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer,0,bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用inputStreamToFile产生异常： " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("inputStreamToFile关闭IO产生异常： " + e.getMessage());
            }
        }
    }

}
