package cn.reminis.o2o.web.frontend;

import cn.reminis.o2o.dto.ShopExecution;
import cn.reminis.o2o.entity.Area;
import cn.reminis.o2o.entity.Shop;
import cn.reminis.o2o.entity.ShopCategory;
import cn.reminis.o2o.service.AreaService;
import cn.reminis.o2o.service.ShopCategoryService;
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
 * @date 2020-07-27 21:33
 * @description
 */
@Controller
@RequestMapping("/frontend")
public class ShopListController {

    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;

    @ResponseBody
    @RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
    public Map<String,Object> listShopPageInfo(HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        //试着从前端获取parentId
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList = null;
        if (parentId != -1) {
            //如果parentId存在，则取出该一级shopCategory下的二级ShopCategory列表
            try {
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getCategories(shopCategoryCondition);
            } catch (Exception e) {
                data.put("success",false);
                data.put("errMsg", e.getMessage());
            }
        } else {
            try {
                //如果parentId不存在，则取出所有一级店铺分类类表
                shopCategoryList = shopCategoryService.getCategories(null);
            } catch (Exception e) {
                data.put("success",false);
                data.put("errMsg", e.getMessage());
            }
        }
        data.put("shopCategoryList",shopCategoryList);

        List<Area> areaList = null;
        try {
            areaList = areaService.getAreaList();
            data.put("areaList",areaList);
            data.put("success",true);
            return data;
        } catch (Exception e) {
            data.put("success",false);
            data.put("errMsg", e.getMessage());
        }
        return data;
    }

    @ResponseBody
    @RequestMapping(value = "/listshops",method = RequestMethod.GET)
    public Map<String,Object> listShops(HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if (pageIndex > -1 && pageSize > -1) {
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            //获取组合之后的查询条件
            Shop shopCondition = compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
            //根据查询条件和分页信息获取店铺列表 ，并返回总数
            ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
            data.put("shopList",se.getShopList());
            data.put("count",se.getCount());
            data.put("success",true);
        } else {
            data.put("success",false);
            data.put("errMsg","empty pageSize or pageIndex");
        }
        return data;
    }

    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != -1L) {
            //查询某一个一级shopCategory下面的所有二级ShopCategory里面的店铺列表
            ShopCategory childShopCategory = new ShopCategory();
            ShopCategory parentShopCategory = new ShopCategory();
            parentShopCategory.setShopCategoryId(parentId);
            childShopCategory.setParent(parentShopCategory);
            shopCondition.setShopCategory(childShopCategory);
        }
        if (shopCategoryId != -1L) {
            //查询某个二级shopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != -1) {
            //查询位于某个区域id下的店铺列表
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if ( !StringUtils.isEmpty(shopName)) {
            shopCondition.setShopName(shopName);
        }
        //只展示审核成功的店铺
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }

}
