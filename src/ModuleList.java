package com.dcop.jx.core;

import java.lang.*;
import java.util.*;
import java.text.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.kernel.*;
import com.dcop.jx.entry.extend.jjs.*;
import com.dcop.jx.core.base.pkg.*;
import com.dcop.jx.core.base.log.*;
import com.dcop.jx.core.kernel.*;


/**
 * 模块列表类
 */
public class ModuleList {

    /////////////////////////////////////////////////////
    /// 静态模块 : 静态注册实现类名(在Entry中进行加载)
    /// 动态模块 : 运行时调用addObject接口添加模块实例
    /////////////////////////////////////////////////////

    /// 静态模块ID
    public static IFactory factory = null;
    public static IManager manager = null;


    /// 扫描
    public static void scan(String pkgname) {
        /// 获取类厂实例
        factory = ClassFactory.getInstance();
        if (factory == null) {
            Logs.record("trace", "Factory Is Null!");
            return;
        }

        /// 把注册类加入到类厂中
        PkgScanner scanner = new PkgScanner(pkgname);
        try {
            Map<String,String> map = scanner.scan();
            map.forEach( (key, value) -> {
                try {
                    Class clazz = Class.forName(key, false, Thread.currentThread().getContextClassLoader());
                    if (clazz != null) {
                        @SuppressWarnings("unchecked")
                        ClassImport annotation = (ClassImport)clazz.getAnnotation(ClassImport.class);
                        if (annotation != null) {
                            String name = annotation.value();
                            Logs.record("trace", String.format("'%s'('%s'|'%s') Add To ClassFactory!", name, key, value));
                            factory.addClass(name, key);
                            factory.setClassFile(key, value);
                        }
                    }
                } catch (Exception e) {

                }
            });
        } catch (Exception e) {

        }
    }
    public static void scan(Class clazz) {
        String pkgname = clazz.getPackage().getName();
        scan(pkgname);
    }

    /// 创建
    public static IManager create() {
        Logs.record("trace", "================= System Start ... =================");
        if (manager != null) {
            Logs.record("trace", "ModuleList aready Loaded!");
            return null;
        }

        /// Dump所有的类
        Logs.record("trace", factory.dump());

        /// 创建管理器
        manager = (IManager)factory.newInstance("manager");
        if (manager == null) {
            Logs.record("trace", "Manager Is Null!");
            return null;
        }

        return manager;
    }

    /// 加载
    public static void load(String[][] list) {
        if (manager == null) return;

        /// 创建模块实例
        for (int i = 0; i < list.length; ++i) {
            String name = list[i][0];
            String cfg = list[i][1];
            IObject obj = (IObject)manager.newObject(i+1, name, cfg);
            if (obj != null) {
                Logs.record("trace", String.format("'%s'(%d) Create OK! (%s)",
                    name, obj.id(), cfg));
            }
            else {
                Logs.record("trace", String.format("'%s'(%d) Create Fail! (%s)", 
                    name, i+1, factory.errInfo()));
            }
        }
    }
    public static void load(String file) {
        IJJs jjs = (IJJs)factory.newInstance("jjs");
        if (jjs != null) {
            IObject obj = (IObject)jjs;
            if (obj != null) obj.init(null, null);
            Logs.record("trace", " --- Create JJs OK!");
            String s = (String)jjs.json("var __cfg_json_obj__ = ", file, ";__cfg_json_obj__.sysinfo;");
            Logs.record("trace", String.format("load '%s' return: %s", file, s));
            if (obj != null) obj.fini();
        } else {
            Logs.record("trace", " --- Create JJs Fail!");
        }
    }

    /// 初始化
    public static ErrCode init(IObject root, Object[] args) {
        ErrCode ret = manager.init(null, null);
        if (ret != ErrCode.SUCCESS) {
            Logs.record("trace", "Manager Init Fail!");
            return ErrCode.FAILURE;
        }

        Logs.record("trace", "Manager Init OK!");
        Logs.record("trace", "================= System Loaded ! ==================");
        Logs.record("trace", manager.dump());
        return ErrCode.SUCCESS;
    }

    /// 系统入口
    public static IManager entry(String[][] list) {
        if (create() == null) return null;
        load(list);

        load("dcop.json");

        if (init(null, null) != ErrCode.SUCCESS) return null;
        return manager;
    }

    /// 系统退出
    public static void exit() {
        if (manager != null) {
            manager.fini();
        }
    }
}

