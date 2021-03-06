package com.dcop.jx.test;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;
import com.dcop.jx.core.base.log.*;
import com.dcop.jx.core.*;


/**
 * 对象测试类
 */
@ClassImport("test_object")
public class TestObject implements IObject {

    private static final String m_name = "TestObject";
    private IObject m_parent = null;
    private IObject m_root = null;
    private int m_id = 0;

    public int testNo = 0;


    /// 构建入口
    @Override
    public void construct(int id, IObject parent, String cfg) {
        m_id = id;
        m_parent = parent;

        Logs.record("trace", "'" + name() + "'(" + id() + ") Construct!");
        String[] ss = cfg.split(";");
        for (String s : ss) {
            String[] cfgItems = s.split("=");
            if (cfgItems.length >= 2) {
                if (cfgItems[0].equals("TestNo")) {
                    testNo = Integer.parseInt(cfgItems[1]);
                    Logs.record("trace", "  TestNo=" + testNo);
                }
            }
        }
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
        String out = name() + " Dump: \n";
        out += String.format("  My ID is:%d \n", id());
        return out;
    }

}

