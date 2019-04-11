package cn.zhiu.restful.api.user.bean;/**
 * @Description:
 * @Auther: mk
 * @Date: 2019/3/20 11:23
 */

import java.io.Serializable;

/**
 * @author mengkai
 * @description
 * @date 2019/3/20
 */
public class ChangePwd implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 新密码
     */
    private String newPwd;

    /**
     * 密码
     */
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
