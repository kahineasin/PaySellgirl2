package com.sellgirl.sgGameHelper;

/**
 * 游戏运行的目标平台的配置
 * 由于发现安卓电视和安卓手机的gdx-controller兼容性很有问题，不一致
 * 
 * 每次发包时应注意此配置可能需要更改
 * 
 * 安卓电视时:
 * 1. 应该设置AndroidControllers.ignoreNoGamepadButtons=false
 */
public interface ISGPlatformConfig {
	PlatformType getPlatformType();
	int[] getPlatformVersion();
	
	public static enum PlatformType{
		ANDROID,AndroidTV
	}
}
