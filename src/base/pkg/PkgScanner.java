package com.dcop.jx.core.base.pkg;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;



public class PkgScanner {
    /**
     * 包名
     */
    private String pkgName;

    /**
     * 包对应的路径名
     */
    private String pkgPath;


    public PkgScanner(String pkgName) {
        this.pkgName = pkgName;
        this.pkgPath = PathUtils.packageToPath(pkgName);
    }

    /**
     * 执行扫描操作.
     *
     * @return
     * @throws IOException
     */
    public Map<String,String> scan() throws IOException {
        Map<String,String> map = new HashMap<String,String>();
        loadResource(map);
        return map;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
        this.pkgPath = PathUtils.packageToPath(pkgName);
    }

    private void loadResource(Map<String,String> map) throws IOException {
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(pkgPath);

        while (urls.hasMoreElements()) {
            URL u = urls.nextElement();

            ResourceType type = determineType(u);
            switch (type) {

                case JAR:
                    String path = PathUtils.distillPathFromJarURL(u.getPath());
                    scanJar(path, map);
                    break;

                case FILE:
                    scanFile(u.getPath(), pkgName, map);
                    break;
            }
        }
    }

    /**
     * 根据URL判断是JAR包还是文件目录
     * @param url
     * @return
     */
    private ResourceType determineType(URL url) {
        if (url.getProtocol().equals(ResourceType.FILE.getTypeString())) {
            return ResourceType.FILE;
        }

        if (url.getProtocol().equals(ResourceType.JAR.getTypeString())) {
            return ResourceType.JAR;
        }

        throw new IllegalArgumentException("不支持该类型:" + url.getProtocol());
    }

    /**
     * 扫描JAR文件
     * @param path
     * @return
     * @throws IOException
     */
    private void scanJar(String path, Map<String,String> map) throws IOException {
        JarFile jar = new JarFile(path);

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if( (name.startsWith(pkgPath)) && (name.endsWith(ResourceType.CLASS_FILE.getTypeString())) ) {
                name = PathUtils.trimSuffix(name);
                name = PathUtils.pathToPackage(name);

                if (map != null) map.put(name, jar.getName());
            }
        }
    }

    /**
     * 扫描文件目录下的类
     * @param path
     * @return
     */
    private void scanFile(String path, String basePkg, Map<String,String> map) {
        File f = new File(path);

        // 得到目录下所有文件(目录)
        File[] files = f.listFiles();
        if (null != files) {
            int LEN = files.length;

            for (int ix = 0 ; ix < LEN ; ++ix) {
                File file = files[ix];

                // 判断是否还是一个目录
                if (file.isDirectory()) {
                    // 递归遍历目录
                    scanFile(file.getAbsolutePath(), PathUtils.concat(basePkg, ".", file.getName()), map);

                } else if (file.getName().endsWith(ResourceType.CLASS_FILE.getTypeString())) {
                    // 如果是以.class结尾
                    String className = PathUtils.trimSuffix(file.getName());
                    // 如果类名中有"$"不计算在内
                    if (-1 != className.lastIndexOf("$")) {
                        continue;
                    }

                    // 命中
                    String result = PathUtils.concat(basePkg, ".", className);
                    if (map != null) map.put(result, file.getAbsolutePath());
                }
            }
        }
    }
}