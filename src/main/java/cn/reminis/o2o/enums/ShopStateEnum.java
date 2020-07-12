package cn.reminis.o2o.enums;

/**
 * @author sun
 * @date 2020-07-12 21:45
 * @description
 */
public enum ShopStateEnum {

    CHECK(0,"审核中"),OFFLINE(-1,"非法店铺"),SUCCESS(1,"操作成功"),PASS(2,"通过认证"),
    INNNER_ERROR(-1001,"系统内部错误"),NULL_SHOPID(-1002,"shopId为空"),NULL_SHOP(-1003,"shop信息为空");

    private int state;
    private String stateInfo;

    private ShopStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ShopStateEnum stateOf(int state){
        for (ShopStateEnum shopStateEnum : values()) {
            if (shopStateEnum.getState() == state) {
                return shopStateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
