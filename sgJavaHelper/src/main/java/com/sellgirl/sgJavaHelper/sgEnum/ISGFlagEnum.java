package com.sellgirl.sgJavaHelper.sgEnum;


/**
 * 使用方法:
 * PFDataHelper.formatFileSize(2000000, new PFEnumFlag(FileSizeUnitType.GB).or(FileSizeUnitType.MB).getBinary())
 *
 * 建议:
 * 1. 有Flag含义的才用此接口,如权限类,一个人可以有多个权限;没有Flag含义的不要用此接口,如SqlFieldType类型不可能并有的,只有程序含义是不同的(如程序认为几种数据类型的处理方式是相似的)
 * 2. 如果enum超过30项，可以考虑加个ISGFlagEnumLong接口getBinary返回long类型
 * 
 * @author 1011712002
 *
 */
public interface ISGFlagEnum extends IPFEnum {
	/**
	 * 2进制的值,用于flag运算
	 * @return
	 */
	int getBinary() ;
}
