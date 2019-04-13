package cn.zhiu.restful.api.cloud.disk.bean;

import cn.zhiu.base.api.cloud.disk.bean.operation.UserOperation;
import cn.zhiu.bean.cloud.disk.entity.enums.file.Status;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: yujuan
 * @Date: 19-4-14 00:08
 * @Description:
 */
public class UserFileResponse implements Serializable {

    private Long id;

    private String userId;

    private String fileName;

    private String fileId;

    private Long fileSize;

    private String fileExtension;

    private Long dirId;

    private String path;

    private Status status;

    private Date fileUpdateTime;

    private List<UserOperationResponse> userOperationResponseList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Long getDirId() {
        return dirId;
    }

    public void setDirId(Long dirId) {
        this.dirId = dirId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getFileUpdateTime() {
        return fileUpdateTime;
    }

    public void setFileUpdateTime(Date fileUpdateTime) {
        this.fileUpdateTime = fileUpdateTime;
    }

    public List<UserOperationResponse> getUserOperationResponseList() {
        return userOperationResponseList;
    }

    public void setUserOperationResponseList(List<UserOperationResponse> userOperationResponseList) {
        this.userOperationResponseList = userOperationResponseList;
    }
}
