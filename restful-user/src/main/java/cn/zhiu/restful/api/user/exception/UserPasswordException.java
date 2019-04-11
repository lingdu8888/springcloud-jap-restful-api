package cn.zhiu.restful.api.user.exception;

import cn.zhiu.framework.base.api.core.annotation.exception.ExceptionCode;
import cn.zhiu.framework.base.api.core.exception.RestfulApiException;

@ExceptionCode(code = "CU0003", desc = "用户密码不正确")
public class UserPasswordException extends RestfulApiException {
}
