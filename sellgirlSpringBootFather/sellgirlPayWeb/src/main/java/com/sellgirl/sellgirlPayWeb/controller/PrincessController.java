package com.sellgirl.sellgirlPayWeb.controller;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.file.SGDirectory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;

import com.perfect.demo.model.PcDeskImgNameResult;
import com.perfect.demo.pfTest.Rgbtocmyk;
//import com.perfect.demo.pfTest.Rgbtocmyk;
import com.sellgirl.sellgirlPayWeb.PrincessSwaggerAttr;
import com.sellgirl.sellgirlPayWeb.model.PcDeskImgType;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.SGLine;
import com.sellgirl.sgJavaHelper.SGLine.SGLineType;
import com.sellgirl.sgJavaHelper.PFPoint;
import com.sellgirl.sgJavaHelper.SGRef;

/**
 * 发现一个问题
 * ImageIO.read 方法读7000x9000的图片会报错:java.lang.OutOfMemoryError: Java heap space
 * @author li
 *
 */
@RestController
//@Controller("myController")
public class PrincessController // extends AbstractController
{
//    @Override
//    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.getWriter().print("Hello world!");
//        return null;
//    }
	@GetMapping({ "/sasha" })
	public File Sasha() {
		File outputfile = new File(sashaImgPath);
		return outputfile;
	}

	private static String sashaImgPath = "D:\\\\picture\\ps\\princessSasha_lostVirginity_byBenjamin.jpg";
//	private static String sashaBeingTiedToTheCrossImgPath = "D:\\\\picture\\ps\\princessSashaBeingTiedToTheCross\\princessSashaBeingTiedToTheCross_byBenjamin.jpg";
	private static String sashaBeingTiedToTheCrossImgPath = "D:\\\\githubRepository\\neris01\\img\\sashabeingtiedtothecrossthumbnail_w2000.jpg";
	private static Dimension sashaBeingTiedToTheCrossImgSize=new Dimension(2000,2571);
	private static String sashaLieImgPath = "D:\\\\picture\\girlCharacter\\princessSasha\\sashaLie.jpg";
	private static String princessAliceMobileImgPath = "D:\\\\picture\\wallpaper\\princessAlice_mobile.jpg";
	private static String princessAlicePcImgPath = "D:\\\\picture\\wallpaper\\princessAlice_pc.jpg";
	private static String niliszImgPath = "D:\\\\picture\\wallpaper\\Nilisz.bmp";
	private static String niliszMobileImgPath = "D:\\\\picture\\wallpaper\\Nilisy_wallpaper_p30pro.jpg";
	private static String athenaImgPath = "D:\\\\picture\\wallpaper\\Athena.jpg";
	private static String athenaArseImgPath = "D:\\\\picture\\girlCharacter\\athenaAsamiya\\athena_arse_3.jpg";
	private static String normalImgPath = "D:\\\\picture\\normal.jpg";
	private static String aliceImgPath = "D:\\\\picture\\wallpaper\\alice.jpeg";
	private static String aliceGkImgPath = "D:\\\\picture\\wallpaper\\alice_gk.jpg";
	private static String aliceGkNakedImgPath = "D:\\\\picture\\wallpaper\\alice_gk_naked.jpg";
	private static String aliceLieImgPath = "D:\\\\picture\\girlCharacter\\alice_爱丽丝\\lolita.jpg";
//	private static String asunaPcImgPath = "D:\\\\picture\\girlCharactor\\asuna_亚丝娜\\asuna_bend.jpg";
//	private static String asunaMobileImgPath = "D:\\\\picture\\girlCharactor\\asuna_亚丝娜\\asuna_sword.jpg";//中文好像在服务器有问题
	private static String asunaPcImgPath = "D:\\\\picture\\wallpaper\\asuna_bend.jpg";
	private static String asunaMobileImgPath = "D:\\\\picture\\wallpaper\\asuna_sword.jpg";
	private static String tiaraPcImgPath = "D:\\\\picture\\girlCharacter\\tiara\\tiara_1920x1080.jpg";
	//private static String tiaraMobileImgPath = "D:\\\\picture\\girlCharacter\\tiara\\tiara_001.PNG";
	private static String tiaraMobileImgPath = "D:\\\\picture\\screenshot\\fairyFencer_妖精剑士F\\tiyala_001.png";
	private static String tiaraDrinkTeaImgPath = "D:\\\\picture\\screenshot\\fairyFencer_妖精剑士F\\tiara_fang_drink_tea_001.png";
	private static String tiaraNudeLieOnTheFloorPcImgPath = "D:\\\\picture\\girlCharacter\\tiara\\tiara_nudeLieOnTheFloor_20200729.jpg";
	private static String tiaraNudeOnTheAirPcImgPath = "D:\\\\picture\\girlCharacter\\tiara\\tiara_nudeOnTheAir_20200101.jpg";

	private static Dimension mobileSize=new Dimension(2160,4680);//p30pro 1080, 2340 
	/**
	 * 柯达相框 800x1280 
	 */
	private static Dimension photoFrameSize=new Dimension(800*4,1280*4);
	
	/**
	 * PrincessSasha原图
	 */
	@GetMapping(value = { "/sasha.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] SashaJpg() throws IOException {
		File file = new File(sashaImgPath);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes, 0, inputStream.available());
		inputStream.close();
		return bytes;
	}

