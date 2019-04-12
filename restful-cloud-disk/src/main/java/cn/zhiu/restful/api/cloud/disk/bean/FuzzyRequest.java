package cn.zhiu.restful.api.cloud.disk.bean;

import java.io.Serializable;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 17:32
 * @Description:
 */
public class FuzzyRequest implements Serializable {
    private Long id;
    private String fuzzy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFuzzy() {
        return fuzzy;
    }

    public void setFuzzy(String fuzzy) {
        this.fuzzy = fuzzy;
    }
}
