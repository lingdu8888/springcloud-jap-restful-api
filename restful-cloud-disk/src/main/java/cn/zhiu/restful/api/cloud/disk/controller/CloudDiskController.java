package cn.zhiu.restful.api.cloud.disk.controller;

import cn.zhiu.base.api.cloud.disk.bean.directory.QUserDirectory;
import cn.zhiu.base.api.cloud.disk.bean.directory.UserDirectory;
import cn.zhiu.base.api.cloud.disk.bean.file.QUserFile;
import cn.zhiu.base.api.cloud.disk.bean.file.UserFile;
import cn.zhiu.base.api.cloud.disk.bean.operation.QUserOperation;
import cn.zhiu.base.api.cloud.disk.bean.operation.UserOperation;
import cn.zhiu.base.api.cloud.disk.service.directory.UserDirectoryApiService;
import cn.zhiu.base.api.cloud.disk.service.file.UserFileApiService;
import cn.zhiu.base.api.cloud.disk.service.operation.UserOperationApiService;
import cn.zhiu.bean.cloud.disk.entity.enums.file.Status;
import cn.zhiu.bean.cloud.disk.entity.enums.operation.FileOperationStatus;
import cn.zhiu.framework.base.api.core.constant.RequestHeaderConstants;
import cn.zhiu.framework.base.api.core.request.ApiRequest;
import cn.zhiu.framework.base.api.core.util.BeanMapping;
import cn.zhiu.framework.restful.api.core.bean.response.DataResponse;
import cn.zhiu.framework.restful.api.core.controller.AbstractBaseController;
import cn.zhiu.restful.api.cloud.disk.bean.*;
import cn.zhiu.restful.api.cloud.disk.service.CloudDiskService;
import com.alibaba.druid.support.spring.stat.annotation.Stat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Auther: yujuan
 * @Date: 19-4-11 16:15
 * @Description:
 */
@RestController
@RequestMapping
public class CloudDiskController extends AbstractBaseController {


    @Autowired
    UserDirectoryApiService userDirectoryApiService;

    @Autowired
    UserFileApiService userFileApiService;

    @Autowired
    UserOperationApiService userOperationApiService;

    @Autowired
    CloudDiskService cloudDiskService;

    /**
     * 新建文件夹接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/directory", method = RequestMethod.POST)
    public DataResponse save(@RequestBody CreateDirectoryRequest request) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.getId(), "所属文件夹不能为空！");
        Objects.requireNonNull(request.getDirectoryName(), "文件夹名称不能为空！");
        String path = "/";
        if (request.getId() != -1) {
            UserDirectory userDirectory = userDirectoryApiService.get(request.getId());
            path = userDirectory.getPath();
        }
        ApiRequest apiRequest = ApiRequest.newInstance().filterEqual(QUserDirectory.parentId, request.getId()).filterEqual(QUserDirectory.userId, currentUserId);

        List<UserDirectory> all = userDirectoryApiService.findAll(apiRequest);
        List<String> fileNameList = all.stream().map(UserDirectory::getDirectoryName).collect(Collectors.toList());
        UserDirectory saveModel = new UserDirectory();
        saveModel.setUserId(currentUserId);
        saveModel.setParentId(request.getId());
        saveModel.setDirectoryName(getNotExistName(fileNameList, request.getDirectoryName()));
        saveModel.setPath(path);
        saveModel = userDirectoryApiService.save(saveModel);
        return new DataResponse<>(saveModel);
    }

    /**
     * 删除文件到回收站
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteToRecycle", method = RequestMethod.PUT)
    public DataResponse deleteToRecycle(@RequestBody DeleteRequest request) {
        cloudDiskService.recycle(request.getDir(), request.getFile(), Status.RECYCLE);
        return new DataResponse<>();
    }

    /**
     * 回收站撤回
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/backToRecycle", method = RequestMethod.PUT)
    public DataResponse backToRecycle(@RequestBody DeleteRequest request) {
        cloudDiskService.recycle(request.getDir(), request.getFile(), Status.NORMAL);
        return new DataResponse<>();
    }

    /**
     * 获取回收站列表
     *
     * @return
     */
    @RequestMapping(value = "/recycle/get", method = RequestMethod.GET)
    public DataResponse recycleGet() {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        List<UserDirectory> directoryList = userDirectoryApiService.findAll(ApiRequest.newInstance().filterEqual(QUserDirectory.status, Status.RECYCLE.getValue()));
        List<UserFile> fileList = userFileApiService.findAll(ApiRequest.newInstance().filterEqual(QUserFile.status, Status.RECYCLE.getValue()));
        List<UserFileResponse> userFileResponseList = BeanMapping.mapList(fileList, UserFileResponse.class);
        userFileResponseList.forEach(a -> a.setUserOperationResponseList(BeanMapping.mapList(userOperationApiService.findAll(ApiRequest.newInstance().filterEqual(QUserOperation.fileId, a.getFileId())), UserOperationResponse.class)));
        FileDirListResponse fileDirListResponse = new FileDirListResponse();
        fileDirListResponse.setDir(directoryList);
        fileDirListResponse.setFile(userFileResponseList);
        return new DataResponse<>(fileDirListResponse);
    }


