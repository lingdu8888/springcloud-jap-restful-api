package cn.zhiu.restful.api.user.controller.user;


import cn.zhiu.base.api.user.bean.user.*;
import cn.zhiu.base.api.user.service.user.UserInfoApiService;
import cn.zhiu.framework.base.api.core.request.ApiRequest;
import cn.zhiu.framework.base.api.core.request.ApiRequestBody;
import cn.zhiu.framework.base.api.core.request.ApiRequestPage;
import cn.zhiu.framework.base.api.core.response.ApiResponse;
import cn.zhiu.framework.base.api.core.util.BeanMapping;
import cn.zhiu.framework.base.api.core.util.MD5Util;
import cn.zhiu.framework.restful.api.core.bean.response.CollectionResponse;
import cn.zhiu.framework.restful.api.core.bean.response.DataResponse;
import cn.zhiu.framework.restful.api.core.bean.response.PageResponse;
import cn.zhiu.framework.restful.api.core.controller.AbstractBaseController;
import cn.zhiu.framework.restful.api.core.exception.user.UserNotFoundException;
import cn.zhiu.restful.api.user.bean.ChangePwd;
import cn.zhiu.restful.api.user.exception.UserExistsException;
import cn.zhiu.restful.api.user.exception.UserPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The type User info controller.
 *
 * @author zhuzz
 * @time 2019 /04/12 00:02:10
 */
@RestController
@RequestMapping("/user")
public class UserInfoController extends AbstractBaseController {

    @Autowired
    private UserInfoApiService userInfoApiService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public DataResponse<UserRegisterResponse> register(@RequestBody UserRegisterRequest request) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.getAccount(), "账户名不能为空！");
        Objects.requireNonNull(request.getPassword(), "密码不能为空！");

        UserInfo one = userInfoApiService.findOne(ApiRequest.newInstance().filterEqual(QUserInfo.account, request.getAccount()));
        if (!Objects.isNull(one)) {
            throw new UserExistsException();
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(request.getPassword());
        userInfo.setAccount(request.getAccount());

        userInfo = userInfoApiService.save(userInfo);
        UserRegisterResponse response = BeanMapping.map(userInfo, UserRegisterResponse.class);

        return new DataResponse<>(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public DataResponse<Boolean> del(@PathVariable("id") String id) {
        userInfoApiService.del(id);
        return new DataResponse<>();
    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.PUT)
    public DataResponse<Boolean> updateUserInfo(@RequestBody UserInfoRequest userInfo) {

        userInfoApiService.save(BeanMapping.map(userInfo, UserInfo.class));
        return new DataResponse<>();
    }

    @RequestMapping(value = "updateUserStat", method = RequestMethod.PUT)
    public DataResponse<Boolean> updateUserStat(@RequestBody UserInfoRequest userInfo) {
        UserInfo info = new UserInfo();
        info.setUserId(userInfo.getUserId());
        info.setStatus(userInfo.getStatus());
        userInfoApiService.save(info);
        return new DataResponse<>();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DataResponse<UserInfoResponse> detail(@PathVariable String id) {
        UserInfoResponse userInfo = userInfoApiService.getUserInfo(id);
        return new DataResponse<>(userInfo);
    }


    @Deprecated
    @RequestMapping(value = "/userDetail", method = RequestMethod.GET)
    public CollectionResponse<UserInfoResponse> userDetail() {
        List<UserInfoResponse> all = userInfoApiService.findAll();
        return new CollectionResponse<>(all);
    }

    @RequestMapping(value = "/userDetailCondition", method = RequestMethod.GET)
    public CollectionResponse<UserInfoResponse> userDetailCondition(@RequestParam String fuzzy) {

        Objects.requireNonNull(fuzzy);

        ApiRequest apiRequest = ApiRequest.newInstance();
        List<String> fields = Arrays.asList(QUserInfo.account, QUserInfo.email, QUserInfo.nickName, QUserInfo.phone);
        apiRequest.filterMultiMatch(fields, fuzzy);

        List<UserInfoResponse> all = userInfoApiService.findAll(apiRequest);

        return new CollectionResponse<>(all);
    }


    @Deprecated
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResponse<UserInfoResponse> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {

        ApiRequest apiRequest = ApiRequest.newInstance();
        ApiRequestPage apiRequestPage = ApiRequestPage.newInstance();
        apiRequestPage.paging(page, size);

        ApiResponse<UserInfoResponse> result = userInfoApiService.findAll(ApiRequestBody.newInstance(apiRequest, apiRequestPage));

        return new PageResponse<>(result.getPage(), result.getPageSize(), result.getTotal(), result.getPagedData());
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public DataResponse<Boolean> resetPassword(@RequestBody UserInfo userInfo) {
        Objects.requireNonNull(userInfo);
        Objects.requireNonNull(userInfo.getUserId());
        Objects.requireNonNull(userInfo.getPassword());

        UserInfo info = userInfoApiService.get(userInfo.getUserId());

        Objects.requireNonNull(info);

        info.setPassword(MD5Util.encoderByMd5(userInfo.getPassword()));

        userInfoApiService.save(info);

        return new DataResponse<>(true);
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public DataResponse<UserInfoResponse> updatePassword(@RequestBody ChangePwd changePwd) {

        UserInfo info = userInfoApiService.get(changePwd.getUserId());
        if (Objects.isNull(info)) {
            throw new UserNotFoundException();
        }

        String passMd5 = MD5Util.encoderByMd5(changePwd.getPassword());

        if (!info.getPassword().equals(passMd5)) {
            throw new UserPasswordException();
        }
        info.setPassword(passMd5);
        info = userInfoApiService.save(info);
        return new DataResponse<>(BeanMapping.map(info, UserInfoResponse.class));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public DataResponse<UserInfoResponse> login(@RequestBody UserInfo userInfo) {

        UserInfo one = userInfoApiService.findOne(ApiRequest.newInstance().filterEqual(QUserInfo.account, userInfo.getAccount()));

        if (Objects.isNull(one)) {
            throw new UserNotFoundException();
        }

        String pass = MD5Util.encoderByMd5(userInfo.getPassword());
        if (!pass.equals(userInfo.getPassword())) {
            throw new UserPasswordException();
        }

        UserInfoResponse response = BeanMapping.map(one, UserInfoResponse.class);

        return new DataResponse<>(response);
    }

    @RequestMapping(value = "/queryUserInfoByUserIds", method = RequestMethod.POST)
    public CollectionResponse<UserInfoResponse> getUserInfoList(@RequestBody List<String> userIds) {

        ApiRequest apiRequest = ApiRequest.newInstance().filterIn(QUserInfo.userId, userIds);
        List<UserInfoResponse> userList = userInfoApiService.findAll(apiRequest);
        return new CollectionResponse<>(userList);
    }


}
