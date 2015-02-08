package JX.Framework;


/**
 * 对象管理器接口
 */
public interface IManager extends IObject
{
    /**
     * 添加一个对象
     * @param IObject obj 对象
     */
    public void addObject(IObject obj);


    /**
     * 删除一个对象
     * @param int objID 对象ID
     */
    public void delObject(int objID);


    /**
     * 新建一个对象
     * @param int objID 对象ID
     * @param String objName 对象名
     * @param String objCfg 对象配置
     * @return IObject 对象实例
     */
    public IObject newObject(int objID, String objName, String objCfg);


    /**
     * 获取一个对象
     * @param int objID 对象ID
     * @return IObject 对象实例
     */
    public IObject getObject(int objID);

}

