package JX;


/**
 * ��Դ����
 */
public final class Resource
{
    public static final class Object
    {
        public static final int Session = 20;                                           // �Ự����
        public static final int Access = 22;                                            // �ֲ�ʽ����
    }

    public static final class Attribute
    {
        public static final int AccessLogin = ((Object.Access << (16)) | 1);            // �����½
        public static final int AccessLogout = ((Object.Access << (16)) | 2);           // �˳���½
    }

    public static final class TTY
    {
        public static int Access = 1;                                                   // ACCESS��ʽ����
    }

    
}

