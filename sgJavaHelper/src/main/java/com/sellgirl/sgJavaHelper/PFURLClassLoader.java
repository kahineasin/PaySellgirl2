////package com.sellgirl.pfHelperNotSpring;
////
////import com.sellgirl.pfHelperNotSpring.config.PFDataHelper;
////import sun.misc.Resource;
////import sun.misc.SharedSecrets;
////import sun.misc.URLClassPath;
////import sun.net.www.ParseUtil;
////
////import java.io.File;
////import java.io.FileInputStream;
////import java.io.IOException;
////import java.io.InputStream;
////import java.net.JarURLConnection;
////import java.net.MalformedURLException;
////import java.net.URL;
////import java.net.URLClassLoader;
////import java.net.URLConnection;
////import java.net.URLStreamHandlerFactory;
////import java.security.AccessControlContext;
////import java.security.AccessController;
////import java.security.CodeSigner;
////import java.security.CodeSource;
////import java.security.PrivilegedExceptionAction;
////import java.security.cert.Certificate;
////import java.util.jar.JarEntry;
////import java.util.jar.JarFile;
////import java.util.jar.Manifest;
////
/////**
//// * 嵌套jar无法读,放弃
//// */
////public class PFURLClassLoader extends URLClassLoader {
////    private  URLClassPath ucp=null;
////    JarEntry jar;
////    private JarFile jarFile;
////    URL[] urls=null;
////    public PFURLClassLoader(URL[] urls, ClassLoader parent) {
////        super(urls, parent);
////        this.urls=urls;
////    }
////
////    public PFURLClassLoader(URL[] urls) {
////        super(urls);
////        this.urls=urls;
////        AccessControlContext acc = AccessController.getContext();
////        ucp = new URLClassPath(urls, acc);
////    }
////
////    public PFURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
////        super(urls, parent, factory);
////        this.urls=urls;
////    }
////    public PFURLClassLoader(URL[] urls,JarEntry jar) {
////        super(urls);
////        this.urls=urls;
////        this.jar=jar;
////    }
//////    @Override
//////    public  Class<?> loadClass(String name, boolean resolve)
//////            throws ClassNotFoundException
//////    {
////////        Class<?> r=super.loadClass(name, resolve);
////////        return r;
//////
////////        synchronized (getClassLoadingLock(name)) {
////////            // First, check if the class has already been loaded
////////            Class<?> c = findLoadedClass(name);
////////            if (c == null) {
////////                long t0 = System.nanoTime();
////////                try {
////////                    if (parent != null) {
////////                        c = parent.loadClass(name, false);
////////                    } else {
////////                        c = findBootstrapClassOrNull(name);
////////                    }
////////                } catch (ClassNotFoundException e) {
////////                    // ClassNotFoundException thrown if class not found
////////                    // from the non-null parent class loader
////////                }
////////
////////                if (c == null) {
////////                    // If still not found, then invoke findClass in order
////////                    // to find the class.
////////                    long t1 = System.nanoTime();
////////                    c = findClass(name);
////////
////////                    // this is the defining class loader; record the stats
////////                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
////////                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
////////                    sun.misc.PerfCounter.getFindClasses().increment();
////////                }
////////            }
////////            if (resolve) {
////////                resolveClass(c);
////////            }
////////            return c;
////////        }
//////
//////
////////        final Class<?> result;
////////        try {
////////            result = AccessController.doPrivileged(
////////                    new PrivilegedExceptionAction<Class<?>>() {
////////                        public Class<?> run() throws ClassNotFoundException {
////////                            String path = name.replace('.', '/').concat(".class");
////////                            Resource res = ucp.getResource(path, false);
////////                            if (res != null) {
////////                                try {
////////                                    return defineClass(name, res);
////////                                } catch (IOException e) {
////////                                    throw new ClassNotFoundException(name, e);
////////                                }
////////                            } else {
////////                                return null;
////////                            }
////////                        }
////////                    }, acc);
////////        } catch (java.security.PrivilegedActionException pae) {
////////            throw (ClassNotFoundException) pae.getException();
////////        }
////////        if (result == null) {
////////            throw new ClassNotFoundException(name);
////////        }
////////        return result;
//////
////////        long t0 = System.nanoTime();
////////        int i = name.lastIndexOf('.');
////////        URL url = res.getCodeSourceURL();
////////        if (i != -1) {
////////            String pkgname = name.substring(0, i);
////////            // Check if package already loaded.
////////            Manifest man = res.getManifest();
////////            definePackageInternal(pkgname, man, url);
////////        }
////////        // Now read the class bytes and define the class
////////        java.nio.ByteBuffer bb = res.getByteBuffer();
////////        if (bb != null) {
////////            // Use (direct) ByteBuffer:
////////            CodeSigner[] signers = res.getCodeSigners();
////////            CodeSource cs = new CodeSource(url, signers);
////////            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
////////            return defineClass(name, bb, cs);
////////        } else {
////////            byte[] b = res.getBytes();
////////            // must read certificates AFTER reading bytes.
////////            CodeSigner[] signers = res.getCodeSigners();
////////            CodeSource cs = new CodeSource(url, signers);
////////            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
////////            return defineClass(name, b, 0, b.length, cs);
////////        }
//////
//////        //new JarFileArchive();
//////
//////
//////        //byte[] b = jar.getBytes();
//////        //byte[] b = urls[0].get;
//////        int size = PFDataHelper.ObjectToInt0( jar.getSize());
//////        // -1 means unknown size.
////////        if (size == -1) {
////////            size = ((Integer) htSizes.get(je.getName())).intValue();
////////        }
//////        byte[] b = new byte[(int) size];
//////        // must read certificates AFTER reading bytes.
//////        CodeSigner[] signers = jar.getCodeSigners();
//////        //CodeSource cs =  new CodeSource(urls[0], signers);
//////        //sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
//////        return defineClass(name, b, 0, b.length);
//////    }
////@Override
////protected Class<?> findClass(final String name)
////        throws ClassNotFoundException
////{
////    final Class<?> result;
////    try {
////        //AccessController好像是JDK的安全模型 https://blog.csdn.net/iflink/article/details/122386922
//////        result = AccessController.doPrivileged(
//////                new PrivilegedExceptionAction<Class<?>>() {
//////                    public Class<?> run() throws ClassNotFoundException {
//////                        String path = name.replace('.', '/').concat(".class");
//////                        Resource res = ucp.getResource(path, false);
//////                        if (res != null) {
//////                            try {
//////                                return defineClass(name, res);
//////                            } catch (IOException e) {
//////                                throw new ClassNotFoundException(name, e);
//////                            }
//////                        } else {
//////                            return null;
//////                        }
//////                    }
//////                }, acc);
////        String path = name.replace('.', '/').concat(".class");
////        Resource res = ucp.getResource(path, false);
////        if (res != null) {
////            try {
////                result= defineClass2(name, res);
////            } catch (IOException e) {
////                throw new ClassNotFoundException(name, e);
////            }
////        } else {
////            result= null;
////        }
////    } catch (Exception pae) {
////        throw pae;
////    }
//////    if (result == null) {
//////        throw new ClassNotFoundException(name);
//////    }
////    return result;
////}
////    public  Class<?> loadClass2(String name, File file)
////            throws Exception
////    {
////        FileInputStream inputStream = new FileInputStream(file);
////        byte[] bytes = new byte[inputStream.available()];
////        // must read certificates AFTER reading bytes.
////        //CodeSigner[] signers = jar.getCodeSigners();
////        //CodeSource cs =  new CodeSource(urls[0], signers);
////        //sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
////        return defineClass(name, bytes, 0, bytes.length);
////    }
////    public  Class<?> loadClass3(String name, File file,
////                                URL baseUrl)
////            throws Exception
////    {
////        final Class<?> result;
////        try {
//////            result = AccessController.doPrivileged(
//////                    new PrivilegedExceptionAction<Class<?>>() {
//////                        public Class<?> run() throws ClassNotFoundException {
//////                            String path = name.replace('.', '/').concat(".class");
//////                            //Resource res = ucp.getResource(path, false);
//////                            Resource res = getResource(path, file,baseUrl);
//////                            if (res != null) {
//////                                try {
//////                                    return defineClass(name, res);
//////                                } catch (IOException e) {
//////                                    throw new ClassNotFoundException(name, e);
//////                                }
//////                            } else {
//////                                return null;
//////                            }
//////                        }
//////                    }, acc);
////
////            String path = name.replace('.', '/').concat(".class");
////            //Resource res = ucp.getResource(path, false);
////            Resource res = getResource(path, file,baseUrl);
////            if (res != null) {
////                try {
////                    return defineClass2(name, res);
////                } catch (IOException e) {
////                    throw new ClassNotFoundException(name, e);
////                }
////            } else {
////                return null;
////            }
////        } catch (Exception pae) {
////            throw  pae;
////        }
//////        if (result == null) {
//////            throw new ClassNotFoundException(name);
//////        }
////       // return result;
////    }
////
////
////    /**
////     * 此方法测试通过
////     * @param name "com.mysql.cj.jdbc.Driver"
////     * @param url new URL("jar:file:/D:/eclipse_release/javaDemo4/mysql-connector-java-8.0.23.jar!/com/mysql/cj/jdbc/Driver.class")
////     * @param baseUrl new URL("jar:file:/D:/eclipse_release/javaDemo4/mysql-connector-java-8.0.23.jar!/")
////     * @return
////     * @throws Exception
////     */
////    public  Class<?> loadClass4(String name,URL url,URL baseUrl)
////            throws Exception
////    {
////        final Class<?> result;
////        try {
////
////            String path = name.replace('.', '/').concat(".class");
////            //Resource res = ucp.getResource(path, false);
////            Resource res = getResource2(path, url,baseUrl);
////            if (res != null) {
////                try {
////                    return defineClass2(name, res);
////                } catch (IOException e) {
////                    throw new ClassNotFoundException(name, e);
////                }
////            } else {
////                return null;
////            }
////        } catch (Exception pae) {
////            throw  pae;
////        }
//////        if (result == null) {
//////            throw new ClassNotFoundException(name);
//////        }
////        // return result;
////    }
////    private Class<?> defineClass2(String name, Resource res) throws IOException {
////        long t0 = System.nanoTime();
////        int i = name.lastIndexOf('.');
////        URL url = res.getCodeSourceURL();
//////        if (i != -1) {
//////            String pkgname = name.substring(0, i);
//////            // Check if package already loaded.
//////            Manifest man = res.getManifest();
//////            definePackageInternal(pkgname, man, url);
//////        }
////        // Now read the class bytes and define the class
////        java.nio.ByteBuffer bb = res.getByteBuffer();
////        //这部分没有完全弄懂,好像是先读内存的
////        if (bb != null) {
////            // Use (direct) ByteBuffer:
////            CodeSigner[] signers = res.getCodeSigners();
////            CodeSource cs = new CodeSource(url, signers);
////            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
////            return defineClass(name, bb, cs);
////        } else {
////            byte[] b = res.getBytes();
////            // must read certificates AFTER reading bytes.
////            CodeSigner[] signers = res.getCodeSigners();
////            CodeSource cs = new CodeSource(url, signers);
////            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
////            return defineClass(name, b, 0, b.length, cs);
////        }
////    }
////
//////    public static Resource getResourceObj(String path) {
//////        return new FileResource(path);
//////    }
//////    public static Resource getResourceObj(String path,JarEntry jarEntry,JarFile jarFile) {
//////        return new Resource() {
//////            public String getName() {
//////                return var1;
//////            }
//////
//////            public URL getURL() {
//////                return var4;
//////            }
//////
//////            public URL getCodeSourceURL() {
//////                return JarLoader.this.csu;
//////            }
//////
//////            public InputStream getInputStream() throws IOException {
//////                return jarFile.getInputStream(jarEntry);
//////            }
//////
//////            public int getContentLength() {
//////                return (int)jarEntry.getSize();
//////            }
//////
//////            public Manifest getManifest() throws IOException {
//////                //SharedSecrets.javaUtilJarAccess().ensureInitialization(JarLoader.this.jar);
//////                return jarFile.getManifest();
//////            }
//////
//////            public Certificate[] getCertificates() {
//////                return jarEntry.getCertificates();
//////            }
//////
//////            public CodeSigner[] getCodeSigners() {
//////                return jarEntry.getCodeSigners();
//////            }
//////        };
//////    }
////
////
////    /**
////     * 参考自 URLClassPath.Loader.getResource
////     * @param var1
////     * @param baseUrl
////     * @return
////     */
////    Resource getResource(final String var1,File file, //boolean var2,
////                         URL baseUrl) {
////        final URL var3;
////        try {
////            var3 = new URL(baseUrl, ParseUtil.encodePath(var1, false));
////        } catch (MalformedURLException var7) {
////            throw new IllegalArgumentException("name");
////        }
////
//////        final URLConnection var4;
//////        try {
//////            if (var2) {
//////                URLClassPath.check(var3);
//////            }
//////
//////            var4 = var3.openConnection();
//////            InputStream var5 = var4.getInputStream();
//////            if (var4 instanceof JarURLConnection) {
//////                JarURLConnection var6 = (JarURLConnection)var4;
//////                this.jarfile = URLClassPath.JarLoader.checkJar(var6.getJarFile());
//////            }
//////        } catch (Exception var8) {
//////            return null;
//////        }
////
////        return new Resource() {
////            public String getName() {
////                return var1;
////            }
////
////            public URL getURL() {
////                return var3;
////            }
////
////            public URL getCodeSourceURL() {
////                return baseUrl;
////            }
////
////            public InputStream getInputStream() throws IOException {
////                return new FileInputStream(file) ;
////            }
////
////            public int getContentLength() throws IOException {
////                return PFDataHelper.ObjectToInt( file.length());
////            }
////        };
////
////    }
////
////    /**
////     * 参考自URLClassPath.Loader.getResource
////     * @param var1
////     * @param url
////     * @param baseUrl
////     * @return
////     */
////    Resource getResource2(String var1,URL url,URL baseUrl) {
////
////        final URLConnection conn;
////        try {
////
////            conn = url.openConnection();
////            InputStream is = conn.getInputStream();
////        } catch (Exception var8) {
////            return null;
////        }
////
////        return new Resource() {
////            public String getName() {
////                return var1;
////            }
////
////            public URL getURL() {
////                return url;
////            }
////
////            public URL getCodeSourceURL() {
////                return baseUrl;
////            }
////
////            public InputStream getInputStream() throws IOException {
////                return conn.getInputStream();
////            }
////
////            public int getContentLength() throws IOException {
////                return conn.getContentLength();
////            }
////        };
////
////    }
////}
//package com;
//
//


