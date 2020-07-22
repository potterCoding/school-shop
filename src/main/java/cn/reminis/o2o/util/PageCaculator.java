package cn.reminis.o2o.util;

/**
 * @author sun
 * @date 2020-07-22 21:21
 * @description
 */
public class PageCaculator {

    public static int calculatePageCount(int totalCount, int pageSize) {
        int idealPage = totalCount / pageSize;
        int totalPage = (totalCount % pageSize == 0) ? idealPage
                : (idealPage + 1);
        return totalPage;
    }

    public static int calculateRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }

}
