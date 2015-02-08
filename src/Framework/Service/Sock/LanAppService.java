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
 * 网络应用服务
 */
public class LanAppService implements ILanApp, Runnable
{
    /// 本端ID
    private int m_localID = 0;

    /// MTU大小
    private int m_MTU = 1024;

    /// 阻塞时间
    private int m_blockTime = 1000;

    /// 事件接收器
    private ILanAppListener m_recver = null;

    /// 选择器
    private Selector m_selector = null;

    /// 退出标识
    private Boolean m_exit = false;


    /// 获取本地ID
    @Override
    public int getLocalID()
    {
        return m_localID;
    }


    /// 获取MTU
    @Override
    public int getMTU()
    {
        return m_MTU;
    }


    /// 获取事件接收器
    @Override
    public ILanAppListener getListener()
    {
        return m_recver;
    }


    /// 获取通道
    @Override
    public Channel getChannel(int channelID)
    {
        return m_channels.get(channelID);
    }


    /// 添加远程通道
    @Override
    public void addRemote(int remoteID, Channel remoteChannel)
    {
        m_remotes.put(remoteID, remoteChannel);
    }


    /// 删除远程通道
    @Override
    public void delRemote(int remoteID)
    {
        m_remotes.remove(remoteID);
    }

    /// 添加一个TCPServer服务
    @Override
    public void addTCPServer(int channelID, int localPort)
    {
        try
        {
            /// 初始化选择器
            if (m_selector == null) m_selector = Selector.open();

            /// 打开ServerSocket通道
            ServerSocketChannel server = ServerSocketChannel.open();

            /// 绑定到本地端口
            server.socket().bind(new InetSocketAddress(localPort));

            /// 设置为非阻塞模式
            server.configureBlocking(false);

            /// 向选择器注册OP_ACCEPT事件
            server.register(m_selector, SelectionKey.OP_ACCEPT)
                  .attach(Integer.valueOf(channelID));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /// 添加一个TCPClient服务
    @Override
    public void addTCPClient(int channelID, String remoteIP, int remotePort)
    {
        try
        {
            /// 初始化选择器
            if (m_selector == null) m_selector = Selector.open();

            /// 打开Socket通道
            SocketChannel client = SocketChannel.open();

            /// 设置为非阻塞模式
            client.configureBlocking(false);

            /// 向channel注册OP_CONNECT事件
            client.register(m_selector, SelectionKey.OP_CONNECT)
                  .attach(Integer.valueOf(channelID));

            /// 连接Server端地址
            client.connect(new InetSocketAddress(remoteIP, remotePort));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /// 添加一个UDP服务
    @Override
    public void addUDP(int channelID, boolean needBind, int localPort, boolean needBoardcast)
    {
        try
        {
            /// 初始化选择器
            if (m_selector == null) m_selector = Selector.open();

            /// 打开Socket通道
            DatagramChannel channel = DatagramChannel.open();
            m_channels.put(channelID, channel);

            /// 设置为非阻塞模式
            channel.configureBlocking(false);

            /// 本地绑定端口
            if (needBind)
            {
                channel.socket().bind(new InetSocketAddress(localPort));
            }

            /// 设置广播属性
            if (needBoardcast)
            {
                channel.socket().setBroadcast(true);
            }

            /// 向channel注册OP_READ事件，并添加附件
            channel.register(m_selector, SelectionKey.OP_READ)
                   .attach(new LanAppChannel(LanAppChannel.UDP, channelID, this));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /// 启动应用服务
    @Override
    public void start(int localID, int MTU, int blockTime, ILanAppListener recver)
    {
        m_localID = localID;
        m_MTU = MTU;
        m_blockTime = blockTime;
        m_recver = recver;

        try
        {
            if (m_selector == null) m_selector = Selector.open();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Thread thread = new Thread(this);
        thread.start();
    }


    /// 停止应用服务
    @Override
    public void stop()
    {
        synchronized(m_exit) {m_exit = true;}
    }


    /// 向远程ID发送消息包(TCP)
    @Override
    public int send(int remoteID, MsgPacket msg)
    {
        if (msg == null) return 0;

        /// 通过远程ID获取通道
        SocketChannel channel = (SocketChannel)m_remotes.get(remoteID);
        if (channel != null)
        {
            try
            {
                return channel.write(msg.pack());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return 0;
    }


    /// 通过通道ID发送消息包(推荐TCP使用)
    @Override
    public int send(MsgPacket msg, int channelID)
    {
        if (msg == null) return 0;

        /// 通过通道ID获取通道
        SocketChannel channel = (SocketChannel)m_channels.get(channelID);
        if (channel != null)
        {
            try
            {
                return channel.write(msg.pack());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return 0;
    }


    /// 仅限唯一通道时发送消息包(推荐TCP使用)
    @Override
    public int send(MsgPacket msg)
    {
        if (msg == null) return 0;

        if (m_channels.size() == 1)
        {
            Iterator iter = m_channels.entrySet().iterator();
            if (iter.hasNext())
            {
                Map.Entry entry = (Map.Entry)iter.next();
                WritableByteChannel channel = (WritableByteChannel)entry.getValue();
                if (channel != null)
                {
                    try
                    {
                        return channel.write(msg.pack());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        return 0;
    }


    /// 通过通道ID向指定地址发送消息包(UDP)
    @Override
    public int send(String remoteIP, int remotePort, MsgPacket msg, int channelID)
    {
        if (msg == null) return 0;

        /// 通过通道ID获取通道
        try
        {
            DatagramChannel channel = (DatagramChannel)m_channels.get(channelID);
            if (channel != null)
            {
                return channel.send(msg.pack(), new InetSocketAddress(remoteIP, remotePort));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return 0;
    }


    /// 仅限唯一通道时向指定地址发送消息包(UDP)
    @Override
    public int send(String remoteIP, int remotePort, MsgPacket msg)
    {
        if (msg == null) return 0;

        /// 通过通道ID获取通道
        try
        {
            if (m_channels.size() == 1)
            {
                Iterator iter = m_channels.entrySet().iterator();
                if (iter.hasNext())
                {
                    Map.Entry entry = (Map.Entry)iter.next();
                    DatagramChannel channel = (DatagramChannel)entry.getValue();
                    if (channel != null)
                    {
                        return channel.send(msg.pack(), new InetSocketAddress(remoteIP, remotePort));
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return 0;
    }


    /// 线程入口
    @Override
    public void run()
    {
        try
        {
            while (!exit())
            {
                /// 每次阻塞m_blockTime时长
                m_selector.select(m_blockTime);

                /// 循环查看事件
                Iterator iter = m_selector.selectedKeys().iterator();
                while (iter.hasNext())
                {
                    SelectionKey key = (SelectionKey)iter.next();
                    iter.remove();
                    process(key);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /// 处理事件
    private void process(SelectionKey key) throws IOException
    {
        if (key.isAcceptable())
        {
            accept(key);
        }

        if (key.isConnectable())
        {
            connect(key);
        }

        if (key.isReadable())
        {
            recv(key);
        }
    }


    /// 收到连接
    private void accept(SelectionKey key) throws IOException
    {
        /// 接收请求
        ServerSocketChannel server = (ServerSocketChannel)key.channel();
        SocketChannel accept = server.accept();

        /// 获取服务器的通道ID
        int serverChannelID = 0;
        Integer serverChannelInt = (Integer)key.attachment();
        if (serverChannelInt != null) serverChannelID = serverChannelInt.intValue();

        /// 通知事件接收器
        if (m_recver != null)
        {
            if (!m_recver.onAccept(serverChannelID, server, accept))
            {
                /// 拒绝请求
                accept.close();
                return;
            }
        }

        ///设置非阻塞模式
        accept.configureBlocking(false);

        /// 最后连接的客户套接字加入到'服务器通道'
        m_channels.put(serverChannelID, accept);

        /// 注册到选择器，并添加附件
        accept.register(m_selector, SelectionKey.OP_READ)
              .attach(new LanAppChannel(LanAppChannel.TCPACCEPT, serverChannelID, this));
    }


    /// 进行连接
    private void connect(SelectionKey key) throws IOException
    {
        /// 完成连接
        SocketChannel client = (SocketChannel)key.channel();
        if (client.isConnectionPending())
        {
            if (!client.finishConnect())
            {
                /// 完成连接失败
                return;
            }
        }

        /// 获取客户端的通道ID
        int clientChannelID = 0;
        Integer clientChannelInt = (Integer)key.attachment();
        if (clientChannelInt != null) clientChannelID = clientChannelInt.intValue();

        /// 通知事件接收器
        if (m_recver != null)
        {
            if (!m_recver.onConnect(clientChannelID, client))
            {
                return;
            }
        }

        /// 将与服务器建立好连接的套接字加入到'客户端通道'
        m_channels.put(clientChannelID, client);

        /// 注册到选择器，并添加附件
        client.register(m_selector, SelectionKey.OP_READ)
              .attach(new LanAppChannel(LanAppChannel.TCPCLIENT, clientChannelID, this));
    }


    /// 接收数据
    private void recv(SelectionKey key) throws IOException
    {
        /// 接收数据
        LanAppChannel channel = (LanAppChannel)key.attachment();
        if (channel != null)
        {
            channel.recv(key);
        }
    }


    /// 是否退出
    private boolean exit()
    {
        boolean exit = false;
        synchronized(m_exit) {exit = m_exit;}

        return exit;
    }


    /// 通道和远程集合的MAP
    private HashMap<Integer, Channel> m_channels;
    private HashMap<Integer, Channel> m_remotes;


    /// 公共初始化块
    {
        m_channels = new HashMap<Integer, Channel>();
        m_remotes = new HashMap<Integer, Channel>();
    }

}

