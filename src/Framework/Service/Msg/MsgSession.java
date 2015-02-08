package JX.Framework.Service.Msg;

import JX.*;
import JX.Framework.*;
import java.nio.*;


/**
 * �Ự��Ϣͷ
 */
public class MsgSession
{
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

    public byte     headType;                   // ͷ������(��'DCOP_MSG_HEAD_TYPE')
    public byte     headSize;                   // ͷ����С(��'DCOP_MSG_HEAD_SIZE')
    public short    valueLen;                   // ͷ�������ֵ�ĳ���
    public int      session;                    // �Ự
    public int      user;                       // �û�
    public int      tty;                        // �ն�
    public int      attribute;                  // ����
    public short    index;                      // ����
    public byte     ctrl;                       // ����(��'DCOP_CTRL_TYPE')
    public byte     ack;                        // �Ƿ�Ӧ����Ϣ('DCOP_NO/DCOP_YES')

    public MsgPacket msg;                       // ָ�����Ϣ��
    public int      valuePosition;              // �Ự��ֵλ����Ϣ����������λ��


    public MsgSession()
    {
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
        valuePosition = 0;
    }


    /**
     * ����Ϣ���н����Ự��Ϣͷ
     * @param MsgPacket msg ��Ϣ��
     * @return MsgSession �Ự��Ϣͷ
     */
    public MsgSession parse(MsgPacket msg)
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

        /// �������Ŀɶ����ȱ�����ڻ���֡��
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < HeaderSize)
        {
            return this;
        }

        /// ͷ��У�������ȷ
        byte headType  = buffer.get();
        byte headSize  = buffer.get();
        if ((headType != HeaderType) ||
            (headSize != HeaderSize))
        {
            buffer.position(position);
            return this;
        }

        /// ��ȡ֡ͷ
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
        this.valuePosition = buffer.position();

        /// ƫ�ƹ�����������
        buffer.position(position + headSize);
        return this;
    }


    /**
     * �ѻỰ��Ϣͷת��Ϊ������
     * @return ByteBuffer ������
     */
    public ByteBuffer pack()
    {
        /// ���仺��������ȡ��Ϣͷ
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



