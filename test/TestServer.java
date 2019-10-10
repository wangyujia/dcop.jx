package com.dcop.jx.test;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;
import com.dcop.jx.core.base.msg.*;
import com.dcop.jx.core.base.log.*;
import com.dcop.jx.core.*;


/**
 * 对象测试类
 */
@ClassImport("test_server")
public class TestServer implements IObject, ILanEvent {

    private static final String m_name = "TestServer";
    private IObject m_parent = null;
    private IObject m_root = null;
    private int m_id = 0;

    private ILanApp m_lanApp = null;
    private int m_localPort = 0;


    /// 构建入口
    @Override
    public void construct(int id, IObject parent, String cfg) {
        m_id = id;
        m_parent = parent;

        Logs.record("trace", "'" + name() + "'(" + id() + ") Construct!");
        String[] ss = cfg.split(";");
        for (String s : ss) {
            String[] cfgItems = s.split("=");
            if (cfgItems.length >= 2) {
                if (cfgItems[0].equals("LocalPort")) {
                    m_localPort = Integer.parseInt(cfgItems[1]);
                    Logs.record("trace", "  LocalPort=" + m_localPort);
                }
            }
        }
    }


    /// 对象名
    @Override
    public String name() {
        return m_name;
    }


    /// 对象ID
    @Override
    public int id() {
        return m_id;
    }


    /// 父对象
    @Override
    public IObject parent() {
        return m_parent;
    }


    /// 根对象
    public IObject root() {
        return m_root;
    }


    /// 初始化入口
    @Override
    public ErrCode init(IObject root, Object[] arg) {
        m_root = root;

        m_lanApp = (ILanApp)ModuleList.factory.newInstance("lan");
        if (m_lanApp == null) {
            Logs.record("trace", "'" + name() + "'(" + id() + ") Init Fail! LanApp Create Fail!");
            return ErrCode.FAILURE;
        }

        m_lanApp.addTCPServer(1, m_localPort);
        m_lanApp.start("TestServer", id(), 10240, 1000, this);

        Logs.record("trace", "'" + name() + "'(" + id() + ") Init!");
        return ErrCode.SUCCESS;
    }


    /// 结束时入口
    @Override
    public void fini() {
        if (m_lanApp != null) {
            m_lanApp.stop();
            m_lanApp = null;
        }

        Logs.record("trace", "'" + name() + "'(" + id() + ") Fini!");
    }


    /// 消息入口
    @Override
    public IMsg proc(IMsg msg) {
        Logs.record("trace", "'" + name() + "'(" + id() + ") Proc Msg!");
        return null;
    }


    /// Dump入口
    @Override
    public String dump() {
        String out = name() + " Dump: \n";
        out += String.format("  My ID is:%d \n", id());
        return out;
    }


    /// TCP服务器收到客户端连接
    @Override
    public boolean onAccept(int channelID, ServerSocketChannel server, SocketChannel accept) {
        InetSocketAddress remoteAddress = (InetSocketAddress)accept.socket().getRemoteSocketAddress();
        Logs.record("trace", "服务器收到客户端连接(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");

        String message = "This Hello From Server(" + id() + ")";
        try {
            accept.write(new MsgPacket().addData(ByteBuffer.wrap(message.getBytes())).pack());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /// TCP客户端连接上服务器
    @Override
    public boolean onConnect(int channelID, SocketChannel client) {
        return false;
    }


    /// TCP连接中断
    @Override
    public void onDisconnect(int channelID, SocketChannel channel) {
        InetSocketAddress remoteAddress = (InetSocketAddress)channel.socket().getRemoteSocketAddress();
        Logs.record("trace", "服务器和客户端连接中断(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");
    }


    /// TCP收到数据
    @Override
    public void onRecv(int channelID, SocketChannel channel, IMsg msg) {
        InetSocketAddress remoteAddress = (InetSocketAddress)channel.socket().getRemoteSocketAddress();
        Logs.record("trace", "服务器收到数据:'" + new String(msg.getData().array()) + "' From(" + 
                        remoteAddress.getAddress().getHostAddress() + ":" + 
                        remoteAddress.getPort() + 
                        ") [Channel:" + channelID + "]");
    }


    /// UDP收到数据
    @Override
    public void onRecv(int channelID, DatagramChannel channel, SocketAddress remote, IMsg msg) {
    }

}

