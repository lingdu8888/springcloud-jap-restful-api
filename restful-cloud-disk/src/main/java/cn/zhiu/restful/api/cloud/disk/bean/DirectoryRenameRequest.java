package cn.zhiu.restful.api.cloud.disk.bean;

import java.io.Serializable;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 17:17
 * @Description:
 */
public class DirectoryRenameRequest implements Serializable {

    private Long id;

    private Long parentId;

    private String directoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
}
