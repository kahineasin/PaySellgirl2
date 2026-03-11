//package org.sellgirl.sellgirlPayWeb.controller;
//
//import java.io.File;
//
//import com.perfect.demo.model.PcDeskImgNameResult;
//
//import junit.framework.TestCase;
//
//public class ImageTest extends TestCase{
//
//    public void testCompareImage()
//    {
//         String niliszMobileImgPath = "D:\\\\picture\\wallpaper\\Nilisy_wallpaper_p30pro.jpg";
//         String niliszMobileSameImgPath = "D:\\\\picture\\wallpaper\\Nilisy_wallpaper_p30pro_same.jpg";
//         String niliszMobileModifiedImgPath = "D:\\\\picture\\wallpaper\\Nilisy_wallpaper_p30proU_modified.jpg";
//
//         File file= new File(niliszMobileImgPath);
//         File fileSame= new File(niliszMobileSameImgPath);
//         File fileModified= new File(niliszMobileModifiedImgPath);
//
//PcDeskImgNameResult r=new PcDeskImgNameResult(file);
//PcDeskImgNameResult rSame= new PcDeskImgNameResult(fileSame);
//         PcDeskImgNameResult rModified= new PcDeskImgNameResult(fileModified);         
//         
//         assertTrue( r.getLastModified()==rSame.getLastModified() );
//         assertTrue( r.getLastModified()!=rModified.getLastModified() );
//         assertTrue( r.getHashMD5().equals(rSame.getHashMD5()));
//         assertTrue(! r.getHashMD5().equals(rModified.getHashMD5()));
//    }
//
//}
