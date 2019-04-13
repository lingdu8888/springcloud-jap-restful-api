package cn.zhiu.restful.api.cloud.disk.bean;

import cn.zhiu.bean.cloud.disk.entity.enums.operation.FileOperationStatus;
import cn.zhiu.bean.cloud.disk.entity.enums.operation.FileOperationType;

import java.io.Serializable;

/**
 * @Auther: yujuan
 * @Date: 19-4-14 00:09
 * @Description:
 */
public class UserOperationResponse  implements Serializable {

    private FileOperationType type;

    private FileOperationStatus status;

    public FileOperationType getType() {
        return type;
    }

    public void setType(FileOperationType type) {
        this.type = type;
    }

    public FileOperationStatus getStatus() {
        return status;
    }

    public void setStatus(FileOperationStatus status) {
        this.status = status;
    }
}
