package com.sellgirl.sgJavaHelper.classLoader;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.sellgirl.sgJavaHelper.ISGDisposable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

@Deprecated
public class ModularExtensionManager implements ISGDisposable {
//    public static Path EXTENSIONS_DIR = Paths.get("extensions");
//    private static final Path EXTENSIONS_DIR = Paths.get("D:/cache");
    private  final Path EXTENSIONS_DIR;// = Paths.get("D:/cache");
    private boolean isFolder;
    private URLClassLoader extensionLoader;
    
    public ModularExtensionManager(String path) {
    	EXTENSIONS_DIR = Paths.get(path);
    	isFolder=(new File(path)).isDirectory();
    }
    public void loadAllExtensions() {
        try {
            if (!Files.exists(EXTENSIONS_DIR)) {
                Files.createDirectories(EXTENSIONS_DIR);
                return;
            }
            List<URL> jarUrls=null;
            if(isFolder) {
            // 查找所有JAR文件
            jarUrls = Files.list(EXTENSIONS_DIR)
                .filter(path -> path.toString().endsWith(".jar"))
                .map(path -> {
                    try {
                        return path.toUri().toURL();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            }else {
            	jarUrls=new ArrayList();
            	jarUrls.add(EXTENSIONS_DIR.toUri().toURL());
            }
            
            if (!jarUrls.isEmpty()) {
                extensionLoader = new URLClassLoader(
                    jarUrls.toArray(new URL[0]), 
                    getClass().getClassLoader()
                );
                System.out.println("Loaded " + jarUrls.size() + " extension(s)");
            }
            
        } catch (Exception e) {
            System.err.println("Error loading extensions: " + e.getMessage());
        }
    }
    
    public <T> T createExtensionInstance(String className) {
        try {
            Class<?> clazz = extensionLoader.loadClass(className);
            return (T) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create extension: " + className, e);
        }
    }

    public  Class<?> loadClass(String className) throws ClassNotFoundException {
        return extensionLoader.loadClass(className);
    }
	@Override
	public  void dispose() {
		if(null!=extensionLoader) {
			try {
				extensionLoader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			extensionLoader=null;
		}
	}
}