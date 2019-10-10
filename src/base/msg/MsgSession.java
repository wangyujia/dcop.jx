package com.dcop.jx.core.base.msg;

import java.nio.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.core.base.*;


/**
 * 会话消息头
 */
public class MsgSession {

    public static byte HeaderType = 0;
    public static byte HeaderSize = 24;

    public static byte CtrlDataCreate = 0;
    public static byte CtrlDataDestroy = 1;
    public static byte CtrlDataAddRecord = 2;
    public static byte CtrlDataDelRecord = 3;
    public static byte CtrlDataEditRecord = 4;
    public static byte CtrlDataQueryRecord = 5;
    public static byte CtrlDataCountRecord = 6;
    public static byte CtrlMethod = 7;
    public static byte CtrlEvent = 8;

    public static byte TypeRequest = 0;
    public static byte TypeResponse = 1;

    public byte        headType;                // 头部类型(见'DCOP_MSG_HEAD_TYPE')
    public byte        headSize;                // 头部大小(见'DCOP_MSG_HEAD_SIZE')
    public short       valueLen;                // 头部后面的值的长度
    public int         session;                 // 会话
    public int         user;                    // 用户
    public int         tty;                     // 终端
    public int         attribute;               // 属性
    public short       index;                   // 索引
    public byte        ctrl;                    // 控制(见'DCOP_CTRL_TYPE')
    public byte        ack;                     // 是否应答消息('DCOP_NO/DCOP_YES')

    public MsgPacket   msg;                     // 指向的消息包
    public int         pos;                     // 会话的值位于消息包数据区的位置


    public MsgSession() {
        headType = HeaderType;
        headSize = HeaderSize;
        valueLen = 0;
        session = 0;
        user = 0;
        tty = 0;
        attribute = 0;
        index = 0;
        ctrl = CtrlMethod;
        ack = TypeRequest;
        msg = null;
        pos = 0;
    }


    /**
     * 从消息包中解析会话消息头
     * @param MsgPacket msg 消息包
     * @return MsgSession 会话消息头
     */
    public MsgSession parse(MsgPacket msg) {
        if (msg == null) {
            return this;
        }

        ByteBuffer buffer = msg.getData();
        if (buffer == null) {
            return this;
        }

        /// 缓冲区的可读长度必须大于基本帧长
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < HeaderSize) {
            return this;
        }

        /// 头部校验必须正确
        byte headType  = buffer.get();
        byte headSize  = buffer.get();
        if ((headType != HeaderType) ||
            (headSize != HeaderSize)) {
            buffer.position(position);
            return this;
        }

        /// 获取帧头
        this.headType = headType;
        this.headSize = headSize;
        this.valueLen = buffer.getShort();
        this.session = buffer.getInt();
        this.user = buffer.getInt();
        this.tty = buffer.getInt();
        this.attribute = buffer.getInt();
        this.index = buffer.getShort();
        this.ctrl = buffer.get();
        this.ack = buffer.get();
        this.msg = msg;
        this.pos = buffer.position();

        /// 偏移过处理后的数据
        buffer.position(position + headSize);
        return this;
    }


    /**
     * 把会话消息头转换为缓冲区
     * @return ByteBuffer 缓冲区
     */
    public ByteBuffer pack() {
        /// 分配缓冲区并获取消息头
        ByteBuffer headBuf = ByteBuffer.allocate(headSize);
        headBuf.put(headType);
        headBuf.put(headSize);
        headBuf.putShort(valueLen);
        headBuf.putInt(session);
        headBuf.putInt(user);
        headBuf.putInt(tty);
        headBuf.putInt(attribute);
        headBuf.putShort(index);
        headBuf.put(ctrl);
        headBuf.put(ack);

        headBuf.flip();
        return headBuf;
    }

}



