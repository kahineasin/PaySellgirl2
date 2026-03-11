package com.sellgirl.sgJavaHelper.classLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sellgirl.sgJavaHelper.ISGDisposable;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.classLoader.ExtensionLoader;
import com.sellgirl.sgJavaHelper.classLoader.ModularExtensionManager;
import com.sellgirl.sgJavaHelper.classLoader.RuntimeClassPathManager;

//import com.sellgirl.pfHelperNotSpring.sql.MycatMulitJdbcVersionTest;

/**
 */
public class SGClassLoader2 implements ISGDisposable {
	private static ModularExtensionManager loader=null;
	private static RuntimeClassPathManager loader2=null;
//	public static URLClassLoader ucl=null;
//	private static URLClassLoader ucl2=null;
	public  HashMap<String,URLClassLoader[]> uclMap=null;
//	private static HashMap<String,URLClassLoader> uclMap2=null;

	/**
	 * 有些情况读不到,如:
	 * 1.子jar放到resource,打成合并主jar,后用java -jar运行时,由于是嵌套jar,所以不能加载到
	 * 2.只要是没包含到java命令的-cp参数中的，似乎都不能load成功
	 * @param jarPath
	 * @param className
	 * @param tryStepRef
	 * @return
	 * @throws Exception
	 */
	public  Class<?> doLoadClass(String jarPath, String className,SGRef<Integer> tryStepRef) throws Exception {
		Class<?> dClass = null;
		

		if(null==uclMap) {uclMap=new HashMap();}
		//URLClassLoader ucl = new URLClassLoader(new URL[] { u,u2,u3 });
		URLClassLoader ucl =null;
		URLClassLoader ucl2 =null;
		if(uclMap.containsKey(jarPath)) {
			ucl=uclMap.get(jarPath)[0];
			ucl2=uclMap.get(jarPath)[1];
		}else {
		
		//注意,不建议嵌套jar包(如把jar放到resource里面打包),因为这样做很可能不兼容,如下面的u4,很可能不兼容(当java -jar执行主包时)
		
		URL u = new URL("jar:file:lib/" + jarPath + "!/");

		// 非spring项目可以把mysql的jar放到resource文件夹下自动加载，如放到
		// resources/jar/mysql-connector-java-8.0.15.jar (实测可用)
		// 奇怪了,今天用win10测试这种方式无法读取,直接放到resources文件夹下才行 -- benjamin 20221202
		//    明白了,这种方式确实能读到u2(不为null),只是后面用class.forName时报错,因为不支持jar嵌套
		URL u2 =null;
		try {
		u2=Thread.currentThread().getContextClassLoader()
				.getResource(Paths.get("jar", jarPath).toString());
		}catch(Exception e) {}
		
		// 非spring项目可以把mysql的jar放到resource文件夹下自动加载，如放到
		// resources/jar/mysql-connector-java-8.0.15.jar (实测可用)
		// 奇怪的是,这种方式加载 sqlserver的驱动 net.sourceforge.jtds.jdbc.Driver时会报错
		URL u3 = null;
		try {
		u3=SGClassLoader2.class.getResource(Paths.get("jar", jarPath).toString());
		}catch(Exception e) {}
		
		URL u4 = new URL("jar:file:jar/" + jarPath + "!/");//这个未测试
		//resources/*.jar
		//win10 cmd 上 java -jar 虽然能读到此url(不为空),但是loadClass却报错
		URL u5 = Thread.currentThread().getContextClassLoader().getResource(jarPath);//这个未测试
		
		URL u6=(new File(jarPath)).toURI().toURL();
		//注意
		//1.如果某个url为空,Class.forName时会报null异常
		//2.url的顺序没有影响
		
		List<URL> urls=new ArrayList<URL>();
		if(null!=u) {urls.add(u);}
		if(null!=u2) {urls.add(u2);}
		if(null!=u3) {urls.add(u3);}
		if(null!=u4) {urls.add(u4);}
		if(null!=u5) {urls.add(u5);}
		if(null!=u6) {urls.add(u6);}
		
		URL[] urlArr=urls.toArray(new URL[urls.size()]);
		//注意,DriverManager.getDrivers()获得的驱动好像和ClassLoader有关,所以可能要考虑url的顺序(不确定)
		
//		if(uclMap.containsKey("1"+jarPath)) {
//			ucl=uclMap.get("1"+jarPath);
//		}else {
//			 ucl = new URLClassLoader(urlArr);
//		}
//		if(uclMap.containsKey("2"+jarPath)) {
//			ucl=uclMap.get("2"+jarPath);
//		}
		ucl = new URLClassLoader(urlArr);
		ucl2 = new URLClassLoader(urlArr,SGClassLoader2.class.getClassLoader());
		uclMap.put(jarPath,new URLClassLoader[] {ucl,ucl2});
		}

		int tryStep = tryStepRef.GetValue();
//		//Class.forName方式
//		try {
//			tryStep=1;
//			dClass = Class.forName(className, true, ucl);
//		} catch (Throwable e) {
//		}

		//URLClassLoader.loadClass方式
		//参考https://blog.csdn.net/weixin_35048266/article/details/114714311
		if (null == dClass) {
			try {
				tryStep=2;
				dClass = ucl.loadClass(className);// "net.sourceforge.jtds.jdbc.Driver"
			} catch (Throwable e) {
			}
		}
				
		//不用url的方式
		if (null == dClass) {
			try {
				//tryStep++;
				tryStep=3;
				// 后来在flink平台上执行时用上面方法get不了class,改用下面方法就可以(这种方式应该是在主jar包的pom中引用了mysql的库的情况)
				dClass = Class.forName(className);
			} catch (Throwable e) {
			}
		}

		//flink项目有用这种方式(暂不知道有没有跟其它方式重复)
		if (null == dClass) {
			try {
				//tryStep++;
				tryStep=4;
				dClass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
			} catch (Throwable e) {
			}
		}

		//20251107
		if (null == dClass) {
			try {
				tryStep=5;
				ExtensionLoader.loadExtensions(new File(jarPath));
				if(null!=ExtensionLoader.classLoader) {
					dClass =ExtensionLoader.loadClass(className);	
				}
			} catch (Throwable e) {
			}
		}

		if (null == dClass) {
			try {
				tryStep=6;
////				ModularExtensionManager.EXTENSIONS_DIR=Paths.get(jarPath);
//				ModularExtensionManager loader=new ModularExtensionManager(jarPath);
//				loader.loadAllExtensions();
//				dClass =loader.loadClass(className);
				if(null==loader) {
					loader=new ModularExtensionManager("lib");
				}
				loader.loadAllExtensions();
				dClass =loader.loadClass(className);
			} catch (Throwable e) {
			}
		}

		if (null == dClass) {
			try {
				tryStep=7;
				dClass = ucl2.loadClass(className);// "net.sourceforge.jtds.jdbc.Driver"
			} catch (Throwable e) {
			}
		}

		if (null == dClass) {
			try {
				tryStep=8;
				if(null==loader2) {
					loader2= new RuntimeClassPathManager();
					loader2.scanAndAddExtensions("lib");
				}
				loader2.scanAndAddExtensions(jarPath);
				dClass=loader2.getLoader().loadClass(className);
			} catch (Throwable e) {
			}
		}
		
		tryStepRef.SetValue(tryStep);
		return dClass;
	}

