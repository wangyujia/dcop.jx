
import JX.*;
import JX.Framework.*;
import JX.Framework.Object.*;


/**
 * ģ���б���
 * ˵��: ���ļ�Ϊģ�壬�뿽������������Դ�ļ��ĸ�Ŀ¼(��Ҫ�ŵ������淽������)
 */
public class ModuleList
{
    /////////////////////////////////////////////////////
    /// ��̬ģ�� : ��̬ע��ʵ������(��Entry�н��м���)
    /// ��̬ģ�� : ����ʱ����addObject�ӿ����ģ��ʵ��
    /////////////////////////////////////////////////////


    /// ��̬ģ��ID
    public static IFactory factory = null;
    public static IManager manager = null;
    public static final int server = 1;
    public static final int client = 2;
    public static final int udp = 3;
    public static final int test1 = 4;
    public static final int test2 = 5;
    public static final int test3 = 6;
    public static final int test4 = 7;
    public static final int test5 = 8;


    /// ʵ����ע��
    public static final String[][] implementClassList = 
    {
        {"Manager", "JX.Framework.Object.ObjectManager"},
        {"LanApp",  "JX.Framework.Service.Sock.LanAppService"},
        {"Server",  "TestServer"},
        {"Client",  "TestClient"},
        {"UDP",     "TestUDP"},
        {"Test",    "TestObject"},
    };


    /// ��̬ģ��ע��
    public static final String[][] staticModuleList = 
    {
        /// {"Server",  "LocalPort=12300"},
        {"Client",  "RemoteCount=2;RemoteIP1=127.0.0.1;RemotePort1=34418;RemoteIP2=192.168.1.100;RemotePort2=34418"},
        /// {"UDP",     "LocalPort=12300;RemoteIP=127.255.255.255;RemotePort=12300"},
        {"Test",    "TestNo=1"},
        {"Test",    "TestNo=2"},
        {"Test",    "TestNo=3"},
        {"Test",    "TestNo=4"},
        {"Test",    "TestNo=5"},
    };


    /// ��̬ģ��ID
    public static final int test6 = staticModuleList.length + 1;


    /// ����ģ�����
    public static void entry()
    {
        System.out.println("================= ModuleList Entry! =================");

        if (manager != null)
        {
            System.out.println("ModuleList aready Loaded!");
        }

        /// ��ȡ�೧ʵ��
        factory = ClassFactory.getInstance();
        if (factory == null)
        {
            System.out.println("Factory Null!");
            return;
        }

        /// ��ע������뵽�೧��
        for (String[] ss : implementClassList)
        {
            System.out.println("Add '" + ss[0] + "'('" + ss[1] + "') to ClassFactory!");
            factory.addClass(ss[0], ss[1]);
        }

        /// Dump���е���
        System.out.print(factory.dump());

        /// ����������
        manager = (IManager)factory.newInstance("Manager");
        if (manager == null)
        {
            System.out.println("Manager Null!");
            return;
        }

        /// ����ģ��ʵ��
        for (int i = 0; i < staticModuleList.length; ++i)
        {
            String objName = staticModuleList[i][0];
            String objCfg = staticModuleList[i][1];
            IObject obj = (IObject)manager.newObject(i + 1, objName, objCfg);
            if (obj != null)
            {
                System.out.println("Create '" + objName + "'(" + obj.id() + ") OK!");
            }
            else
            {
                System.out.println("Create '" + objName + "'(" + (i + 1) + ") Fail!");
            }
        }

    }

}

