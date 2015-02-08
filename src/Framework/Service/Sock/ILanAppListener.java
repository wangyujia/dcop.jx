package JX.Framework.Service.Sock;

import java.net.*;
import java.nio.channels.*;
import JX.*;
import JX.Framework.*;
import JX.Framework.Service.Msg.*;


/**
 * 网络应用事件
 */
public interface ILanAppListener
{
    /**
     * TCP服务器收到客户端连接
     * @param int channelID 通道ID
     * @param ServerSocketChannel server 套接字
     * @param SocketChannel accept 套接字
     * @return boolean 是否接受连接
     */
    public boolean onAccept(int channelID, ServerSocketChannel server, SocketChannel accept);


    /**
     * TCP客户端连接上服务器
     * @param int channelID 通道ID
     * @param SocketChannel client 套接字
     * @return boolean 是否接受连接
     */
    public boolean onConnect(int channelID, SocketChannel client);


    /**
     * TCP连接中断
     * @param int channelID 通道ID
     * @param SocketChannel channel 套接字
     */
    public void onDisconnect(int channelID, SocketChannel channel);


    /**
     * TCP收到数据
     * @param int channelID 通道ID
     * @param SocketChannel channel 套接字
     * @param MsgPacket msg 消息包
     */
    public void onRecv(int channelID, SocketChannel channel, MsgPacket msg);


    /**
     * UDP收到数据
     * @param int channelID 通道ID
     * @param DatagramChannel channel 套接字
     * @param SocketAddress remote 远端地址
     * @param MsgPacket msg 消息包
     */
    public void onRecv(int channelID, DatagramChannel channel, SocketAddress remote, MsgPacket msg);

}

