package com.dcop.jx.extend.jjs;

import java.lang.*;
import java.util.*;
import javax.script.*;
import java.io.*;

import com.dcop.jx.entry.*;
import com.dcop.jx.entry.base.*;
import com.dcop.jx.entry.kernel.*;
import com.dcop.jx.entry.extend.jjs.*;
import com.dcop.jx.core.base.file.*;
import com.dcop.jx.core.base.log.*;


@ClassImport("jjs")
public class JJs implements IJJs, IObject {

    private static final String m_name = "JJs";
    private IObject m_parent = null;
    private IObject m_root = null;
    private int m_id = 0;

    private ScriptEngine m_engine = null;


    /// 构建入口
    @Override
    public void construct(int id, IObject parent, String cfg) {
        m_id = id;
        m_parent = parent;
    }

    /// 对象名
    @Override
    public String name() {
        return m_name;
    }

    /// 对象ID
    @Override
    public int id() {
        return m_id;
    }

    /// 父对象
    @Override
    public IObject parent() {
        return m_parent;
    }

    /// 根对象
    public IObject root() {
        return m_root;
    }

    /// 初始化入口
    @Override
    public ErrCode init(IObject root, Object[] arg) {
        m_root = root;

        try {
            m_engine = new ScriptEngineManager().getEngineByExtension("js");
            if (m_engine == null) {
                Logs.record("trace", "engine is null");
                return ErrCode.FAILURE;
            }

            Bindings bind = m_engine.createBindings();
            bind.put("jjs", this);
            m_engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);

        } catch (Exception e) {
            Logs.record("trace", "js execute error! " + e.getMessage());
            e.printStackTrace();
            return ErrCode.FAILURE;
        }
        
        Logs.record("trace", "'" + name() + "'(" + id() + ") Init!");
        return ErrCode.SUCCESS;
    }


    /// 结束时入口
    @Override
    public void fini() {
        Logs.record("trace", "'" + name() + "'(" + id() + ") Fini!");
    }


    /// 消息入口
    @Override
    public IMsg proc(IMsg msg) {
        Logs.record("trace", "'" + name() + "'(" + id() + ") Proc Msg!");
        return null;
    }


    /// Dump入口
    @Override
    public String dump() {
        return dumpEngines();
    }

    /// 加载js
    @Override
    public Object load(String file) {
        if (m_engine == null) return null;

        Object ret = null;
        try {
            if (new File(file).isFile()) {
                ret = m_engine.eval(new FileReader(file));
            } else {
                ret = m_engine.eval(file);
            }

        } catch (Exception e) {
            Logs.record("trace", String.format("js load '%s' error: %s", file, e.getMessage()));
            e.printStackTrace();
        }

        return ret;
    }

    /// 加载json
    @Override
    public Object json(String prefix, String file, String suffix) {
        if (m_engine == null) return null;

        Object ret = null;
        try {
            if (new File(file).isFile()) {
                ret = m_engine.eval(prefix + Files.readFile(file) + suffix);
            } else {
                ret = m_engine.eval(prefix + file + suffix);
            }
        } catch (Exception e) {
            Logs.record("trace", String.format("js parse json '%s' error: %s", file, e.getMessage()));
            e.printStackTrace();
        }

        return ret;
    }

    /// 执行js函数
    @Override
    public Object exec(String entry, Object...args) {
        if (m_engine == null) return null;

        Object ret = null;
        try {
            if (m_engine instanceof Invocable) {
                Invocable in = (Invocable)m_engine;
                ret = in.invokeFunction(entry, args);
            }
        } catch (Exception e) {
            Logs.record("trace", String.format("js exec '%s' error: %s", entry, e.getMessage()));
            e.printStackTrace();
        }

        return ret;
    }

    /// 获取引擎列表
    private String dumpEngines() {
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = manager.getEngineFactories();
        String out = String.format("ScriptEngineFactory Dump: (size:%d)\n", factories.size());
        out += "-----------------------------------------------\n";
        for (ScriptEngineFactory factory : factories) {
            out += String.format(
                "  Name: %s%n" +
                "  Version: %s%n" +
                "  Language name: %s%n" +
                "  Language version: %s%n" +
                "  Extensions: %s%n" +
                "  Mime types: %s%n" +
                "  Names: %s%n",
                factory.getEngineName(),
                factory.getEngineVersion(),
                factory.getLanguageName(),
                factory.getLanguageVersion(),
                factory.getExtensions(),
                factory.getMimeTypes(),
                factory.getNames());
            out += "-----------------------------------------------\n";
        }
        return out;
    }

}
