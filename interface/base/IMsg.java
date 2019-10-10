package com.dcop.jx.entry.base;

import java.nio.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;


/**
 * 消息包
 */
public interface IMsg {

    /**
     * 从缓冲区中解析一帧数据
     * (一帧数据构成一个消息包)
     * @param ByteBuffer buffer 缓冲区
     * @return IMsg 消息包
     */
    public IMsg parse(ByteBuffer buffer);


    /**
     * 把消息包转换为缓冲区
     * (一帧数据构成一个消息包)
     * @return ByteBuffer 缓冲区
     */
    public ByteBuffer pack();


    /**
     * 获取消息长度
     * @return int 长度
     */
    public int getLen();


    /**
     * 控制
     * (替换原有控制信息)
     * (不超过255-HeaderSize)
     * @param ByteBuffer buffer 缓冲区
     * @return IMsg 消息包
     */
    public IMsg setCtrl(ByteBuffer buffer);
    public ByteBuffer getCtrl();


    /**
     * 数据
     * (在原有数据后面继续添加)
     * @param ByteBuffer buffer 缓冲区
     * @return IMsg 消息包
     */
    public IMsg addData(ByteBuffer buffer);
    public ByteBuffer getData();

}

