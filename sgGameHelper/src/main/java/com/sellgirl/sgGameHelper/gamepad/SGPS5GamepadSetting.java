package com.sellgirl.sgGameHelper.gamepad;

import com.badlogic.gdx.controllers.Controller;
//import com.mygdx.game.sasha.GameSetting;
//import com.mygdx.game.share.SGControllerName;
//import com.mygdx.game.share.XBoxKey;

/**
 * 以后处理识别不了的手柄, 可以把所有手柄键配置到此类
 */
public class SGPS5GamepadSetting implements com.sellgirl.sgJavaHelper.ISGUnProGuard{
//	private final int CROSS;
//	private final int ROUND;
//	private final int SQUARE;
//	private final int TRIANGLE;
//	private final int L1;
//	private final int R1;
//	private int L2;
//	private int R2;
//	private final int AXISL2;
//	private final int AXISR2;
//	//private boolean supportAXISL2=true;
//	/**
//	 * 开始键左边的功能键, 备用
//	 */
//	private final int BACK;
//
//	private final int START;
//
//	private final int UP;
//	private final int DOWN;
//	private final int LEFT;
//	private final int RIGHT;
//	private final int axisLeftX;
//	private final int axisLeftY;
//	private final int axisRightX;
//	private final int axisRightY;

	private final SGPS5Gamepad.AxisSpace axisLeftSpace=new SGPS5Gamepad.AxisSpace();
	private final SGPS5Gamepad.AxisSpace axisRightSpace=new SGPS5Gamepad.AxisSpace();
	private float l2Space=0.1f;
	private float r2Space=0.1f;


	public SGPS5Gamepad.AxisSpace getAxisLeftSpace() {
		return axisLeftSpace;
	}

	public void setAxisLeftSpace(float x1,float x2,float y1, float y2) {
		axisLeftSpace.x1=x1;
		axisLeftSpace.x2=x2;
		axisLeftSpace.y1=y1;
		axisLeftSpace.y2=y2;
	}
	public SGPS5Gamepad.AxisSpace getAxisRightSpace() {
		return axisRightSpace;
	}

	public void setAxisRightSpace(float x1,float x2,float y1, float y2) {
		 axisRightSpace.x1=x1;
		axisRightSpace.x2=x2;
		axisRightSpace.y1=y1;
		axisRightSpace.y2=y2;
	}

	public float getL2Space() {
		return l2Space;
	}

	public void setL2Space(float l2Space) {
		this.l2Space = l2Space;
	}

	public float getR2Space() {
		return r2Space;
	}

	public void setR2Space(float r2Space) {
		this.r2Space = r2Space;
	}
}
