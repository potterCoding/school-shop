package cn.reminis.o2o.util;

import cn.reminis.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author sun
 * @date 2020-07-12 15:19
 * @description 图片处理工具类
 */
public class ImageUtil {

    //获取classpath的绝对值路径
    private static String basePath = Thread.currentThread().getContextClassLoader()
            .getResource("").getPath();
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public static File trnasferCommonsMultipartFileToFile(CommonsMultipartFile file) {
        File newFile = new File(file.getOriginalFilename());
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            logger.error("trnasferCommonsMultipartFileToFile error: " + e.getMessage());
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 处理缩略图并返回新生成图片的绝对值路径
     * @param thumbanil
     * @param targetAddr
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbanil, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbanil.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is: " + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is: " + PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbanil.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/water.png")), 0.5f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (Exception e) {
            logger.error("generateThumbnail error: " + e.getMessage());
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 处理缩略图并返回新生成图片的绝对值路径
     * @param thumbanil
     * @param targetAddr
     * @return
     */
    public static String generateNormalThumbnail(ImageHolder thumbanil, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbanil.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is: " + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is: " + PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbanil.getImage()).size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/water.png")), 0.5f)
                    .outputQuality(0.35f).toFile(dest);
        } catch (Exception e) {
            logger.error("generateNormalThumbnail error: " + e.getMessage());
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     * @return
     */
    public static String getRandomFileName() {
        //获取五位随机数
        int ranNum = random.nextInt(89999) + 10000;
        String nowTimeStr = df.format(new Date());
        return nowTimeStr + ranNum;
    }

    /**
     * 获取输入文件流的扩展名（例如jpg,png,jpeg）
     * @return
     */
    private static String getFileExtension(String fileName) {
        String originalFilename = fileName;
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    /**
     * 创建目标路径所涉及到的路径，即/home/work/reminis/xxx.jpg
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 判断storePath是文件的路径还是目录的路径
     * 如果storePath是文件路径，则删除该文件
     * 如果storePath是目录路径，则删除该目录下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if (fileOrPath.exists()) {
            if (fileOrPath.isDirectory()) {
                File[] files = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }

    }

    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("G:/images/yellow.jpeg"))
                .size(300,300).watermark(Positions.BOTTOM_RIGHT, //水印图添加的位置
                ImageIO.read(new File(basePath + "/water.png")),//添加水印图片的路径
                0.75f). //透明度
        outputQuality(0.8f) //压缩图片，压缩到80%
                .toFile("G:/images/yellow-new.jpeg"); //压缩后的图片输出的位置
    }
}
