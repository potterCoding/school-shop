package cn.reminis.o2o.dao.split;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;

/**
 * @author sun
 * @date 2020-07-14 19:44
 * @description  mybatis会将增删改的操作封装在update中
 */
@Intercepts({@Signature(type = Executor.class,method = "update",args = {MappedStatement.class,Object.class}),
        @Signature(type = Executor.class,method = "query",
                args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})})
public class DynamicDataSourceInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    /**
     * 拦截方法
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 判断当前操作是否是事务的
        // 使用@Transactional来处理，则会返回true
        boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        String lookupKey = DynamicDataSourceHolder.DB_MASTER;
        if ( !transactionActive ) {
            // 如果是查询操作
            if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                //selectKey 为自增id查询主键SELECT_KEY_SUFFIX()方法，使用主库
                if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)){
                    lookupKey = DynamicDataSourceHolder.DB_MASTER;
                } else {
                    BoundSql boundSql = ms.getSqlSource().getBoundSql(args[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                    //增删改使用主库，查使用从库
                    if (sql.matches(REGEX)) {
                        lookupKey = DynamicDataSourceHolder.DB_MASTER;
                    } else {
                        lookupKey = DynamicDataSourceHolder.DB_SLAVE;
                    }
                }
            }

        } else {
            lookupKey = DynamicDataSourceHolder.DB_MASTER;
        }
        logger.debug("设置方法[{}] use [{}] Strategy,SqlCommandType [{}] ...",
                ms.getId(), lookupKey, ms.getSqlCommandType().name());
        DynamicDataSourceHolder.setDBType(lookupKey);
        return invocation.proceed();
    }

    /**
     * 决定返回封装好的对象还是代理对象
     * 增删改查得操作
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        //当我们拦截的对象是Executor时，就拦截，通过intercept()方法 决定所使用的数据源
        //为什么要拦截Executor类型呢？因为在我们的mybatis中，Executor是用来支持一系列增删改查操作的
        //只要我们检测到拦截的对象包含增删改查操作，就拦截下来，使用intercept()方法，决定所使用的数据源
        if (target instanceof Executor) {
            return Plugin.wrap(target,this);
        } else {
            return target;
        }
    }

    /**
     * 在类初始化的时候，去做一些相关的设置
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {

    }
}
