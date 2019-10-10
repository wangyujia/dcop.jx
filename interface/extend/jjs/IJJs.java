package com.dcop.jx.entry.extend.jjs;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;


/**
 * JJS接口
 */
public interface IJJs {

    /**
     * 加载js文件
     * @param file 文件路径或者脚本内容
     * @return 执行结果
     */
    public Object load(String file);

    /**
     * 加载json文件
     * @param prefix 前缀
     * @param file 文件路径或者脚本内容
     * @param suffix 后缀
     * @return 执行结果
     */
    public Object json(String prefix, String file, String suffix);


    /**
     * 执行js函数
     * @param entry
     * @param args
     * @return 执行结果
     */
    public Object exec(String entry, Object...args);
}

