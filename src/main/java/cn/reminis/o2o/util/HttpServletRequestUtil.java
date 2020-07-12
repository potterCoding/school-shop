package cn.reminis.o2o.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sun
 * @date 2020-07-12 22:52
 * @description
 */
public class HttpServletRequestUtil {

    public static int getInt(HttpServletRequest request,String key) {
        try {
          return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static long getLong(HttpServletRequest request,String key){
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1L;
        }
    }

    public static double getDouble(HttpServletRequest request,String key){
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1d;
        }
    }

    public static boolean getBoolean(HttpServletRequest request,String key){
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(HttpServletRequest request,String key){
        try {
            String result = request.getParameter(key);
            if (!StringUtils.isEmpty(result)) {
                result = result.trim();
            } else {
                result = null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
