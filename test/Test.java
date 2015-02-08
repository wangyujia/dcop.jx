
import java.lang.*;
import java.util.*;
import JX.*;


/**
 * ��������
 */
public class Test
{
    public static void main(String args[])
    {
        /// ��ڼ���
        ModuleList.entry();
        if (ModuleList.manager == null)
        {
            return;
        }

        /// ��ʼ��
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

        /// ��ȡDump��Ϣ
        System.out.print(ModuleList.manager.dump());

        TestObject testObj3 = (TestObject)ModuleList.manager.getObject(ModuleList.test3);
        if (testObj3 == null)
        {
            System.out.println("TestObj3 Not Found!");
            return;
        }

        System.out.println("TestObj3 Found!");

        /// ����
        ModuleList.manager.proc(null);

        /// ����
        ModuleList.manager.fini();
    }

}


