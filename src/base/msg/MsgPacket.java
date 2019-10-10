package com.dcop.jx.core.base.msg;

import java.nio.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.core.base.*;


/**
 * 消息包
 */
@ClassImport("msg")
public class MsgPacket implements IMsg {

    public static byte HeaderSize = 20;
    public static byte MagicWord0 = 0x44;
    public static byte MagicWord1 = 0x63;
    public static byte MagicWord2 = 0x6F;

    public byte        magicWord0;              // 头部魔术字(固定为'D'-0x44)
    public byte        magicWord1;              // 头部魔术字(固定为'c'-0x63)
    public byte        magicWord2;              // 头部魔术字(固定为'o'-0x6F)
    public byte        offset;                  // 头部长度(HeaderSize>=sizeof(OSMsgHeader))
    public int         dataLen;                 // 数据长度(除头部外的数据长度)
    public int         msgType;                 // 消息类型(用于分流消息的处理)
    public int         srcID;                   // 源(发送者ID)
    public int         dstID;                   // 宿(接收者ID)

    public ByteBuffer  ctrl;                    // 控制信息
    public ByteBuffer  data;                    // 数据信息


    public MsgPacket() {
        magicWord0 = MagicWord0;
        magicWord1 = MagicWord1;
        magicWord2 = MagicWord2;
        offset = HeaderSize;
        dataLen = 0;
        msgType = 0;
        srcID = 0;
        dstID = 0;
        ctrl = null;
        data = null;
    }


    /**
     * 判断缓冲区的数据是否是数据帧
     * (读取后会将缓冲区的'当前位置'复原)
     * @param ByteBuffer buffer 缓冲区
     * @return int <0为不够判断长度; ==0为非数据帧; >0为数据帧长度
     */
    public static int bFrame(ByteBuffer buffer) {
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < 8) {
            return -1;
        }

        byte magicWord0  = buffer.get();
        byte magicWord1  = buffer.get();
        byte magicWord2  = buffer.get();
        byte offset  = buffer.get();
        if ((magicWord0 != MagicWord0) ||
            (magicWord1 != MagicWord1) ||
            (magicWord2 != MagicWord2) ||
            (offset < HeaderSize)) {
            buffer.position(position);
            return 0;
        }

        int dataLen = buffer.getInt();
        buffer.position(position);
        return (offset + dataLen);
    }


    /**
     * 从缓冲区中解析一帧数据
     * (一帧数据构成一个消息包)
     * @param ByteBuffer buffer 缓冲区
     * @return MsgPacket 消息包
     */
    public MsgPacket parse(ByteBuffer buffer) {
        /// 缓冲区的可读长度必须大于基本帧长
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < HeaderSize) {
            return this;
        }

        /// 头部校验必须正确
        byte magicWord0  = buffer.get();
        byte magicWord1  = buffer.get();
        byte magicWord2  = buffer.get();
        byte offset  = buffer.get();
        if ((magicWord0 != MagicWord0) ||
            (magicWord1 != MagicWord1) ||
            (magicWord2 != MagicWord2) ||
            (offset < HeaderSize)) {
            buffer.position(position);
            return this;
        }

        /// 获取帧头
        this.magicWord0 = magicWord0;
        this.magicWord1 = magicWord1;
        this.magicWord2 = magicWord2;
        this.offset = offset;
        this.dataLen = buffer.getInt();
        this.msgType = buffer.getInt();
        this.srcID = buffer.getInt();
        this.dstID = buffer.getInt();

        /// 获取控制信息(不超过255-HeaderSize)
        int ctrlLen = offset - HeaderSize;
        if (ctrlLen > 0) {
            if (bufLen < offset) {
                ctrlLen = bufLen - HeaderSize;
            }
            if (ctrlLen > 0) {
                if ((ctrlLen + HeaderSize) > 0xff) {
                    ctrlLen = 0xff - HeaderSize;
                }
                this.ctrl = ByteBuffer.allocate(ctrlLen);
                this.ctrl.put(buffer.array(), position + HeaderSize, ctrlLen);
                this.ctrl.flip();
            } else {
                ctrlLen = 0;
                this.ctrl = null;
            }
            this.offset = (byte)(HeaderSize + ctrlLen);
        }

        /// 获取数据信息
        int dataLen = this.dataLen;
        if (dataLen > 0) {
            if (bufLen < (offset + dataLen)) {
                dataLen = bufLen - offset;
            }
            if (dataLen > 0) {
                this.data = ByteBuffer.allocate(dataLen);
                this.data.put(buffer.array(), position + offset, dataLen);
                this.data.flip();
            } else {
                dataLen = 0;
                this.data = null;
            }
            this.dataLen = dataLen;
        }

        /// 偏移过处理后的数据
        buffer.position(position + offset + dataLen);
        return this;
    }


    /**
     * 把消息包转换为缓冲区
     * (一帧数据构成一个消息包)
     * @return ByteBuffer 缓冲区
     */
    public ByteBuffer pack() {
        /// 分配缓冲区并获取帧头
        ByteBuffer allBuf = ByteBuffer.allocate(offset + dataLen);
        allBuf.put(magicWord0);
        allBuf.put(magicWord1);
        allBuf.put(magicWord2);
        allBuf.put(offset);
        allBuf.putInt(dataLen);
        allBuf.putInt(msgType);
        allBuf.putInt(srcID);
        allBuf.putInt(dstID);

        /// 获取控制信息
        if (((offset - HeaderSize) > 0) && (ctrl != null)) {
            allBuf.put(ctrl);
        }

        /// 获取数据信息
        if ((dataLen > 0) && (data != null)) {
            allBuf.put(data);
        }

        allBuf.flip();
        return allBuf;
    }


    /**
     * 获取消息长度
     * @return int 长度
     */
    public int getLen() {
        return (int)offset + dataLen;
    }


    /**
     * 设置控制信息
     * (替换原有控制信息)
     * (不超过255-HeaderSize)
     * @param ByteBuffer buffer 缓冲区
     * @return MsgPacket 消息包
     */
    public MsgPacket setCtrl(ByteBuffer buffer) {
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if ((bufLen <= 0) || ((bufLen + HeaderSize) > 0xff)) {
            return this;
        }

        ctrl = ByteBuffer.allocate(bufLen);
        ctrl.put(buffer);
        offset = (byte)(bufLen + HeaderSize);

        buffer.position(position + bufLen);
        ctrl.flip();
        return this;
    }
    public ByteBuffer getCtrl() {
        return ctrl;
    }


    /**
     * 添加数据
     * (在原有数据后面继续添加)
     * @param ByteBuffer buffer 缓冲区
     * @return MsgPacket 消息包
     */
    public MsgPacket addData(ByteBuffer buffer) {
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen <= 0) {
            return this;
        }

        int newDataLen = dataLen + bufLen;
        ByteBuffer newData = ByteBuffer.allocate(newDataLen);
        if (data != null) {
            newData.put(data);
        }
        newData.put(buffer);
        data = newData;
        dataLen = newDataLen;

        buffer.position(position + bufLen);
        data.flip();
        return this;
    }
    public ByteBuffer getData() {
        return data;
    }

}

