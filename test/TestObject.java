
import JX.*;
import JX.Framework.*;


/**
 * ���������
 */
public class TestObject implements IObject
{
    private static final String m_Name = "Test";
    private IObject m_iParent = null;
    private IObject m_iRoot = null;
    private int m_ID = 0;

    public int testNo = 0;


    /// �������
    @Override
    public void construct(int id, IObject parent, String cfg)
    {
        m_ID = id;
        m_iParent = parent;

        System.out.println("'" + name() + "'(" + id() + ") Construct!");
        String[] ss = cfg.split(";");
        for (String s : ss)
        {
            String[] cfgItems = s.split("=");
            if (cfgItems.length >= 2)
            {
                if (cfgItems[0].equals("TestNo"))
                {
                    testNo = Integer.parseInt(cfgItems[1]);
                    System.out.println("  TestNo=" + testNo);
                }
            }
        }
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

}

