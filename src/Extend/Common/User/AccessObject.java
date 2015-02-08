package JX.Extend.Common.User;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import JX.*;
import JX.Framework.*;
import JX.Framework.Object.*;
import JX.Framework.Service.Msg.*;
import JX.Framework.Service.Sock.*;


/**
 * 接入对象
 */
public class AccessObject implements IAccess, IObject, ILanAppListener
{
    /// 对象本身属性
    private static final String m_Name = "Access";
    private IObject m_iParent = null;
    private IObject m_iRoot = null;
    private int m_ID = 0;

    /// 网络应用
    private ILanApp m_lanApp = null;
    private String[] m_remoteIPs = null;
    private int[] m_remotePorts = null;
    private int m_remoteCount = 0;
    private String m_serverIP = null;
    private int m_serverPort = 0;

    /// 会话
    private IObject m_session = null;
    private int m_loginUser = 0;


    /// 构建入口
    @Override
    public void construct(int id, IObject parent, String cfg)
    {
        m_ID = id;
        m_iParent = parent;

        /// 获取服务器的配置参数
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

        /// 获取管理器
        IManager manager = (IManager)root();
        if (manager == null)
        {
            System.out.println("'" + name() + "'(" + id() + ") Init Fail(Manager Is Null)!");
            return Errno.FAILURE;
        }

        /// 获取会话对象
        m_session = manager.getObject(Resource.Object.Session);
        if (m_session == null)
        {
            System.out.println("'" + name() + "'(" + id() + ") Init Fail(Session Is Null)!");
            return Errno.FAILURE;
        }

        /// 创建网络应用实例
        m_lanApp = (ILanApp)ClassFactory.getInstance().newInstance("LanApp");
        if (m_lanApp == null)
        {
            System.out.println("'" + name() + "'(" + id() + ") Init Fail(LanApp Create Fail)!");
            return Errno.FAILURE;
        }

        /// 添加TCP和UDP服务，并启动服务
        for (int i = 0; i < m_remoteCount; ++i)
        {
            m_lanApp.addTCPClient((i+1), m_remoteIPs[i], m_remotePorts[i]);
        }
        m_lanApp.addUDP(m_remoteCount+1, false, 0, false);
        m_lanApp.start(id(), 1024, 1000, this);

        System.out.println("'" + name() + "'(" + id() + ") Init OK!");
        return Errno.SUCCESS;
    }


    /// 结束时入口
    @Override
    public void fini()
    {
        /// 停止网络应用实例
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
        if (m_lanApp == null) return;
        if (m_serverIP == null) return;

        MsgPacket msgPack = (MsgPacket)msg;
        if (msgPack == null) return;

        /// 收到内部的消息，从UDP通道(最后一个通道)转发出去
        m_lanApp.send(m_serverIP, m_serverPort, msgPack, m_remoteCount+1);
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
        /// 无效的Server端
        return false;
    }


    /// TCP客户端连接上服务器
    @Override
    public boolean onConnect(int channelID, SocketChannel client)
    {
        if (m_serverIP == null)
        {
            InetSocketAddress remoteAddress = (InetSocketAddress)client.socket().getRemoteSocketAddress();
            m_serverIP = new String(remoteAddress.getAddress().getHostAddress());
            m_serverPort = remoteAddress.getPort();

            System.out.println("客户端成功连接服务器(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");
            
        }

        /// 临时的Client，只用于找到第一个能连接上的Server端
        return false;
    }


    /// TCP连接中断
    @Override
    public void onDisconnect(int channelID, SocketChannel channel)
    {
        /// 无TCP连接
    }


    /// TCP收到数据
    @Override
    public void onRecv(int channelID, SocketChannel channel, MsgPacket msg)
    {
        /// 无TCP数据
    }


    /// UDP收到数据
    @Override
    public void onRecv(int channelID, DatagramChannel channel, SocketAddress remote, MsgPacket msg)
    {
        if (m_session == null)
        {
            return;
        }

        /// 所有受到的UDP数据都传给会话对象
        m_session.proc((Object)msg);
    }


    /// 系统登录
    @Override
    public void login(String username, String password)
    {
        if (m_lanApp == null) return;
        if (m_serverIP == null) return;
    
        MsgParaNode usernamePara = new MsgParaNode();
        usernamePara.paraID = 1;
        usernamePara.opCode = MsgParaNode.OpCodeNone;
        usernamePara.paraType = MsgParaNode.ParaTypeString;
        usernamePara.paraSize = 32;

        ByteBuffer usernameData = ByteBuffer.allocate(usernamePara.paraSize);
        usernameData.put(username.getBytes());

        MsgParaNode passwordPara = new MsgParaNode();
        passwordPara.paraID = 2;
        passwordPara.opCode = MsgParaNode.OpCodeNone;
        passwordPara.paraType = MsgParaNode.ParaTypeString;
        passwordPara.paraSize = 32;

        ByteBuffer passwordData = ByteBuffer.allocate(passwordPara.paraSize);
        passwordData.put(password.getBytes());

        MsgRequest request = new MsgRequest();
        request.paraCount = 2;
        request.paraLen = (short)(usernamePara.paraSize + passwordPara.paraSize);
        request.valueLen = (short)(request.paraCount * MsgParaNode.HeaderSize + request.paraLen);

        MsgSession session = new MsgSession();
        session.session = 0;
        session.user = 0;
        session.tty = Resource.TTY.Access;
        session.attribute = Resource.Attribute.AccessLogin;
        session.index = 0;
        session.ctrl = MsgSession.CtrlMethod;
        session.ack = MsgSession.TypeRequest;
        session.valueLen = (short)(request.headSize + request.valueLen);

        MsgPacket msg = new MsgPacket();
        msg.addData(session.pack());
        msg.addData(request.pack());
        msg.addData(usernamePara.pack());
        msg.addData(passwordPara.pack());
        msg.addData(usernameData);
        msg.addData(passwordData);

        int rc = m_lanApp.send(m_serverIP, m_serverPort, msg, m_remoteCount+1);
        System.out.println("send login msg rc:" + rc);
    }


    /// 退出登录
    public void logout()
    {
        if (m_lanApp == null) return;
        if (m_serverIP == null) return;
        if (m_loginUser == 0) return;

        MsgRequest request = new MsgRequest();
        request.paraCount = 0;
        request.paraLen = 0;
        request.valueLen = 0;

        MsgSession session = new MsgSession();
        session.session = 0;
        session.user = m_loginUser;
        session.tty = Resource.TTY.Access;
        session.attribute = Resource.Attribute.AccessLogout;
        session.index = 0;
        session.ctrl = MsgSession.CtrlMethod;
        session.ack = MsgSession.TypeRequest;
        session.valueLen = (short)(request.headSize + request.valueLen);

        MsgPacket msg = new MsgPacket();
        msg.addData(session.pack());
        msg.addData(request.pack());

        int rc = m_lanApp.send(m_serverIP, m_serverPort, msg, m_remoteCount+1);
        System.out.println("send logout msg rc:" + rc);
    }

}

