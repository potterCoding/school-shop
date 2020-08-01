package cn.reminis.o2o.service;

import cn.reminis.o2o.dto.WechatAuthExecution;
import cn.reminis.o2o.entity.WechatAuth;
import cn.reminis.o2o.exceptions.WechatAuthOperationException;

/**
 * @author sun
 * @date 2020-07-31 22:45
 * @description
 */
public interface WechatAuthService {

    /**
     * 通过openId查找平台对应的微信账号
     * @param openId
     * @return
     */
    WechatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信账号
     * @param wechatAuth
     * @return
     */
    WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;

}
