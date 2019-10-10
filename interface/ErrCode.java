package com.dcop.jx.entry;


/**
 * 错误码定义 error code definitions
 */
public enum ErrCode
{
    SUCCESS(0),

    

    FAILURE(-1);


    /// 定义私有变量
    private int nCode;


    /// 构造函数，枚举类型只能为私有
    private ErrCode(int nCode)
    {
        this.nCode = nCode;
    }


    /// 重载字符串转换
    @Override
    public String toString()
    {
        return String.valueOf(this.nCode);
    }

}

