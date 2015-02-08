package JX.Framework.Service.Msg;

import JX.*;
import JX.Framework.*;
import java.nio.*;


/**
 * ��Ϣ��
 */
public class MsgPacket
{
    public static byte HeaderSize = 20;
    public static byte MagicWord0 = 0x44;
    public static byte MagicWord1 = 0x63;
    public static byte MagicWord2 = 0x6F;

    public byte magicWord0;                     // ͷ��ħ����(�̶�Ϊ'D'-0x44)
    public byte magicWord1;                     // ͷ��ħ����(�̶�Ϊ'c'-0x63)
    public byte magicWord2;                     // ͷ��ħ����(�̶�Ϊ'o'-0x6F)
    public byte offset;                         // ͷ������(HeaderSize>=sizeof(OSMsgHeader))
    public int  dataLen;                        // ���ݳ���(��ͷ��������ݳ���)
    public int  msgType;                        // ��Ϣ����(���ڷ�����Ϣ�Ĵ���)
    public int  srcID;                          // Դ(������ID)
    public int  dstID;                          // ��(������ID)

    public ByteBuffer ctrl;                     // ������Ϣ
    public ByteBuffer data;                     // ������Ϣ


    public MsgPacket()
    {
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
     * �жϻ������������Ƿ�������֡
     * (��ȡ��Ὣ��������'��ǰλ��'��ԭ)
     * @param ByteBuffer buffer ������
     * @return int <0Ϊ�����жϳ���; ==0Ϊ������֡; >0Ϊ����֡����
     */
    public static int bFrame(ByteBuffer buffer)
    {
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < 8)
        {
            return -1;
        }

        byte magicWord0  = buffer.get();
        byte magicWord1  = buffer.get();
        byte magicWord2  = buffer.get();
        byte offset  = buffer.get();
        if ((magicWord0 != MagicWord0) ||
            (magicWord1 != MagicWord1) ||
            (magicWord2 != MagicWord2) ||
            (offset < HeaderSize))
        {
            buffer.position(position);
            return 0;
        }

        int dataLen = buffer.getInt();
        buffer.position(position);
        return (offset + dataLen);
    }


    /**
     * �ӻ������н���һ֡����
     * (һ֡���ݹ���һ����Ϣ��)
     * @param ByteBuffer buffer ������
     * @return MsgPacket ��Ϣ��
     */
    public MsgPacket parse(ByteBuffer buffer)
    {
        /// �������Ŀɶ����ȱ�����ڻ���֡��
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < HeaderSize)
        {
            return this;
        }

        /// ͷ��У�������ȷ
        byte magicWord0  = buffer.get();
        byte magicWord1  = buffer.get();
        byte magicWord2  = buffer.get();
        byte offset  = buffer.get();
        if ((magicWord0 != MagicWord0) ||
            (magicWord1 != MagicWord1) ||
            (magicWord2 != MagicWord2) ||
            (offset < HeaderSize))
        {
            buffer.position(position);
            return this;
        }

        /// ��ȡ֡ͷ
        this.magicWord0 = magicWord0;
        this.magicWord1 = magicWord1;
        this.magicWord2 = magicWord2;
        this.offset = offset;
        this.dataLen = buffer.getInt();
        this.msgType = buffer.getInt();
        this.srcID = buffer.getInt();
        this.dstID = buffer.getInt();

        /// ��ȡ������Ϣ(������255-HeaderSize)
        int ctrlLen = offset - HeaderSize;
        if (ctrlLen > 0)
        {
            if (bufLen < offset)
            {
                ctrlLen = bufLen - HeaderSize;
            }
            if (ctrlLen > 0)
            {
                if ((ctrlLen + HeaderSize) > 0xff)
                {
                    ctrlLen = 0xff - HeaderSize;
                }
                this.ctrl = ByteBuffer.allocate(ctrlLen);
                this.ctrl.put(buffer.array(), position + HeaderSize, ctrlLen);
                this.ctrl.flip();
            }
            else
            {
                ctrlLen = 0;
                this.ctrl = null;
            }
            this.offset = (byte)(HeaderSize + ctrlLen);
        }

        /// ��ȡ������Ϣ
        int dataLen = this.dataLen;
        if (dataLen > 0)
        {
            if (bufLen < (offset + dataLen))
            {
                dataLen = bufLen - offset;
            }
            if (dataLen > 0)
            {
                this.data = ByteBuffer.allocate(dataLen);
                this.data.put(buffer.array(), position + offset, dataLen);
                this.data.flip();
            }
            else
            {
                dataLen = 0;
                this.data = null;
            }
            this.dataLen = dataLen;
        }

        /// ƫ�ƹ�����������
        buffer.position(position + offset + dataLen);
        return this;
    }


    /**
     * ����Ϣ��ת��Ϊ������
     * (һ֡���ݹ���һ����Ϣ��)
     * @return ByteBuffer ������
     */
    public ByteBuffer pack()
    {
        /// ���仺��������ȡ֡ͷ
        ByteBuffer allBuf = ByteBuffer.allocate(offset + dataLen);
        allBuf.put(magicWord0);
        allBuf.put(magicWord1);
        allBuf.put(magicWord2);
        allBuf.put(offset);
        allBuf.putInt(dataLen);
        allBuf.putInt(msgType);
        allBuf.putInt(srcID);
        allBuf.putInt(dstID);

        /// ��ȡ������Ϣ
        if (((offset - HeaderSize) > 0) && (ctrl != null))
        {
            allBuf.put(ctrl);
        }

        /// ��ȡ������Ϣ
        if ((dataLen > 0) && (data != null))
        {
            allBuf.put(data);
        }

        allBuf.flip();
        return allBuf;
    }


    /**
     * ��ȡ��Ϣ����
     * @return int ����
     */
    public int getLen()
    {
        return (int)offset + dataLen;
    }


    /**
     * ���ÿ�����Ϣ
     * (�滻ԭ�п�����Ϣ)
     * (������255-HeaderSize)
     * @param ByteBuffer buffer ������
     * @return MsgPacket ��Ϣ��
     */
    public MsgPacket setCtrl(ByteBuffer buffer)
    {
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if ((bufLen <= 0) || ((bufLen + HeaderSize) > 0xff))
        {
            return this;
        }

        ctrl = ByteBuffer.allocate(bufLen);
        ctrl.put(buffer);
        offset = (byte)(bufLen + HeaderSize);

        buffer.position(position + bufLen);
        ctrl.flip();
        return this;
    }


    /**
     * �������
     * (��ԭ�����ݺ���������)
     * @param ByteBuffer buffer ������
     * @return MsgPacket ��Ϣ��
     */
    public MsgPacket addData(ByteBuffer buffer)
    {
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen <= 0)
        {
            return this;
        }

        int newDataLen = dataLen + bufLen;
        ByteBuffer newData = ByteBuffer.allocate(newDataLen);
        if (data != null)
        {
            newData.put(data);
        }
        newData.put(buffer);
        data = newData;
        dataLen = newDataLen;

        buffer.position(position + bufLen);
        data.flip();
        return this;
    }

}

