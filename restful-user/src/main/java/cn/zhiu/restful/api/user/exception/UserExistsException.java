package cn.zhiu.restful.api.user.exception;

import cn.zhiu.framework.base.api.core.annotation.exception.ExceptionCode;
import cn.zhiu.framework.base.api.core.exception.RestfulApiException;

@ExceptionCode(code = "CU0002", desc = "用户已存在")
public class UserExistsException extends RestfulApiException {
}
