package cn.reminis.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author sun
 * @date 2020-07-13 20:13
 * @description
 */
@Controller
@RequestMapping(value = "shopadmin",method = RequestMethod.GET)
public class ShopAdminController {

    @RequestMapping(value = "/shopoperation")
    public String shopOperation(){
        return "shop/shopoperation";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList(){
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopmanagement")
    public String shopManagement(){
        return "shop/shopmanagement";
    }

    @RequestMapping(value = "/productcategorymanage")
    public String productCategoryManage(){
        return "shop/productcategorymanage";
    }

    @RequestMapping("/productoperation")
    public String productOperation() {
        return "shop/productoperation";
    }

}
