package com.dcop.jx.test;

import java.lang.*;
import java.util.*;
import java.util.regex.Pattern;
import java.text.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.kernel.*;
import com.dcop.jx.core.base.pkg.*;
import com.dcop.jx.core.base.log.*;
import com.dcop.jx.core.kernel.*;
import com.dcop.jx.core.*;
import com.dcop.jx.test.*;


/**
 * 测试主类
 */
public class Test {

    public static void main(String args[]) {
        System.out.println("package: " + Test.class.getPackage().getName());
        Package[] pack = Package.getPackages();
        for (int i = 0; i < pack.length; i++) {
            String name = pack[i].getName();
            if (name == null) continue;
            // name = name.split(Pattern.quote("."))[0];
            // if (name == null) continue;
            // if (name.equals("java") || name.equals("sun")) continue;
            System.out.println("    " + name);
        }
        PkgScanner scanner = new PkgScanner("");
        try {
            Map<String,String> map = scanner.scan();
            map.forEach( (key, value) -> System.out.println(key + " : " + value));
        } catch (Exception e) {

        }

        /// 入口加载
        IManager manager = Module.entry();
        if (manager == null) {
            return;
        }

        TestObject testObj6 = new TestObject();
        testObj6.construct(Module.test6, manager, "TestNo=6");
        manager.addObject(testObj6);

        TestObject testObj3 = (TestObject)manager.getObject(Module.test3);
        if (testObj3 == null) {
            Logs.record("trace", "TestObj3 Not Found!");
            return;
        }

        Logs.record("trace", "TestObj3 Found!");

        /// 处理
        manager.proc(null);

        System.out.println("system start! input commands or 'exit<return>' to exit. ");

        /// 输入
        while (true) {
            Scanner sc = new Scanner(System.in);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String date = df.format(new Date());
            System.out.printf("[%s] ", date);
            String str = sc.nextLine();
            if (str.equals("exit")) {
                System.out.printf("Are you sure to exit? <Y/N> ");
                str = sc.nextLine();
                if (str.equals("Y")) {
                    System.out.println("now exit system ... ");
                    break;
                }
            }
        }

        /// 结束
        Module.exit();

    }

}


