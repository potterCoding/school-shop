package cn.reminis.o2o.dao;

import cn.reminis.o2o.entity.PersonInfo;

/**
 * @author sun
 * @date 2020-07-31 22:20
 * @description
 */
public interface PersonInfoDao {

    /**
     * 通过用户id查询用户
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfoById(long userId);

    /**
     * 添加用户信息
     * @param personInfo
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);
}
