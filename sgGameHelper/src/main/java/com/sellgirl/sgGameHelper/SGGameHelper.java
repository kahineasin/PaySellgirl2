package com.sellgirl.sgGameHelper;

import com.sellgirl.sgGameHelper.gamepad.ISGPS5Gamepad;
import com.sellgirl.sgGameHelper.gamepad.SGPS5GamepadSetting;

public class SGGameHelper {

	public static ISGGameConfig _gameConfig= null;
	public static ISGLanguage _language= null;
	public static ISGPlatformConfig _platformConfig= null;

	public static ISGGameConfig getGameConfig() {
		if (_gameConfig == null) {

			return new SGGameConfigEmpty();
		}
		return _gameConfig;
	}

	public static void setGameConfig(ISGGameConfig configMapper) {
		_gameConfig = configMapper;
	}

	private static class SGGameConfigEmpty implements ISGGameConfig {


		@Override
		public int getGdxControllerVersion() {
			return 223;
		}

		private ScreenSetting screenSetting=null;
		@Override
		public ScreenSetting getScreenSetting() {
			if(null==screenSetting) {
				screenSetting=new ScreenSetting();
			}
			return screenSetting;
		}

		@Override
		public SGPS5GamepadSetting getGamepadSetting(ISGPS5Gamepad pad) {
			return null;
		}		
	}
	public static ISGLanguage getLanguage() {
		if (_language == null) {

			return new SGLanguageEmpty();
		}
		return _language;
	}

	public static void setLanguage(ISGLanguage language) {
		_language = language;
	}
	private static class SGLanguageEmpty implements ISGLanguage {


		@Override
		public String g(String key) {
			return key;
		}		
	}

	public static ISGPlatformConfig getPlatform() {
		if (_platformConfig == null) {

			return new SGPlatformEmpty();
		}
		return _platformConfig;
	}

	public static void setPlatform(ISGPlatformConfig platformConfig) {
		_platformConfig = platformConfig;
	}
	private static class SGPlatformEmpty implements ISGPlatformConfig {

		@Override
		public PlatformType getPlatformType() {
			return PlatformType.ANDROID;//.AndroidTV;
		}

		private int[] v=new int[] {14};//12};
		@Override
		public int[] getPlatformVersion() {
			return v;
		}
	}
}
