package com.dcop.jx.entry.base;

import java.nio.channels.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;


/**
 * 网络应用接口
 */
public interface ILanApp {
    /**
     * 获取本地ID
     * @return 本地ID
     */
    public int getLocalID();


    /**
     * 获取MTU
     * @return MTU大小
     */
    public int getMTU();


    /**
     * 获取事件接收器
     * @return 事件接收器
     */
    public ILanEvent getListener();


    /**
     * 获取通道
     * @param channelID 通道ID
     * @return 通道对象
     */
    public Channel getChannel(int channelID);


    /**
     * 添加远程通道
     * @param remoteID 远程ID
     * @param remoteChannel 远程通道对象
     */
    public void addRemote(int remoteID, Channel remoteChannel);


    /**
     * 删除远程通道
     * @param remoteID 远程ID
     */
    public void delRemote(int remoteID);


    /**
     * 添加一个TCPServer服务
     * @param channelID 通道ID
     * @param localPort 本地端口
     */
    public void addTCPServer(int channelID, int localPort);


    /**
     * 添加一个TCPClient服务
     * @param channelID 通道ID
     * @param remoteIP 远端IP
     * @param remotePort 远端端口
     */
    public void addTCPClient(int channelID, String remoteIP, int remotePort);


    /**
     * 添加一个UDP服务
     * @param channelID 通道ID
     * @param needBind 是否需要绑定
     * @param localPort 本地端口
     * @param needBoardcast 是否需要广播
     */
    public void addUDP(int channelID, boolean needBind, int localPort, boolean needBoardcast);


    /**
     * 启动应用服务
     * @param appName 应用名
     * @param localID 本地ID
     * @param MTU MTU大小
     * @param blockTime 循环阻塞时间
     * @param recver 消息接收器
     */
    public void start(String appName, int localID, int MTU, int blockTime, ILanEvent recver);


    /**
     * 停止应用服务
     */
    public void stop();


    /**
     * 向远程ID发送消息包(TCP)
     * @param remoteID 远程ID
     * @param msg 消息包对象
     * @return 发送大小
     */
    public int send(int remoteID, IMsg msg);


    /**
     * 通过通道ID发送消息包(推荐TCP使用)
     * (UDP如果要使用，请先进行connect目的地址)
     * @param msg 消息包对象
     * @param channelID 通道ID
     * @return 发送大小
     */
    public int send(IMsg msg, int channelID);


    /**
     * 仅限唯一通道时发送消息包(推荐TCP使用)
     * (UDP如果要使用，请先进行connect目的地址)
     * @param msg 消息包对象
     * @return 发送大小
     */
    public int send(IMsg msg);


    /**
     * 通过通道ID向指定地址发送消息包(UDP)
     * @param msg 消息包对象
     * @param channelID 通道ID
     * @return 发送大小
     */
    public int send(String remoteIP, int remotePort, IMsg msg, int channelID);


    /**
     * 仅限唯一通道时向指定地址发送消息包(UDP)
     * @param msg 消息包对象
     * @return 发送大小
     */
    public int send(String remoteIP, int remotePort, IMsg msg);

}

