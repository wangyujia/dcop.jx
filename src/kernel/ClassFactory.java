package com.dcop.jx.core.kernel;

import java.lang.*;
import java.util.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;


/**
 * 类工厂
 */
@ClassImport("factory")
public class ClassFactory implements IFactory {

    /// 类集合的MAP
    private Map<String, String> m_classes;

    /// 类的文件路径集合
    private Map<String, String> m_paths;

    /// 类厂单实例
    private static IFactory instance = null;

    /// 错误信息
    private String errString;


    /// 公共初始化块
    {
        m_classes = new TreeMap<String, String>();
        m_paths   = new TreeMap<String, String>();
    }


    /**
     * 获取类厂实例
     * @return IFactory 类厂实例
     */
    public static IFactory getInstance() {
        if (instance == null) {
            instance = new ClassFactory();
        }

        return instance;
    }


    /// 添加一个类名
    @Override
    public void addClass(String key, String name) {
        m_classes.put(key, name);
    }


    /// 设置类的文件路径
    @Override
    public void setClassFile(String name, String path) {
        m_paths.put(name, path);
    }

    @Override
    public String getClassFile(String name) {
        return m_paths.get(name);
    }


    /// 创建实例(对象)
    @Override
    public Object newInstance(String key) {

        Class  clazz;
        Object object;

        /// 获取全名
        String name = m_classes.get(key);
        if (name == null) {
            errString = String.format("Can't find class: %s", name);
            return null;
        }

        /// 获取类 (forName和ClassLoader)
        try {
            clazz = Class.forName(name);
        }
        catch (Exception e) {
            clazz = null;
        }
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            clazz = loader.loadClass(name);
        }
        catch (Exception e) {
            clazz = null;
        }

        if (clazz == null) {
            errString = String.format("Can't load class: %s", name);
            return null;
        }

        /// 创建对象
        try {
            object = clazz.newInstance();
        }
        catch (Exception e) {
            object = null;
        }

        if (object == null) {
            errString = String.format("Can't create object: %s", name);
            return null;
        }

        return object;
    }


    /// Dump入口
    @Override
    public String dump() {
        String out = String.format("Factory Dump: (size:%d)\n", m_classes.size());
        out += "-----------------------------------------------\n";
        Iterator iter = m_classes.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry)iter.next();
            String key = (String)entry.getKey();
            String name = (String)entry.getValue();
            out += String.format("  key:'%s', name:'%s', path:'%s' \n", 
                        key, name, m_paths.get(name));
        }

        out += "-----------------------------------------------\n";
        return out;
    }


    /// 错误信息
    @Override
    public String errInfo() {
        return errString;
    }

}

