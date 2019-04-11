package cn.zhiu.restful.api.user;

import cn.zhiu.base.api.user.bean.user.UserInfo;
import cn.zhiu.framework.base.api.core.request.ApiRequest;
import cn.zhiu.framework.base.api.core.request.ApiRequestBody;
import cn.zhiu.framework.base.api.core.response.ApiResponse;
import cn.zhiu.framework.base.api.core.service.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "service-user")
public interface UserInfoApiService extends BaseApiService {

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    UserInfo get(@PathVariable("userId") String userId);

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    UserInfo save(@RequestBody UserInfo model);

    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    boolean del(@RequestParam("userId") String userId);

    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    List<UserInfo> findAll(@RequestBody ApiRequest apiRequest);

    @RequestMapping(value = "/user/list/page", method = RequestMethod.POST)
    ApiResponse<UserInfo> findAll(@RequestBody ApiRequestBody requestBody);

    @RequestMapping(value = "/user/all", method = RequestMethod.GET)
    List<UserInfo> findAll();


}