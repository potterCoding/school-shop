package cn.reminis.o2o.util;

/**
 * @author sun
 * @date 2020-07-12 15:41
 * @description
 */
public class PathUtil {

    private static String separator = System.getProperty("file.separator");

    /**
     * 返回项目图片的根路径
     * @return
     */
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = "G:/images/";
        }else {
            basePath = "/home/reminis/imgage";
        }
        basePath = basePath.replace("/",separator);
        return basePath;
    }

    /**
     * 依据不同的业务需求，返回项目图片的子路径
     * @param shopIp
     * @return
     */
    public static String getShopImagePath(Long shopIp){
        String imagePath = "upload/item/shop/" + shopIp + "/";
        return imagePath.replace("/",separator);
    }

}
