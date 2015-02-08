package JX.Framework.Object;

import JX.*;
import JX.Framework.*;
import JX.Framework.Service.Msg.*;


/**
 * ��������
 */
public class ObjectAttribute
{
    public static int TypeData = 0;
    public static int TypeMethod = 0;
    public static int TypeEvent = 0;

    private int m_attrID;
    private IObject m_owner;


    public ObjectAttribute()
    {
        m_attrID = 0;
        m_owner = null;
    }


    /**
     * ��������ID�������߶���
     * @param int attrID ����ID
     * @param IObject owner ����������
     * @return ObjectAttribute ��������
     */
    public ObjectAttribute set(int attrID, IObject owner)
    {
        m_attrID = attrID;
        m_owner = owner;

        return this;
    }


    /**
     * ��ȡ����ID
     * @return boolean �Ƿ�������Ϣ
     */
    public int getAttrID()
    {
        return m_attrID;
    }


    /**
     * ������Ϣ
     * @param MsgPacket msg ԭʼ��Ϣ��
     * @param MsgSession session ����Ϣ���н����ĻỰ��Ϣͷ
     */
    public void action(MsgPacket msg, MsgSession session)
    {
        
    }
    
}

