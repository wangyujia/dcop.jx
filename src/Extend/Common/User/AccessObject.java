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
 * �������
 */
public class AccessObject implements IAccess, IObject, ILanAppListener
{
    /// ����������
    private static final String m_Name = "Access";
    private IObject m_iParent = null;
    private IObject m_iRoot = null;
    private int m_ID = 0;

    /// ����Ӧ��
    private ILanApp m_lanApp = null;
    private String[] m_remoteIPs = null;
    private int[] m_remotePorts = null;
    private int m_remoteCount = 0;
    private String m_serverIP = null;
    private int m_serverPort = 0;

    /// �Ự
    private IObject m_session = null;
    private int m_loginUser = 0;


    /// �������
    @Override
    public void construct(int id, IObject parent, String cfg)
    {
        m_ID = id;
        m_iParent = parent;

        /// ��ȡ�����������ò���
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


    /// ������
    @Override
    public String name()
    {
        return m_Name;
    }


    /// ����ID
    @Override
    public int id()
    {
        return m_ID;
    }


    /// ������
    @Override
    public IObject parent()
    {
        return m_iParent;
    }


    /// ������
    public IObject root()
    {
        return m_iRoot;
    }


    /// ��ʼ�����
    @Override
    public Errno init(IObject root, Object[] arg)
    {
        m_iRoot = root;

        /// ��ȡ������
        IManager manager = (IManager)root();
        if (manager == null)
        {
            System.out.println("'" + name() + "'(" + id() + ") Init Fail(Manager Is Null)!");
            return Errno.FAILURE;
        }

        /// ��ȡ�Ự����
        m_session = manager.getObject(Resource.Object.Session);
        if (m_session == null)
        {
            System.out.println("'" + name() + "'(" + id() + ") Init Fail(Session Is Null)!");
            return Errno.FAILURE;
        }

        /// ��������Ӧ��ʵ��
        m_lanApp = (ILanApp)ClassFactory.getInstance().newInstance("LanApp");
        if (m_lanApp == null)
        {
            System.out.println("'" + name() + "'(" + id() + ") Init Fail(LanApp Create Fail)!");
            return Errno.FAILURE;
        }

        /// ���TCP��UDP���񣬲���������
        for (int i = 0; i < m_remoteCount; ++i)
        {
            m_lanApp.addTCPClient((i+1), m_remoteIPs[i], m_remotePorts[i]);
        }
        m_lanApp.addUDP(m_remoteCount+1, false, 0, false);
        m_lanApp.start(id(), 1024, 1000, this);

        System.out.println("'" + name() + "'(" + id() + ") Init OK!");
        return Errno.SUCCESS;
    }


    /// ����ʱ���
    @Override
    public void fini()
    {
        /// ֹͣ����Ӧ��ʵ��
        if (m_lanApp != null)
        {
            m_lanApp.stop();
        }

        System.out.println("'" + name() + "'(" + id() + ") Fini!");
    }


    /// ��Ϣ���
    @Override
    public void proc(Object msg)
    {
        if (m_lanApp == null) return;
        if (m_serverIP == null) return;

        MsgPacket msgPack = (MsgPacket)msg;
        if (msgPack == null) return;

        /// �յ��ڲ�����Ϣ����UDPͨ��(���һ��ͨ��)ת����ȥ
        m_lanApp.send(m_serverIP, m_serverPort, msgPack, m_remoteCount+1);
    }


    /// Dump���
    @Override
    public String dump()
    {
        String out = name() + " Dump: \n";
        out += String.format("  My ID is:%d \n", id());
        return out;
    }


    /// TCP�������յ��ͻ�������
    @Override
    public boolean onAccept(int channelID, ServerSocketChannel server, SocketChannel accept)
    {
        /// ��Ч��Server��
        return false;
    }


    /// TCP�ͻ��������Ϸ�����
    @Override
    public boolean onConnect(int channelID, SocketChannel client)
    {
        if (m_serverIP == null)
        {
            InetSocketAddress remoteAddress = (InetSocketAddress)client.socket().getRemoteSocketAddress();
            m_serverIP = new String(remoteAddress.getAddress().getHostAddress());
            m_serverPort = remoteAddress.getPort();

            System.out.println("�ͻ��˳ɹ����ӷ�����(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");
            
        }

        /// ��ʱ��Client��ֻ�����ҵ���һ���������ϵ�Server��
        return false;
    }


    /// TCP�����ж�
    @Override
    public void onDisconnect(int channelID, SocketChannel channel)
    {
        /// ��TCP����
    }


    /// TCP�յ�����
    @Override
    public void onRecv(int channelID, SocketChannel channel, MsgPacket msg)
    {
        /// ��TCP����
    }


    /// UDP�յ�����
    @Override
    public void onRecv(int channelID, DatagramChannel channel, SocketAddress remote, MsgPacket msg)
    {
        if (m_session == null)
        {
            return;
        }

        /// �����ܵ���UDP���ݶ������Ự����
        m_session.proc((Object)msg);
    }


    /// ϵͳ��¼
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


    /// �˳���¼
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

