package cn.reminis.o2o.service.impl;

import cn.reminis.o2o.dao.PersonInfoDao;
import cn.reminis.o2o.entity.PersonInfo;
import cn.reminis.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sun
 * @date 2020-07-31 23:07
 * @description
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfoById(Long userId) {
        return personInfoDao.queryPersonInfoById(userId);
    }
}
