package cn.reminis.o2o.web.frontend;

import cn.reminis.o2o.entity.HeadLine;
import cn.reminis.o2o.entity.ShopCategory;
import cn.reminis.o2o.service.HeadLineService;
import cn.reminis.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sun
 * @date 2020-07-27 20:32
 * @description
 */
@Controller
@RequestMapping("/frontend")
public class MainPageController {

    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private HeadLineService headLineService;

    /**
     * 初始化前端的展示信息，包括获取一级店铺类别以及头条列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listmainpageinfo",method = RequestMethod.GET)
    public Map<String,Object> listMainPageInfo(){
        Map<String,Object> data = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        try {
            //获取一级店铺列表，即parent_id为空
            shopCategoryList = shopCategoryService.getCategories(null);
            data.put("shopCategoryList",shopCategoryList);
        } catch (Exception e) {
            data.put("success",false);
            data.put("errMsg",e.getMessage());
            return data;
        }

        List<HeadLine> headLines = new ArrayList<>();
        try {
            //获取头条状态可用的头条列表
            HeadLine headLine = new HeadLine();
            headLine.setEnableStatus(1);
            headLines = headLineService.getHeadLineList(headLine);
            data.put("headLineList",headLines);
        } catch (Exception e) {
            data.put("success",false);
            data.put("errMsg",e.getMessage());
            return data;
        }
        data.put("success",true);
        return data;
    }


}
