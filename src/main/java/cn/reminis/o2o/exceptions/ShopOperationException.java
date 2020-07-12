package cn.reminis.o2o.exceptions;

/**
 * @author sun
 * @date 2020-07-12 22:20
 * @description
 */
public class ShopOperationException extends RuntimeException {

    private static final long serialVersionUID = -770338138475153310L;

    public ShopOperationException(String message) {
        super(message);
    }
}
