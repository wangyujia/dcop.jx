package com.dcop.jx.core.base.msg;

import java.nio.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.core.base.*;


/**
 * 参数节点
 */
public class MsgParaNode {

    public static byte HeaderSize = 8;

    public static byte OpCodeNone = 0;
    public static byte OpCodeAdd = 1;
    public static byte OpCodeSub = 2;
    public static byte OpCodeMul = 3;
    public static byte OpCodeDiv = 4;
    public static byte OpCodeEqual = 5;
    public static byte OpCodeMoreEqual = 6;
    public static byte OpCodeMore = 7;
    public static byte OpCodeLess = 8;
    public static byte OpCodeLessEqual = 9;
    public static byte OpCodeNotEqual = 10;
    public static byte OpCodeInclude = 11;
    public static byte OpCodeNotInclude = 12;

    public static byte ParaTypeNull = 0;
    public static byte ParaTypeIdentify = 1;
    public static byte ParaTypeByte = 2;
    public static byte ParaTypeWord = 3;
    public static byte ParaTypeDword = 4;
    public static byte ParaTypeChar = 5;
    public static byte ParaTypeShort = 6;
    public static byte ParaTypeInteger = 7;
    public static byte ParaTypeNumber = 8;
    public static byte ParaTypeString = 9;
    public static byte ParaTypeBuffer = 10;
    public static byte ParaTypeDate = 11;
    public static byte ParaTypeIP = 12;
    

    public int         paraID;                  // 参数ID(从1开始的编号,0表示所有参数)
    public byte        opCode;                  // 操作符(见'DCOP_OPCODE_TYPE')
    public byte        paraType;                // 参数类型(具体定义和实际使用有关，如果使用的对象属性建模，那就是数据字段)
    public short       paraSize;                // 参数大小


    public MsgParaNode() {
        paraID = 0;
        opCode = OpCodeNone;
        paraType = 0;
        paraSize = 0;
    }


    /**
     * 从消息包中获取参数节点
     * @param ByteBuffer buffer 缓冲区
     * @return MsgParaNode 条件消息头
     */
    public MsgParaNode parse(ByteBuffer buffer) {
        /// 缓冲区的可读长度必须大于基本帧长
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < HeaderSize) {
            return this;
        }

        /// 获取参数
        this.paraID = buffer.getInt();
        this.opCode = buffer.get();
        this.paraType = buffer.get();
        this.paraSize = buffer.getShort();

        /// 偏移过处理后的数据
        buffer.position(position + HeaderSize);
        return this;
    }


    /**
     * 把消息参数节点转换为缓冲区
     * @return ByteBuffer 缓冲区
     */
    public ByteBuffer pack() {
        /// 分配缓冲区并获取参数节点
        ByteBuffer headBuf = ByteBuffer.allocate(HeaderSize);
        headBuf.putInt(paraID);
        headBuf.put(opCode);
        headBuf.put(paraType);
        headBuf.putShort(paraSize);

        headBuf.flip();
        return headBuf;
    }

}

