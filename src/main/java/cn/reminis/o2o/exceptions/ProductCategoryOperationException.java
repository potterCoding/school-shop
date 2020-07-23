package cn.reminis.o2o.exceptions;

/**
 * @author sun
 * @date 2020-07-12 22:20
 * @description
 */
public class ProductCategoryOperationException extends RuntimeException {

    private static final long serialVersionUID = -3016700954828380153L;

    public ProductCategoryOperationException(String message) {
        super(message);
    }
}
