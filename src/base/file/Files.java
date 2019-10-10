package com.dcop.jx.core.base.file;

import java.io.*;

import com.dcop.jx.entry.*;


@ClassImport("file")
public class Files {

    /**
     * 将文本文件中的内容读入到buffer中
     * @param buffer buffer
     * @param file 文件路径
     */
    public static void readToBuffer(StringBuffer buffer, String file) {
        try {
            InputStream is = new FileInputStream(file);
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                buffer.append("\n");
                line = reader.readLine();
            }
            reader.close();
            is.close();
        } catch (Exception e) {

        }
    }

    /**
     * 读取文本文件内容
     * @param file 文件所在路径
     * @return 文本内容
     */
    public static String readFile(String file) {
        StringBuffer sb = new StringBuffer();
        readToBuffer(sb, file);
        return sb.toString();
    }

    /**
     * 获取文件路径
     * @param file 文件所在路径
     * @return 文件绝对路径
     */
    public static String getPath(String file) {
        File directory = new File(file);
        try {
            return directory.getAbsolutePath();
        } catch(Exception e) {
        }

        return null;
    }
}
