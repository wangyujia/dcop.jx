package JX.Framework;


/**
 * �๤���ӿ�
 */
public interface IFactory
{
    /**
     * ���һ������
     * @param String objName ������
     * @param String className ʵ��������
     */
    public void addClass(String objName, String className);


    /**
     * ���һ������
     * @param String objName ������
     * @return Object ����ʵ��
     */
    public Object newInstance(String objName);


    /**
     * Dump���
     * @param String objName ������
     * @return Object ����ʵ��
     */
    public String dump();

}

