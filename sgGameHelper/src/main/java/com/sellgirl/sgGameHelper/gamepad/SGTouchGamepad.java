package com.sellgirl.sgGameHelper.gamepad;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sellgirl.sgGameHelper.SGGameHelper;
//import com.mygdx.game.sasha.screen.MainMenuScreen;
import com.sellgirl.sgGameHelper.SGLibGdxHelper;
import com.sellgirl.sgGameHelper.ScreenSetting;

public class SGTouchGamepad extends Group implements ISGPS5Gamepad{
	private Button CROSS;
	private Button ROUND;
	private Button SQUARE;
	private Button TRIANGLE;
	private Button L1;
	private Button L2;
	private Button R1;
	private Button R2;
	private int START;
	private int UP;
	private int DOWN;
	private int LEFT;
	private int RIGHT;
	private Skin skin;


	private Touchpad touchpad;
	Texture tTouchPad1;
	Texture tTouchPad2;
//	public SGTouchGamepad(Skin skin,TextButtonStyle buttonStyle){
//		init(skin,buttonStyle);
//	}
	public SGTouchGamepad(Skin skin//,TextButtonStyle buttonStyle
			){
		this.skin=skin;
		init(//skin//,buttonStyle
				);
	}
	boolean inited=false;
//	Button btnSQUARE;
	public void init(//Skin skin//,TextButtonStyle buttonStyle
			){//Touchpad touchpad, Button CROSS, Button ROUND, Button SQUARE, Button TRIANGLE, Button L1, Button R1) {
//		this.touchpad = touchpad;
//		this.CROSS=CROSS;
//		this.ROUND=ROUND;
//		this.SQUARE=SQUARE;
//		this.TRIANGLE=TRIANGLE;
//		this.L1=L1;
//		this.R1=R1;
		if(inited){return;}
		inited=true;
//		skin = MainMenuScreen.getSkin();
//		skin.add("default", MainMenuScreen.getButtonStyle(skin));
//		this.skin = skin;
//		this.skin.add("default", buttonStyle);

		ScreenSetting screenSetting=SGGameHelper.getGameConfig().getScreenSetting();
		int out=((Float)(screenSetting.getWORLD_HEIGHT()/6)).intValue();
		int inner=((Float)(screenSetting.getWORLD_HEIGHT()/12)).intValue();
		float btnW=screenSetting.getWORLD_HEIGHT()/10;
		tTouchPad1 = SGLibGdxHelper.getCircleTexture(out, new Color(1,1,0f,0.5f));
		tTouchPad2 = SGLibGdxHelper.getCircleTexture(inner,new Color(1,1,0f,1f) );//Color.YELLOW

		//ui部分
		Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle(
				new TextureRegionDrawable(tTouchPad1),
				new TextureRegionDrawable(tTouchPad2)
		);
		touchpad = new Touchpad(15f, touchpadStyle);
		//touchpad.setSize(ScreenSetting.WORLD_HEIGHT/6, ScreenSetting.WORLD_HEIGHT/6);
//			touchpad.setBounds(0, 0, ScreenSetting.WORLD_HEIGHT/6, ScreenSetting.WORLD_HEIGHT/6);
		//touchpad.setBounds(inner,inner,inner,inner);
		//touchpad.setBounds(out,out,out,out);
		//touchpad.setBounds(0,0,out*2,out*2);
		touchpad.setBounds(btnW*2.5f-out,btnW*2.5f-out,out*2,out*2);
		this.addActor(touchpad);
		//uiStage.addActor(touchpad);

		SQUARE = new TextButton("□", skin);
		TRIANGLE = new TextButton("△", skin);
		ROUND = new TextButton("○", skin);
		CROSS = new TextButton("X", skin);
		L1 = new TextButton("L1", skin);
		L2 = new TextButton("L2", skin);
		R1 = new TextButton("R1", skin);
		R2 = new TextButton("R2", skin);
		SQUARE.setBounds(screenSetting.getWORLD_WIDTH()-(btnW*4.5f),btnW*2,btnW,btnW);
		TRIANGLE.setBounds(screenSetting.getWORLD_WIDTH()-(btnW*3f),btnW*3.5f,btnW,btnW);
		ROUND.setBounds(screenSetting.getWORLD_WIDTH()-(btnW*1.5f),btnW*2,btnW,btnW);
		CROSS.setBounds(screenSetting.getWORLD_WIDTH()-(btnW*3f),btnW*0.5f,btnW,btnW);
		L1.setBounds(btnW*0.5F,(btnW*5f),btnW,btnW);
		L2.setBounds(btnW*0.5F,(btnW*6.5f),btnW,btnW);
		R1.setBounds(screenSetting.getWORLD_WIDTH()-(btnW*1.5f),(btnW*5f),btnW,btnW);
		R2.setBounds(screenSetting.getWORLD_WIDTH()-(btnW*1.5f),(btnW*6.5f),btnW,btnW);
		this.addActor(SQUARE);
		this.addActor(TRIANGLE);
		this.addActor(ROUND);
		this.addActor(CROSS);
		this.addActor(L1);
		this.addActor(L2);
		this.addActor(R1);
		this.addActor(R2);
		
	}
	public boolean isCROSS() {
		return CROSS.isOver();
	}
	public boolean isROUND() {
		return ROUND.isOver();
	}
	public boolean isSQUARE() {
		return SQUARE.isOver();
	}
	public boolean isTRIANGLE() {
		return TRIANGLE.isOver();
	}

