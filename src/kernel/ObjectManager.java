package com.dcop.jx.core.kernel;

import java.lang.*;
import java.util.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;


/**
 * 对象管理器
 */
@ClassImport("manager")
public class ObjectManager implements IManager {

    /// 对象管理器的名字和ID
    private static final String m_name = "manager";

    /// 对象管理器的父对象(可无)
    private IObject m_parent = null;

    /// 对象管理器的ID(可默认为0)
    private static int m_id = 0;


    /**
     * 所有对象的根对象
     * 说明 : 1) 整个系统唯一的管理器入口
     *        2) 默认使用第一次初始化时的root
     *        3) 可主动进行设置
     * @return String 对象名
     */
    public static IManager root = null;


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
        return ObjectManager.root;
    }


    /// 初始化入口
    @Override
    public ErrCode init(IObject root, Object[] arg) {
        /// 默认第一个初始化的管理器为系统根对象
        if (ObjectManager.root == null) {
            ObjectManager.root = this;
        }

        /// 遍历所有对象，进行初始化
        ErrCode ret = ErrCode.SUCCESS;
        Iterator iter = m_objects.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            IObject obj = (IObject)entry.getValue();
            ErrCode retTmp =  obj.init(this, arg);
            if (retTmp != ErrCode.SUCCESS) {
                ret = retTmp;
            }
        }

        return ret;
    }


    /// 结束时入口
    @Override
    public void fini() {
        /// 遍历所有对象，进行结束
        Iterator iter = m_objects.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            IObject obj = (IObject)entry.getValue();
            obj.fini();
        }
    }


    /// 消息入口
    @Override
    public IMsg proc(IMsg msg) {
        /// 遍历所有对象，进行消息广播
        Iterator iter = m_objects.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            IObject obj = (IObject)entry.getValue();
            obj.proc(msg);
        }
        return null;
    }


    /// Dump入口
    @Override
    public String dump() {
        String out = String.format("Manager Dump: (size:%d)\n", m_objects.size());
        out += "-----------------------------------------------\n";
        Iterator iter = m_objects.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry)iter.next();
            Integer key = (Integer)entry.getKey();
            IObject obj = (IObject)entry.getValue();
            out += String.format("  objKey:%d, objID:%d, objName:'%s' \n", 
                        key.intValue(), obj.id(), obj.name());
        }

        out += "-----------------------------------------------\n";
        return out;
    }


    /// 添加对象
    @Override
    public void addObject(IObject obj) {
        m_objects.put(obj.id(), obj);
    }


    /// 删除对象
    @Override
    public void delObject(int objID) {
        m_objects.remove(objID);
    }


    /// 新建对象
    @Override
    public IObject newObject(int objID, String objName, String objCfg) {
        IObject obj = (IObject)ClassFactory.getInstance().newInstance(objName);
        if (obj == null) {
            return null;
        }

        obj.construct(objID, this, objCfg);
        addObject(obj);

        return obj;
    }


    /// 获取对象
    @Override
    public IObject getObject(int objID) {
        return m_objects.get(objID);
    }
    @Override
    public IObject getObject(String objName) {
        IObject object = null;
        for (Map.Entry<Integer, IObject> entry : m_objects.entrySet()) {
            object = entry.getValue();
            if (object == null) continue;
            if (object.name().equals(objName)) break;
        }
        return object;
    }


    /// 对象集合的MAP
    private Map<Integer, IObject> m_objects;


    /// 公共初始化块
    {
        m_objects = new TreeMap<Integer, IObject>();
    }
}
 
