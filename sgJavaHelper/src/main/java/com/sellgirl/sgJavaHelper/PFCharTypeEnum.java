package com.sellgirl.sgJavaHelper;

/**
 * 
 *                                                                          65280-----65375
 *                                                                          FullChar
 * 						                             19968------------40959
 * 						                             Chinese 
 *                              8216-----------12301
 *                              ChineseChar
 *         65----90  97----122
 *         English(大小写)
 * 32------------------------127
 * HalfChar
 *  48---57
 *  Number
 * 
 * 以后用这个取代PFCharType.java
 */
public enum PFCharTypeEnum {
	Default,
	/**
	 * 19968~40959
	 */
	Chinese,
	/**
	 * 8216~8217	‘ ’
	 * 12300~12301	「 」
	 */
	ChineseChar,
	/**
	 * 97~122
	 * 65~90
	 */
	English,
	/**
	 * 48~57
	 */
	Number,
	/**
	 * 65280~65375
	 * 12288
	 */
	FullChar,
	/**
	 * 32~127
	 * 9
	 */
	HalfChar
	;		
}