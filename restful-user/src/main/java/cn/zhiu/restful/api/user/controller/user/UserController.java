package cn.zhiu.restful.api.user.controller.user;

import cn.zhiu.base.api.user.service.user.UserApiService;
import cn.zhiu.framework.restful.api.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractBaseController {

    @Autowired
    private UserApiService userApiService;

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String sayHi(@RequestParam String name) {
//        return "ddddd";
        String s = userApiService.sayHiFromClientOne(name);
        return s;
    }



}
