package JX.Framework.Service.Msg;

import JX.*;
import JX.Framework.*;
import java.nio.*;


/**
 * �����ڵ�
 */
public class MsgParaNode
{
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
    

    public int      paraID;                     // ����ID(��1��ʼ�ı��,0��ʾ���в���)
    public byte     opCode;                     // ������(��'DCOP_OPCODE_TYPE')
    public byte     paraType;                   // ��������(���嶨���ʵ��ʹ���йأ����ʹ�õĶ������Խ�ģ���Ǿ��������ֶ�)
    public short    paraSize;                   // ������С


    public MsgParaNode()
    {
        paraID = 0;
        opCode = OpCodeNone;
        paraType = 0;
        paraSize = 0;
    }


    /**
     * ����Ϣ���л�ȡ�����ڵ�
     * @param ByteBuffer buffer ������
     * @return MsgParaNode ������Ϣͷ
     */
    public MsgParaNode parse(ByteBuffer buffer)
    {
        /// �������Ŀɶ����ȱ�����ڻ���֡��
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < HeaderSize)
        {
            return this;
        }

        /// ��ȡ����
        this.paraID = buffer.getInt();
        this.opCode = buffer.get();
        this.paraType = buffer.get();
        this.paraSize = buffer.getShort();

        /// ƫ�ƹ�����������
        buffer.position(position + HeaderSize);
        return this;
    }


    /**
     * ����Ϣ�����ڵ�ת��Ϊ������
     * @return ByteBuffer ������
     */
    public ByteBuffer pack()
    {
        /// ���仺��������ȡ�����ڵ�
        ByteBuffer headBuf = ByteBuffer.allocate(HeaderSize);
        headBuf.putInt(paraID);
        headBuf.put(opCode);
        headBuf.put(paraType);
        headBuf.putShort(paraSize);

        headBuf.flip();
        return headBuf;
    }

}

