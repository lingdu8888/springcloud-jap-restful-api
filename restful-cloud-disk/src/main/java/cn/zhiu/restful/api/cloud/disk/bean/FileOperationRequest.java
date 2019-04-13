package cn.zhiu.restful.api.cloud.disk.bean;


import cn.zhiu.bean.cloud.disk.entity.enums.operation.FileOperationType;

import java.io.Serializable;

/**
 * @Auther: yujuan
 * @Date: 19-4-13 09:30
 * @Description:
 */
public class FileOperationRequest implements Serializable {

    private String fileId;
    private String type;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public FileOperationType getType() {
        return FileOperationType.valueOf(type);
    }

    public void setType(String type) {
        this.type = type;
    }
}
