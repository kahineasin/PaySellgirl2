package com.sellgirl.sgJavaHelper.classLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.sellgirl.sgJavaHelper.ISGDisposable;

public class RuntimeClassPathManager implements ISGDisposable{
	private URLClassLoader classLoader;
	public RuntimeClassPathManager() {
		classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	} 
//    public  void addJarToClasspath(File jarFile) throws Exception {
////        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
//        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
//        addURL.setAccessible(true);
//        addURL.invoke(classLoader, jarFile.toURI().toURL());
//    }
    
    public  void scanAndAddExtensions(String path) throws NoSuchMethodException, SecurityException {
        File extDir = new File(path);//"extensions");
        if (!extDir.exists()) return;

        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
//        boolean can=addURL.canAccess(classLoader);//jdk1.8 eclipse标红, 似乎要jdk11
        boolean can=addURL.isAccessible();
        if(!can) {
        	addURL.setAccessible(true);
        }
        if(extDir.isDirectory()) {
	        for (File jar : extDir.listFiles((dir, name) -> name.endsWith(".jar"))) {
	            try {
	//                addJarToClasspath(jar);
	                addURL.invoke(classLoader, jar.toURI().toURL());
	                System.out.println("Added to classpath: " + jar.getName());
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
        }else {
            try {
//                addJarToClasspath(jar);
                addURL.invoke(classLoader, extDir.toURI().toURL());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!can) {
        	addURL.setAccessible(can);
        }
    }
    public URLClassLoader getLoader() {
    	return classLoader;
    }
	@Override
	public  void dispose() {
		if(null!=classLoader) {
			try {
				classLoader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			classLoader=null;
		}
	}
}