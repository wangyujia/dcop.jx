package com.dcop.jx.core.base.log;

import java.io.*;
import java.util.*;
import java.text.*;
import java.util.zip.*;

import com.dcop.jx.entry.*;


@ClassImport("log")
public class Logs {
    private String path = "./log";
    private int MAX_LENGTH = 512*1024;
    private int ZIP_BUFFER =  16*1024;
    private boolean print = true;
    private boolean call = false;

    /// 单个日志
    public class Log {
        private String name;
        private boolean start = false;

        /// 构造器
        public Log(String name) {
            this.name = name;
        }

        /// 写日志
        public void write(String info) {
            if (info.isEmpty()) return;

            File file = new File(name);
            FileOutputStream stream = null;
            OutputStreamWriter writer = null;
            try {
                stream = new FileOutputStream(file, true);
                writer = new OutputStreamWriter(stream);
                
                writer.append(info);
                
                writer.close();
                stream.close();
            }
            catch (Exception e) {

            }
            finally {
                try {
                    if (writer != null) writer.close();
                    if (stream != null) stream.close();
                }
                catch (Exception e) {

                }
            }

            if (file.length() > MAX_LENGTH) bakup();
        }

        /// 备份日志
        private void bakup() {

            File file = new File(name);
            FileInputStream  fis = null;
            FileOutputStream fos = null;
            GZIPOutputStream gos = null;
            try {
                
                fis = new FileInputStream(file);
                fos = new FileOutputStream(String.format("%s.bak.gz", name), false);
                gos = new GZIPOutputStream(fos);
                
                int ZIP_BUFFER = 1024 * 16;
                int count;
                byte data[] = new byte[ZIP_BUFFER];
                while ((count = fis.read(data, 0, ZIP_BUFFER)) != -1) {
                    gos.write(data, 0, count);
                }

                gos.finish();
                gos.flush();
                gos.close();

                fis.close();
                fos.flush();
                fos.close();

                file.delete();
            }
            catch (Exception e) {

            }
            finally {
                try {
                    if (fis != null) fis.close();
                    if (fos != null) fos.close();
                    if (gos != null) gos.close();
                }
                catch (Exception e) {

                }
            }

        }

        /// 设置开始标志
        public void setStart() {
            start = true;
        }

        /// 获取开始标志
        public boolean getStart() {
            return start;
        }

    }


    /// 日志对象列表
    private Map<String, Log> logs = new TreeMap<String, Log>();

    /// 日志单件
    private static Logs me;
    private Logs() {
        if (me == null) me = this;
        setPath(path);
    }
    public static Logs instance() {
        if (me == null) new Logs();
        return me;
    }

    /// 设置目录
    public void setPath(String path) {
        this.path = path;
        File file = new File(path);
        file.mkdirs();
    }

    /// 设置是否打印
    public void setPrint(boolean print) {
        this.print = print;
    }

    /// 设置最大长度
    public void setMaxLength(int max_length) {
        this.MAX_LENGTH = max_length;
    }

    /// 设置压缩缓冲
    public void setZipLength(int zip_buffer) {
        this.ZIP_BUFFER = zip_buffer;
    }

    /// 记录日志文件
    public static void record(String type, String info) {
        if (info.isEmpty()) return;

        /// 获取日志实例
        Logs me = instance();
        Log log = me.logs.get(type);
        if (log == null) {
            log = me.new Log(String.format("%s/%s.log", me.path, type));
            me.logs.put(type, log);
        }

        /// 获取当前日志
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String date = df.format(new Date());

        /// 增加开始记录
        if (log.start == false) {
            log.write(String.format("\r\n[%s] ================== %s start ================== \r\n", date, type));
            System.out.printf(      "\r\n[%s] ================== %s start ================== \r\n", date, type);
            log.start = true;
        }

        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileName1   = (stack.length > 2)? stack[2].getFileName() : "";
        int lineNumber1    = (stack.length > 2)? stack[2].getLineNumber() : 0;
        String methodName1 = (stack.length > 2)? stack[2].getMethodName() : "";
        String fileName2   = (stack.length > 3)? stack[3].getFileName() : "";
        int lineNumber2    = (stack.length > 3)? stack[3].getLineNumber() : 0;
        String methodName2 = (stack.length > 3)? stack[3].getMethodName() : "";

        String s = (me.call)? String.format("[%s] %s <%s:%d(%s)<-%s:%d(%s)> \r\n", date, info, 
                fileName1, lineNumber1, methodName1, 
                fileName2, lineNumber2, methodName2) : 
            String.format("[%s] %s <%s:%d> \r\n", date, info, 
                fileName1, lineNumber1);

        log.write(s);

        if (me.print) System.out.printf(s);
    }
}
