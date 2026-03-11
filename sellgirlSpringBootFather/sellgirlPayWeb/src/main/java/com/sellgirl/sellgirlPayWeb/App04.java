//package com.sellgirl.sellgirlPayWeb;
//
//
//import org.mybatis.spring.annotation.MapperScan;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//
//import java.io.BufferedInputStream;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.jar.Attributes;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
//import java.util.jar.Manifest;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
////import com.sellgirl.sellgirlPayMq.*;
//
//
////import com.sellgirl.sellgirlPayWeb.configuration.Inject001;
////import com.sellgirl.sellgirlPayWeb.configuration.InjectHelper;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.boot.CommandLineRunner;
////import org.springframework.context.annotation.Bean;
//
////import com.sellgirl.sellgirlPayMq.MonthDataCompareCntConsumer;
////import com.sellgirl.sellgirlPayWeb.controller.PrincessDayController;
////import com.sellgirl.sellgirlPayMq.PFMqHelper;
//
///**
// * 测试嵌套JarFile,首先用file读父,最后一层才用jarFile读,不解压缩
// *
// * https://blog.csdn.net/weixin_40750633/article/details/123866345
// */
//@SpringBootApplication
//@MapperScan("com.sellgirl.sellgirlPayDao")
//@ComponentScan(basePackages = {
//		"pf.java.pfHelper.config",
//		"com.sellgirl.sellgirlPayWeb.configuration",
//		"com.sellgirl.sellgirlPayWeb.aop",
//		//"com.sellgirl.*",
//		"com.sellgirl.sellgirlPayWeb.model.game",
//		"com.sellgirl.sellgirlPayWeb.model.xbox",
//		//,"com.sellgirl.sellgirlPayMq.consumer"
//		"com.sellgirl.sellgirlPayDao",
//		"com.sellgirl.sellgirlPayDao.*",
//		"com.sellgirl.sellgirlPayService",
//		"com.sellgirl.sellgirlPayWeb.service",
//		"com.sellgirl.sellgirlPayWeb.service.*",
//		"com.sellgirl.sellgirlPayWeb.apiController",
//		"com.sellgirl.sellgirlPayWeb.controller",
//		})
//public class App04 {
//    private static final Logger LOGGER = LoggerFactory.getLogger(App04.class);
//
//    public static void main(String[] args) throws IOException {
////        JarFile jf=new JarFile("\\C:\\Users\\1011712002\\AppData\\Local\\JetBrains\\IntelliJIdea2022.2\\captureAgent\\debugger-agent.jar");
////
////        if(null == jf) {
////            LOGGER.info("class" + 1 + " is null");
////        }else {
////            LOGGER.info("class" + 1 + " is not null");
////        }
//
//        getJar("\\C:\\Users\\1011712002\\AppData\\Local\\JetBrains\\IntelliJIdea2022.2\\captureAgent\\debugger-agent.jar",1);//class1 is not null
//        getJar("\\C:\\Users\\1011712002\\AppData\\Local\\JetBrains\\IntelliJIdea2022.2\\captureAgent\\debugger-agentxxx.jar",2);//null
//        JarFile jf3= getJar("D:/eclipse_release/javaDemo4/mysql-connector-java-8.0.23.jar",3);//not null
//        JarFile jf4= getJar("D:/eclipse_release/javaDemo4/javaDemo4-0.0.1-jar-with-dependencies.jar!/jar/mysql-connector-java-8.0.23.jar!/",4);//null
//        JarFile jf5= getJar("D:/eclipse_release/javaDemo4/javaDemo4-0.0.1-jar-with-dependencies.jar",5);//not null
//
//        //String jarFilePath = "D:/eclipse_release/javaDemo4/mysql-connector-java-8.0.23.jar";
//        String jarFilePath = "D:/eclipse_release/javaDemo4/javaDemo4-0.0.1-jar-with-dependencies.jar";
//        //获取文件输入流
//        FileInputStream input = new FileInputStream("D:\\2022-03-28.zip");
//
//        //获取ZIP输入流(一定要指定字符集Charset.forName("GBK")否则会报java.lang.IllegalArgumentException: MALFORMED)
//        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), Charset.forName("GBK"));
//
//        //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
//        ZipEntry ze;
//        List<List<Object>> list;
//        //循环遍历
//        while ((ze = zipInputStream.getNextEntry()) != null) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            if (!ze.isDirectory() && ze.toString().endsWith("xls")) {
//                //读取
//                byte[] buffer = new byte[1024];
//                int len;
//                while ((len = zipInputStream.read(buffer)) > -1) {
//                    baos.write(buffer, 0, len);
//                }
//                baos.flush();
//                InputStream stream = new ByteArrayInputStream(baos.toByteArray()); //excel 流
//                //根据excel输入流读取EXCEL中的数据
//                ExcelReader excelReader = ExcelUtil.getReader(stream);
//                list =  excelReader.read(2, excelReader.getRowCount());
//                for(List<Object> objList : list){
//                    objList.get(0);
//                    objList.get(1);
//                    //获取到数据进行相关处理
//                 ......
//                }
//            }
//        }
//        //一定记得关闭流
//        zipInputStream.closeEntry();
//        input.close();
//	}
//
//    public static void getEntity(){
//
//    }
//
//    /**
//     *
//     * @author hy
//     * @createTime 2021-06-20 10:31:25
//     * @description 加载类和实例化类
//     * @param clazzName
//     * @param classLoader
//     *
//     */
//    private static void loadAndInstanceClass(String clazzName, ClassLoader classLoader) {
//        try {
//            // 需要使用其他的classLoader加载
//            Class<?> forName = classLoader.loadClass(clazzName);
//            System.out.println(forName);
//            try {
//                Object newInstance = forName.newInstance();
//                System.out.println(newInstance);
//            } catch (InstantiationException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     *
//     * @author hy
//     * @createTime 2021-06-20 10:24:46
//     * @description 打印并获取所有的class
//     * @param entries
//     *
//     */
//    private static List<String> getClassNames(Enumeration<JarEntry> entries) {
//        List<String> classNames = new ArrayList<String>();
//        while (entries.hasMoreElements()) {
//            JarEntry nextElement = entries.nextElement();
//            String name = nextElement.getName();
//            // 这个获取的就是一个实体类class java.util.jar.JarFile$JarFileEntry
//            // Class<? extends JarEntry> class1 = nextElement.getClass();
//            //System.out.println("entry name=" + name);
//            // 这样就获取所有的jar中的class文件
//
//            // 加载某个class文件，并实现动态运行某个class
//            if (name.endsWith(".class")) {
//                String replace = name.replace(".class", "").replace("/", ".");
//                classNames.add(replace);
//            }
//        }
//        return classNames;
//    }
//
//    /**
//     *
//     * @author hy
//     * @createTime 2021-06-20 10:32:16
//     * @description 输出当前的manifest文件中的信息内容
//     * @param manifest
//     *
//     */
//    private static void pringManifestFile(Manifest manifest) {
//        Attributes mainAttributes = manifest.getMainAttributes();
//        Set<Map.Entry<Object, Object>> entrySet = mainAttributes.entrySet();
//        Iterator<Map.Entry<Object, Object>> iterator = entrySet.iterator();
//        // 打印并显示当前的MAINFEST.MF文件中的信息
//        while (iterator.hasNext()) {
//            Map.Entry<Object, Object> next = iterator.next();
//            Object key = next.getKey();
//            Object value = next.getValue();
//            // 这里可以获取到Class-Path,或者某个执行的Main-Class
//            System.out.println(key + ": " + value);
//        }
//    }
//    private static JarFile getJar(String u9, int idx) {
//        JarFile jf= null;
//        try {
//            jf = new JarFile(u9);
//            if(null == jf) {
//                LOGGER.info("JarFile" + idx + " is null");
//            }else {
//                LOGGER.info("JarFile" + idx + " is not null");
//                return jf;
//            }
//        } catch (IOException e) {
//            //throw new RuntimeException(e);
//            LOGGER.info("JarFile" + idx + " is null");
//        }
//        return null;
////		LOGGER.info("class" + idx + " is null:");
////		LOGGER.info(String.valueOf(null == dClass9));
//    }
//}
//
