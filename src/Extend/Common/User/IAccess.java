package JX.Extend.Common.User;

import JX.*;
import JX.Framework.Service.Msg.*;


/**
 * ����ӿ�
 */
public interface IAccess
{
    /**
     * ϵͳ��¼
     * @param String username �û���
     * @param String password ����
     */
    public void login(String username, String password);

    /**
     * �˳���¼
     */
    public void logout();
}

