package cn.zhiu.restful.api.cloud.disk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 16:30
 * @Description:
 */
public class DeleteRequest implements Serializable {
    private List<Long> file;

    private List<Long> dir;

    public List<Long> getFile() {
        return file;
    }

    public void setFile(List<Long> file) {
        this.file = file;
    }

    public List<Long> getDir() {
        return dir;
    }

    public void setDir(List<Long> dir) {
        this.dir = dir;
    }
}
