package cn.zhiu.restful.api.cloud.disk.service;

import cn.zhiu.base.api.cloud.disk.bean.directory.UserDirectory;
import cn.zhiu.base.api.cloud.disk.bean.file.QUserFile;
import cn.zhiu.base.api.cloud.disk.bean.file.UserFile;
import cn.zhiu.base.api.cloud.disk.service.directory.UserDirectoryApiService;
import cn.zhiu.base.api.cloud.disk.service.file.UserFileApiService;
import cn.zhiu.bean.cloud.disk.entity.enums.file.Status;
import cn.zhiu.framework.base.api.core.request.ApiRequest;
import cn.zhiu.framework.base.api.core.service.impl.AbstractBaseApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public void recycle(List<Long> dirIdsList, List<Long> fileIdsList, Status status) {
        List<UserFile> userFileList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dirIdsList)) {
            List<UserDirectory> directoryList = new ArrayList<>();
            UserDirectory userDirectory;
            for (Long id : dirIdsList) {
                userDirectory = userDirectoryApiService.get(id);
                userDirectory.setStatus(status);
                directoryList.add(userDirectory);
            }
            List<UserFile> userFileAll = userFileApiService.findAll(ApiRequest.newInstance().filterIn(QUserFile.dirId, dirIdsList));
            userFileAll.forEach(a -> a.setStatus(status));
            userFileList.addAll(userFileAll);
            userDirectoryApiService.updateBatch(directoryList);
        }
        if (!CollectionUtils.isEmpty(fileIdsList)) {
            UserFile userFile;
            for (Long id : fileIdsList) {
                userFile = userFileApiService.get(id);
                userFile.setStatus(status);
                userFileList.add(userFile);
            }
            userFileApiService.updateBatch(userFileList);
        }
    }

    @Override
    public void deleteThoroughly(List<Long> dirIdsList, List<Long> fileIdsList) {
        List<Long> userFileList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dirIdsList)) {
            userDirectoryApiService.deleteBatch(dirIdsList);
            List<Long> userFileAll = userFileApiService.findAll(ApiRequest.newInstance().filterIn(QUserFile.dirId, dirIdsList)).stream().map(UserFile::getId).collect(Collectors.toList());
            userFileList.addAll(userFileAll);
        }

        if (!CollectionUtils.isEmpty(fileIdsList)) {
            userFileList.addAll(fileIdsList);
        }
        if (!CollectionUtils.isEmpty(userFileList)) {
            userFileApiService.deleteBatch(userFileList);
        }
    }
}
