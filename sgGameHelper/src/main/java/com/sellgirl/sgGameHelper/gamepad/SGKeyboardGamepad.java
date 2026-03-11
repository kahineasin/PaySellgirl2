package com.sellgirl.sgGameHelper.gamepad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * 把键盘当作手柄一样处理的方式
 * 其实键盘似乎不太适合使用这种方式
 *
 * 缺点:
 * 1. 按键设置功能就知道了, 现在这种方式, 别人想设置跳键到 K上, 好像就不行了
 * 倒不如直接用GameKey来配置键盘  -- benjamin todo
 *
 * 优点:
 * 1. 如果在不使用GameKey的情况下, 此类还是有用的
 */
public class SGKeyboardGamepad implements ISGPS5Gamepad{
	private final int CROSS;
	private final int ROUND;
	private final int SQUARE;
	private final int TRIANGLE;
	private final int L1;
	private final int R1;
	private final int L2;
	private final int R2;
	private final int L3;
	private final int R3;
	private final int START;
	private final int BACK;
	private final int rightX1;
	private final int rightX2;
	private final int rightY1;
	private final int rightY2;
	public SGKeyboardGamepad() {
		CROSS=Keys.X;
		ROUND=Keys.C;
		SQUARE=Keys.Z;
		TRIANGLE=Keys.V;
		L1=Keys.SHIFT_LEFT;
		R1=Keys.S;
		L2=Keys.D;
		R2=Keys.F;
		L3=Keys.T;
		R3=Keys.G;
		rightX1=Keys.Q;
		rightX2=Keys.R;
		rightY1=Keys.E;
		rightY2=Keys.W;
		START=Keys.P;
		BACK=Keys.BACK;
	}
	public boolean isCROSS() {
		return Gdx.input.isKeyPressed(CROSS);
	}
	public boolean isROUND() {
		return Gdx.input.isKeyPressed(ROUND);
	}
	public boolean isSQUARE() {
		return Gdx.input.isKeyPressed(SQUARE);
	}
	public boolean isTRIANGLE() {
		return Gdx.input.isKeyPressed(TRIANGLE);
	}

	public boolean isL1() {
		return Gdx.input.isKeyPressed(L1);
	}
	public boolean isR1() {
		return Gdx.input.isKeyPressed(R1);
	}
	@Override
	public boolean isL3() {
		return Gdx.input.isKeyPressed(L3);
	}
	@Override
	public boolean isR3() {
		return Gdx.input.isKeyPressed(R3);
	}
	@Override
	public boolean isSTART() {
		return Gdx.input.isKeyPressed(START);
	}
	@Override
	public boolean isBACK() {
		return Gdx.input.isKeyPressed(BACK);
	}

	@Override
	public boolean isUP() {
		return Gdx.input.isKeyPressed(Keys.UP);
	}
	@Override
	public boolean isDOWN() {
		return Gdx.input.isKeyPressed(Keys.DOWN);
	}
	@Override
	public boolean isLEFT() {
		return Gdx.input.isKeyPressed(Keys.LEFT);
	}
	@Override
	public boolean isRIGHT() {
		return Gdx.input.isKeyPressed(Keys.RIGHT);
	}
	
	public float axisLeftX() {
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {return -1f;}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {return 1f;}
		return 0;
	}

	public float axisLeftY() {
		if(Gdx.input.isKeyPressed(Keys.UP)) {return -1f;}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {return 1f;}
		return 0;
	}

	public float axisRightX() {
		if(Gdx.input.isKeyPressed(rightX1)) {return -1f;}
		if(Gdx.input.isKeyPressed(rightX2)) {return 1f;}
		return 0;
	}
	public float axisRightY() {
		if(Gdx.input.isKeyPressed(rightY1)) {return -1f;}
		if(Gdx.input.isKeyPressed(rightY2)) {return 1f;}
		return 0;
	}
	public float axisL2() {
		if(Gdx.input.isKeyPressed(L2)) {return 1f;}
		return 0;
	}
	public float axisR2() {

		if(Gdx.input.isKeyPressed(R2)) {return 1f;}
		return 0;
	}

	@Override
	public boolean isL2() {
		return 0<axisL2();
	}
	@Override
	public boolean isR2() {
		return 0<axisR2();
	}
	@Override
	public boolean isStick1Up() {
		return 0>axisLeftY();
	}
	@Override
	public boolean isStick1Down() {
		return 0<axisLeftY();
	}
	@Override
	public boolean isStick1Left() {
		return 0>axisLeftX();
	}
	@Override
	public boolean isStick1Right() {
		return 0<axisLeftX();
	}


	@Override
	public boolean isStick2Up() {
		return 0>axisRightY();
	}
	@Override
	public boolean isStick2Down() {
		return 0<axisRightY();
	}
	@Override
	public boolean isStick2Left() {
		return 0>axisRightX();
	}
	@Override
	public boolean isStick2Right() {
		return 0<axisRightX();
	}
	
	
	@Override
	public String getPadName() {
		return "keyboard";
	}
	@Override
	public String getPadUniqueName() {
		return "keyboard";
	}

	@Override
	public int getQuickBtnKey() {
		//return SGPS5Gamepad.getQuickBtnKey(this);
////
////		//XBoxKey r=null;
////		int mask=0;
////		if(Gdx.input.isKeyPressed(Keys.A)) {
////			mask|=(1 << Keys.A);
////		}if(Gdx.input.isKeyPressed(Keys.S)) {
////			mask|=(1 << Keys.S);
////		}if(Gdx.input.isKeyPressed(Keys.Z)) {
////			mask|=(1 << Keys.Z);
////		}if(Gdx.input.isKeyPressed(Keys.X)) {
////			mask|=(1 << Keys.X);
////		}if(Gdx.input.isKeyPressed(Keys.C)) {
////			mask|=(1 << Keys.C);
////		}if(Gdx.input.isKeyPressed(Keys.V)) {
////			mask|=(1 << Keys.V);
////		}
////		return mask;
		return getQuickBtnKey2();
	}

	public static int getQuickBtnKey2() {
		//XBoxKey r=null;
		int mask=0;

		if(Gdx.input.isKeyPressed(Keys.A)) {
			mask|=(1 << Keys.A);
		}if(Gdx.input.isKeyPressed(Keys.S)) {
			mask|=(1 << Keys.S);
		}if(Gdx.input.isKeyPressed(Keys.Z)) {
			mask|=(1 << Keys.Z);
		}if(Gdx.input.isKeyPressed(Keys.X)) {
			mask|=(1 << Keys.X);
		}if(Gdx.input.isKeyPressed(Keys.C)) {
			mask|=(1 << Keys.C);
		}if(Gdx.input.isKeyPressed(Keys.V)) {
			mask|=(1 << Keys.V);
		}
		return mask;
	}
//	@Override
//	public String getBtnIcon(XBoxKey key) {
//		switch(key) {
//			case CROSS:
//				return "X";
//			case ROUND:
//				return "○";
//			case SQUARE:
//				return "□";
//			case TRIANGLE:
//				return "△";
//			default:
//				return key.toString();
//		}
//	}

//	//private final SGPS5Gamepad.AxisSpace axisRightSpace=new SGPS5Gamepad.AxisSpace();
//	public SGPS5Gamepad.AxisSpace getAxisRightSpace() {
//		return null;//axisRightSpace;
//	}
//
//	public void setAxisRightSpace(float x1,float x2,float y1, float y2) {
//		axisRightSpace.x1=x1;
//		axisRightSpace.x2=x2;
//		axisRightSpace.y1=y1;
//		axisRightSpace.y2=y2;
//	}
}
