//package org.pfHelperSwt;
//
//import junit.framework.TestCase;
//import com.sellgirl.pfHelperNotSpring.sql.MycatMulitJdbcVersionTest;
//
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.nio.file.Paths;
//
//public class UncheckDynamicLoadClass001 extends TestCase {
//    /**
//     * 测试ClassLoader接收多个url时,url的顺序是否有影响
//     *
//     * 结论:无影响
//     */
//    public void testLoadClass() throws Exception {
//        Class<?> cls1=Class.forName("com.sellgirl.pfHelperNotSpring.config.PFDataHelper");
//        Class<?> cls2=Class.forName("net.jpountz.lz4.LZ4Factory");
//        System.out.println("cls1 is null:"+(null==cls1));
//        System.out.println("cls2 is null:"+(null==cls2));
//    }
//
//    public void testLoadClassInJar() throws Exception {
//        URL u = new URL("jar:file:lib/clickhouse-native-jdbc-1.7-stable.jar!/");
//        URLClassLoader ucl = new URLClassLoader(new URL[] { u});
//        Class<?> dClass = Class.forName("com.github.housepower.jdbc.ClickHouseDriver", true, ucl);
//        System.out.println("class is null:"+(null==dClass));
//
//    }
//    //ok
//    //神奇,当引用了ru.yandex.clickhouse,就可以动态加载;当没有引用,就不能动态加载..估计是 net.jpountz.lz4.LZ4Factory 引用了 ru.yandex.clickhouse
//    public void testLoadClassInJar2() throws Exception {
//        URL u = new URL("jar:file:lib/lz4-1.3.0-sources.jar!/");
//        URLClassLoader ucl = new URLClassLoader(new URL[] { u});
//        Class<?> dClass = Class.forName("net.jpountz.lz4.LZ4Factory", true, ucl);
//        System.out.println("class is null:"+(null==dClass));
//
//    }
//    public void testLoadClassInJar4() throws Exception {
//        URL u = new URL("D://eclipse_workspace_sellgirlPay/sellgirlPay/PaySellgirl/sellgirlPayHelperClickHouse/lib/lz4-1.3.0-sources.jar!/");
//        URLClassLoader ucl = new URLClassLoader(new URL[] { u});
//
//        Class<?> dClass = Class.forName("net.jpountz.lz4.LZ4Factory", true, ucl);
//        System.out.println("class is null:"+(null==dClass));
//
//    }
//    //ok
//    public void testLoadClassInJar3() throws Exception {
//        URL u = new URL("jar:file:lib/lz4-1.3.0-sources.jar!/");
//        //URLClassLoader ucl = new URLClassLoader(new URL[] { u});
//
//        String version = "5.1.34";
//        String className = "com.mysql.jdbc.Driver";
//        String jarPath = "mysql-connector-java-" + version + ".jar";
//        //URL u = new URL("jar:file:lib/" + jarPath + "!/");
//        URL u2 = Thread.currentThread().getContextClassLoader().getResource(Paths.get("jar", jarPath).toString());
//        URL u3 = MycatMulitJdbcVersionTest.class.getResource(Paths.get("jar", jarPath).toString());
//        URLClassLoader ucl = new URLClassLoader(new URL[] { u, u2, u3 });
//
//        Class<?> dClass = Class.forName("net.jpountz.lz4.LZ4Factory", true, ucl);
//        System.out.println("class is null:"+(null==dClass));
//
//    }
//}