	/**
	 * 建议在退出程序前调用此方法
	 */
	@Override
	public  void dispose() {
		if(null!=uclMap) {
			for(String key:uclMap.keySet()) {
				for(URLClassLoader loader:uclMap.get(key)) {
					try {
						loader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			uclMap.clear();
			uclMap=null;
		}
		if(null!=loader) {
			loader.dispose();
			loader=null;
		}
		if(null!=loader2) {
			loader2.dispose();
			loader2=null;
		}
	}
//	//在这测试是便于修改-cp参数
//    public static void main(String[] args) {
////    	String jarName="D:/cache/knightsashax.jar";
//    	String jarName="knightsashax.jar";
//		SGRef<Integer> i=new SGRef<Integer>(0);
//		if(new File(jarName).exists()) {
//			System.out.println("f exist");
//		}else {
//			System.out.println("f not exist");
//		}
//		Class<?> c1=null;
//		try {
//			c1 = SGClassLoader2.doLoadClass(jarName, "com.sellgirl.knightsashax.GameXScreen", i);
////			 c1=SGClassLoader.doLoadClass(jarName, "com.sellgirl.sgJavaHelper.config.SGDataHelper", i);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(null!=c1) {
//			System.out.println("c exist step:"+i.GetValue());
//		}else {
//			System.out.println("c not exist");
//		}
//    }
}
