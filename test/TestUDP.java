
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import JX.*;
import JX.Framework.*;
import JX.Framework.Service.Msg.*;
import JX.Framework.Service.Sock.*;


/**
 * ���������
 */
public class TestUDP implements IObject, ILanAppListener
{
    private static final String m_Name = "UDP";
    private IObject m_iParent = null;
    private IObject m_iRoot = null;
    private int m_ID = 0;

    private ILanApp m_lanApp = null;
    private int m_localPort = 0;
    private String m_remoteIP = null;
    private int m_remotePort = 0;


    /// �������
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

                if (cfgItems[0].equals("RemoteIP"))
                {
                    m_remoteIP = cfgItems[1];
                    System.out.println("  RemoteIP=" + m_remoteIP);
                }

                if (cfgItems[0].equals("RemotePort"))
                {
                    m_remotePort = Integer.parseInt(cfgItems[1]);
                    System.out.println("  RemotePort=" + m_remotePort);
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

        m_lanApp = (ILanApp)ModuleList.factory.newInstance("LanApp");
        if (m_lanApp == null)
        {
            System.out.println("'" + name() + "'(" + id() + ") Init Fail! LanApp Create Fail!");
            return Errno.FAILURE;
        }

        m_lanApp.addUDP(1, true, m_localPort, true);
        m_lanApp.start(id(), 1024, 1000, this);

        System.out.println("'" + name() + "'(" + id() + ") Init!");
        return Errno.SUCCESS;
    }


    /// ����ʱ���
    @Override
    public void fini()
    {
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
        System.out.println("'" + name() + "'(" + id() + ") Proc Msg!");
        if (m_lanApp != null)
        {
            for (int i = 0; i < 10; ++i)
            {
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                String message = "UDP Send No:" + (i + 1);
                m_lanApp.send(m_remoteIP, m_remotePort, new MsgPacket().addData(ByteBuffer.wrap(message.getBytes())), 0);
            }
        }
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
        return false;
    }


    /// TCP�ͻ��������Ϸ�����
    @Override
    public boolean onConnect(int channelID, SocketChannel client)
    {
        return false;
    }


    /// TCP�����ж�
    @Override
    public void onDisconnect(int channelID, SocketChannel channel)
    {
    }


    /// TCP�յ�����
    @Override
    public void onRecv(int channelID, SocketChannel channel, MsgPacket msg)
    {
    }


    /// UDP�յ�����
    @Override
    public void onRecv(int channelID, DatagramChannel channel, SocketAddress remote, MsgPacket msg)
    {
        InetSocketAddress remoteAddress = (InetSocketAddress)remote;
        System.out.println("UDP�յ�����:'" + new String(msg.data.array()) + "' From(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");
    }

}

