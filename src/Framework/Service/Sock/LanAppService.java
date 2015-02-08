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
 * ����Ӧ�÷���
 */
public class LanAppService implements ILanApp, Runnable
{
    /// ����ID
    private int m_localID = 0;

    /// MTU��С
    private int m_MTU = 1024;

    /// ����ʱ��
    private int m_blockTime = 1000;

    /// �¼�������
    private ILanAppListener m_recver = null;

    /// ѡ����
    private Selector m_selector = null;

    /// �˳���ʶ
    private Boolean m_exit = false;


    /// ��ȡ����ID
    @Override
    public int getLocalID()
    {
        return m_localID;
    }


    /// ��ȡMTU
    @Override
    public int getMTU()
    {
        return m_MTU;
    }


    /// ��ȡ�¼�������
    @Override
    public ILanAppListener getListener()
    {
        return m_recver;
    }


    /// ��ȡͨ��
    @Override
    public Channel getChannel(int channelID)
    {
        return m_channels.get(channelID);
    }


    /// ���Զ��ͨ��
    @Override
    public void addRemote(int remoteID, Channel remoteChannel)
    {
        m_remotes.put(remoteID, remoteChannel);
    }


    /// ɾ��Զ��ͨ��
    @Override
    public void delRemote(int remoteID)
    {
        m_remotes.remove(remoteID);
    }

