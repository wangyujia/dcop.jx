package JX.Framework;


/**
 * 类工厂接口
 */
public interface IFactory
{
    /**
     * 添加一个类名
     * @param String objName 对象名
     * @param String className 实现类类名
     */
    public void addClass(String objName, String className);


    /**
     * 添加一个类名
     * @param String objName 对象名
     * @return Object 对象实例
     */
    public Object newInstance(String objName);


    /**
     * Dump入口
     * @param String objName 对象名
     * @return Object 对象实例
     */
    public String dump();

}

