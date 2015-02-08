package JX.Framework.Service.Sock;

import java.net.*;
import java.nio.channels.*;
import JX.*;
import JX.Framework.*;
import JX.Framework.Service.Msg.*;


/**
 * ����Ӧ���¼�
 */
public interface ILanAppListener
{
    /**
     * TCP�������յ��ͻ�������
     * @param int channelID ͨ��ID
     * @param ServerSocketChannel server �׽���
     * @param SocketChannel accept �׽���
     * @return boolean �Ƿ��������
     */
    public boolean onAccept(int channelID, ServerSocketChannel server, SocketChannel accept);


    /**
     * TCP�ͻ��������Ϸ�����
     * @param int channelID ͨ��ID
     * @param SocketChannel client �׽���
     * @return boolean �Ƿ��������
     */
    public boolean onConnect(int channelID, SocketChannel client);


    /**
     * TCP�����ж�
     * @param int channelID ͨ��ID
     * @param SocketChannel channel �׽���
     */
    public void onDisconnect(int channelID, SocketChannel channel);


    /**
     * TCP�յ�����
     * @param int channelID ͨ��ID
     * @param SocketChannel channel �׽���
     * @param MsgPacket msg ��Ϣ��
     */
    public void onRecv(int channelID, SocketChannel channel, MsgPacket msg);


    /**
     * UDP�յ�����
     * @param int channelID ͨ��ID
     * @param DatagramChannel channel �׽���
     * @param SocketAddress remote Զ�˵�ַ
     * @param MsgPacket msg ��Ϣ��
     */
    public void onRecv(int channelID, DatagramChannel channel, SocketAddress remote, MsgPacket msg);

}

