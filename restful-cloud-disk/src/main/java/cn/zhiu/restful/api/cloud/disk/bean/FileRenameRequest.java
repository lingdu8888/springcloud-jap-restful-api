package cn.zhiu.restful.api.cloud.disk.bean;

import java.io.Serializable;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 17:17
 * @Description:
 */
public class FileRenameRequest implements Serializable {

    private Long id;
    private Long dirId;
    private String fileName;
    private String fileExtension;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDirId() {
        return dirId;
    }

    public void setDirId(Long dirId) {
        this.dirId = dirId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
