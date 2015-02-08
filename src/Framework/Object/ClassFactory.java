package JX.Framework.Object;

import java.lang.*;
import java.util.*;
import JX.*;
import JX.Framework.*;


/**
 * �๤��
 */
public class ClassFactory implements IFactory
{
    /**
     * ��ȡ�೧ʵ��
     * @return IFactory �೧ʵ��
     */
    public static IFactory getInstance()
    {
        if (instance == null)
        {
            instance = new ClassFactory();
        }

        return instance;
    }


    /// ���һ������
    @Override
    public void addClass(String objName, String className)
    {
        m_classes.put(objName, className);
    }


    /// ����ʵ��(����)
    @Override
    public Object newInstance(String objName)
    {
        /// ���÷�����ƴ�������
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


    /// Dump���
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


    /// �༯�ϵ�MAP
    private HashMap<String, String> m_classes;


    /// ������ʼ����
    {
        m_classes = new HashMap<String, String>();
    }

    /// �೧��ʵ��
    private static IFactory instance = null;
}

