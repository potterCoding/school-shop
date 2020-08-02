package cn.reminis.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author sun
 * @date 2020-08-02 18:25
 * @description
 */
public class EncryptPropertyPlaceHolderConfigure extends PropertyPlaceholderConfigurer {

    private String[] encryptPropNames = {"jdbc.username","jdbc.password"};

    /**
     * 对关键的属性进行转换
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            //对已加密的字段进行解密
            String decrypt = AESUtils.decrypt(propertyValue);
            return decrypt;
        } else {
            return propertyValue;
        }
    }

    /**
     * 该属性书否已加密
     * @param propertyName
     * @return
     */
    private boolean isEncryptProp(String propertyName) {
        for (String encryptPropName : encryptPropNames) {
            if (encryptPropName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }


}
