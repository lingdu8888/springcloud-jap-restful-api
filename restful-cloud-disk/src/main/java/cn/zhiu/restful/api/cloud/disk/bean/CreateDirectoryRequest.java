package cn.zhiu.restful.api.cloud.disk.bean;

import java.io.Serializable;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 15:37
 * @Description:
 */
public class CreateDirectoryRequest implements Serializable {

    private Long id;
    private String directoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
}