	/**
	 * PrincessSasha缩略图
	 */
	@GetMapping(value = { "/sashathumbnail" }, produces = { "image/jpeg" })
	@ResponseBody
	@PrincessSwaggerAttr
//    @CrossOrigin
	public byte[] SasaThumbnail(int w, int h,SGLine.SGLineType t) throws IOException {

		return getThumbnailBySize(w,h,t,sashaImgPath);
	}
	@GetMapping(value = { "/sashabeingtiedtothecrossthumbnail" }, produces = { "image/jpeg" })
	@ResponseBody
	@PrincessSwaggerAttr
//    @CrossOrigin
	public byte[] SashaBeingTiedToTheCrossThumbnail(int w, int h,SGLine.SGLineType t) throws IOException {

		return getThumbnailBySize(w,h,t,sashaBeingTiedToTheCrossImgPath);
	}
	/**
	 * 此方法测试不通过,似乎网页不支持cmyk的tiff图片--benjamin 20220422
	 * 最终只能成功转为cmyk的tiff文件(Rgbtocmyk类里),但不能转为cmyk的jpg
	 * @param w
	 * @param h
	 * @param t
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = { "/sashabeingtiedtothecrossthumbnailcmyk" }, produces = { "image/jpeg" })
	//@GetMapping(value = { "/sashabeingtiedtothecrossthumbnailcmyk/{w}/{h}.tiff" }, produces = { "image/jpeg" })
	@ResponseBody
	@PrincessSwaggerAttr
//    @CrossOrigin
	@Deprecated
	public byte[] SashaBeingTiedToTheCrossThumbnailCMYK(int w, int h,SGLine.SGLineType t) throws IOException {

		//return getThumbnailBySize(w,h,t,sashaBeingTiedToTheCrossImgPath);

		Dimension backgroundSize=new Dimension(w, h);
		File infile = new File(sashaBeingTiedToTheCrossImgPath);
		Image image = ImageIO.read(infile);
		String tmpImgPath = SGDataHelper.backgroundImg(backgroundSize, image,
				null,
				new SGLine(t), Color.RED,
				false);


		try {		
//			String tmpDstImgPath = Paths
//				.get(PFDataHelper.GetBaseDirectory(), new String[] { "tmpfile\\tmpimg_" + PFDataHelper.GetNewUniqueHashId() + ".jpg" })
//				.toString();
//			Rgbtocmyk.rpgToCmykJpg3(tmpImgPath, tmpDstImgPath);
//			byte[] r=this.GetByteByImgPath(tmpDstImgPath);
			byte[] r= Rgbtocmyk.rpgToCmykJpg3Byte(tmpImgPath);
			new File(tmpImgPath).delete();
			//new File(tmpDstImgPath).delete();
			return r;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
////		
////		SGRef<Canvas> canvasRef = new SGRef<Canvas>(null);
////		SGRef<Graphics> ctx1Ref = new SGRef<Graphics>(null);
////		BufferedImage paintBi =null;
////		 paintBi = PFDataHelper.backgroundImgInBuffer(
////				//canvas,ctx1,
////				canvasRef,ctx1Ref,
////				null,new Dimension(w, h), image,
////				null,
////				//new PFLine(new PFPoint(0,0),new PFPoint(3840,2160)),
////				//new PFLine(new PFPoint(0,0),new PFPoint(3840,1000)),
////				new PFLine(new PFPoint(0,0),new PFPoint(w,h)),
////				new PFLine(t), Color.RED,
////				false);
//		 
////		// BufferedImage cmyk=rgbToCmyk();
////
////	    	ColorSpace cmyk=ColorSpace.getInstance(ColorSpace.TYPE_CMYK);
////	    	float[] rgbFloatArray=paintBi;
////	    	float[] values = cmyk.fromRGB(rgbFloatArray);
//
//	    	//https://blog.csdn.net/and_bjdbc/article/details/87980706
////			BufferedImage destImage = new BufferedImage(paintBi.getWidth(), paintBi.getHeight(), BufferedImage.TYPE_INT_ARGB);
////			ColorConvertOp cco = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
//		 
//			//BufferedImage cmykImage = new BufferedImage(paintBi.getWidth(), paintBi.getHeight(), BufferedImage.TYPE_INT_RGB);
//			//ColorConvertOp cco = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.TYPE_CMYK), null);
//			//ColorConvertOp cco = new ColorConvertOp(paintBi.getColorModel().getColorSpace(), cpace, null);
//		 
////		 ColorSpace cs = new ICC_ColorSpace(ICC_Profile.getInstance(Rgbtocmyk.class.getClassLoader().getResourceAsStream("ISOcoated_v2_300_eci.icc")));
//		 ColorSpace cpace = new ICC_ColorSpace(ICC_Profile.getInstance("D:\\download\\ISOcoated_v2_300_eci.icc"));
////			ColorConvertOp cco = new ColorConvertOp(paintBi.getColorModel().getColorSpace(), ColorSpace.getInstance(ColorSpace.TYPE_CMYK), null);//报错:名命空间不存在
//			ColorConvertOp cco = new ColorConvertOp(paintBi.getColorModel().getColorSpace(), cpace, null);
//			//cco.filter(paintBi, cmykImage);
//			BufferedImage cmykImage =cco.filter(paintBi, null);
//
//////			String tmpImgPath = PFDataHelper.backgroundImg(mobileSize, image,
//////					null,
//////					PFLine.FitHeightAndCenterHorizontally(),
////////					GetImgLineByHeight(backWidth , 
////////							 backHeight , imgWidth,
////////							 imgHeight),
//////					Color.RED,
//////					false);
//////			File file = new File(tmpImgPath);
////			FileInputStream inputStream = new FileInputStream(cmykImage);
////			byte[] bytes = new byte[inputStream.available()];
////			inputStream.read(bytes, 0, inputStream.available());
////			inputStream.close();
////			file.delete();
////			return bytes;
//			
//			return GetByteByBufferedImage(cmykImage);

	}
	
	@GetMapping(value = { "/sashaliethumbnail" }, produces = { "image/jpeg" })
	@ResponseBody
	@PrincessSwaggerAttr
//    @CrossOrigin
	public byte[] SashaLieThumbnail(int w, int h,SGLine.SGLineType t) throws IOException {

		return getThumbnailBySize(w,h,t,sashaLieImgPath);
	}
	@GetMapping(value = { "/aliceliethumbnail" }, produces = { "image/jpeg" })
	@ResponseBody
	public byte[] AliceLieThumbnail(int w, int h,SGLine.SGLineType t) throws IOException {

		return getThumbnailBySize(w,h,t,aliceLieImgPath);
	}
	private byte[] getThumbnailBySize(int w, int h,SGLine.SGLineType t,String imgPath)  {
//		try {
//		File infile = new File(imgPath);
//		Image image = ImageIO.read(infile);
//		int backWidth = w;
//		int backHeight = h;
//
//		String tmpImgPath = PFDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
//				null,
//				new PFLine(t), Color.RED,
//				false);
//		File file = new File(tmpImgPath);
//		FileInputStream inputStream = new FileInputStream(file);
//		byte[] bytes = new byte[inputStream.available()];
//		inputStream.read(bytes, 0, inputStream.available());
//		inputStream.close();
//		file.delete();
//		return bytes;
//		}
//		catch(Exception e) {
//	    }
//		return null;
		return getThumbnailBySize(w,  h,new SGLine(t),imgPath);
	}
	/**
	 * 改用 getThumbnailBySize(Dimension backgroundSize,PFLine line,String imgPath) 
	 * @param w
	 * @param h
	 * @param line
	 * @param imgPath
	 * @return
	 */
	@Deprecated
	private byte[] getThumbnailBySize(int w, int h,SGLine line,String imgPath)  {
		try {
		File infile = new File(imgPath);
		Image image = ImageIO.read(infile);
		int backWidth = w;
		int backHeight = h;

		String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
				null,
				line, Color.RED,
				false);
		File file = new File(tmpImgPath);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes, 0, inputStream.available());
		inputStream.close();
		file.delete();
		return bytes;
		}
		catch(Exception e) {
	    }
		return null;
	}
	private byte[] getThumbnailBySize(Dimension backgroundSize,SGLine line,String imgPath)  {
		try {
		File infile = new File(imgPath);
		Image image = ImageIO.read(infile);
//		int backWidth = w;
//		int backHeight = h;

		String tmpImgPath = SGDataHelper.backgroundImg(backgroundSize, image,
				null,
				line, Color.RED,
				false);
		File file = new File(tmpImgPath);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes, 0, inputStream.available());
		inputStream.close();
		file.delete();
		return bytes;
		}
		catch(Exception e) {
	    }
		return null;
	}

	/*
	 * PrincessSasha电脑桌面
	 */
