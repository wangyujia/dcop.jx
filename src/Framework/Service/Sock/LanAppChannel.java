package JX.Framework.Service.Sock;

import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;  
import JX.*;
import JX.Framework.*;
import JX.Framework.Service.Msg.*;


/**
 * ����Ӧ��ͨ��
 */
public class LanAppChannel
{
    public static int NONE = 0;
    public static int UDP = 1;
    public static int TCPSERVER = 2;
    public static int TCPACCEPT = 3;
    public static int TCPCLIENT = 4;

    private int m_sockType = NONE;
    private int m_channelID = 0;
    private ILanApp m_lanApp = null;

    private ByteBuffer m_buffer = null;
    private ByteBuffer m_packet = null;
    private int m_frameLen = 0;
    private int m_savedLen = 0;


    public LanAppChannel(int sockType, int channelID, ILanApp lanApp)
    {
        m_sockType = sockType;
        m_channelID = channelID;
        m_lanApp = lanApp;
    }


    /**
     * �׽�������
     * @return int TCPSERVER/TCPCLIENT/UDP
     */
    public int type()
    {
        return m_sockType;
    }


    /**
     * ͨ��ID
     * @return int ͨ��ID
     */
    public int id()
    {
        return m_channelID;
    }


    /**
     * ���ݽ���
     * @param SelectionKey key ����ֵ
     */
    public void recv(SelectionKey key) throws IOException
    {
        /// ����Ӧ�ú��¼�������Ϊ�ղ��������
        if (m_lanApp == null) return;
        if (m_lanApp.getListener() == null) return;

        /// ������ջ�����
        if (m_buffer == null) m_buffer = ByteBuffer.allocate(m_lanApp.getMTU());

        /// ���н��մ���ͷַ�
        if (m_sockType == UDP)
        {
            DatagramChannel udpChannel = (DatagramChannel)key.channel();
            SocketAddress udpAddress = udpChannel.receive(m_buffer);
            udpRecv(udpChannel, udpAddress);
            restartRecv();
        }
        else
        {
            SocketChannel tcpChannel = (SocketChannel)key.channel();
            int count = tcpChannel.read(m_buffer);
            if (count > 0)
            {
                tcpRecv(tcpChannel);
            }
            else
            {
                m_lanApp.getListener().onDisconnect(m_channelID, tcpChannel);
                tcpChannel.close();
            }
        }
    }


    /**
     * TCP���ݽ���
     * @param SocketChannel channel ͨ��
     */
    private void tcpRecv(SocketChannel channel)
    {
        m_buffer.flip();

        for (;;)
        {
            int position = m_buffer.position();
            int bufLen = m_buffer.limit() - position;
            if (bufLen <= 0)
            {
                /// ������������Ϊ��
                restartRecv();
                break;
            }

            if (m_packet != null)
            {
                /// �ȴ����������֡
                bufferRecv(channel);
                continue;
            }

            int frameLen = MsgPacket.bFrame(m_buffer);
            if (frameLen < 0)
            {
                /// ����֡��������Ҫ��������
                continueRecv();
                break;
            }

            if (frameLen == 0)
            {
                /// ����֡������Ҫ���ֽ�ƫ��
                m_buffer.position(position + 1);
                continue;
            }

            /// ����֡��ȷ, �ȿ��յ��Ļ������Ƿ��㹻
            if (frameLen > bufLen)
            {
                /// �������������ٿ�һ֡�ǲ��ǳ�������������
                if (frameLen > (m_buffer.capacity() - position))
                {
                    /// ����������ֻ�������⻺������
                    m_frameLen = frameLen;
                    bufferRecv(channel);
                    restartRecv();
                    break;
                }

                /// û�г��������ļ����û���������
                continueRecv();
                break;
            }

            /// �������ѹ�һ֡���������
            dispatchRecv(channel, m_buffer);

        }
    }


    /**
     * ���¿�ʼ����
     */
    private void restartRecv()
    {
        m_buffer.clear();
    }


    /**
     * �������Ž���
     * (��Ҫ�ѵ�ǰλ�õ������Ƶ���ͷ)
     */
    private void continueRecv()
    {
        int position = m_buffer.position();
        int limit = m_buffer.limit();

        if ((position == 0) ||
            (limit <= position))
        {
            return;
        }

        for (int i = 0; i < (limit - position); ++i)
        {
            m_buffer.put(i, m_buffer.get(position + i));
        }

        m_buffer.position(limit - position);
        m_buffer.limit(m_buffer.capacity());
    }


    /**
     * ���������
     * @param SocketChannel channel ͨ��
     */
    private void bufferRecv(SocketChannel channel)
    {
        if (m_packet == null)
        {
            return;
        }

        int position = m_buffer.position();
        int bufLen = m_buffer.limit() - position;
        if (bufLen <= 0)
        {
            return;
        }

        if (m_packet == null)
        {
            m_packet = ByteBuffer.allocate(m_frameLen);
            m_savedLen = 0;
        }

        if (bufLen > (m_frameLen - m_savedLen))
        {
            bufLen = m_frameLen - m_savedLen;
        }

        m_packet.put(m_buffer.array(), position, bufLen);
        m_packet.flip();
        m_savedLen += bufLen;
        m_buffer.position(position + bufLen);

        if (m_savedLen >= m_frameLen)
        {
            /// ����ĳ����Ѿ��㹻
            dispatchRecv(channel, m_packet);
            m_packet = null;
            m_frameLen = 0;
            m_savedLen = 0;
        }
    }


    /**
     * �ַ�����������
     * @param SocketChannel channel ͨ��
     */
    private void dispatchRecv(SocketChannel channel, ByteBuffer buffer)
    {
        m_lanApp.getListener().onRecv(m_channelID, channel, new MsgPacket().parse(buffer));
    }


    /**
     * UDP���ݽ���
     * @param SocketChannel channel ͨ��
     */
    private void udpRecv(DatagramChannel channel, SocketAddress remote)
    {
        m_buffer.flip();

        m_lanApp.getListener().onRecv(m_channelID, channel, remote, new MsgPacket().parse(m_buffer));
    }

}

