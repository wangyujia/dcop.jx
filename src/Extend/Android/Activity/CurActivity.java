package JX.Extend.Android.Activity;

import android.app.Activity;
import JX.*;
import JX.Framework.Object.*;


/**
 * ȷ�϶Ի���
 */
public class CurActivity implements IObject
{
    private static final String m_Name = "CurActivity";
    private IObject m_iParent = null;
    private IObject m_iRoot = null;
    private int m_ID = 0;

    /// �������
    @Override
    public void construct(int id, IObject parent, String cfg)
    {
        m_ID = id;
        m_iParent = parent;
    }

    /// ������
    @Override
    public String name()
    {
        return m_Name;
    }

    /// ����ID
    @Override
    public int id()
    {
        return m_ID;
    }

    /// ������
    @Override
    public IObject parent()
    {
        return m_iParent;
    }

    /// ������
    public IObject root()
    {
        return m_iRoot;
    }

    /// ��ʼ�����
    @Override
    public Errno init(IObject root, Object[] arg)
    {
        m_iRoot = root;
        System.out.println("'" + name() + "'(" + id() + ") Init!");

        return Errno.SUCCESS;
    }

    /// ����ʱ���
    @Override
    public void fini()
    {
        System.out.println("'" + name() + "'(" + id() + ") Fini!");
    }

    /// ��Ϣ���
    @Override
    public void proc(Object msg)
    {
        System.out.println("'" + name() + "'(" + id() + ") Proc Msg!");
    }

    /// Dump���
    @Override
    public String dump()
    {
        String out = name() + " Dump: \n";
        out += String.format("  My ID is:%d \n", id());

        return out;
    }

    /**
     * ���õ�ǰ����
     * @param int objIDAct Activity����
     * @param String strTitle ����
     * @param String strYes Yes��ť��
     * @param String strNo No��ť��
     */
    public void setCur()
    {
    }
}

