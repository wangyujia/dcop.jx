package com.dcop.jx.test;

import java.lang.*;
import java.util.*;
import javax.script.*;
import java.io.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;
import com.dcop.jx.entry.extend.jjs.*;
import com.dcop.jx.core.base.log.*;
import com.dcop.jx.core.*;


@ClassImport("test_jjs")
public class TestJJs implements IObject {

    private static final String m_name = "TestJJs";
    private IObject m_parent = null;
    private IObject m_root = null;
    private int m_id = 0;

    IJJs m_jjs = null;


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

        IManager manager = (IManager)root;
        if (manager == null) return ErrCode.FAILURE;

        m_jjs = (IJJs)manager.getObject("JJs");
        Logs.record("trace", String.format("get jjs %s", (m_jjs != null)? "success" : "failure"));

        IObject jjs = (IObject)m_jjs;
        Logs.record("trace", jjs.dump());
        
        Logs.record("trace", "'" + name() + "'(" + id() + ") Init!");
        return ErrCode.SUCCESS;
    }


    /// 结束时入口
    @Override
    public void fini() {
        Logs.record("trace", "'" + name() + "'(" + id() + ") Fini!");
    }


    /// 消息入口
    @Override
    public IMsg proc(IMsg msg) {
        Logs.record("trace", "'" + name() + "'(" + id() + ") Proc Msg!");
        return null;
    }


    /// Dump入口
    @Override
    public String dump() {
        return "";
    }
}
