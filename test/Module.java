package com.dcop.jx.test;

import java.lang.*;
import java.util.*;
import java.text.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.kernel.*;
import com.dcop.jx.core.base.pkg.*;
import com.dcop.jx.core.kernel.*;
import com.dcop.jx.core.*;


/**
 * 模块列表类
 * 说明: 该文件为模板，请拷贝到各个工程源文件的根目录(不要放到包里面方便引用)
 */
public class Module {

    /// 静态模块ID
    public static final int server = 1;
    public static final int client = 2;
    public static final int udp = 3;
    public static final int test1 = 4;
    public static final int test2 = 5;
    public static final int test3 = 6;
    public static final int test4 = 7;
    public static final int test5 = 8;
    /// 静态模块注册
    public static final String[][] staticModuleList = {
        {"test_server", "LocalPort=12300"},
        {"test_client", "RemoteCount=2;RemoteIP1=127.0.0.1;RemotePort1=12300;RemoteIP2=127.0.0.1;RemotePort2=50000"},
        /// {"UDP",         "LocalPort=12300;RemoteIP=127.255.255.255;RemotePort=12300"},
        {"test_object", "TestNo=1"},
        {"test_object", "TestNo=2"},
        {"test_object", "TestNo=3"},
        {"test_object", "TestNo=4"},
        {"test_object", "TestNo=5"},
        {"jjs", ""},
        {"test_jjs", ""}
    };


    /// 动态模块ID
    public static final int test6 = staticModuleList.length + 1;


    /// 系统入口
    public static IManager entry() {
        ModuleList.scan("com.dcop.jx");
        ModuleList.entry(staticModuleList);
        return ModuleList.manager;
    }

    /// 系统退出
    public static void exit() {
        ModuleList.exit();
    }
}

