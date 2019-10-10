package com.dcop.jx.components.android.activity;

import android.app.Activity;

import com.dcop.jx.*;
import com.dcop.jx.frameworks.object.*;


/**
 * 确认对话框
 */
public class CurActivity implements IObject
{
    private static final String m_name = "CurActivity";
    private IObject m_parent = null;
    private IObject m_root = null;
    private int m_id = 0;

    /// 构建入口
    @Override
    public void construct(int id, IObject parent, String cfg) {
        m_id = id;
        m_parent = parent;
    }

    /// 对象名
    @Override
    public String name() {
        return m_name;
    }

    /// 对象ID
    @Override
    public int id() {
        return m_id;
    }

    /// 父对象
    @Override
    public IObject parent() {
        return m_parent;
    }

    /// 根对象
    public IObject root() {
        return m_root;
    }

    /// 初始化入口
    @Override
    public ErrCode init(IObject root, Object[] arg) {
        m_root = root;
        System.out.println("'" + name() + "'(" + id() + ") Init!");

        return ErrCode.SUCCESS;
    }

    /// 结束时入口
    @Override
    public void fini() {
        System.out.println("'" + name() + "'(" + id() + ") Fini!");
    }

    /// 消息入口
    @Override
    public IMsg proc(IMsg msg) {
        System.out.println("'" + name() + "'(" + id() + ") Proc Msg!");
        return null;
    }

    /// Dump入口
    @Override
    public String dump() {
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
    public void setCur() {}
}

