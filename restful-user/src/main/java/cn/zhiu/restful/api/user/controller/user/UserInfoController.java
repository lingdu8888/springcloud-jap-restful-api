package cn.zhiu.restful.api.user.controller.user;


import cn.zhiu.base.api.user.bean.user.QUserInfo;
import cn.zhiu.base.api.user.bean.user.UserInfo;
import cn.zhiu.base.api.user.bean.user.UserRegisterRequest;
import cn.zhiu.base.api.user.service.user.UserInfoApiService;
import cn.zhiu.framework.base.api.core.request.ApiRequest;
import cn.zhiu.framework.base.api.core.request.ApiRequestBody;
import cn.zhiu.framework.base.api.core.request.ApiRequestPage;
import cn.zhiu.framework.base.api.core.response.ApiResponse;
import cn.zhiu.framework.base.api.core.util.MD5Util;
import cn.zhiu.framework.restful.api.core.bean.response.CollectionResponse;
import cn.zhiu.framework.restful.api.core.bean.response.DataResponse;
import cn.zhiu.framework.restful.api.core.bean.response.PageResponse;
import cn.zhiu.framework.restful.api.core.controller.AbstractBaseController;
import cn.zhiu.framework.restful.api.core.exception.user.UserNotFoundException;
import cn.zhiu.restful.api.user.bean.ChangePwd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserInfoController extends AbstractBaseController {

    @Autowired
    private UserInfoApiService userInfoApiService;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public DataResponse<UserInfo> register(@RequestBody UserRegisterRequest request) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.getAccount(), "账户名不能为空！");
        Objects.requireNonNull(request.getPassword(), "密码不能为空！");

        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(request.getPassword());
        userInfo.setAccount(request.getAccount());

        userInfo = userInfoApiService.save(userInfo);

        return new DataResponse<>(userInfo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public DataResponse<Boolean> del(@PathVariable("id") String id) {
        userInfoApiService.del(id);
        return new DataResponse<>();
    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.PUT)
    public DataResponse<Boolean> updateUserInfo(@RequestBody UserInfo userInfo) {

        userInfoApiService.save(userInfo);
        return new DataResponse<>();
    }

    @RequestMapping(value = "updateUserStat", method = RequestMethod.PUT)
    public DataResponse<Boolean> updateUserStat(@RequestBody UserInfo userInfo) {
        // TODO zhuzhzh  date  任务：？？？
        userInfoApiService.save(userInfo);
        return new DataResponse<>();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DataResponse<UserInfo> detail(@PathVariable String id) {
        UserInfo userInfo = userInfoApiService.get(id);
        return new DataResponse<>(userInfo);
    }


    @RequestMapping(value = "/userDetail", method = RequestMethod.GET)
    public DataResponse<List<UserInfo>> userDetail() {
        List<UserInfo> all = userInfoApiService.findAll();
        return new DataResponse<>(all);
    }

    @RequestMapping(value = "/userDetailCondition", method = RequestMethod.GET)
    public CollectionResponse<UserInfo> userDetailCondition(@RequestParam String fuzzy) {

        Objects.requireNonNull(fuzzy);

        ApiRequest apiRequest = ApiRequest.newInstance();
        List<String> fields = Arrays.asList(QUserInfo.account, QUserInfo.email, QUserInfo.nickName, QUserInfo.phone);
        apiRequest.filterMultiMatch(fields, fuzzy);

        List<UserInfo> all = userInfoApiService.findAll(apiRequest);

        return new CollectionResponse<>(all);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResponse<UserInfo> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {

        ApiRequest apiRequest = ApiRequest.newInstance();
        ApiRequestPage apiRequestPage = ApiRequestPage.newInstance();
        apiRequestPage.paging(page, size);

        ApiResponse<UserInfo> result = userInfoApiService.findAll(ApiRequestBody.newInstance(apiRequest, apiRequestPage));

        return new PageResponse<>(result.getPage(), result.getPageSize(), result.getTotal(), result.getPagedData());
    }

    @RequestMapping(value = "resetPassword", method = RequestMethod.PUT)
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

    @RequestMapping(value = "updatePassword", method = RequestMethod.PUT)
    public DataResponse<UserInfo> updatePassword(@RequestBody ChangePwd changePwd) {

        UserInfo info = userInfoApiService.get(changePwd.getUserId());
        if (Objects.isNull(info)) {
            throw new UserNotFoundException();
        }

        String passMd5 = MD5Util.encoderByMd5(changePwd.getPassword());

        if (!info.getPassword().equals(passMd5)) {
            // TODO zhuzhzh  date  任务：验证密码
        }
        info.setPassword(passMd5);
        info = userInfoApiService.save(info);
        info.setPassword("");
        return new DataResponse<>(info);
    }

}
