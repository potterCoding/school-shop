package cn.reminis.o2o.dao.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sun
 * @date 2020-07-14 19:37
 * @description
 */
public class DynamicDataSourceHolder {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceHolder.class);

    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();
    public static final String DB_MASTER = "master";
    public static final String DB_SLAVE = "slave";

    public static String getDBType() {
        String db = contextHolder.get();
        if (db == null){
            db = DB_MASTER; // 默认为master,因为master即支持读也支持写
        }
        return db;
    }

    /**
     * 设置线程的dbType
     * @param str
     */
    public static void setDBType(String str) {
        logger.debug("所使用的数据源："+ str);
        contextHolder.set(str);
    }

    /**
     * 清理连接类型
     */
    public static void clearDBType(){
        contextHolder.remove();
    }
}
