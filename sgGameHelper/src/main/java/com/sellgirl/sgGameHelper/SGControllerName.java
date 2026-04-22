package com.sellgirl.sgGameHelper;

public class SGControllerName {
	public final static String XInputController="XInput Controller";
	/**
	 * win10电脑上的PS5手柄
	 */
	public final static String PS5Controller="PS5 Controller";
	/**
	 * android手机上识别的PS5手柄的controller.getName值
	 * 安卓电视上的PS5手柄也是
	 */
	public final static String PS5ControllerOnAndroidPhone="DualSense Wireless Controller";
//	/**
//	 * PS5手柄, 在SONY安卓电视上有可能识别为以下名字--benjamin20260422
//	 */
//	public final static String[] DualSenseWirelessController=new String[] { 
//			"DualSense Wireless Controller Touchpad",
//			"DualSense Wireless Controller Motion Sensor"};
	public final static String SwitchControllerOnAndroidPhone="Pro Controller";

	public final static String Switch="Nintendo Switch Pro Controller";
	/**
	 * sony电视遥控
	 */
	public final static String SonyTvController="SONY TV VRC 001";
	
	public static boolean isPS5Controller(String controllerName) {
		if(controllerName.startsWith(PS5ControllerOnAndroidPhone)) {
			return true;
		}
		return false;
	}
}
