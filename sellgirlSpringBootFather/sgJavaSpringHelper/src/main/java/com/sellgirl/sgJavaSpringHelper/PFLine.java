//package com.sellgirl.sgJavaSpringHelper;
//
//import java.awt.Dimension;
//
//public class PFLine {
//	public enum PFLineType {
//		/**
//		 * 按对角线对齐(即自定义)
//		 */
//		Diagonal,
//		/**
//		 * 原图角度
//		 */
//		Normal, 
//		/**
//		 * 按高对齐，水平居中
//		 */
//		FitHeightAndCenterHorizontally,
//		/**
//		 * 逆时针90度
//		 */
//		Anticlockwise90
//	}
//
//	public PFPoint s;
//	public PFPoint e;
//	public PFLineType t = PFLineType.Diagonal;
//
//	private PFLine() {
//	}
//
//	public PFLine(PFPoint s, PFPoint e) {
//		this.s = s;
//		this.e = e;
//	}
//
//	public  PFLine(PFLineType t) {
//		this.t=t;
//	}
//
//	/**
//	 * 按高对齐，水平居中
//	 */
//	public static PFLine FitHeightAndCenterHorizontally() {
//		PFLine l = new PFLine();
//		l.t = PFLineType.FitHeightAndCenterHorizontally;
//		return l;
//	}
//
//	public void CaculateLineByType(Dimension backgroundSize, Dimension imgSize// ,
////			int backWidth, int backHeight, int imgWidth, int imgHeight
//	) {
//		int backWidth = backgroundSize.width;
//		int backHeight = backgroundSize.height;
//		int imgWidth = imgSize.width;
//		int imgHeight = imgSize.height;
//		
//		int sx=0;
//		int sy=0;
//		int ex=0;
//		int ey=0;
//		switch (t) {
//		case FitHeightAndCenterHorizontally:
//			 sx = (imgWidth - (imgHeight * backWidth / backHeight)) / 2;
//			 sy = 0;
//			 ey = imgHeight;
//			 ex = (ey * backWidth / backHeight) + sx;
//			s = new PFPoint(sx, sy);
//			e = new PFPoint(ex, ey);
//			break;
//		case Anticlockwise90:
//			 sx = imgWidth;
//			 sy = 0;
//			 ey = imgHeight;
//			 ex = 0;
//			 if(backHeight==0) {//90度时，可以只给宽度，反过来计算高度
//				 backgroundSize.height= backWidth*imgWidth/imgHeight;
//			 }
//			s = new PFPoint(sx, sy);
//			e = new PFPoint(ex, ey);
//			break;
//		case Normal:
//			 sx = 0;
//			 sy = 0;
//			 ey = imgHeight;
//			 ex = imgWidth;
//			 if(backHeight==0) {//s可以只给宽度，反过来计算高度
//				 backgroundSize.height= backWidth*imgHeight/imgWidth;
//			 }
//			s = new PFPoint(sx, sy);
//			e = new PFPoint(ex, ey);
//			break;
//		case Diagonal:
//			break;
//		default:
//			break;
//		}
//		// return new PFLine(new PFPoint(sx, sy), new PFPoint(ex, ey));
//	}
//
//}
