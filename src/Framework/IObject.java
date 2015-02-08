package JX.Framework;

import JX.*;


/**
 * ����ӿ�
 */
public interface IObject
{
    /**
     * �������(��������ʱ����Ҫ����)
     * @param IObject parent ������
     * @param String cfg ���ò���
     */
    public void construct(int id, IObject parent, String cfg);


    /**
     * ������
     * @return String ������
     */
    public String name();


    /**
     * ����ID
     * @return int ����ID
     */
    public int id();


    /**
     * ������
     * @return IObject ������
     */
    public IObject parent();


    /**
     * ������
     * @return IObject ������
     */
    public IObject root();


    /**
     * ��ʼ�����(�ɶ��������ͳһ����)
     * @param IObject root ������
     * @param Object[] arg ����ʱ����
     * @return Errno ������
     */
    public Errno init(IObject root, Object[] arg);


    /**
     * ����ʱ���(�ɶ��������ͳһ����)
     * @param IObject root ������
     */
    public void fini();


    /**
     * ��Ϣ���
     * @param Object msg ��Ϣ����
     */
    public void proc(Object msg);


    /**
     * Dump���
     * @return String �����Ϣ
     */
    public String dump();

}

