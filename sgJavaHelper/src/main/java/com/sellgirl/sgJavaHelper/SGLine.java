package com.sellgirl.sgJavaHelper;

import java.awt.Dimension;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class SGLine {
	public enum SGLineType {
		/**
		 * 按对角线对齐(即自定义)
		 */
		Diagonal,
		/**
		 * 原图角度
		 */
		Normal, 
		/**
		 * 按高对齐，水平居中
		 */
		FitHeightAndCenterHorizontally,
		/**
		 * 逆时针90度
		 */
		Anticlockwise90,
		/**
		 * 顺时针90度
		 */
		Clockwise90,
	}

	public PFPoint s;
	public PFPoint e;
	public SGLineType t = SGLineType.Diagonal;
	/**
	 * true时,s和e均是百分比数, 100即为100%
	 */
	public boolean isPercent=false;

	private SGLine() {
	}

	public SGLine(PFPoint s, PFPoint e) {
		this.s = s;
		this.e = e;
	}

	public  SGLine(SGLineType t) {
		this.t=t;
	}

	/**
	 * 常这样用:
	 * new PFLine(new PFPoint(0, 0), 
	              new PFPoint(100, 100)).IsPercent()
       当想表示来源图的参照线,又不知道来源图的长宽(或者为了方便)时,
       上面那样可以表示任何规格图片的左上右下对角线
	 * @return
	 */
	public SGLine IsPercent() {
		this.isPercent=true;
		return this;
	}

	/**
	 * 按高对齐，水平居中
	 */
	public static SGLine FitHeightAndCenterHorizontally() {
		SGLine l = new SGLine();
		l.t = SGLineType.FitHeightAndCenterHorizontally;
		return l;
	}

	/**
	 * 主要用于SGDataHelper.backgroundImgInBuffer
	 * 调整后的this作为img的参照线,用于对齐background的左上右下对角线的话,就不会因为变形而旋转
	 * @param backgroundSize 背景图(目标)尺寸
	 * @param imgSize 图片(源图)尺寸
	 */
	public void CaculateLineByType(Dimension backgroundSize, Dimension imgSize// ,
//			int backWidth, int backHeight, int imgWidth, int imgHeight
	) {
		int backWidth = backgroundSize.width;
		int backHeight = backgroundSize.height;
		int imgWidth = imgSize.width;
		int imgHeight = imgSize.height;
		
		int sx=0;
		int sy=0;
		int ex=0;
		int ey=0;
		switch (t) {
		case FitHeightAndCenterHorizontally:
			 sx = (imgWidth - (imgHeight * backWidth / backHeight)) / 2;
			 sy = 0;
			 ey = imgHeight;
			 ex = (ey * backWidth / backHeight) + sx;
			s = new PFPoint(sx, sy);
			e = new PFPoint(ex, ey);
			break;
		case Anticlockwise90:
			 sx = imgWidth;
			 sy = 0;
			 ey = imgHeight;
			 ex = 0;
			 if(backHeight==0) {//90度时，可以只给宽度，反过来计算高度
				 backgroundSize.height= backWidth*imgWidth/imgHeight;
			 }
			s = new PFPoint(sx, sy);
			e = new PFPoint(ex, ey);
			break;
		case Clockwise90:
			 sx = 0 ;
			 sy =imgHeight;
			 ey =0 ;
			 ex =imgWidth ;
			 if(backHeight==0) {//90度时，可以只给宽度，反过来计算高度
				 backgroundSize.height= backWidth*imgWidth/imgHeight;
			 }
			s = new PFPoint(sx, sy);
			e = new PFPoint(ex, ey);
			break;
		case Normal:
			 sx = 0;
			 sy = 0;
			 ey = imgHeight;
			 ex = imgWidth;
			 if(backHeight==0) {//s可以只给宽度，反过来计算高度
				 backgroundSize.height= backWidth*imgHeight/imgWidth;
			 }
			s = new PFPoint(sx, sy);
			e = new PFPoint(ex, ey);
			break;
		case Diagonal:
			break;
		default:
			break;
		}
		// return new PFLine(new PFPoint(sx, sy), new PFPoint(ex, ey));
	}

}
