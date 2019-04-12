package cn.zhiu.restful.api.cloud.disk.service;


import java.util.List;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 16:33
 * @Description:
 */
public interface CloudDiskService {

    void deleteToRecycle(List<Long> dirIdsList, List<Long> fileIdsList);

    void deleteThoroughly(List<Long> dirIdsList, List<Long> fileIdsList);
}