	@Override
	public boolean isL1() {
		return L1.isOver();
	}
	@Override
	public boolean isR1() {
		return R1.isOver();
	}
	@Override
	public boolean isL3() {
		return false;
	}
	@Override
	public boolean isR3() {
		return false;
	}
	@Override
	public boolean isSTART() {
		return false;
	}
	@Override
	public boolean isBACK() {
		return false;
	}

	@Override
	public boolean isUP() {
		return false;
	}
	@Override
	public boolean isDOWN() {
		return false;
	}
	@Override
	public boolean isLEFT() {
		return false;
	}
	@Override
	public boolean isRIGHT() {
		return false;
	}

	@Override
	public float axisLeftX() {
		return touchpad.getKnobPercentX();
	}

	@Override
	public float axisLeftY() {
		return -touchpad.getKnobPercentY();
	}

	public float axisRightX() {
		return 0;
	}
	public float axisRightY() {
		return 0;
	}
	public float axisL2() {
		if(L2.isOver()){return 1;}return 0;
	}
	public float axisR2() {
		if(R2.isOver()){return 1;}
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

//		return this.getClass().getSimpleName();
		return "TouchGamepad";
	}
	@Override
	public String getPadUniqueName() {
		return "TouchGamepad";
	}

	@Override
	public int getQuickBtnKey() {
//		//XBoxKey r=null;
//		int mask=0;
//		if(isSQUARE()) {
//			//return XBoxKey.SQUARE;
//			XBoxKey.config(mask, XBoxKey.SQUARE, true);
//		}else if(isCROSS()) {
////			return XBoxKey.CROSS;
//			XBoxKey.config(mask, XBoxKey.CROSS, true);
//		}else if(isROUND()) {
////			return XBoxKey.ROUND;
//			XBoxKey.config(mask, XBoxKey.ROUND, true);
//		}else if(isL1()) {
////			return XBoxKey.L1;
//			XBoxKey.config(mask, XBoxKey.L1, true);
//		}else if(isR1()) {
////			return XBoxKey.R1;
//			XBoxKey.config(mask, XBoxKey.R1, true);
//		}
//		return
		return SGPS5Gamepad.getQuickBtnKey(this);
	}

	@Override
	public boolean remove() {
		super.remove();
		super.clear();
		if(null!=skin) {skin.dispose();skin=null;}
		if(null!=tTouchPad1){this.tTouchPad1.dispose();tTouchPad1=null;}
		if(null!=tTouchPad2){this.tTouchPad2.dispose();tTouchPad2=null;}
		inited=false;
		return true;
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
}