    /**
     * 彻底删除文件或者文件夹
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteThoroughly", method = RequestMethod.PUT)
    public DataResponse deleteThoroughly(@RequestBody DeleteRequest request) {
        cloudDiskService.deleteThoroughly(request.getDir(), request.getFile());
        return new DataResponse<>();
    }


    /**
     * 获取某个文件夹下的列表
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public DataResponse get(@RequestParam("id") Long id) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        List<UserDirectory> directoryList = userDirectoryApiService.findAll(ApiRequest.newInstance().filterEqual(QUserDirectory.parentId, id).filterEqual(QUserDirectory.status, Status.NORMAL.getValue()));
        List<UserFile> fileList = userFileApiService.findAll(ApiRequest.newInstance().filterEqual(QUserFile.dirId, id).filterEqual(QUserFile.status, Status.NORMAL.getValue()));
        List<UserFileResponse> userFileResponseList = BeanMapping.mapList(fileList, UserFileResponse.class);
        userFileResponseList.forEach(a -> a.setUserOperationResponseList(BeanMapping.mapList(userOperationApiService.findAll(ApiRequest.newInstance().filterEqual(QUserOperation.fileId, a.getFileId())), UserOperationResponse.class)));
        FileDirListResponse fileDirListResponse = new FileDirListResponse();
        fileDirListResponse.setDir(directoryList);
        fileDirListResponse.setFile(userFileResponseList);
        return new DataResponse<>(fileDirListResponse);
    }

    /**
     * 文件上传
     *
     * @param userFile
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public DataResponse upload(@RequestBody UserFile userFile) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        userFile.setUserId(currentUserId);
        ApiRequest apiRequest = ApiRequest.newInstance().filterEqual(QUserFile.dirId, userFile.getDirId()).filterEqual(QUserFile.userId, currentUserId).filterEqual(QUserFile.fileExtension, userFile.getFileExtension());
        List<String> fileNameList = userFileApiService.findAll(apiRequest).stream().map(UserFile::getFileName).collect(Collectors.toList());
        userFile.setFileName(getNotExistName(fileNameList, userFile.getFileName()));
        userFileApiService.save(userFile);
        return new DataResponse<>();
    }


    /**
     * 文件重命名
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/rename", method = RequestMethod.PUT)
    public DataResponse<UserFile> fileRename(@RequestBody FileRenameRequest request) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        ApiRequest apiRequest = ApiRequest.newInstance().filterEqual(QUserFile.dirId, request.getDirId()).filterEqual(QUserFile.userId, currentUserId).filterNotEqual(QUserFile.id, request.getId()).filterEqual(QUserFile.fileExtension, request.getFileExtension());
        List<String> fileNameList = userFileApiService.findAll(apiRequest).stream().map(UserFile::getFileName).collect(Collectors.toList());
        UserFile userFile = userFileApiService.get(request.getId());
        userFile.setFileName(getNotExistName(fileNameList, request.getFileName()));
        if (!StringUtils.isBlank(request.getFileExtension())) {
            userFile.setFileExtension(request.getFileExtension());
        }
        userFile = userFileApiService.update(userFile);
        return new DataResponse<>(userFile);
    }

    /**
     * 文件夹重命名
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/directory/rename", method = RequestMethod.PUT)
    public DataResponse directoryRename(@RequestBody DirectoryRenameRequest request) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        ApiRequest apiRequest = ApiRequest.newInstance().filterEqual(QUserDirectory.parentId, request.getParentId()).filterEqual(QUserDirectory.userId, currentUserId).filterNotEqual(QUserDirectory.id, request.getId());
        List<String> fileNameList = userDirectoryApiService.findAll(apiRequest).stream().map(UserDirectory::getDirectoryName).collect(Collectors.toList());
        UserDirectory userDirectory = userDirectoryApiService.get(request.getId());
        userDirectory.setDirectoryName(getNotExistName(fileNameList, request.getDirectoryName()));
        userDirectory = userDirectoryApiService.update(userDirectory);
        return new DataResponse<>(userDirectory);
    }


    @RequestMapping(value = "/file/operation", method = RequestMethod.POST)
    public DataResponse<Boolean> fileOperation(@RequestBody FileOperationRequest request) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        UserOperation userOperation = new UserOperation();
        userOperation.setUserId(currentUserId);
        userOperation.setType(request.getType());
        userOperation.setStatus(FileOperationStatus.PROCESSING);
        userOperation.setFileId(request.getFileId());
        userOperationApiService.save(userOperation);
        //TODO 发送消息到文件服务器
        return new DataResponse<>();
    }


    /**
     * 获取某个文件夹下搜索
     *
     * @return
     */
    @RequestMapping(value = "/fuzzy", method = RequestMethod.GET)
    public DataResponse fuzzy(@RequestParam("id") Long id, @RequestParam("fuzzy") String fuzzy) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        ApiRequest dirApiRequest = ApiRequest.newInstance().filterEqual(QUserDirectory.parentId, id).filterEqual(QUserDirectory.userId, currentUserId).filterLike(QUserDirectory.directoryName, fuzzy).filterEqual(QUserDirectory.status, Status.NORMAL.getValue());

