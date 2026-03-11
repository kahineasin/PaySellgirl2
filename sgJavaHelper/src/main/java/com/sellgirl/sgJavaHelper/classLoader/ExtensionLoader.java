package com.sellgirl.sgJavaHelper.classLoader;

import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.lang.reflect.Method;

/**
 * 来自deepseek
 */
@Deprecated
public class ExtensionLoader {
    public static URLClassLoader classLoader;
    
    public static void loadExtensions(File libDir) {
//        if (!libDir.exists() || !libDir.isDirectory()) return;
        if (!libDir.exists() ) return;
        
        File[] jarFiles=null;
        if(libDir.isDirectory()) {
	        jarFiles = libDir.listFiles((dir, name) -> name.endsWith(".jar"));
	        if (jarFiles == null || jarFiles.length == 0) return;
	        
        }else {
        	jarFiles=new File[] {libDir};
        }
        URL[] urls = new URL[jarFiles.length];
        try {
            for (int i = 0; i < jarFiles.length; i++) {
                urls[i] = jarFiles[i].toURI().toURL();
                System.out.println("Loading: " + jarFiles[i].getName());
            }
            
            // 创建新的类加载器
            classLoader = new URLClassLoader(urls, ExtensionLoader.class.getClassLoader());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        return classLoader != null ? classLoader.loadClass(className) : 
               Class.forName(className);
    }
}