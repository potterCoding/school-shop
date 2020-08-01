package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.dao.PersonInfoDao;
import cn.reminis.o2o.dao.WechatAuthDao;
import cn.reminis.o2o.dto.WechatAuthExecution;
import cn.reminis.o2o.entity.PersonInfo;
import cn.reminis.o2o.entity.WechatAuth;
import cn.reminis.o2o.enums.WechatAuthStateEnum;
import cn.reminis.o2o.exceptions.WechatAuthOperationException;
import cn.reminis.o2o.service.WechatAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author sun
 * @date 2020-07-31 22:52
 * @description
 */
@Service
public class WechatAuthServiceImpl implements WechatAuthService {
    private Logger logger = LoggerFactory.getLogger(WechatAuthServiceImpl.class);

    @Autowired
    private WechatAuthDao wechatAuthDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatAuthByOpenId(openId);
    }

    @Override
    @Transactional
    public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
        if (wechatAuth == null || wechatAuth.getOpenId() == null) {
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            wechatAuth.setCreateTime(new Date());
            //如果微信账号里及夹带着用户信息，并且用户id为空，则认为该用户第一次使用平台（且通过微信登陆）
            //则自动创建用户信息
            PersonInfo personInfo = wechatAuth.getPersonInfo();
            if (personInfo != null && personInfo.getUserId() == null) {
                try {
                    personInfo.setCreateTime(new Date());
                    personInfo.setEnableStatus(1);
                    int num = personInfoDao.insertPersonInfo(personInfo);
                    wechatAuth.setPersonInfo(personInfo);
                    if (num <= 0) {
                        throw new WechatAuthOperationException("添加用户信息失败");
                    }
                } catch (Exception e) {
                    logger.error("insertPersonInfo error: " + e.toString());
                    throw new WechatAuthOperationException("insertPersonInfo error: " + e.getMessage());
                }
            }
            //创建专属于该平台的微信账号
            int num = wechatAuthDao.insertWechatAuth(wechatAuth);
            if (num <= 0) {
                throw new WechatAuthOperationException("账号创建失败");
            } else {
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS,wechatAuth);
            }
        } catch (Exception e) {
            logger.error("insertWechatAuth error: " + e.toString());
            throw new WechatAuthOperationException("insertWechatAuth error: " + e.getMessage());
        }
    }
}
