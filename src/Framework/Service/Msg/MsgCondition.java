package JX.Framework.Service.Msg;

import JX.*;
import JX.Framework.*;
import java.nio.*;


/**
 * 条件消息头
 */
public class MsgCondition
{
    public static byte HeaderType = 1;
    public static byte HeaderSize = 8;

    public static byte TypeAny = 0;
    public static byte TypeAll = 1;
    public static byte TypeOne = 2;

    public byte     headType;                   // 头部类型(见'DCOP_MSG_HEAD_TYPE')
    public byte     headSize;                   // 头部大小(见'DCOP_MSG_HEAD_SIZE')
    public short    valueLen;                   // 头部后面的值的长度
    public byte     condition;                  // 条件(见'DCOP_CONDITION_TYPE')
    public byte     paraCount;                  // 参数个数
    public short    paraLen;                    // 参数长度

    public MsgPacket msg;                       // 指向的消息包
    public int      valuePosition;              // 会话的值位于消息包数据区的位置


    public MsgCondition()
    {
        headType = HeaderType;
        headSize = HeaderSize;
        valueLen = 0;
        condition = TypeAny;
        paraCount = 0;
        paraLen = 0;
        msg = null;
        valuePosition = 0;
    }


    /**
     * 从消息包中解析条件消息头
     * @param MsgPacket msg 消息包
     * @return MsgCondition 条件消息头
     */
    public MsgCondition parse(MsgPacket msg)
    {
        if (msg == null)
        {
            return this;
        }

        ByteBuffer buffer = msg.data;
        if (msg.data == null)
        {
            return this;
        }

        /// 缓冲区的可读长度必须大于基本帧长
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < HeaderSize)
        {
            return this;
        }

        /// 头部校验必须正确
        byte headType  = buffer.get();
        byte headSize  = buffer.get();
        if ((headType != HeaderType) ||
            (headSize != HeaderSize))
        {
            buffer.position(position);
            return this;
        }

        /// 获取帧头
        this.headType = headType;
        this.headSize = headSize;
        this.valueLen = buffer.getShort();
        this.condition = buffer.get();
        this.paraCount = buffer.get();
        this.paraLen = buffer.getShort();
        this.msg = msg;
        this.valuePosition = buffer.position();

        /// 偏移过处理后的数据
        buffer.position(position + headSize);
        return this;
    }


    /**
     * 把条件消息头转换为缓冲区
     * @return ByteBuffer 缓冲区
     */
    public ByteBuffer pack()
    {
        /// 分配缓冲区并获取消息头
        ByteBuffer headBuf = ByteBuffer.allocate(headSize);
        headBuf.put(headType);
        headBuf.put(headSize);
        headBuf.putShort(valueLen);
        headBuf.put(condition);
        headBuf.put(paraCount);
        headBuf.putShort(paraLen);

        headBuf.flip();
        return headBuf;
    }

}

