package JX.Framework.Object;

import java.lang.*;
import java.util.*;
import JX.*;
import JX.Framework.*;


/**
 * 类工厂
 */
public class ClassFactory implements IFactory
{
    /**
     * 获取类厂实例
     * @return IFactory 类厂实例
     */
    public static IFactory getInstance()
    {
        if (instance == null)
        {
            instance = new ClassFactory();
        }

        return instance;
    }


    /// 添加一个类名
    @Override
    public void addClass(String objName, String className)
    {
        m_classes.put(objName, className);
    }


    /// 创建实例(对象)
    @Override
    public Object newInstance(String objName)
    {
        /// 利用反射机制创建对象
        Class objectClass;
        Object object;

        String className = m_classes.get(objName);
        if (className == null)
        {
            return null;
        }

        try
        {
            objectClass = Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            objectClass = null;
        }

        if (objectClass == null)
        {
            return null;
        }

        try
        {
            object = objectClass.newInstance();
        }
        catch (InstantiationException e)
        {
            object = null;
        }
        catch (IllegalAccessException e)
        {
            object = null;
        }

        return object;
    }


    /// Dump入口
    public String dump()
    {
        String out = "-----------------------------------------------\n";
        out += "Factory Dump: \n";
        Iterator iter = m_classes.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry)iter.next();
            String objName = (String)entry.getKey();
            String className = (String)entry.getValue();
            out += String.format("  objName:'%s', className:'%s' \n", 
                        objName, className);
        }

        out += "-----------------------------------------------\n";
        return out;
    }


    /// 类集合的MAP
    private HashMap<String, String> m_classes;


    /// 公共初始化块
    {
        m_classes = new HashMap<String, String>();
    }

    /// 类厂单实例
    private static IFactory instance = null;
}

