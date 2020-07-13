package cn.reminis.o2o.web.superadmin;

import cn.reminis.o2o.entity.Area;
import cn.reminis.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sun
 * @date 2020-07-12 11:00
 * @description
 */
@Controller
@RequestMapping("/superadmin")
public class AreaController {

    private Logger logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/listarea",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listArea(){
        logger.info("====start====");
        long startTime = System.currentTimeMillis();

        Map<String,Object> map = new HashMap<>();
        List<Area> areas;
        try {
            areas = areaService.getAreaList();
            map.put("rows",areas);
            map.put("total",areas.size());
        } catch (Exception e) {
            e.getStackTrace();
            map.put("success",false);
            map.put("errMsg",e.toString());
        }

        logger.error("test error!");
        long endTime = System.currentTimeMillis();
        logger.debug("costTime: [{}ms]", endTime - startTime);
        logger.info("====add====");

        return map;
    }

}
