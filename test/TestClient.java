
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import JX.*;
import JX.Framework.*;
import JX.Framework.Service.Msg.*;
import JX.Framework.Service.Sock.*;


/**
 * 对象测试类
 */
public class TestClient implements IObject, ILanAppListener
{
    private static final String m_Name = "Client";
    private IObject m_iParent = null;
    private IObject m_iRoot = null;
    private int m_ID = 0;

    private ILanApp m_lanApp = null;
    private String[] m_remoteIPs = null;
    private int[] m_remotePorts = null;
    private int m_remoteCount = 0;
    private String m_serverIP = null;
    private int m_serverPort = 0;

    /// 构建入口
    @Override
    public void construct(int id, IObject parent, String cfg)
    {
        m_ID = id;
        m_iParent = parent;

        System.out.println("'" + name() + "'(" + id() + ") Construct!");
        String[] ss = cfg.split(";");
        for (String s : ss)
        {
            String[] cfgItems = s.split("=");
            if (cfgItems.length >= 2)
            {
                if (cfgItems[0].equals("RemoteCount"))
                {
                    m_remoteCount = Integer.parseInt(cfgItems[1]);
                    System.out.println("  RemoteCount=" + m_remoteCount);
                    m_remoteIPs = new String[m_remoteCount];
                    m_remotePorts = new int[m_remoteCount];
                }

                for (int i = 0; i < m_remoteCount; ++i)
                {
                    if (cfgItems[0].equals("RemoteIP" + (i+1)))
                    {
                        m_remoteIPs[i] = cfgItems[1];
                        System.out.println("  RemoteIP" + (i+1) + "=" + m_remoteIPs[i]);
                    }

                    if (cfgItems[0].equals("RemotePort" + (i+1)))
                    {
                        m_remotePorts[i] = Integer.parseInt(cfgItems[1]);
                        System.out.println("  RemotePort" + (i+1) + "=" + m_remotePorts[i]);
                    }
                }
            }
        }
    }


    /// 对象名
    @Override
    public String name()
    {
        return m_Name;
    }


    /// 对象ID
    @Override
    public int id()
    {
        return m_ID;
    }


    /// 父对象
    @Override
    public IObject parent()
    {
        return m_iParent;
    }


    /// 根对象
    public IObject root()
    {
        return m_iRoot;
    }


    /// 初始化入口
    @Override
    public Errno init(IObject root, Object[] arg)
    {
        m_iRoot = root;

        m_lanApp = (ILanApp)ModuleList.factory.newInstance("LanApp");
        if (m_lanApp == null)
        {
            System.out.println("'" + name() + "'(" + id() + ") Init Fail! LanApp Create Fail!");
            return Errno.FAILURE;
        }

        for (int i = 0; i < m_remoteCount; ++i)
        {
            m_lanApp.addTCPClient((i+1), m_remoteIPs[i], m_remotePorts[i]);
        }
        m_lanApp.addUDP(m_remoteCount+1, false, 0, false);
        m_lanApp.start(id(), 1024, 1000, this);

        System.out.println("'" + name() + "'(" + id() + ") Init!");
        return Errno.SUCCESS;
    }


    /// 结束时入口
    @Override
    public void fini()
    {
        if (m_lanApp != null)
        {
            m_lanApp.stop();
        }
        System.out.println("'" + name() + "'(" + id() + ") Fini!");
    }


    /// 消息入口
    @Override
    public void proc(Object msg)
    {
        System.out.println("'" + name() + "'(" + id() + ") Proc Msg!");
        
        if (m_lanApp == null) return;
        if (m_serverIP == null) return;

        /// TCP发送
        for (int i = 0; i < 10; ++i)
        {
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            String message = "Client Send No:" + (i + 1);
            m_lanApp.send(new MsgPacket().addData(ByteBuffer.wrap(message.getBytes())), 1);
        }

        /// UDP发送
        for (int i = 0; i < 10; ++i)
        {
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            String message = "UDP Send No:" + (i + 1);
            m_lanApp.send(m_serverIP, m_serverPort, 
                        new MsgPacket().addData(ByteBuffer.wrap(message.getBytes())), 
                        m_remoteCount+1);
        }
    }


    /// Dump入口
    @Override
    public String dump()
    {
        String out = name() + " Dump: \n";
        out += String.format("  My ID is:%d \n", id());
        return out;
    }


    /// TCP服务器收到客户端连接
    @Override
    public boolean onAccept(int channelID, ServerSocketChannel server, SocketChannel accept)
    {
        return false;
    }


    /// TCP客户端连接上服务器
    @Override
    public boolean onConnect(int channelID, SocketChannel client)
    {
        return false;
    }


    /// TCP连接中断
    @Override
    public void onDisconnect(int channelID, SocketChannel channel)
    {
    }


    /// TCP收到数据
    @Override
    public void onRecv(int channelID, SocketChannel channel, MsgPacket msg)
    {
        InetSocketAddress remoteAddress = (InetSocketAddress)channel.socket().getRemoteSocketAddress();
        System.out.println("客户端收到数据:'" + new String(msg.data.array()) + "' From(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");
    }


    /// UDP收到数据
    @Override
    public void onRecv(int channelID, DatagramChannel channel, SocketAddress remote, MsgPacket msg)
    {
        InetSocketAddress remoteAddress = (InetSocketAddress)remote;
        System.out.println("UDP收到数据:'" + new String(msg.data.array()) + "' From(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");
    }

}

