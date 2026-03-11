package com.sellgirl.sgGameHelper.gamepad;

/**
 * 此类作为所有手柄类(12键双摇杆)的统一处理方式
 *
 * 此接口可以用键盘实现,便于1p 2p之类的操作方式(不想这样的话,可以把键盘作为额外的,键盘和pad1可以同时操作p1,但这样不完美,p2想用键盘就不行了)
 * 
 * 用户按键映射、组合键设置、按钮历史状态等应该在GameKey内自行实现，
 * 理由是游戏功能键极可能比XBoxKey的键多，因为可以用组合键或者是键盘
 * 最好的实现可以参考： 
 * D:\actiongame2d\knightsasha\knightsasha\core\src\main\java\com\mygdx\game\demo\piano\GamepadPiano.java
 * 
 * 越来越感觉GameKey的作用大于ISGPS5Gamepad(GameKey的key是对应游戏功能的，更适用)
 * 
 * 注意：
 * 1. 按钮图标不要在ISGPS5Gamepad声明，因为和用户的自定义映射有关。
 *    参考 com.mygdx.game.sasha.bulletD3.IKnightSashaGameKey.getKeyNamesByMask()的做法，
 *    注意可能是组合键
 * 
 */
public interface ISGPS5Gamepad {

	boolean isCROSS();
	boolean isROUND();
	boolean isSQUARE();
	boolean isTRIANGLE();
	boolean isL1();
	boolean isR1();
	boolean isL2();
	boolean isR2();
	boolean isL3();
	boolean isR3();
	boolean isSTART();
	boolean isBACK();
	boolean isUP();
	boolean isDOWN();
	boolean isLEFT();
	boolean isRIGHT();

	/**
	 * 正数为右
	 * @return
	 */
	float axisLeftX() ;

	/**
	 * 正数为下
	 * @return
	 */
	float axisLeftY() ;

	float axisRightX() ;
	float axisRightY();
	boolean isStick1Up();
	boolean isStick1Down();
	boolean isStick1Left();
	boolean isStick1Right();
	boolean isStick2Up();
	boolean isStick2Down();
	boolean isStick2Left();
	boolean isStick2Right();
	/**
	 * 0.1<gamepad.axisL2()
	 * 不支持轴时，返回0或1
	 * @return
	 */
	float axisL2();
	/**
	 * 0.1<gamepad.axisR2()
	 * 不支持轴时，返回0或1
	 * @return
	 */
	float axisR2();
	
	String getPadName();

	/**
	 * 唯一的名称(不是严格唯一的)
	 * 便于设置游戏按键时, 区分多个手柄
	 * 要求
	 * 1. 名称要尽量和上次启动游戏时的手柄名一致, 便于使用上次的配置. 比如 PS5 Controller 1 或 Nintendo Switch Pro Controller 2 之类的
	 * @return
	 */
	String getPadUniqueName();
	int getQuickBtnKey();
//	String getBtnIcon(XBoxKey key);

//	 SGPS5Gamepad.AxisSpace getAxisRightSpace();
//	 void setAxisRightSpace(float x1,float x2,float y1, float y2) ;
}
