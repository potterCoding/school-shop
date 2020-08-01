package cn.reminis.o2o.util.wechat;

import cn.reminis.o2o.dto.WechatUser;
import cn.reminis.o2o.entity.PersonInfo;
import cn.reminis.o2o.dto.UserAccessToken;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author sun
 * @date 2020-07-30 20:49
 * @description
 */
public class WechatUserUtil {
    private static Logger log = LoggerFactory.getLogger(WechatUserUtil.class);

    public static void getCode() throws UnsupportedEncodingException {
        // String codeUrl =
        // "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf0e81c3bee622d60&redirect_uri="
        // + URLEncoder.encode("www.cityrun.com", "utf-8")
        // +
        // "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    }

    public static UserAccessToken getUserAccessToken(String code)
            throws IOException {
//        Properties pro = new Properties();
//        pro.load(WechatUserUtil.class.getClassLoader().getResourceAsStream(
//                "weixin.properties"));
//        String appId = DESUtils
//                .getDecryptString(pro.getProperty("weixinappid"));
//        log.debug("appId:" + appId);
//        String appsecret = DESUtils.getDecryptString(pro
//                .getProperty("weixinappsecret"));
//        log.debug("secret:" + appsecret);

        String appId = "wxa18b6661330ce005";
        String appsecret = "04a38f7f4726422d7fe927a70ef2f759";

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + appId + "&secret=" + appsecret + "&code=" + code
                + "&grant_type=authorization_code";
        JSONObject jsonObject = WechatUtil.httpsRequest(url, "GET", null);
        log.debug("userAccessToken:" + jsonObject.toString());
        String accessToken = jsonObject.getString("access_token");
        if (null == accessToken) {
            log.debug("获取用户accessToken失败。");
            return null;
        }
        UserAccessToken token = new UserAccessToken();
        token.setAccessToken(accessToken);
        token.setExpiresIn(jsonObject.getString("expires_in"));
        token.setOpenId(jsonObject.getString("openid"));
        token.setRefreshToken(jsonObject.getString("refresh_token"));
        token.setScope(jsonObject.getString("scope"));
        return token;
    }

    public static WechatUser getUserInfo(String accessToken, String openId) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + accessToken + "&openid=" + openId + "&lang=zh_CN";
        JSONObject jsonObject = WechatUtil.httpsRequest(url, "GET", null);
        WechatUser user = new WechatUser();
        String openid = jsonObject.getString("openid");
        if (openid == null) {
            log.debug("获取用户信息失败。");
            return null;
        }
        user.setOpenId(openid);
        user.setNickName(jsonObject.getString("nickname"));
        user.setSex(jsonObject.getInt("sex"));
        user.setProvince(jsonObject.getString("province"));
        user.setCity(jsonObject.getString("city"));
        user.setCountry(jsonObject.getString("country"));
        user.setHeadimgurl(jsonObject.getString("headimgurl"));
        user.setPrivilege(null);
        // user.setUnionid(jsonObject.getString("unionid"));
        return user;
    }

    public static boolean validAccessToken(String accessToken, String openId) {
        String url = "https://api.weixin.qq.com/sns/auth?access_token="
                + accessToken + "&openid=" + openId;
        JSONObject jsonObject = WechatUtil.httpsRequest(url, "GET", null);
        int errcode = jsonObject.getInt("errcode");
        if (errcode == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static PersonInfo getPersonInfoFromRequest(WechatUser user) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(user.getNickName());
        personInfo.setGender(user.getSex() + "");
        personInfo.setProfileImg(user.getHeadimgurl());
        personInfo.setEnableStatus(1);
        return personInfo;
    }

}
