package com.dcop.jx.entry.kernel;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;


/**
 * 对象管理器接口
 */
public interface IManager extends IObject {

    /**
     * 添加一个对象
     * @param IObject obj 对象
     */
    public void addObject(IObject obj);


    /**
     * 删除一个对象
     * @param int objID 对象ID
     */
    public void delObject(int objId);


    /**
     * 新建一个对象
     * @param int objID 对象ID
     * @param String objName 对象名
     * @param String objCfg 对象配置
     * @return IObject 对象实例
     */
    public IObject newObject(int objId, String objName, String objCfg);


    /**
     * 获取一个对象
     * @param int objId 对象ID
     * @param String objName 对象名称
     * @return IObject 对象实例
     */
    public IObject getObject(int objId);
    public IObject getObject(String objName);

}

