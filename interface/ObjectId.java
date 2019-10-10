package com.dcop.jx.entry;


/**
 * 资源定义
 */
public final class ObjectId
{
    public static final class Object
    {
        public static final int Session = 20;                                           // 会话管理
        public static final int Access = 22;                                            // 分布式接入
    }

    public static final class Attribute
    {
        public static final int AccessLogin = ((Object.Access << (16)) | 1);            // 接入登陆
        public static final int AccessLogout = ((Object.Access << (16)) | 2);           // 退出登陆
    }

    public static final class TTY
    {
        public static int Access = 1;                                                   // ACCESS方式接入
    }

    
}

