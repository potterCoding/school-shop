package cn.reminis.o2o.dao;

import cn.reminis.o2o.entity.WechatAuth;

/**
 * @author sun
 * @date 2020-07-31 22:23
 * @description
 */
public interface WechatAuthDao {

    /**
     * 通过openId查询对应平台的微信账号
     * @param openId
     * @return
     */
    WechatAuth queryWechatAuthByOpenId(String openId);

    /**
     * 添加对应本平台的微信账号
     * @param wechatAuth
     * @return
     */
    int insertWechatAuth(WechatAuth wechatAuth);

}
