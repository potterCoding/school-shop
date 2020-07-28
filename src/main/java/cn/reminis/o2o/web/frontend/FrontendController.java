package cn.reminis.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author sun
 * @date 2020-07-27 20:54
 * @description
 */
@Controller
@RequestMapping("/frontend")
public class FrontendController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index() {
        return "frontend/index";
    }


}
