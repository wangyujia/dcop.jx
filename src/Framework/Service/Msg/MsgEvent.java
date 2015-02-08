package JX.Framework.Service.Msg;

import JX.*;
import JX.Framework.*;
import java.nio.*;


/**
 * 事件消息头
 */
public class MsgEvent
{
    public static byte HeaderType = 4;
    public static byte HeaderSize = 16;

    public byte     headType;                   // 头部类型(见'DCOP_MSG_HEAD_TYPE')
    public byte     headSize;                   // 头部大小(见'DCOP_MSG_HEAD_SIZE')
    public short    valueLen;                   // 头部后面的值的长度
    public short    recordCount;                // 记录条数
    public short    recordIndex;                // 本次记录索引
    public short    paraCount;                  // 参数个数
    public short    paraLen;                    // 参数长度

    public MsgPacket msg;                       // 指向的消息包
    public int      valuePosition;              // 会话的值位于消息包数据区的位置


    public MsgEvent()
    {
        headType = HeaderType;
        headSize = HeaderSize;
        valueLen = 0;
        paraCount = 0;
        paraLen = 0;
        msg = null;
        valuePosition = 0;
    }


    /**
     * 从消息包中解析事件消息头
     * @param MsgPacket msg 消息包
     * @return MsgEvent 事件消息头
     */
    public MsgEvent parse(MsgPacket msg)
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
        this.recordCount = buffer.getShort();
        this.recordIndex = buffer.getShort();
        this.paraCount = buffer.getShort();
        this.paraLen = buffer.getShort();
        this.msg = msg;
        this.valuePosition = buffer.position();

        /// 偏移过处理后的数据
        buffer.position(position + headSize);
        return this;
    }


    /**
     * 把事件消息头转换为缓冲区
     * @return ByteBuffer 缓冲区
     */
    public ByteBuffer pack()
    {
        /// 分配缓冲区并获取消息头
        ByteBuffer headBuf = ByteBuffer.allocate(headSize);
        headBuf.put(headType);
        headBuf.put(headSize);
        headBuf.putShort(valueLen);
        headBuf.putShort(recordCount);
        headBuf.putShort(recordIndex);
        headBuf.putShort(paraCount);
        headBuf.putShort(paraLen);

        headBuf.flip();
        return headBuf;
    }

}




