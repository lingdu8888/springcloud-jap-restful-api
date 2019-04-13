package cn.zhiu.restful.api.cloud.disk.exception;

import cn.zhiu.framework.base.api.core.annotation.exception.ExceptionCode;
import cn.zhiu.framework.base.api.core.exception.RestfulApiException;

/**
 * @Auther: yujuan
 * @Date: 19-4-13 08:58
 * @Description:
 */
@ExceptionCode(code = "1201", desc = "当前目录不存在")
public class DirectoryNotFoundException extends RestfulApiException {


}
