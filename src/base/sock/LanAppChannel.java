package com.dcop.jx.core.base.sock;

import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;  

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.core.base.msg.*;


/**
 * 网络应用通道
 */
public class LanAppChannel {

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


    public LanAppChannel(int sockType, int channelID, ILanApp lanApp) {
        m_sockType = sockType;
        m_channelID = channelID;
        m_lanApp = lanApp;
    }


    /**
     * 套接字类型
     * @return TCPSERVER/TCPCLIENT/UDP
     */
    public int type() {
        return m_sockType;
    }


    /**
     * 通道ID
     * @return 通道ID
     */
    public int id() {
        return m_channelID;
    }


    /**
     * 数据接收
     * @param key 索引值
     */
    public void recv(SelectionKey key) throws IOException {
        /// 网络应用和事件接收器为空不处理接收
        if (m_lanApp == null) return;
        if (m_lanApp.getListener() == null) return;

        /// 申请接收缓冲区
        if (m_buffer == null) m_buffer = ByteBuffer.allocate(m_lanApp.getMTU());

        /// 进行接收处理和分发
        if (m_sockType == UDP) {
            DatagramChannel udpChannel = (DatagramChannel)key.channel();
            SocketAddress udpAddress = udpChannel.receive(m_buffer);
            udpRecv(udpChannel, udpAddress);
            restartRecv();
        }
        else {
            SocketChannel tcpChannel = (SocketChannel)key.channel();
            int count = tcpChannel.read(m_buffer);
            if (count > 0) {
                tcpRecv(tcpChannel);
            }
            else {
                m_lanApp.getListener().onDisconnect(m_channelID, tcpChannel);
                tcpChannel.close();
            }
        }
    }


    /**
     * TCP数据接收
     * @param channel 通道
     */
    private void tcpRecv(SocketChannel channel) {
        m_buffer.flip();

        for (;;) {
            int position = m_buffer.position();
            int bufLen = m_buffer.limit() - position;
            if (bufLen <= 0) {
                /// 缓冲区中数据为空
                restartRecv();
                break;
            }

            if (m_packet != null) {
                /// 先处理缓存的数据帧
                bufferRecv(channel);
                continue;
            }

            int frameLen = MsgPacket.bFrame(m_buffer);
            if (frameLen < 0) {
                /// 数据帧不够，需要继续接收
                continueRecv();
                break;
            }

            if (frameLen == 0) {
                /// 数据帧错误，需要单字节偏移
                m_buffer.position(position + 1);
                continue;
            }

            /// 数据帧正确, 先看收到的缓冲区是否足够
            if (frameLen > bufLen) {
                /// 缓冲区不够，再看一帧是不是超过缓存区容量
                if (frameLen > (m_buffer.capacity() - position)) {
                    /// 超过容量的只有先另外缓存起来
                    m_frameLen = frameLen;
                    bufferRecv(channel);
                    restartRecv();
                    break;
                }

                /// 没有超过容量的继续让缓冲区接收
                continueRecv();
                break;
            }

            /// 缓冲区已够一帧，处理接收
            dispatchRecv(channel, m_buffer);

        }
    }


    /**
     * 重新开始接收
     */
    private void restartRecv() {
        m_buffer.clear();
    }


    /**
     * 继续接着接收
     * (需要把当前位置的数据移到开头)
     */
    private void continueRecv() {
        int position = m_buffer.position();
        int limit = m_buffer.limit();

        if ((position == 0) ||
            (limit <= position)) {
            return;
        }

        for (int i = 0; i < (limit - position); ++i) {
            m_buffer.put(i, m_buffer.get(position + i));
        }

        m_buffer.position(limit - position);
        m_buffer.limit(m_buffer.capacity());
    }


    /**
     * 处理缓存接收
     * @param channel 通道
     */
    private void bufferRecv(SocketChannel channel) {
        if (m_packet == null) {
            return;
        }

        int position = m_buffer.position();
        int bufLen = m_buffer.limit() - position;
        if (bufLen <= 0) {
            return;
        }

        if (m_packet == null) {
            m_packet = ByteBuffer.allocate(m_frameLen);
            m_savedLen = 0;
        }

        if (bufLen > (m_frameLen - m_savedLen)) {
            bufLen = m_frameLen - m_savedLen;
        }

        m_packet.put(m_buffer.array(), position, bufLen);
        m_packet.flip();
        m_savedLen += bufLen;
        m_buffer.position(position + bufLen);

        if (m_savedLen >= m_frameLen) {
            /// 缓存的长度已经足够
            dispatchRecv(channel, m_packet);
            m_packet = null;
            m_frameLen = 0;
            m_savedLen = 0;
        }
    }


    /**
     * 分发缓冲区数据
     * @param channel 通道
     * @param buffer 缓冲区
     */
    private void dispatchRecv(SocketChannel channel, ByteBuffer buffer) {
        m_lanApp.getListener().onRecv(m_channelID, channel, new MsgPacket().parse(buffer));
    }


    /**
     * UDP数据接收
     * @param channel 通道
     * @param remote 远程信息
     */
    private void udpRecv(DatagramChannel channel, SocketAddress remote) {
        m_buffer.flip();

        m_lanApp.getListener().onRecv(m_channelID, channel, remote, new MsgPacket().parse(m_buffer));
    }

}

