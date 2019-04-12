package cn.zhiu.restful.api.cloud.disk.service;

import cn.zhiu.base.api.cloud.disk.bean.directory.UserDirectory;
import cn.zhiu.base.api.cloud.disk.bean.file.UserFile;
import cn.zhiu.base.api.cloud.disk.service.directory.UserDirectoryApiService;
import cn.zhiu.base.api.cloud.disk.service.file.UserFileApiService;
import cn.zhiu.bean.cloud.disk.entity.enums.file.Status;
import cn.zhiu.framework.base.api.core.service.impl.AbstractBaseApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 16:34
 * @Description:
 */
@Service
@Transactional
public class CloudDiskServiceImpl extends AbstractBaseApiServiceImpl implements CloudDiskService {

    @Autowired
    UserFileApiService userFileApiService;

    @Autowired
    UserDirectoryApiService userDirectoryApiService;


    @Override
    public void deleteToRecycle(List<Long> dirIdsList, List<Long> fileIdsList) {
        if (!CollectionUtils.isEmpty(dirIdsList)) {
            List<UserDirectory> directoryList = new ArrayList<>();
            UserDirectory userDirectory;
            for (Long id : dirIdsList) {
                userDirectory = new UserDirectory();
                userDirectory.setId(id);
                userDirectory.setStatus(Status.RECYCLE);
                directoryList.add(userDirectory);
            }
            userDirectoryApiService.updateBatch(directoryList);
        }
        if (!CollectionUtils.isEmpty(fileIdsList)) {
            List<UserFile> userFileList = new ArrayList<>();
            UserFile userFile;
            for (Long id : dirIdsList) {
                userFile = new UserFile();
                userFile.setId(id);
                userFile.setStatus(Status.RECYCLE);
                userFileList.add(userFile);
            }
            userFileApiService.updateBatch(userFileList);
        }
    }

    @Override
    public void deleteThoroughly(List<Long> dirIdsList, List<Long> fileIdsList) {
        if (!CollectionUtils.isEmpty(dirIdsList)) {
            userDirectoryApiService.deleteBatch(dirIdsList);
        }

        if (!CollectionUtils.isEmpty(fileIdsList)) {
            userFileApiService.deleteBatch(fileIdsList);
        }
    }
}
