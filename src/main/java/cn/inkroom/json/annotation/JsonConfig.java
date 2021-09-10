package cn.inkroom.json.annotation;

import java.util.LinkedList;
import java.util.List;

public class JsonConfig {


    /**
     * 启用的feature
     */
    private List<JsonFeature> enable;
    /**
     * 关闭的feature
     */
    private List<JsonFeature> disable;


    public JsonConfig() {
        enable = new LinkedList<>();
        disable = new LinkedList<>();

        // 设置默认的特性
        JsonFeature[] values = JsonFeature.values();
        for (JsonFeature f : values) {
            if (f.isEnable()) {
                enable.add(f);
            } else {
                disable.add(f);
            }
        }

    }

    /**
     * 判断某项feature是否启用
     *
     * @param feature 特性
     * @return 是否启用
     */
    public boolean isEnable(JsonFeature feature) {
        return enable.contains(feature);
    }

    /**
     * 启用某项特性
     *
     * @param feature
     */
    public void enable(JsonFeature feature) {
        disable.remove(feature);
        if (!enable.contains(feature))
            enable.add(feature);
    }

    /**
     * 关闭某项特性
     *
     * @param feature
     */
    public void disable(JsonFeature feature) {

        if (!disable.contains(feature))
            disable.add(feature);
        enable.remove(feature);

    }


}
