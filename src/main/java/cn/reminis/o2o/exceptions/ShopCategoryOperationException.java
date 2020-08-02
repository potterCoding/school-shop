package cn.reminis.o2o.exceptions;

/**
 * @author sun
 * @date 2020-08-02 22:33
 * @description
 */
public class ShopCategoryOperationException extends RuntimeException {
    private static final long serialVersionUID = -2562305465363451192L;

    public ShopCategoryOperationException(String message) {
        super(message);
    }
}
