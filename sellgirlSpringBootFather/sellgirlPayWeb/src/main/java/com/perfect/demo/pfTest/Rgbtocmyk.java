package com.perfect.demo.pfTest;


import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
//import javax.media.jai.JAI;

import com.sellgirl.sgJavaHelper.file.SGDirectory;

@SuppressWarnings("unused")
public class Rgbtocmyk {

	public static String iccPath="D:\\\\icc\\ISOcoated_v2_300_eci.icc";
    /**
     * 功能：把rbg格式的图片转为cmyk格式的图片
     * ISOcoated_v2_300_eci.icc 文件是网上下载的 ，下载网址：http://download.java.net/media/jai/builds/release/1_1_3/
     * 本段代码参考网址：http://stackoverflow.com/questions/4472362/how-can-i-convert-an-rgb-image-to-cmyk-and-vice-versa-in-java
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
    	String srcPath="D://download/rgb.jpg";
    	String dstPath="D:/download/rpgToCmykJpg3.jpg";
    	//rpgToCmykTiff();
    	//rpgToCmykJpg();
    	//rpgToCmykJpg2();
//    	rpgToCmykJpg3(srcPath,dstPath);
    	rpgToCmykJpg4(srcPath,"D:/download/rpgToCmykJpg4.jpg");

    }
    /**
     * 测试可用--benjamin
     * @throws IOException
     */
    private static void rpgToCmykTiff()  throws IOException {
        BufferedImage rgbImage = ImageIO.read(new File("D://download/rgb.jpg"));//读取rbg图片
        BufferedImage cmykImage = null;
//        ColorSpace cpace = new ICC_ColorSpace(ICC_Profile.getInstance(Rgbtocmyk.class.getClassLoader().getResourceAsStream("ISOcoated_v2_300_eci.icc")));
        ColorSpace cpace = new ICC_ColorSpace(ICC_Profile.getInstance("D:\\download\\ISOcoated_v2_300_eci.icc"));
        ColorConvertOp op = new ColorConvertOp(rgbImage.getColorModel().getColorSpace(), cpace, null);       
        cmykImage = op.filter(rgbImage, null);

        //JAI.create("filestore", cmykImage, "D:/download/CMYK_IMAGE.TIF", "TIFF");//生成cmyk格式图片
    }
/**
 * 运行报错
 * @throws IOException
 */
    public static void rpgToCmykJpg() throws IOException {
        BufferedImage rgbImage = ImageIO.read(new File("D://download/rgb.jpg"));//读取rbg图片
        BufferedImage cmykImage = null;
//        ColorSpace cpace = new ICC_ColorSpace(ICC_Profile.getInstance(Rgbtocmyk.class.getClassLoader().getResourceAsStream("ISOcoated_v2_300_eci.icc")));
        ColorSpace cpace = new ICC_ColorSpace(ICC_Profile.getInstance("D:\\download\\ISOcoated_v2_300_eci.icc"));
        ColorConvertOp op = new ColorConvertOp(rgbImage.getColorModel().getColorSpace(), cpace, null);       
        cmykImage = op.filter(rgbImage, null);

        
		File file2 = new File("D:/download/CMYK_JPG.jpg" );
		ImageIO.write(cmykImage, "jpg", file2);//报错:Invalid argument to native writeImage

    }
    public static void rpgToCmykJpg2() throws Exception { 
    BufferedImage cmykImage = ImageIO.read(new File(
      "D://download/rgb.jpg"));  
    BufferedImage rgbImage = null; 

   // ColorSpace cpace = new ICC_ColorSpace(ICC_Profile.getInstance(Rgbtocmyk.class.getClassLoader().getResourceAsStream("icc/USWebCoatedSWOP.icc"))); 
    ColorSpace cpace = new ICC_ColorSpace(ICC_Profile.getInstance("D:\\download\\ISOcoated_v2_300_eci.icc"));
    ColorConvertOp op = new ColorConvertOp(cpace, null);  
    rgbImage = op.filter(cmykImage, null); 

    ImageIO.write(rgbImage, "JPEG", new File("CMYK_Sample_RGB_OUTPUT2.jpg")); 

    } 
    /**
     * 此方法好像可行
     * https://stackoverflow.com/questions/22298328/convert-rgb-png-to-cmyk-jpeg-using-icc-color-profiles
     * @throws Exception
     */
    public static void rpgToCmykJpg3(String srcPath,String dstPath) throws Exception
    {
        //final String imageFile = "/tmp/page0.png";
        final String imageFile = srcPath;

        final BufferedImage pngImage = ImageIO.read(new File(imageFile));

        // convert PNG to JPEG
        // http://www.mkyong.com/java/convert-png-to-jpeg-image-file-in-java/
        final BufferedImage rgbImage = new BufferedImage(pngImage.getWidth(), pngImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        rgbImage.createGraphics().drawImage(pngImage, 0, 0, Color.WHITE, null);

        // RGB to CMYK using ColorConvertOp
        // http://stackoverflow.com/questions/380678/how-to-set-icc-color-profile-in-java-and-change-colorspace/2804370#2804370
        //final ICC_Profile ip = ICC_Profile.getInstance("icc/USWebUncoated.icc");
        final ICC_Profile ip = ICC_Profile.getInstance("D:\\download\\ISOcoated_v2_300_eci.icc");

        final ColorConvertOp cco = new ColorConvertOp(rgbImage.getColorModel().getColorSpace(), new ICC_ColorSpace(ip), null);

        final BufferedImage cmykImage = new BufferedImage(pngImage.getWidth(), pngImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        cco.filter(rgbImage, cmykImage);

//        // Write the result into an bytearray
//        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(cmykImage, "JPEG", baos);
//        baos.flush();
        

		File file2 = new File(dstPath );
		ImageIO.write(cmykImage, "JPEG", file2);//报错:Invalid argument to native writeImage
    }
/**
 * test ok
 * @param srcPath
 * @return
 * @throws Exception
 */
    public static byte[] rpgToCmykJpg3Byte(String srcPath) throws Exception
    {
        //final String imageFile = "/tmp/page0.png";
        final String imageFile = srcPath;

        final BufferedImage pngImage = ImageIO.read(new File(imageFile));

        // convert PNG to JPEG
        // http://www.mkyong.com/java/convert-png-to-jpeg-image-file-in-java/
        final BufferedImage rgbImage = new BufferedImage(pngImage.getWidth(), pngImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        rgbImage.createGraphics().drawImage(pngImage, 0, 0, Color.WHITE, null);

        // RGB to CMYK using ColorConvertOp
        // http://stackoverflow.com/questions/380678/how-to-set-icc-color-profile-in-java-and-change-colorspace/2804370#2804370
        //final ICC_Profile ip = ICC_Profile.getInstance("icc/USWebUncoated.icc");
        final ICC_Profile ip = ICC_Profile.getInstance("D:\\\\icc\\ISOcoated_v2_300_eci.icc");

        final ColorConvertOp cco = new ColorConvertOp(rgbImage.getColorModel().getColorSpace(), new ICC_ColorSpace(ip), null);

        final BufferedImage cmykImage = new BufferedImage(pngImage.getWidth(), pngImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        cco.filter(rgbImage, cmykImage);

        // Write the result into an bytearray
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(cmykImage, "JPEG", baos);
        baos.flush();

        return baos.toByteArray();
    }
    /**
     * 测试不通过
     * @param srcPath
     * @param dstPath
     * @throws Exception
     */
    public static void rpgToCmykJpg4(String srcPath,String dstPath) throws Exception
    {
        //final String imageFile = "/tmp/page0.png";
        final String imageFile = srcPath;

        final BufferedImage pngImage = ImageIO.read(new File(imageFile));

        // convert PNG to JPEG
        // http://www.mkyong.com/java/convert-png-to-jpeg-image-file-in-java/
        final BufferedImage rgbImage = new BufferedImage(pngImage.getWidth(), pngImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        rgbImage.createGraphics().drawImage(pngImage, 0, 0, Color.WHITE, null);

        // RGB to CMYK using ColorConvertOp
        // http://stackoverflow.com/questions/380678/how-to-set-icc-color-profile-in-java-and-change-colorspace/2804370#2804370
        //final ICC_Profile ip = ICC_Profile.getInstance("icc/USWebUncoated.icc");
        
        //这样是可以的
//        final ICC_Profile ip = ICC_Profile.getInstance("D:\\download\\ISOcoated_v2_300_eci.icc");
//        final ColorConvertOp cco = new ColorConvertOp(rgbImage.getColorModel().getColorSpace(), new ICC_ColorSpace(ip), null);
        //试试能不能不用icc
        ColorSpace cspace=ColorSpace.getInstance(ColorSpace.TYPE_CMYK);//Unknown color space
        //ColorSpace cspace=ICC_ColorSpace.getInstance(ICC_ColorSpace.TYPE_CMYK);//Unknown color space
        final ColorConvertOp cco = new ColorConvertOp(rgbImage.getColorModel().getColorSpace(),cspace, null);

        final BufferedImage cmykImage = new BufferedImage(pngImage.getWidth(), pngImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        cco.filter(rgbImage, cmykImage);

//        // Write the result into an bytearray
//        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(cmykImage, "JPEG", baos);
//        baos.flush();
        

		File file2 = new File(dstPath );
		ImageIO.write(cmykImage, "JPEG", file2);//报错:Invalid argument to native writeImage
    }

//  /**
//   * rgb转为cmyk
//   * @throws IOException
//   */
//  public static BufferedImage rgbToCmyk() throws IOException {
//      String pathName="D:\\service\\IMAGE-RGB.jpg";
//      BufferedImage rgbImage = ImageIO.read(new File(pathName));
//      BufferedImage cmykImage = null;
//      
//      ColorSpace cpace = new ICC_ColorSpace(ICC_Profile.getInstance("C:\\Windows\\System32\\spool\\drivers\\color\\JapanColor2001Coated.icc"));
//      ColorConvertOp op = new ColorConvertOp(rgbImage.getColorModel().getColorSpace(), cpace, null);
//      cmykImage = op.filter(rgbImage, null);
//      return cmykImage;
//      //似乎不需要保存为tiff
////      String newFileName = "D:\\service\\IMAGE-CMYK.tif";
////      JAI.create("filestore", cmykImage, newFileName, "TIFF");
//  }
	
//	//https://www.thinbug.com/q/4472362
//  public static rgbToCmyk2() {
//  	//ColorSpace cmyk = new ColorSpace(ColorSpace.TYPE_CMYK, 4);
//  	ColorSpace cmyk=ColorSpace.getInstance(ColorSpace.TYPE_CMYK);
//  	float[] rgbFloatArray=getThumbnailBySize(1,1,PFLine.PFLineType.Normal,sashaBeingTiedToTheCrossImgPath);
//  	float[] values = cmyk.fromRGB(rgbFloatArray);
//  }
}