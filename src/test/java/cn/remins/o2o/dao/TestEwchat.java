//package cn.remins.o2o.dao;
//
//import cn.reminis.o2o.dao.WechatAuthDao;
//import cn.reminis.o2o.entity.PersonInfo;
//import cn.reminis.o2o.entity.WechatAuth;
//import cn.remins.o2o.BaseTest;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Date;
//
///**
// * @author sun
// * @date 2020-08-01 0:42
// * @description
// */
//public class TestEwchat extends BaseTest {
//
//
//    @Autowired
//    private WechatAuthDao wechatAuthDao;
//
//    @Test
//    public void test(){
//        WechatAuth wechatAuth = new WechatAuth();
//        wechatAuth.setOpenId("13123214");
//        PersonInfo personInfo = new PersonInfo();
//        personInfo.setUserId(1L);
//        wechatAuth.setPersonInfo(personInfo);
//        wechatAuth.setCreateTime(new Date());
//        int i = wechatAuthDao.insertWechatAuth(wechatAuth);
//        System.out.println(i);
//    }
//
//
//}
