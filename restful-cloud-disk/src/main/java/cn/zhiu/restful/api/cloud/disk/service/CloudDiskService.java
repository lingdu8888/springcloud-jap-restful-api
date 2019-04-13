package cn.zhiu.restful.api.cloud.disk.service;


import cn.zhiu.bean.cloud.disk.entity.enums.file.Status;

import java.util.List;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 16:33
 * @Description:
 */
public interface CloudDiskService {

    void recycle(List<Long> dirIdsList, List<Long> fileIdsList, Status status);


    void deleteThoroughly(List<Long> dirIdsList, List<Long> fileIdsList);
}
