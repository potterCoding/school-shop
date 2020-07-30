package cn.reminis.o2o.web.wechat;

import cn.reminis.o2o.entity.WechatAuth;
import cn.reminis.o2o.dto.WechatUser;
import cn.reminis.o2o.util.wechat.WeiXinUserUtil;
import cn.reminis.o2o.dto.UserAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sun
 * @date 2020-07-30 20:47
 * @description
 *
 * 从微信菜单点击后调用的接口，可以在url里增加参数（role_type）来表明是从商家还是从玩家按钮进来的，依次区分登陆后跳转不同的页面
 * 玩家会跳转到index.html页面
 * 商家如果没有注册，会跳转到注册页面，否则跳转到任务管理页面
 * 如果是商家的授权用户登陆，会跳到授权店铺的任务管理页面
 *
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {
    private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);

//    @Resource
//    private PersonInfoService personInfoService;
//    @Resource
//    private WechatAuthService WechatAuthService;

//    @Resource
//    private ShopService shopService;

//    private static final String FRONTEND = "1";
//    private static final String SHOPEND = "2";

    @RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("weixin login get...");
        String code = request.getParameter("code");
        String roleType = request.getParameter("state");
        log.debug("weixin login code:" + code);
        WechatAuth auth = null;
        WechatUser user = null;
        String openId = null;
        if (null != code) {
            UserAccessToken token;
            try {
                token = WeiXinUserUtil.getUserAccessToken(code);
                log.debug("weixin login token:" + token.toString());
                String accessToken = token.getAccessToken();
                openId = token.getOpenId();
                user = WeiXinUserUtil.getUserInfo(accessToken, openId);
                log.debug("weixin login user:" + user.toString());
                request.getSession().setAttribute("openId", openId);
//                auth = WechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                log.error("error in getUserAccessToken or getUserInfo or findByOpenId: "
                        + e.toString());
                e.printStackTrace();
            }
        }

        if (user != null) {
            //获取到微信验证的信息后，返回指定的路由
            return "frontend/index";
        } else {
            return null;
        }

//        ==========todo begin=============
//        获取到openId后，可以通过它去数据库判断该微信账号是否在我们网站里有对应的账号了
//        没有的话这里可以自动创建上，直接实现微信与咱们网站的无缝对接
//        ===========todo end==============

//        log.debug("weixin login success.");
//        log.debug("login role_type:" + roleType);
//        if (FRONTEND.equals(roleType)) {
//            PersonInfo personInfo = WeiXinUserUtil
//                    .getPersonInfoFromRequest(user);
//            if (auth == null) {
//                personInfo.setCustomerFlag(1);
//                auth = new WechatAuth();
//                auth.setOpenId(openId);
//                auth.setPersonInfo(personInfo);
//                WechatAuthExecution we = WechatAuthService.register(auth, null);
//                if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
//                    return null;
//                }
//            }
//            personInfo = personInfoService.getPersonInfoById(auth.getUserId());
//            request.getSession().setAttribute("user", personInfo);
//            return "frontend/index";
//        }
//        if (SHOPEND.equals(roleType)) {
//            PersonInfo personInfo = null;
//            WechatAuthExecution we = null;
//            if (auth == null) {
//                auth = new WechatAuth();
//                auth.setOpenId(openId);
//                personInfo = WeiXinUserUtil.getPersonInfoFromRequest(user);
//                personInfo.setShopOwnerFlag(1);
//                auth.setPersonInfo(personInfo);
//                we = WechatAuthService.register(auth, null);
//                if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
//                    return null;
//                }
//            }
//            personInfo = personInfoService.getPersonInfoById(auth.getUserId());
//            request.getSession().setAttribute("user", personInfo);
//            ShopExecution se = shopService.getByEmployeeId(personInfo
//                    .getUserId());
//            request.getSession().setAttribute("user", personInfo);
//            if (se.getShopList() == null || se.getShopList().size() <= 0) {
//                return "shop/registershop";
//            } else {
//                request.getSession().setAttribute("shopList", se.getShopList());
//                return "shop/shoplist";
//            }
//        }
    }
}
