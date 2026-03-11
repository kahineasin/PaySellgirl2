package com.sellgirl.sgGameHelper.gamepad;

import com.sellgirl.sgJavaHelper.ISGUnProGuard;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 经测试,xbox和PS5手柄的键位一样
 * 
 * 注意:
 * L2要和摇杆一样判断,用index为4
 * R2要和摇杆一样判断,用index为5
 */
public enum XBoxKey implements com.sellgirl.sgJavaHelper.sgEnum.ISGFlagEnum, ISGUnProGuard{
	NONE,
	/**
	 * 1
	 */
	ROUND,
	/**
	 * 2
	 */
	SQUARE,
	/**
	 * 3
	 */
	TRIANGLE,
	/**
	 * 4(对应controller.buttonMap.BACK)
	 */
	MENU,
	/**
	 * 5 switch的home键 或 ps5手柄中间的PS键
	 */
	HOME,
	/**
	 * 6
	 */
	START,
	/**
	 * 7
	 */
	L3,
	/**
	 * 8
	 */
	R3,
	/**
	 * 9
	 */
	L1,
	/**
	 * 10
	 */
	R1,
	/**
	 * 11
	 */
	UP,
	/**
	 * 12
	 */
	DOWN,
	/**
	 * 13
	 */
	LEFT,
	/**
	 * 14
	 */
	RIGHT,
	/**
	 * 15 switch的截图键
	 */
	SCREENSHOT,
	/**
	 * 原来是0, 但为了便于flag运算,移到最后
	 */
	CROSS,
	L2,	
	R2,
	/**
	 * 左摇杆的上方向
	 */
	stick1Up,
	stick1Down,
	stick1Left,
	stick1Right,
	/**
	 * 右摇杆的上方向
	 */
	stick2Up,
	stick2Down,
	stick2Left,
	stick2Right;
	XBoxKey() {
		mask = (1 << ordinal());
	}

	public final int mask;

	public static boolean isEnabled(int features, XBoxKey feature) {
		return (features & feature.mask) != 0; // 差不多相当于PFDataHelper.EnumHasFlag
	}
	public static int config(int features, XBoxKey feature, boolean state) {
		if (state) {
			features |= feature.mask;
		} else {
			features &= ~feature.mask;
		}
		return features;
	}
	public static String getTexts(int features) {
		StringBuilder sb=new StringBuilder();
		boolean isFirst=true;
		for(XBoxKey i: XBoxKey.values()){
			if(SGDataHelper.EnumHasFlag(features,i.getBinary())){
				if(!isFirst){
					sb.append("+");
				}
				sb.append(i.name());
				isFirst=false;
			}
		}
		return sb.toString();
	}
	@Override
	public int getValue() {
		return this.ordinal();
	}

	@Override
	public String getText() {
		return this.name();
	}

	@Override
	public int getBinary() {
		return this.mask;
	}
}
