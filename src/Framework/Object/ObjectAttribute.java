package JX.Framework.Object;

import JX.*;
import JX.Framework.*;
import JX.Framework.Service.Msg.*;


/**
 * 对象属性
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
     * 设置属性ID和所有者对象
     * @param int attrID 属性ID
     * @param IObject owner 对象所有者
     * @return ObjectAttribute 对象属性
     */
    public ObjectAttribute set(int attrID, IObject owner)
    {
        m_attrID = attrID;
        m_owner = owner;

        return this;
    }


    /**
     * 获取属性ID
     * @return boolean 是否本属性消息
     */
    public int getAttrID()
    {
        return m_attrID;
    }


    /**
     * 处理消息
     * @param MsgPacket msg 原始消息包
     * @param MsgSession session 从消息包中解析的会话消息头
     */
    public void action(MsgPacket msg, MsgSession session)
    {
        
    }
    
}

