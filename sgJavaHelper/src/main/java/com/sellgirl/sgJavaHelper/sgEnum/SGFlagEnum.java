package com.sellgirl.sgJavaHelper.sgEnum;

/**
 * 使用方法:
 * PFDataHelper.formatFileSize(2000000, new PFEnumFlag(FileSizeUnitType.GB).or(FileSizeUnitType.MB).getBinary())
 * 
 * 事实证明,此方法的速度是以前 PFEnumClass.HasFlag方法的20倍以上,见测试方法 testFlagEnumSpeed1
 * @author 1011712002
 *
 */

public class SGFlagEnum<T extends ISGFlagEnum> {
	private int binary=0;
	public SGFlagEnum() {
	}
	public SGFlagEnum(T obj) {
		or(obj);
	}
	public SGFlagEnum<T> or(T obj) {
		binary |= obj.getBinary();
		return this;
	}

	public int getBinary() {
		return binary;
	}
	public boolean hasFlag(T feature) {
		return (binary & feature.getBinary()) != 0; // 差不多相当于PFDataHelper.EnumHasFlag
	}
}
