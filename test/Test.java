
import java.lang.*;
import java.util.*;
import JX.*;


/**
 * 测试主类
 */
public class Test
{
    public static void main(String args[])
    {
        /// 入口加载
        ModuleList.entry();
        if (ModuleList.manager == null)
        {
            return;
        }

        /// 初始化
        Errno ret = ModuleList.manager.init(null, null);
        if (ret != Errno.SUCCESS)
        {
            System.out.println("Manager Init Fail!");
            return;
        }

        System.out.println("Manager Init OK!");

        TestObject testObj6 = new TestObject();
        testObj6.construct(ModuleList.test6, ModuleList.manager, "TestNo=6");
        ModuleList.manager.addObject(testObj6);

        /// 获取Dump信息
        System.out.print(ModuleList.manager.dump());

        TestObject testObj3 = (TestObject)ModuleList.manager.getObject(ModuleList.test3);
        if (testObj3 == null)
        {
            System.out.println("TestObj3 Not Found!");
            return;
        }

        System.out.println("TestObj3 Found!");

        /// 处理
        ModuleList.manager.proc(null);

        /// 结束
        ModuleList.manager.fini();
    }

}


