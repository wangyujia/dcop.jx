package JX.Framework;


/**
 * ����������ӿ�
 */
public interface IManager extends IObject
{
    /**
     * ���һ������
     * @param IObject obj ����
     */
    public void addObject(IObject obj);


    /**
     * ɾ��һ������
     * @param int objID ����ID
     */
    public void delObject(int objID);


    /**
     * �½�һ������
     * @param int objID ����ID
     * @param String objName ������
     * @param String objCfg ��������
     * @return IObject ����ʵ��
     */
    public IObject newObject(int objID, String objName, String objCfg);


    /**
     * ��ȡһ������
     * @param int objID ����ID
     * @return IObject ����ʵ��
     */
    public IObject getObject(int objID);

}

