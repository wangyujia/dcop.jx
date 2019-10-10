package com.dcop.jx.entry.extend.access;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;


/**
 * 接入接口
 */
public interface IAccess {

    /**
     * 系统登录
     * @param String username 用户名
     * @param String password 密码
     */
    public void login(String username, String password);

    /**
     * 退出登录
     */
    public void logout();
}

