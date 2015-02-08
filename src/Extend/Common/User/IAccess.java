package JX.Extend.Common.User;

import JX.*;
import JX.Framework.Service.Msg.*;


/**
 * 接入接口
 */
public interface IAccess
{
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

