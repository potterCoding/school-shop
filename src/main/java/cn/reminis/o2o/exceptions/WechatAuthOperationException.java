package cn.reminis.o2o.exceptions;

/**
 * @author sun
 * @date 2020-07-31 22:51
 * @description
 */
public class WechatAuthOperationException extends RuntimeException {

    private static final long serialVersionUID = 8628827094350893540L;

    public WechatAuthOperationException(String message) {
        super(message);
    }
}
