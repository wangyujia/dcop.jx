package JX.Extend.Android.Activity;

import android.app.Activity;
import JX.*;
import JX.Framework.Object.*;


/**
 * 确认对话框
 */
public class CurActivity implements IObject
{
    private static final String m_Name = "CurActivity";
    private IObject m_iParent = null;
    private IObject m_iRoot = null;
    private int m_ID = 0;

    /// 构建入口
    @Override
    public void construct(int id, IObject parent, String cfg)
    {
        m_ID = id;
        m_iParent = parent;
    }

    /// 对象名
    @Override
    public String name()
    {
        return m_Name;
    }

    /// 对象ID
    @Override
    public int id()
    {
        return m_ID;
    }

    /// 父对象
    @Override
    public IObject parent()
    {
        return m_iParent;
    }

    /// 根对象
    public IObject root()
    {
        return m_iRoot;
    }

    /// 初始化入口
    @Override
    public Errno init(IObject root, Object[] arg)
    {
        m_iRoot = root;
        System.out.println("'" + name() + "'(" + id() + ") Init!");

        return Errno.SUCCESS;
    }

    /// 结束时入口
    @Override
    public void fini()
    {
        System.out.println("'" + name() + "'(" + id() + ") Fini!");
    }

    /// 消息入口
    @Override
    public void proc(Object msg)
    {
        System.out.println("'" + name() + "'(" + id() + ") Proc Msg!");
    }

    /// Dump入口
    @Override
    public String dump()
    {
        String out = name() + " Dump: \n";
        out += String.format("  My ID is:%d \n", id());

        return out;
    }

    /**
     * 设置当前窗口
     * @param int objIDAct Activity对象
     * @param String strTitle 标题
     * @param String strYes Yes按钮名
     * @param String strNo No按钮名
     */
    public void setCur()
    {
    }
}