//	@GetMapping(value = { "/sashapcdesktop.jpg" }, produces = { "image/jpeg" })
//	@ResponseBody
//	@CrossOrigin
//	public byte[] SashaPcDesktopJpg() {
//		try {
//			File infile = new File(sashaImgPath);
//			Image image = ImageIO.read(infile);
//			int backWidth = 3840; // 1920;
//			int backHeight = 2160;// 1080;
////    	      int sx = 700;
////    	      int sy = 3850;
////    	      int ex = 2816;
//			int sx = 650;
//			int sy = 4000;
//			int ex = 2900;
//			int ey = sy - (ex - sx) * backWidth / backHeight;
//			String tmpImgPath = PFDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
//					new Dimension(3316, 4000), new PFLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), Color.RED, false);
//			File file = new File(tmpImgPath);
//			FileInputStream inputStream = new FileInputStream(file);
//			byte[] bytes = new byte[inputStream.available()];
//			inputStream.read(bytes, 0, inputStream.available());
//			inputStream.close();
//			file.delete();
//			return bytes;
//		} catch (Exception e) {
//			return null;
//		}
//	}
	@GetMapping(value = { "/sashapcdesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] SashaPcDesktopJpg() {
		try {


			int backWidth = 3840; // 1920;
			int backHeight = 2160;// 1080;
			
			File infile = new File(sashaImgPath);
			Image image = ImageIO.read(infile);
			File infile2 = new File(sashaBeingTiedToTheCrossImgPath);
			Image image2 = ImageIO.read(infile2);


			SGRef<Canvas> canvasRef = new SGRef<Canvas>(null);
			SGRef<Graphics> ctx1Ref = new SGRef<Graphics>(null);
			BufferedImage paintBi =null;
			 paintBi = SGDataHelper.backgroundImgInBuffer(
					//canvas,ctx1,
					canvasRef,ctx1Ref,
					null,new Dimension(backWidth, backHeight), image,
					null,
					//new PFLine(new PFPoint(0,0),new PFPoint(3840,2160)),
					//new PFLine(new PFPoint(0,0),new PFPoint(3840,1000)),
					new SGLine(new PFPoint(0,0),new PFPoint(1790,2160)),
					new SGLine(new PFPoint(0,0),new PFPoint(100,100)).IsPercent(), Color.RED,
					false);


//			 paintBi = PFDataHelper.backgroundImgInBuffer(
//						canvasRef,ctx1Ref,
//					 paintBi,
//					 new Dimension(backWidth, backHeight), image2,
//					null,
//					new PFLine(new PFPoint(1790,0),new PFPoint(1790+1680,2160)),
//					new PFLine(new PFPoint(0,0),new PFPoint(100,100)).IsPercent(), Color.RED,
//					false);
			 

			 
				BufferedImage bfImg2 = SGDataHelper.ObjectAs(image2);
			 //Dimension	img2Size = new Dimension(bfImg2.getWidth(), bfImg2.getHeight());
			 //3840-1790
				//a根据想显示的第二张图的范围来计算背景对齐线的end位置
			 int imgStartX=0;//%
			 int imgStartY=11;//%
			 int imgEndX=100;//%
			 int imgEndY=96;//%
			 //int endY=3700;
			 int backEndX=(backHeight*(bfImg2.getWidth()*(imgEndX-imgStartX))/(bfImg2.getHeight()*(imgEndY-imgStartY)))+1790;
			 bfImg2=null;
			 paintBi = SGDataHelper.backgroundImgInBuffer(
						canvasRef,ctx1Ref,
					 paintBi,
					 new Dimension(backWidth, backHeight), image2,
					null,
					new SGLine(new PFPoint(1790,0),new PFPoint(backEndX,2160)),
					new SGLine(new PFPoint(imgStartX,imgStartY),new PFPoint(imgEndX,imgEndY)).IsPercent(), Color.RED,
					false);

			 //canvas=canvasRef.GetValue();
			canvasRef.GetValue().printAll(ctx1Ref.GetValue());

			String tmpImgPath = Paths
					.get(SGDataHelper.GetBaseDirectory(), new String[] { "tmpfile\\tmpimg_" + SGDataHelper.GetNewUniqueHashId() + ".jpg" })
					.toString();
			SGDirectory.EnsureExists(tmpImgPath);
			File file2 = new File(tmpImgPath);
			ImageIO.write(paintBi, "jpg", file2);
			
			try {ctx1Ref.GetValue().dispose();}catch(Exception e) {}
			
			//----------------------------------
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping(value = { "/sashacrossmobiledesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] SashaCrossMobileDesktopJpg() {
		//return this.getThumbnailBySize(mobileSize, PFLine.FitHeightAndCenterHorizontally(), sashaBeingTiedToTheCrossImgPath);
		return this.getThumbnailBySize(mobileSize, new SGLine(
//				new PFPoint(26,15), new PFPoint(74,95)).IsPercent(),
				new PFPoint(25,13), new PFPoint(75,97)).IsPercent(),
				sashaBeingTiedToTheCrossImgPath);
	}

	@GetMapping(value = { "/sashacrossphotoframedesktopjpg.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] SashaCrossPhotoFrameDesktopJpg() {
		
		
		double sx = sashaBeingTiedToTheCrossImgSize.width*0.18;
		double sy = sashaBeingTiedToTheCrossImgSize.height*0.09;
		double ex = sashaBeingTiedToTheCrossImgSize.width-sx;
		double ey = ((ex-sx) * photoFrameSize.height/ photoFrameSize.width ) + sy;
		return this.getThumbnailBySize(photoFrameSize, new SGLine(
//				new PFPoint(26,15), new PFPoint(74,95)).IsPercent(),
				new PFPoint(sx,sy), new PFPoint(ex,ey)),
				sashaBeingTiedToTheCrossImgPath);
	}

	@GetMapping(value = { "/alicepcdesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] AlicePcDesktopJpg() {
		try {

			File infile = new File(aliceImgPath);
			Image image = ImageIO.read(infile);
			int backWidth = 3840; // 1920;
			int backHeight = 2160;// 1080;

			int sx = -600;
			int sy = 0;
			int ey = 845;
			int ex = (845 * backWidth / backHeight) + sx;
//    	      int ex = 2900;
//    	      int ey = sy - (ex - sx) * backWidth / backHeight;
			String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
					new Dimension(600, 845), new SGLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), Color.RED, false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping(value = { "/alicegkpcdesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] AliceGkPcDesktopJpg() {

		try {

			File infile = new File(aliceGkImgPath);
			Image image = ImageIO.read(infile);
			int backWidth = 3840; // 1920;
			int backHeight = 2160;// 1080;

			int sx = 0;
			int sy = 0;
			int ey = 3648;
			int ex = (ey * backWidth / backHeight) + sx;
//    	      int ex = 2900;
//    	      int ey = sy - (ex - sx) * backWidth / backHeight;
			String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
					new Dimension(2736, 3648), new SGLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), Color.RED, false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping(value = { "/alicegknakedpcdesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] AliceGkNakedPcDesktopJpg() {

		try {

			File infile = new File(aliceGkNakedImgPath);
			Image image = ImageIO.read(infile);
			int backWidth = 3840; // 1920;
			int backHeight = 2160;// 1080;

			int sx = 0;
			int sy = 0;
			int ey = 1280;
			int ex = (ey * backWidth / backHeight) + sx;
//    	      int ex = 2900;
//    	      int ey = sy - (ex - sx) * backWidth / backHeight;
			String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
					new Dimension(853, 1280), new SGLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), Color.RED, false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping(value = { "/sashapcdesktopflip.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] SashaPcDesktopFlipJpg() throws IOException {
		File infile = new File(sashaImgPath);
		Image image = ImageIO.read(infile);
		int backWidth = 1920;
		int backHeight = 1080;
//      int sx = 700;
//      int sy = 3850;
//      int ex = 2816;
		int sx = 650;
		int sy = 4000;
		int ex = 2900;
		int ey = sy - (ex - sx) * backWidth / backHeight;
		String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
				new Dimension(3316, 4000), new SGLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), Color.RED, true);
		File file = new File(tmpImgPath);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes, 0, inputStream.available());
		inputStream.close();
		file.delete();
		return bytes;
	}

	/*
	 * PrincessSasha手机桌面
	 */
	@GetMapping(value = { "/sashamobiledesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] SashaMobileDesktopJpg() throws IOException {
		File infile = new File(sashaImgPath);
		Image image = ImageIO.read(infile);
		String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(1080, 2340), image, new Dimension(3316, 4000),
				new SGLine(new PFPoint(600, -300), new PFPoint(2916, 4718)), new Color(34, 28, 62), false);
		File file = new File(tmpImgPath);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes, 0, inputStream.available());
		inputStream.close();
		file.delete();
		return bytes;
	}
	@GetMapping(value = { "/tiaramobiledesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] TiaraMobileDesktopJpg() {
		return this.getThumbnailBySize(1080, 2340,SGLineType.Anticlockwise90, tiaraMobileImgPath);
	}
	public byte[] TiaraDrinkTeaMobileDesktopJpg() {
		return this.getThumbnailBySize(1080, 2340,
				new SGLine(new PFPoint(61,0),new PFPoint(86,100)).IsPercent(),
				tiaraDrinkTeaImgPath);
	}
	public byte[] TiaraNudeOnTheAirMobileDesktopJpg() {
		return this.getThumbnailBySize(mobileSize,
//				new PFLine(new PFPoint(40,0),new PFPoint(60,100)).IsPercent(),
				SGLine.FitHeightAndCenterHorizontally(),
				PrincessController.tiaraNudeOnTheAirPcImgPath);
	}
	

	@GetMapping(value = { "/sashamobiledesktopflip.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] SashaMobileDesktopFlipJpg() {
		try {

			File infile = new File(sashaImgPath);
			Image image = ImageIO.read(infile);

			int backWidth = 2160; // 1080
			int backHeight = 4680;// 2340 ;

			int sx = 600;
			int sy = -300;
			int ex = 2916;
			int ey = sy + ((ex - sx) * backHeight / backWidth);

			String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
					new Dimension(3316, 4000),
					// new PFLine(new PFPoint(600, -300), new PFPoint(2916, 4718)),
					new SGLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), new Color(34, 28, 62), true);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
		}
		return null;
	}

	@GetMapping(value = { "/alicemobiledesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] AliceMobileDesktopJpg() {

		try {

			File infile = new File(aliceImgPath);
			Image image = ImageIO.read(infile);

			int backWidth = 2160; // 1080
			int backHeight = 4680;// 2340 ;

			int sx = 100;
			int sy = 0;
			int ey = 845;
			int ex = (845 * backWidth / backHeight) + sx;
//    	      int ex = 450;
//    	      int ey = sy + ((ex - sx) * backHeight/backWidth);

			String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
					new Dimension(600, 845),
					// new PFLine(new PFPoint(600, -300), new PFPoint(2916, 4718)),
					new SGLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), new Color(34, 28, 62), false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping(value = { "/alicgkemobiledesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] AliceGkMobileDesktopJpg() {

		try {

			File infile = new File(aliceGkImgPath);
			Image image = ImageIO.read(infile);

			int backWidth = 2160; // 1080
			int backHeight = 4680;// 2340 ;

			int sx = 530;
			int sy = 0;
			int ey = 3648;
			int ex = (ey * backWidth / backHeight) + sx;
//    	      int ex = 450;
//    	      int ey = sy + ((ex - sx) * backHeight/backWidth);

			String tmpImgPath = SGDataHelper.backgroundImg(mobileSize, image,
					new Dimension(2736, 3648),
					// new PFLine(new PFPoint(600, -300), new PFPoint(2916, 4718)),
					new SGLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), new Color(34, 28, 62), false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping(value = { "/alicegknakedmobiledesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] AliceGkNakedMobileDesktopJpg() {

		try {

			File infile = new File(aliceGkNakedImgPath);
			Image image = ImageIO.read(infile);

			int backWidth = 2160; // 1080
			int backHeight = 4680;// 2340 ;

			int sx = 85;
			int sy = 0;
			int ey = 1280;
			int ex = (ey * backWidth / backHeight) + sx;
//    	      int ex = 450;
//    	      int ey = sy + ((ex - sx) * backHeight/backWidth);

			String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
					new Dimension(853, 1280),
					// new PFLine(new PFPoint(600, -300), new PFPoint(2916, 4718)),
					new SGLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), new Color(34, 28, 62), false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}


	public byte[] AthenaArseMobileDesktopJpg() {
		return this.getThumbnailBySize(PrincessController.mobileSize,SGLine.FitHeightAndCenterHorizontally(), athenaArseImgPath);
	}
	
	/**
	 * 手机桌面, 竖向雅典娜被触手插
	 * @return
	 */
	@GetMapping(value = { "/athenamobiledesktop.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] AthenaMobileDesktopJpg() {
		try {

			File infile = new File(athenaImgPath);
			Image image = ImageIO.read(infile);
//			int backWidth = 2160; // 1080
//			int backHeight = 4680;// 2340 ;

//			int sx = 1125;
//			int sy = 150;
//			int ex = 0;
//			int ey = sy + (sx - ex) * backWidth / backHeight;
			String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(2160, 4680), image, 
					null,
					//new Dimension(1125, 740),
					new SGLine(new PFPoint(1125, 110), new PFPoint(0, 630)), Color.RED, false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping(value = { "/athenamobiledesktop2.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] AthenaMobileDesktopJpg2() throws IOException {
		File infile = new File(athenaImgPath);
		Image image = ImageIO.read(infile);
//		int backWidth = 2160; // 1080
//		int backHeight = 4680;// 2340 ;

//		int sx = 1125;
//		int sy = 150;
//		int ex = 0;
//		int ey = sy + (sx - ex) * backWidth / backHeight;
		String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(1125, 740), image, new Dimension(1125, 740),
				new SGLine(new PFPoint(1125, 740), new PFPoint(0, 0)), Color.RED, false);
		File file = new File(tmpImgPath);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes, 0, inputStream.available());
		inputStream.close();
		file.delete();
		return bytes;
	}

	public byte[] AsunaMobileDesktopJpg() {
		try {

			File infile = new File(asunaMobileImgPath);
			Image image = ImageIO.read(infile);
			// 手机分辨率，一般不要改
			int backWidth = 2160; // 1080
			int backHeight = 4680;// 2340 ;

			int imgWidth = 2641;
			int imgHeight = 3800;

			int sx = 443;
			int sy = 0;
			int ey = 3800;
			int ex = (ey * backWidth / backHeight) + sx;
			String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
					new Dimension(imgWidth, imgHeight), new SGLine(new PFPoint(sx, sy), new PFPoint(ex, ey)), Color.RED,
					false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}
	
//	/**
//	 * 按高对齐，宽度居中，计算对齐线
//	 * @param backWidth
//	 * @param backHeight
//	 * @param imgWidth
//	 * @param imgHeight
//	 * @return
//	 */
//	private PFLine GetImgLineByHeight(int backWidth , 
//	int backHeight ,int imgWidth,
//	int imgHeight) {
//		int sx=(imgWidth-(imgHeight*backWidth/backHeight))/2;
//		int sy=0;
//		int ey=imgHeight;
//		int ex = (ey * backWidth / backHeight) + sx;
//		return  new PFLine(new PFPoint(sx, sy), new PFPoint(ex, ey));
//	}
	
	public byte[] PrincessAliceMobileDesktopJpg() {
		try {

			File infile = new File(princessAliceMobileImgPath);
			Image image = ImageIO.read(infile);
//			// 手机分辨率，一般不要改
//			int backWidth = 2160; // 1080
//			int backHeight = 4680;// 2340 ;
//
//			int imgWidth = 872;
//			int imgHeight = 1200;

//			int sx = 443;
//			int sy = 0;
//			int ey = 3800;
//			int ex = (ey * backWidth / backHeight) + sx;
			String tmpImgPath = SGDataHelper.backgroundImg(mobileSize, image,
					null,
					SGLine.FitHeightAndCenterHorizontally(),
//					GetImgLineByHeight(backWidth , 
//							 backHeight , imgWidth,
//							 imgHeight),
					Color.RED,
					false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			file.delete();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}
	

	public static String currentPcDeskImg = "PrincessSasha";
	public static PcDeskImgType currentPcDeskImgType = PcDeskImgType.PrincessSasha;
	public static String currentPcDeskImgTypeLocalTextKey="currentPcDeskImgType.txt";
	public static void InitCurrentImgTypeByLocalText() {
		String s=SGDataHelper.ReadLocalTxt(currentPcDeskImgTypeLocalTextKey,LocalDataType.User);
		if(!SGDataHelper.StringIsNullOrWhiteSpace(s)) {
			currentPcDeskImgType=PcDeskImgType.EnumParse(PcDeskImgType.class, s);
			currentPcDeskImg=currentPcDeskImgType.getText();
		}
	}
	public static void SetCurrentImgType(PcDeskImgType t) {
		SGDataHelper.WriteLocalTxt(t.getText(), currentPcDeskImgTypeLocalTextKey,LocalDataType.User);
		currentPcDeskImgType=t;
		currentPcDeskImg=currentPcDeskImgType.getText();
	}

	@PostMapping(value = "/postchangepcdeskimg")
	public AbstractApiResult<?> PostChangePcDeskImg(String imgPath) {
		if (imgPath == null) {
			return AbstractApiResult.error("imgPath为null");
		}
//		currentPcDeskImg = imgPath;
//		currentPcDeskImgType = PcDeskImgType.EnumParse(PcDeskImgType.class, imgPath);
		PrincessController.SetCurrentImgType( PcDeskImgType.EnumParse(PcDeskImgType.class, imgPath));
		return AbstractApiResult.success("设置桌面图片成功");
	}


	private ImgPathModel GetImgPathByKey(PcDeskImgType key) {
		HashMap<PcDeskImgType, ImgPathModel> r = new HashMap<PcDeskImgType, ImgPathModel>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1079641369858320152L;

			{
				put(PcDeskImgType.PrincessSasha, new ImgPathModel(
						new String[] {sashaImgPath,sashaBeingTiedToTheCrossImgPath},null, a -> SashaPcDesktopJpg(),
						a -> SashaMobileDesktopFlipJpg()));
				put(PcDeskImgType.SashaCross, new ImgPathModel(
						new String[] {sashaImgPath,sashaBeingTiedToTheCrossImgPath},
						PrincessController.sashaBeingTiedToTheCrossImgPath, 
						a -> SashaPcDesktopJpg(), 
						a-> SashaCrossMobileDesktopJpg()
						));
				put(PcDeskImgType.Neris, new ImgPathModel(niliszImgPath, niliszMobileImgPath, null, null));
				put(PcDeskImgType.Normal, new ImgPathModel(normalImgPath));
				// put(PcDeskImgType.PrincessSasha,new ImgPathModel(sashaImgPath));
				put(PcDeskImgType.Athena, new ImgPathModel(athenaImgPath, null, null, a -> AthenaArseMobileDesktopJpg()));
				put(PcDeskImgType.Alice,
						new ImgPathModel(aliceImgPath, null, a -> AlicePcDesktopJpg(), a -> AliceMobileDesktopJpg()));
				put(PcDeskImgType.AliceGk, new ImgPathModel(aliceGkImgPath, null, a -> AliceGkPcDesktopJpg(),
						a -> AliceGkMobileDesktopJpg()));
				put(PcDeskImgType.AliceGkNaked, new ImgPathModel(aliceGkNakedImgPath, null,
						a -> AliceGkNakedPcDesktopJpg(), a -> AliceGkNakedMobileDesktopJpg()));
				put(PcDeskImgType.Asuna, new ImgPathModel(asunaPcImgPath, null, null, a -> AsunaMobileDesktopJpg()));
				put(PcDeskImgType.PrincessAlice, new ImgPathModel(princessAlicePcImgPath, princessAliceMobileImgPath, null, a -> PrincessAliceMobileDesktopJpg()));
				put(PcDeskImgType.Tiara, new ImgPathModel(tiaraPcImgPath, null, null, 
						//a -> TiaraMobileDesktopJpg()
						a-> TiaraDrinkTeaMobileDesktopJpg()
						));
				put(PcDeskImgType.TiaraNude, new ImgPathModel(
						PrincessController.tiaraNudeLieOnTheFloorPcImgPath, 
						null, null, 
						a-> TiaraNudeOnTheAirMobileDesktopJpg()
						));
				
			}
		};
		// map的get方法不支持object
		Iterator<Entry<PcDeskImgType, ImgPathModel>> iter = r.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<PcDeskImgType, ImgPathModel> item = iter.next();
			if (item.getKey().equals(key)) {
				return item.getValue();
			}
		}
		return null;
	}

	@GetMapping(value = { "/pcdeskimg.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] PcDeskImg() throws IOException {
		return GetImgPathByKey(currentPcDeskImgType).getPcDeskImgByte();

	}

	/*
	 * 手机桌面
	 */
	@GetMapping(value = { "/mobiledeskimg.jpg" }, produces = { "image/jpeg" })
	@ResponseBody
	@CrossOrigin
	public byte[] MobileDeskImg() throws IOException {
		return GetImgPathByKey(currentPcDeskImgType).getMobileDeskImgByte();

	}

	public class ImgPathModel {
		private String[] pcDeskPath = null;
		private String mobileDeskPath = null;
		private Function<String, byte[]> pcDeskImg = null;
		private Function<String, byte[]> mobileDeskImg = null;

		/**
		 * 
		 * @param pcDeskPath 用数组是为了可以使用多张图组合成pc桌面的情况--benjamin20211225
		 * @param mobileDeskPath
		 * @param pcDeskImg
		 * @param mobileDeskImg
		 */
		public ImgPathModel(String[] pcDeskPath, String mobileDeskPath, Function<String, byte[]> pcDeskImg,
				Function<String, byte[]> mobileDeskImg) {
			super();
			this.pcDeskPath = pcDeskPath;
			if (mobileDeskPath == null&&pcDeskPath!=null) {
				this.mobileDeskPath = pcDeskPath[0];
			} else {
				this.mobileDeskPath = mobileDeskPath;
			}

			if (pcDeskImg == null) {
				this.pcDeskImg = a -> GetByteByImgPath(a);
			} else {
				this.pcDeskImg = pcDeskImg;
			}
			if (mobileDeskImg == null) {
				// this.mobileDeskImg = pcDeskImg;
				this.mobileDeskImg = a -> GetByteByImgPath(a);
			} else {
				this.mobileDeskImg = mobileDeskImg;
			}

		}
		public ImgPathModel(String pcDeskPath, String mobileDeskPath, Function<String, byte[]> pcDeskImg,
				Function<String, byte[]> mobileDeskImg) {
			this(new String[] {pcDeskPath},  mobileDeskPath, pcDeskImg,
					 mobileDeskImg);

		}

		public ImgPathModel(String pcDeskPath) {
			this(new String[] {pcDeskPath}, pcDeskPath, null, null);
		}

		public String[] getPcDeskPath() {
			return pcDeskPath;
		}

		public void setPcDeskPath(String[] pcDeskPath) {
			this.pcDeskPath = pcDeskPath;
		}

		public String getMobileDeskPath() {
			return mobileDeskPath;
		}

		public void setMobileDeskPath(String mobileDeskPath) {
			this.mobileDeskPath = mobileDeskPath;
		}

		public Function<String, byte[]> getPcDeskImg() {
			return pcDeskImg;
		}

		public byte[] getPcDeskImgByte() {
			return pcDeskImg.apply(pcDeskPath[0]);
		}

		public void setPcDeskImg(Function<String, byte[]> pcDeskImg) {
			this.pcDeskImg = pcDeskImg;
		}

		public Function<String, byte[]> getMobileDeskImg() {
			return mobileDeskImg;
		}

		public byte[] getMobileDeskImgByte() {
			return mobileDeskImg.apply(mobileDeskPath);
		}

		public void setMobileDeskImg(Function<String, byte[]> mobileDeskImg) {
			this.mobileDeskImg = mobileDeskImg;
		}
	}

//	@GetMapping(value = { "/getpcdeskimgname" })
//	public AbstractApiResult<?> GetPcDeskImgName() throws IOException {
//		File file = new File(GetImgPathByKey(currentPcDeskImgType).getPcDeskPath());
//		PcDeskImgNameResult r = new PcDeskImgNameResult(file);
//		r.setImgName(currentPcDeskImg);
//		return AbstractApiResult.success(r);
////    	return currentPcDeskImg;
//	}
	@GetMapping(value = { "/getpcdeskimgname" })
	public AbstractApiResult<?> GetPcDeskImgName() throws IOException {
		PcDeskImgNameResult r = null;
		int idx=0;
		for(String i :GetImgPathByKey(currentPcDeskImgType).getPcDeskPath()) {
			File file = new File(i);
			PcDeskImgNameResult tmp = new PcDeskImgNameResult(file);		    
			if(0==idx||r.getLastModified()<tmp.getLastModified()) {
				 r = tmp;
			}
			idx++;
		}
		r.setImgName(currentPcDeskImg);
		return AbstractApiResult.success(r);
//    	return currentPcDeskImg;
	}

	/*
	 * 查看手机端当前使用的壁纸的信息（注意要和GetPcDeskImgName区分开，因为Nilisz这个图在pc端和手机端的来源不是一样的)
	 */
	@GetMapping(value = { "/getmobiledeskimgname" })
	public AbstractApiResult<?> GetMobileDeskImgName() throws IOException {
		File file = new File(GetImgPathByKey(currentPcDeskImgType).getMobileDeskPath());

		PcDeskImgNameResult r = new PcDeskImgNameResult(file);
		r.setImgName(currentPcDeskImg);
		return AbstractApiResult.success(r);
	}

//    @GetMapping("/getpcdeskimglist")
//    public AbstractApiResult<?> GetPcDeskImgList()
//    {
//		Map<String, Object> requiredMap = new HashMap<String, Object>();
//		String[] texts=PcDeskImgType.PrincessSasha.GetAllTexts();
//		for( int i = 0 ; i < texts.length ; i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
//			requiredMap.put(texts[i],texts[i]);
//		}
//		return AbstractApiResult.success(requiredMap);
//    }
    @GetMapping("/getpcdeskimglist")
    public String[] GetPcDeskImgList()
    {
//		String[] texts=PcDeskImgType.PrincessSasha.GetAllTexts();
//		List<String> list=Arrays.asList(texts);
//        Collections.sort(list, new Comparator<String>() {
//            public int compare(String o1, String o2) {
//                return  o1.compareTo(o2);
//            }
//        });
//
//		return list.toArray(new String[list.size()]);
		return GetPcDeskImageTypeOrderedList();
    }
    public static String[] GetPcDeskImageTypeOrderedList() {
		String[] texts=PcDeskImgType.PrincessSasha.GetAllTexts();
        Arrays.sort(texts, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return  o1.compareTo(o2);
            }
        });	
        return texts;
    }
//    public static String[] GetPcDeskImageTypeOrderedList2() {
//		String[] texts=PcDeskImgType.PrincessSasha.GetAllTexts();
//		List<String> list=Arrays.asList(texts);
//        Collections.sort(list, new Comparator<String>() {
//            public int compare(String o1, String o2) {
//                return  o1.compareTo(o2);
//            }
//        });
//
//		return list.toArray(new String[list.size()]);
//    }
	@GetMapping(value = { "/getpcdeskimglist2" })
	public AbstractApiResult<?> GetPcDeskImgList2() {
//		String[] r=PcDeskImgType.PrincessSasha.GetAllTexts();
//		return AbstractApiResult.success(r);

        return AbstractApiResult.success(GetPcDeskImageTypeOrderedList());
	}
    
	private byte[] GetByteByImgPath(String imgPath) {
		try {
			File file = new File(imgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			return bytes;

		} catch (Exception e) {

		}
		return null;
	}
	/**
	 * 此方法未测试通过--benjamin
     * BufferedImage转byte[]
    *
    * @param bImage BufferedImage对象
    * @return byte[]
    * @auth zhy
    */
   @SuppressWarnings("unused")
private static byte[] GetByteByBufferedImage(BufferedImage bImage) {
       ByteArrayOutputStream out = new ByteArrayOutputStream();
       try {
           ImageIO.write(bImage, "png", out);
       } catch (IOException e) {
           //log.error(e.getMessage());
       }
       return out.toByteArray();
   }
}
