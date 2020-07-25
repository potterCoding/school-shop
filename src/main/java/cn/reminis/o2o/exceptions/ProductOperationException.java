package cn.reminis.o2o.exceptions;

/**
 * @author sun
 * @date 2020-07-12 22:20
 * @description
 */
public class ProductOperationException extends RuntimeException {

    private static final long serialVersionUID = -1764857101825072738L;

    public ProductOperationException(String message) {
        super(message);
    }
}
