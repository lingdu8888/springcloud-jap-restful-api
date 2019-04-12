package cn.zhiu.restful.api.cloud.disk.controller;

import cn.zhiu.base.api.cloud.disk.bean.directory.QUserDirectory;
import cn.zhiu.base.api.cloud.disk.bean.directory.UserDirectory;
import cn.zhiu.base.api.cloud.disk.bean.file.QUserFile;
import cn.zhiu.base.api.cloud.disk.bean.file.UserFile;
import cn.zhiu.base.api.cloud.disk.service.directory.UserDirectoryApiService;
import cn.zhiu.base.api.cloud.disk.service.file.UserFileApiService;
import cn.zhiu.framework.base.api.core.constant.RequestHeaderConstants;
import cn.zhiu.framework.base.api.core.request.ApiRequest;
import cn.zhiu.framework.restful.api.core.bean.response.DataResponse;
import cn.zhiu.framework.restful.api.core.controller.AbstractBaseController;
import cn.zhiu.restful.api.cloud.disk.bean.*;
import cn.zhiu.restful.api.cloud.disk.service.CloudDiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    CloudDiskService cloudDiskService;

    /**
     * 新建文件夹接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/directory", method = RequestMethod.POST)
    public DataResponse<UserDirectory> save(@RequestBody CreateDirectoryRequest request) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.getId(), "所属文件夹不能为空！");
        Objects.requireNonNull(request.getDirectoryName(), "文件夹名称不能为空！");
        UserDirectory userDirectory = userDirectoryApiService.get(request.getId());
        Objects.requireNonNull(request, "父级目录不存在");
        UserDirectory saveModel = new UserDirectory();
        saveModel.setUserId(currentUserId);
        saveModel.setParentId(request.getId());
        saveModel.setDirectionName(getNotExistDirectoryName(request.getId(), currentUserId, request.getDirectoryName()));
        saveModel.setPath(userDirectory.getPath());
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
    public DataResponse<Boolean> deleteToRecycle(@RequestBody DeleteRequest request) {
        cloudDiskService.deleteToRecycle(request.getDir(), request.getFile());
        return new DataResponse<>();
    }

    /**
     * 彻底删除文件或者文件夹
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteThoroughly", method = RequestMethod.PUT)
    public DataResponse<Boolean> deleteThoroughly(@RequestBody DeleteRequest request) {
        cloudDiskService.deleteThoroughly(request.getDir(), request.getFile());
        return new DataResponse<>();
    }


    /**
     * 获取某个文件夹下的列表
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public DataResponse<FileDirListResponse> get(@RequestParam("id") Long id) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        ApiRequest dirApiRequest = ApiRequest.newInstance();
        dirApiRequest.conditionEqual(QUserDirectory.parentId, id);
        List<UserDirectory> directoryList = userDirectoryApiService.findAll(dirApiRequest);

        ApiRequest fileApiRequest = ApiRequest.newInstance();
        fileApiRequest.conditionEqual(QUserFile.dirId, id);
        List<UserFile> fileList = userFileApiService.findAll(fileApiRequest);

        FileDirListResponse fileDirListResponse = new FileDirListResponse();
        fileDirListResponse.setDir(directoryList);
        fileDirListResponse.setFile(fileList);

        return new DataResponse<>(fileDirListResponse);
    }

    /**
     * 文件上传
     *
     * @param userFile
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public DataResponse<UserFile> upload(@RequestBody UserFile userFile) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        userFile.setUserId(currentUserId);
        userFileApiService.save(userFile);
        return new DataResponse<>();
    }


    /**
     * 文件重命名
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/rename", method = RequestMethod.PUT)
    public DataResponse<UserFile> fileRename(@RequestBody FileRenameRequest request) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        UserFile userFile = new UserFile();
        userFile.setUserId(currentUserId);
        userFile.setFileName(getNotExistFileName(request.getDirId(), currentUserId, request.getFileName()));
        userFile = userFileApiService.update(userFile);
        return new DataResponse<>(userFile);
    }

    /**
     * 文件夹重命名
     * @param request
     * @return
     */
    @RequestMapping(value = "directory/rename",method = RequestMethod.PUT)
    public DataResponse<UserDirectory> directoryRename(@RequestBody DirectoryRenameRequest request) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        UserDirectory userDirectory = new UserDirectory();
        userDirectory.setUserId(currentUserId);
        userDirectory.setDirectionName(getNotExistFileName(request.getParentId(), currentUserId, request.getDirectoryName()));
        userDirectory = userDirectoryApiService.update(userDirectory);
        return new DataResponse<>(userDirectory);
    }



    /**
     * 获取某个文件夹下搜索
     *
     * @return
     */
    @RequestMapping(value = "/fuzzy", method = RequestMethod.POST)
    public DataResponse<FileDirListResponse> fuzzy(@RequestBody FuzzyRequest request) {
        String currentUserId = getHeader(RequestHeaderConstants.ACCESS_USERID);
        Objects.requireNonNull(currentUserId, "当前用户未登录");
        ApiRequest dirApiRequest = ApiRequest.newInstance();
        dirApiRequest.conditionEqual(QUserDirectory.parentId, request.getId());
        dirApiRequest.conditionEqual(QUserDirectory.userId, currentUserId);
        dirApiRequest.like(QUserDirectory.directionName, request.getId());

        List<UserDirectory> directoryList = userDirectoryApiService.findAll(dirApiRequest);

        ApiRequest fileApiRequest = ApiRequest.newInstance();
        fileApiRequest.conditionEqual(QUserFile.dirId, id);
        List<UserFile> fileList = userFileApiService.findAll(fileApiRequest);

        FileDirListResponse fileDirListResponse = new FileDirListResponse();
        fileDirListResponse.setDir(directoryList);
        fileDirListResponse.setFile(fileList);

        return new DataResponse<>(fileDirListResponse);
    }




    /**
     * 根据新增的文件夹名称 检查是否与已有的文件夹冲突 有冲突则重命名
     *
     * @param parentId
     * @param currentUserId
     * @param directoryName
     * @return
     */
    private String getNotExistDirectoryName(Long parentId, String currentUserId, String directoryName) {
        ApiRequest apiRequest;
        while (true) {
            apiRequest = ApiRequest.newInstance();
            apiRequest.conditionEqual(QUserDirectory.directionName, directoryName);
            apiRequest.conditionEqual(QUserDirectory.userId, currentUserId);
            apiRequest.conditionEqual(QUserDirectory.parentId, parentId);
            List<UserDirectory> all = userDirectoryApiService.findAll(apiRequest);
            if (CollectionUtils.isEmpty(all)) {
                return directoryName;
                //TODO 需要解决命名同一文件夹内命名重复的问题
            }
        }
    }

    /**
     * 根据新增的文件名称 检查是否与已有的文件冲突 有冲突则重命名
     *
     * @param dirId
     * @param currentUserId
     * @param fileName
     * @return
     */
    private String getNotExistFileName(Long dirId, String currentUserId, String fileName) {
        ApiRequest apiRequest;
        while (true) {
            apiRequest = ApiRequest.newInstance();
            apiRequest.conditionEqual(QUserFile.fileName, fileName);
            apiRequest.conditionEqual(QUserFile.userId, currentUserId);
            apiRequest.conditionEqual(QUserFile.dirId, dirId);
            List<UserFile> all = userFileApiService.findAll(apiRequest);
            if (CollectionUtils.isEmpty(all)) {
                return fileName;
                //TODO 需要解决命名同一文件夹内命名重复的问题
            }
        }
    }

}
