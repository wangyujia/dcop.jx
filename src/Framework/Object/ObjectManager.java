package JX.Framework.Object;

import java.lang.*;
import java.util.*;
import JX.*;
import JX.Framework.*;


/**
 * ���������
 */
public class ObjectManager implements IManager
{
    /// ��������������ֺ�ID
    private static final String m_Name = "Manager";

    /// ����������ĸ�����(����)
    private IObject m_iParent = null;

    /// �����������ID(��Ĭ��Ϊ0)
    private static int m_ID = 0;


    /**
     * ���ж���ĸ�����
     * ˵�� : 1) ����ϵͳΨһ�Ĺ��������
     *        2) Ĭ��ʹ�õ�һ�γ�ʼ��ʱ��root
     *        3) ��������������
     * @return String ������
     */
    public static IManager root = null;


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
        return ObjectManager.root;
    }


    /// ��ʼ�����
    @Override
    public Errno init(IObject root, Object[] arg)
    {
        /// Ĭ�ϵ�һ����ʼ���Ĺ�����Ϊϵͳ������
        if (ObjectManager.root == null)
        {
            ObjectManager.root = this;
        }

        /// �������ж��󣬽��г�ʼ��
        Errno ret = Errno.SUCCESS;
        Iterator iter = m_objects.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry)iter.next();
            IObject obj = (IObject)entry.getValue();
            Errno retTmp =  obj.init(this, arg);
            if (retTmp != Errno.SUCCESS)
            {
                ret = retTmp;
            }
        }

        return ret;
    }


    /// ����ʱ���
    @Override
    public void fini()
    {
        /// �������ж��󣬽��н���
        Iterator iter = m_objects.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry)iter.next();
            IObject obj = (IObject)entry.getValue();
            obj.fini();
        }
    }


    /// ��Ϣ���
    @Override
    public void proc(Object msg)
    {
        /// �������ж��󣬽�����Ϣ�㲥
        Iterator iter = m_objects.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry)iter.next();
            IObject obj = (IObject)entry.getValue();
            obj.proc(msg);
        }
    }


    /// Dump���
    @Override
    public String dump()
    {
        String out = "-----------------------------------------------\n";
        out += "Manager Dump: \n";
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


    /// ��Ӷ���
    @Override
    public void addObject(IObject obj)
    {
        m_objects.put(obj.id(), obj);
    }


    /// ɾ������
    @Override
    public void delObject(int objID)
    {
        m_objects.remove(objID);
    }


    /// �½�����
    @Override
    public IObject newObject(int objID, String objName, String objCfg)
    {
        IObject obj = (IObject)ClassFactory.getInstance().newInstance(objName);
        if (obj == null)
        {
            return null;
        }

        obj.construct(objID, this, objCfg);
        addObject(obj);

        return obj;
    }


    /// ��ȡ����
    @Override
    public IObject getObject(int objID)
    {
        return m_objects.get(objID);
    }


    /// ���󼯺ϵ�MAP
    private HashMap<Integer, IObject> m_objects;


    /// ������ʼ����
    {
        m_objects = new HashMap<Integer, IObject>();
    }
}
 
