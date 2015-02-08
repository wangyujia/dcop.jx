
import JX.*;
import JX.Framework.*;
import JX.Framework.Object.*;


/**
 * 模块列表类
 * 说明: 该文件为模板，请拷贝到各个工程源文件的根目录(不要放到包里面方便引用)
 */
public class ModuleList
{
    /////////////////////////////////////////////////////
    /// 静态模块 : 静态注册实现类名(在Entry中进行加载)
    /// 动态模块 : 运行时调用addObject接口添加模块实例
    /////////////////////////////////////////////////////


    /// 静态模块ID
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


    /// 实现类注册
    public static final String[][] implementClassList = 
    {
        {"Manager", "JX.Framework.Object.ObjectManager"},
        {"LanApp",  "JX.Framework.Service.Sock.LanAppService"},
        {"Server",  "TestServer"},
        {"Client",  "TestClient"},
        {"UDP",     "TestUDP"},
        {"Test",    "TestObject"},
    };


    /// 静态模块注册
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


    /// 动态模块ID
    public static final int test6 = staticModuleList.length + 1;


    /// 加载模块入口
    public static void entry()
    {
        System.out.println("================= ModuleList Entry! =================");

        if (manager != null)
        {
            System.out.println("ModuleList aready Loaded!");
        }

        /// 获取类厂实例
        factory = ClassFactory.getInstance();
        if (factory == null)
        {
            System.out.println("Factory Null!");
            return;
        }

        /// 把注册类加入到类厂中
        for (String[] ss : implementClassList)
        {
            System.out.println("Add '" + ss[0] + "'('" + ss[1] + "') to ClassFactory!");
            factory.addClass(ss[0], ss[1]);
        }

        /// Dump所有的类
        System.out.print(factory.dump());

        /// 创建管理器
        manager = (IManager)factory.newInstance("Manager");
        if (manager == null)
        {
            System.out.println("Manager Null!");
            return;
        }

        /// 创建模块实例
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

