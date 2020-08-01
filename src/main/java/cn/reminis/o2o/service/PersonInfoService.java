package cn.reminis.o2o.service;

import cn.reminis.o2o.entity.PersonInfo;

/**
 * @author sun
 * @date 2020-07-31 23:06
 * @description
 */
public interface PersonInfoService {

    /**
     * 根据用户id获取personInfo信息
     * @param userId
     * @return
     */
    PersonInfo getPersonInfoById(Long userId);

}