        List<UserDirectory> directoryList = userDirectoryApiService.findAll(dirApiRequest);

        List<UserFile> fileList = userFileApiService.findAll(ApiRequest.newInstance().filterEqual(QUserFile.userId, currentUserId).filterEqual(QUserFile.dirId, id).filterLike(QUserFile.fileName, fuzzy).filterEqual(QUserFile.status, Status.NORMAL.getValue()));
        List<UserFileResponse> userFileResponseList = BeanMapping.mapList(fileList, UserFileResponse.class);
        userFileResponseList.forEach(a -> a.setUserOperationResponseList(BeanMapping.mapList(userOperationApiService.findAll(ApiRequest.newInstance().filterEqual(QUserOperation.fileId, a.getFileId())), UserOperationResponse.class)));
        FileDirListResponse fileDirListResponse = new FileDirListResponse();
        fileDirListResponse.setDir(directoryList);
        fileDirListResponse.setFile(userFileResponseList);

        return new DataResponse<>(fileDirListResponse);
    }


    /**
     * 根据新增的文件名称 检查是否与已有的文件冲突 有冲突则重命名
     *
     * @param nameList
     * @param name
     * @return
     */
    private static String getNotExistName(List<String> nameList, String name) {
        while (true) {
            if (!nameList.contains(name)) return name;
            Integer left = name.lastIndexOf("(");
            if (left == -1) {
                return getNotExistName(nameList, name + "(1)");
            }
            Integer right = name.indexOf(")", left);
            if (right == -1) {
                return getNotExistName(nameList, name + "(1)");
            }
            if (right < name.length() - 1) return getNotExistName(nameList, name + "(1)");
            String substring = name.substring(left + 1, right);
            try {
                Integer i = Integer.parseInt(substring) + 1;
                return getNotExistName(nameList, name.substring(0, left) + "(" + i + ")");

            } catch (ClassCastException e) {
                return getNotExistName(nameList, name + "(1)");
            }
        }
    }
}

