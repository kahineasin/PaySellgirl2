package com.sellgirl.sgJavaHelper.model;

import com.sellgirl.sgJavaHelper.sgEnum.ISGFlagEnum;

//import com.sellgirl.pfHelperNotSpring.IPFEnum;

public enum FileSizeUnitType 
implements ISGFlagEnum{
	 //implements IPFEnum {
	GB, MB, KB;

	FileSizeUnitType() {
		mask = (1 << ordinal());
	}

	public final int mask;

//	public static boolean isEnabled(int features, FileSizeUnitType feature) {
//		return (features & feature.mask) != 0; // 差不多相当于PFDataHelper.EnumHasFlag
//	}

//	public static int config(int features, FileSizeUnitType feature, boolean state) {
//		if (state) {
//			features |= feature.mask;
//		} else {
//			features &= ~feature.mask;
//		}
//		return features;
//	}
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