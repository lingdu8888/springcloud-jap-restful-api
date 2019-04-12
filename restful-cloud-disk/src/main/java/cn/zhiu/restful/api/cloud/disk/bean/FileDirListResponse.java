package cn.zhiu.restful.api.cloud.disk.bean;

import cn.zhiu.base.api.cloud.disk.bean.directory.UserDirectory;
import cn.zhiu.base.api.cloud.disk.bean.file.UserFile;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 17:03
 * @Description:
 */
public class FileDirListResponse implements Serializable {

    private List<UserFile> file;

    private List<UserDirectory> dir;


    public List<UserFile> getFile() {
        return file;
    }

    public void setFile(List<UserFile> file) {
        this.file = file;
    }

    public List<UserDirectory> getDir() {
        return dir;
    }

    public void setDir(List<UserDirectory> dir) {
        this.dir = dir;
    }
}
