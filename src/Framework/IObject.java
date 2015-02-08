package JX.Framework;

import JX.*;


/**
 * 对象接口
 */
public interface IObject
{
    /**
     * 构建入口(创建对象时就需要调用)
     * @param IObject parent 父对象
     * @param String cfg 配置参数
     */
    public void construct(int id, IObject parent, String cfg);


    /**
     * 对象名
     * @return String 对象名
     */
    public String name();


    /**
     * 对象ID
     * @return int 对象ID
     */
    public int id();


    /**
     * 父对象
     * @return IObject 父对象
     */
    public IObject parent();


    /**
     * 根对象
     * @return IObject 根对象
     */
    public IObject root();


    /**
     * 初始化入口(由对象管理器统一调用)
     * @param IObject root 根对象
     * @param Object[] arg 运行时参数
     * @return Errno 错误码
     */
    public Errno init(IObject root, Object[] arg);


    /**
     * 结束时入口(由对象管理器统一调用)
     * @param IObject root 根对象
     */
    public void fini();


    /**
     * 消息入口
     * @param Object msg 消息对象
     */
    public void proc(Object msg);


    /**
     * Dump入口
     * @return String 输出信息
     */
    public String dump();

}

