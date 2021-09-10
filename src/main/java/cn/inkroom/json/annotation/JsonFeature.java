package cn.inkroom.json.annotation;

/**
 * 用于启停一些功能
 */
public enum JsonFeature {


    /**
     * 允许最后一个元素后面的逗号存在；例如
     * [2,]
     * <br>
     * 默认启用
     */
    ALLOW_LAST_COMMA(true)
    ;

    /**
     * 是否默认启用
     */
    private boolean enable;

    JsonFeature(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }
}