    /// ���һ��TCPServer����
    @Override
    public void addTCPServer(int channelID, int localPort)
    {
        try
        {
            /// ��ʼ��ѡ����
            if (m_selector == null) m_selector = Selector.open();

            /// ��ServerSocketͨ��
            ServerSocketChannel server = ServerSocketChannel.open();

            /// �󶨵����ض˿�
            server.socket().bind(new InetSocketAddress(localPort));

            /// ����Ϊ������ģʽ
            server.configureBlocking(false);

            /// ��ѡ����ע��OP_ACCEPT�¼�
            server.register(m_selector, SelectionKey.OP_ACCEPT)
                  .attach(Integer.valueOf(channelID));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /// ���һ��TCPClient����
    @Override
    public void addTCPClient(int channelID, String remoteIP, int remotePort)
    {
        try
        {
            /// ��ʼ��ѡ����
            if (m_selector == null) m_selector = Selector.open();

            /// ��Socketͨ��
            SocketChannel client = SocketChannel.open();

            /// ����Ϊ������ģʽ
            client.configureBlocking(false);

            /// ��channelע��OP_CONNECT�¼�
            client.register(m_selector, SelectionKey.OP_CONNECT)
                  .attach(Integer.valueOf(channelID));

            /// ����Server�˵�ַ
            client.connect(new InetSocketAddress(remoteIP, remotePort));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /// ���һ��UDP����
    @Override
    public void addUDP(int channelID, boolean needBind, int localPort, boolean needBoardcast)
    {
        try
        {
            /// ��ʼ��ѡ����
            if (m_selector == null) m_selector = Selector.open();

            /// ��Socketͨ��
            DatagramChannel channel = DatagramChannel.open();
            m_channels.put(channelID, channel);

            /// ����Ϊ������ģʽ
            channel.configureBlocking(false);

            /// ���ذ󶨶˿�
            if (needBind)
            {
                channel.socket().bind(new InetSocketAddress(localPort));
            }

            /// ���ù㲥����
            if (needBoardcast)
            {
                channel.socket().setBroadcast(true);
            }

            /// ��channelע��OP_READ�¼�������Ӹ���
            channel.register(m_selector, SelectionKey.OP_READ)
                   .attach(new LanAppChannel(LanAppChannel.UDP, channelID, this));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /// ����Ӧ�÷���
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


    /// ֹͣӦ�÷���
    @Override
    public void stop()
    {
        synchronized(m_exit) {m_exit = true;}
    }


    /// ��Զ��ID������Ϣ��(TCP)
    @Override
    public int send(int remoteID, MsgPacket msg)
    {
        if (msg == null) return 0;

        /// ͨ��Զ��ID��ȡͨ��
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


    /// ͨ��ͨ��ID������Ϣ��(�Ƽ�TCPʹ��)
    @Override
    public int send(MsgPacket msg, int channelID)
    {
        if (msg == null) return 0;

        /// ͨ��ͨ��ID��ȡͨ��
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


    /// ����Ψһͨ��ʱ������Ϣ��(�Ƽ�TCPʹ��)
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


    /// ͨ��ͨ��ID��ָ����ַ������Ϣ��(UDP)
    @Override
    public int send(String remoteIP, int remotePort, MsgPacket msg, int channelID)
    {
        if (msg == null) return 0;

        /// ͨ��ͨ��ID��ȡͨ��
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


    /// ����Ψһͨ��ʱ��ָ����ַ������Ϣ��(UDP)
    @Override
    public int send(String remoteIP, int remotePort, MsgPacket msg)
    {
        if (msg == null) return 0;

        /// ͨ��ͨ��ID��ȡͨ��
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


    /// �߳����
    @Override
    public void run()
    {
        try
        {
            while (!exit())
            {
                /// ÿ������m_blockTimeʱ��
                m_selector.select(m_blockTime);

                /// ѭ���鿴�¼�
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


    /// �����¼�
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


    /// �յ�����
    private void accept(SelectionKey key) throws IOException
    {
        /// ��������
        ServerSocketChannel server = (ServerSocketChannel)key.channel();
        SocketChannel accept = server.accept();

        /// ��ȡ��������ͨ��ID
        int serverChannelID = 0;
        Integer serverChannelInt = (Integer)key.attachment();
        if (serverChannelInt != null) serverChannelID = serverChannelInt.intValue();

        /// ֪ͨ�¼�������
        if (m_recver != null)
        {
            if (!m_recver.onAccept(serverChannelID, server, accept))
            {
                /// �ܾ�����
                accept.close();
                return;
            }
        }

        ///���÷�����ģʽ
        accept.configureBlocking(false);

        /// ������ӵĿͻ��׽��ּ��뵽'������ͨ��'
        m_channels.put(serverChannelID, accept);

        /// ע�ᵽѡ����������Ӹ���
        accept.register(m_selector, SelectionKey.OP_READ)
              .attach(new LanAppChannel(LanAppChannel.TCPACCEPT, serverChannelID, this));
    }


    /// ��������
    private void connect(SelectionKey key) throws IOException
    {
        /// �������
        SocketChannel client = (SocketChannel)key.channel();
        if (client.isConnectionPending())
        {
            if (!client.finishConnect())
            {
                /// �������ʧ��
                return;
            }
        }

        /// ��ȡ�ͻ��˵�ͨ��ID
        int clientChannelID = 0;
        Integer clientChannelInt = (Integer)key.attachment();
        if (clientChannelInt != null) clientChannelID = clientChannelInt.intValue();

        /// ֪ͨ�¼�������
        if (m_recver != null)
        {
            if (!m_recver.onConnect(clientChannelID, client))
            {
                return;
            }
        }

        /// ������������������ӵ��׽��ּ��뵽'�ͻ���ͨ��'
        m_channels.put(clientChannelID, client);

        /// ע�ᵽѡ����������Ӹ���
        client.register(m_selector, SelectionKey.OP_READ)
              .attach(new LanAppChannel(LanAppChannel.TCPCLIENT, clientChannelID, this));
    }


    /// ��������
    private void recv(SelectionKey key) throws IOException
    {
        /// ��������
        LanAppChannel channel = (LanAppChannel)key.attachment();
        if (channel != null)
        {
            channel.recv(key);
        }
    }


    /// �Ƿ��˳�
    private boolean exit()
    {
        boolean exit = false;
        synchronized(m_exit) {exit = m_exit;}

        return exit;
    }


    /// ͨ����Զ�̼��ϵ�MAP
    private HashMap<Integer, Channel> m_channels;
    private HashMap<Integer, Channel> m_remotes;


    /// ������ʼ����
    {
        m_channels = new HashMap<Integer, Channel>();
        m_remotes = new HashMap<Integer, Channel>();
    }

}

