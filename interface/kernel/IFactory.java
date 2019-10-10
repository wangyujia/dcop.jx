package com.dcop.jx.entry.kernel;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;


/**
 * 类工厂接口
 */
public interface IFactory {

    /**
     * 添加一个类名
     * @param key 关键名
     * @param name 类名
     */
    public void addClass(String key, String name);


    /**
     * 设置类的文件路径
     * @param name 类名
     * @param path 路径
     */
    public void setClassFile(String name, String path);
    public String getClassFile(String name);
    


    /**
     * 添加一个类名
     * @param String objName 对象名
     * @return Object 对象实例
     */
    public Object newInstance(String objName);


    /**
     * Dump入口
     * @return String Dump信息
     */
    public String dump();

    
    /**
     * 错误信息
     * @return String 错误信息
     */
    public String errInfo();
}

