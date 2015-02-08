package JX.Framework.Service.Sock;

import java.nio.channels.*;
import JX.*;
import JX.Framework.*;
import JX.Framework.Service.Msg.*;


/**
 * ����Ӧ�ýӿ�
 */
public interface ILanApp
{
    /**
     * ��ȡ����ID
     * @return int ����ID
     */
    public int getLocalID();


    /**
     * ��ȡMTU
     * @return int MTU��С
     */
    public int getMTU();


    /**
     * ��ȡ�¼�������
     * @return ILanAppListener �¼�������
     */
    public ILanAppListener getListener();


    /**
     * ��ȡͨ��
     * @param int channelID ͨ��ID
     * @return Channel ͨ��
     */
    public Channel getChannel(int channelID);


    /**
     * ���Զ��ͨ��
     * @param int remoteID Զ��ID
     * @param Channel remoteChannel Զ��ͨ��
     */
    public void addRemote(int remoteID, Channel remoteChannel);


    /**
     * ɾ��Զ��ͨ��
     * @param int remoteID Զ��ID
     */
    public void delRemote(int remoteID);


    /**
     * ���һ��TCPServer����
     * @param int channelID ͨ��ID
     * @param int localPort ���ض˿�
     */
    public void addTCPServer(int channelID, int localPort);


    /**
     * ���һ��TCPClient����
     * @param int channelID ͨ��ID
     * @param String remoteIP Զ��IP
     * @param int remotePort Զ�˶˿�
     */
    public void addTCPClient(int channelID, String remoteIP, int remotePort);


    /**
     * ���һ��UDP����
     * @param int channelID ͨ��ID
     * @param boolean needBind �Ƿ���Ҫ��
     * @param int localPort ���ض˿�
     * @param boolean needBoardcast �Ƿ���Ҫ�㲥
     */
    public void addUDP(int channelID, boolean needBind, int localPort, boolean needBoardcast);


    /**
     * ����Ӧ�÷���
     * @param int localID ����ID
     * @param int MTU MTU��С
     * @param int blockTime ѭ������ʱ��
     * @param MsgRecver recver ��Ϣ������
     */
    public void start(int localID, int MTU, int blockTime, ILanAppListener recver);


    /**
     * ֹͣӦ�÷���
     */
    public void stop();


    /**
     * ��Զ��ID������Ϣ��(TCP)
     * @param int remoteID Զ��ID
     * @param MsgPacket msg ��Ϣ��
     * @return int ���ʹ�С
     */
    public int send(int remoteID, MsgPacket msg);


    /**
     * ͨ��ͨ��ID������Ϣ��(�Ƽ�TCPʹ��)
     * (UDP���Ҫʹ�ã����Ƚ���connectĿ�ĵ�ַ)
     * @param MsgPacket msg ��Ϣ��
     * @param int channelID ͨ��ID
     * @return int ���ʹ�С
     */
    public int send(MsgPacket msg, int channelID);


    /**
     * ����Ψһͨ��ʱ������Ϣ��(�Ƽ�TCPʹ��)
     * (UDP���Ҫʹ�ã����Ƚ���connectĿ�ĵ�ַ)
     * @param MsgPacket msg ��Ϣ��
     * @return int ���ʹ�С
     */
    public int send(MsgPacket msg);


    /**
     * ͨ��ͨ��ID��ָ����ַ������Ϣ��(UDP)
     * @param MsgPacket msg ��Ϣ��
     * @param int channelID ͨ��ID
     * @return int ���ʹ�С
     */
    public int send(String remoteIP, int remotePort, MsgPacket msg, int channelID);


    /**
     * ����Ψһͨ��ʱ��ָ����ַ������Ϣ��(UDP)
     * @param MsgPacket msg ��Ϣ��
     * @return int ���ʹ�С
     */
    public int send(String remoteIP, int remotePort, MsgPacket msg);

}

