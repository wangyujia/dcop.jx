package JX.Framework.Service.Sock;

import java.nio.channels.*;
import JX.*;
import JX.Framework.*;
import JX.Framework.Service.Msg.*;


/**
 * 网络应用接口
 */
public interface ILanApp
{
    /**
     * 获取本地ID
     * @return int 本地ID
     */
    public int getLocalID();


    /**
     * 获取MTU
     * @return int MTU大小
     */
    public int getMTU();


    /**
     * 获取事件接收器
     * @return ILanAppListener 事件接收器
     */
    public ILanAppListener getListener();


    /**
     * 获取通道
     * @param int channelID 通道ID
     * @return Channel 通道
     */
    public Channel getChannel(int channelID);


    /**
     * 添加远程通道
     * @param int remoteID 远程ID
     * @param Channel remoteChannel 远程通道
     */
    public void addRemote(int remoteID, Channel remoteChannel);


    /**
     * 删除远程通道
     * @param int remoteID 远程ID
     */
    public void delRemote(int remoteID);


    /**
     * 添加一个TCPServer服务
     * @param int channelID 通道ID
     * @param int localPort 本地端口
     */
    public void addTCPServer(int channelID, int localPort);


    /**
     * 添加一个TCPClient服务
     * @param int channelID 通道ID
     * @param String remoteIP 远端IP
     * @param int remotePort 远端端口
     */
    public void addTCPClient(int channelID, String remoteIP, int remotePort);


    /**
     * 添加一个UDP服务
     * @param int channelID 通道ID
     * @param boolean needBind 是否需要绑定
     * @param int localPort 本地端口
     * @param boolean needBoardcast 是否需要广播
     */
    public void addUDP(int channelID, boolean needBind, int localPort, boolean needBoardcast);


    /**
     * 启动应用服务
     * @param int localID 本地ID
     * @param int MTU MTU大小
     * @param int blockTime 循环阻塞时间
     * @param MsgRecver recver 消息接收器
     */
    public void start(int localID, int MTU, int blockTime, ILanAppListener recver);


    /**
     * 停止应用服务
     */
    public void stop();


    /**
     * 向远程ID发送消息包(TCP)
     * @param int remoteID 远程ID
     * @param MsgPacket msg 消息包
     * @return int 发送大小
     */
    public int send(int remoteID, MsgPacket msg);


    /**
     * 通过通道ID发送消息包(推荐TCP使用)
     * (UDP如果要使用，请先进行connect目的地址)
     * @param MsgPacket msg 消息包
     * @param int channelID 通道ID
     * @return int 发送大小
     */
    public int send(MsgPacket msg, int channelID);


    /**
     * 仅限唯一通道时发送消息包(推荐TCP使用)
     * (UDP如果要使用，请先进行connect目的地址)
     * @param MsgPacket msg 消息包
     * @return int 发送大小
     */
    public int send(MsgPacket msg);


    /**
     * 通过通道ID向指定地址发送消息包(UDP)
     * @param MsgPacket msg 消息包
     * @param int channelID 通道ID
     * @return int 发送大小
     */
    public int send(String remoteIP, int remotePort, MsgPacket msg, int channelID);


    /**
     * 仅限唯一通道时向指定地址发送消息包(UDP)
     * @param MsgPacket msg 消息包
     * @return int 发送大小
     */
    public int send(String remoteIP, int remotePort, MsgPacket msg);

}

