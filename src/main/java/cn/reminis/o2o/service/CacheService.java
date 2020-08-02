package cn.reminis.o2o.service;

/**
 * @author sun
 * @date 2020-08-02 22:36
 * @description
 */
public interface CacheService {

    /**
     * 依据key前缀删除匹配模式下的所有key-value
     * 如传入shopcategory,则shopcategory_allfirstlevel等的，
     * 以shopcategory开头的key-value都会被清空
     *
     * @param keyPrefix
     */
    void removeFromCache(String keyPrefix);

}
