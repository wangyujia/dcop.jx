
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
public class TestServer implements IObject, ILanAppListener
{
    private static final String m_Name = "Server";
    private IObject m_iParent = null;
    private IObject m_iRoot = null;
    private int m_ID = 0;

    private ILanApp m_lanApp = null;
    private int m_localPort = 0;


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
                if (cfgItems[0].equals("LocalPort"))
                {
                    m_localPort = Integer.parseInt(cfgItems[1]);
                    System.out.println("  LocalPort=" + m_localPort);
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

        m_lanApp.addTCPServer(1, m_localPort);
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
        InetSocketAddress remoteAddress = (InetSocketAddress)accept.socket().getRemoteSocketAddress();
        System.out.println("服务器收到客户端连接(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");

        String message = "This Hello From Server(" + id() + ")";
        try
        {
            accept.write(new MsgPacket().addData(ByteBuffer.wrap(message.getBytes())).pack());
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

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
        InetSocketAddress remoteAddress = (InetSocketAddress)channel.socket().getRemoteSocketAddress();
        System.out.println("服务器和客户端连接中断(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");
    }


    /// TCP收到数据
    @Override
    public void onRecv(int channelID, SocketChannel channel, MsgPacket msg)
    {
        InetSocketAddress remoteAddress = (InetSocketAddress)channel.socket().getRemoteSocketAddress();
        System.out.println("服务器收到数据:'" + new String(msg.data.array()) + "' From(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");
    }


    /// UDP收到数据
    @Override
    public void onRecv(int channelID, DatagramChannel channel, SocketAddress remote, MsgPacket msg)
    {
    }

}

