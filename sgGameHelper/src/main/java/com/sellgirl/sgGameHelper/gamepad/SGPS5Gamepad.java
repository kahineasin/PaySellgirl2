package com.sellgirl.sgGameHelper.gamepad;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.sellgirl.sgGameHelper.ISGPlatformConfig;
//import com.sellgirl.sgGameHelper.GameSetting;
//import com.mygdx.game.sasha.screen.MainMenuScreen;
//import com.mygdx.game.sasha.util.LocalSaveSettingHelper2;
//import com.mygdx.game.sasha.util.LocalSaveSettingHelper3;
import com.sellgirl.sgGameHelper.SGControllerName;
import com.sellgirl.sgGameHelper.SGGameHelper;
//import com.mygdx.game.share.XBoxKey;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sgEnum.ISGFlagEnum;

/**
 * PS5 XBox 北通 等等手柄的类,
 * 对应 libGDX的 Controllers.getControllers 的所有类型
 */
public class SGPS5Gamepad implements ISGPS5Gamepad{

	private  int CROSS;
	private  int ROUND;
	private  int SQUARE;
	private  int TRIANGLE;
	private  int L1;
	private  int R1;
	private final int L3;
	private final int R3;
	private int L2;
	private int R2;
	private final int AXISL2;
	private final int AXISR2;
	//private boolean supportAXISL2=true;
	/**
	 * 开始键左边的功能键, 备用
	 */
	private final int BACK;

	private  int START;
	
	private final int UP;
	private final int DOWN;
	private final int LEFT;
	private final int RIGHT;
	private final int axisLeftX;
	private final int axisLeftY;
	private  int axisRightX;
	private  int axisRightY;

//	private final AxisSpace axisRightSpace=new AxisSpace();
private AxisSpace axisLeftSpace;
	private  AxisSpace axisRightSpace;
	private float l2Space;
	private float r2Space;

	/**
	 * 摇杆识别区(摇杆偏移)
	 */
	public static class AxisSpace{

	/**
	 * 左负数 maxX, 如-0.1
	 */
	public float x1;
	/**
	 * 右正数 minX, 如0.1
	 */
	public float x2;
	/**
	 * 上负数 maxY, 如-0.1
	 */
	public float y1;
	/**
	 * 下正数 minY, 如0.1
	 */
	public float y2;
	public AxisSpace(){
//		x1=-0.1f;
//		x2=0.1f;
//		y1=-0.1f;
//		y2=0.1f;
		init();
	}
	public void init(){
			x1=-0.1f;
			x2=0.1f;
			y1=-0.1f;
			y2=0.1f;
		}
	public float filterX(float x){
//		if(x1<x&&x<x2){return 0;} //这个版本和对应的isLeft版本不等价
		if(x1<=x&&x<=x2){return 0;}
//		return x;//这样会有 刚过阈值 数就比较大的问题
		if(0<=x) {
			return (x-x2)/(1f-x2);//减去阈值后要相对放大
		}else {
			return (x-x1)/(1f+x1);	
		}
	}
		public float filterY(float y){
//			if(y1<y&&y<y2){return 0;}
			if(y1<=y&&y<=y2){return 0;}
//			return y;
			if(0<=y) {
				return (y-y2)/(1f-y2);//减去阈值后要相对放大
			}else {
				return (y-y1)/(1f+y1);	
			}
		}
		public boolean isLeft(float x){
			if(x1<=x){return false;}
			return true;
		}
		public boolean isRight(float x){
			if(x2>=x){return false;}
			return true;
		}
		public boolean isUp(float y){
			if(y1<=y){return false;}
			return true;
		}
		public boolean isDown(float y){
			if(y2>=y){return false;}
			return true;
		}
}
//	private final Controller controller;
	protected Controller controller;
//	protected String uuid;
//	protected SGXInputControllerListener controllerListener;
	private final String padUniqueName;
	/**
	 * 
	 * @param controller
	 * @param setting  SGPS5GamepadSetting setting= new LocalSaveSettingHelper3().readGameKey(padUniqueName,new SGPS5GamepadSetting());
	 */
	public SGPS5Gamepad(Controller controller//,SGPS5GamepadSetting setting
			) {
		this.controller=controller;

		CROSS=controller.getMapping().buttonA;
		ROUND=controller.getMapping().buttonB;
		SQUARE=controller.getMapping().buttonX;
		TRIANGLE=controller.getMapping().buttonY;

		L1=controller.getMapping().buttonL1;
		R1=controller.getMapping().buttonR1;
		
		L2=controller.getMapping().buttonL2;
		R2=controller.getMapping().buttonR2;
		L3=controller.getMapping().buttonLeftStick;
		R3=controller.getMapping().buttonRightStick;

		UP=controller.getMapping().buttonDpadUp;
		DOWN=controller.getMapping().buttonDpadDown;
		LEFT=controller.getMapping().buttonDpadLeft;
		RIGHT=controller.getMapping().buttonDpadRight;

		axisLeftX=controller.getMapping().axisLeftX;
		axisLeftY=controller.getMapping().axisLeftY;
		axisRightX=controller.getMapping().axisRightX;
		axisRightY=controller.getMapping().axisRightY;
		
		START=controller.getMapping().buttonStart;
		
		if(com.sellgirl.sgGameHelper.SGControllerName.Switch.equals(controller.getName())) {
			//com.badlogic.gdx.controllers.desktop.support.JamepadController
			//Nintendo Switch Pro Controller
			//在安卓电视上变得不正常了,比如十字方向键无支应，怀疑是gdx或安卓版本的问题-benjamin 20250817
			/*
GDX-> A:0 B:1 X:2 Y:3 L1:9 R1:10 L2:-1 R2:-1 L3:7 R3:8 x1:0 y1:1 x2:2 y2:3 x3:- y3:- <:13 >:14 ^:11 v:12
SG->  A:0 B:1 X:2 Y:3 L1:9 R1:10 L2:-1 R2:-1 L3:7 R3:8 x1:0 y1:1 x2:2 y2:3 x3:4 y3:5 <:13 >:14 ^:11 v:12
			 */
			if(223==SGGameHelper.getGameConfig().getGdxControllerVersion()//com.sellgirl.sgGameHelper.GameSetting.gdxControllerVersion
					
					){
				CROSS=controller.getMapping().buttonA;
				ROUND=controller.getMapping().buttonB;
				SQUARE=controller.getMapping().buttonX;
				TRIANGLE=controller.getMapping().buttonY;
			}else{
				//2.2.1版本
				CROSS=controller.getMapping().buttonB;
				ROUND=controller.getMapping().buttonA;
				SQUARE=controller.getMapping().buttonY;
				TRIANGLE=controller.getMapping().buttonX;
			}

			L1=controller.getMapping().buttonL1;
			R1=controller.getMapping().buttonR1;
//			AXISL2=4;
//			AXISR2=5;
			AXISL2=-1;//switch手柄的L2不是轴
			AXISR2=-1;
			BACK=controller.getMapping().buttonBack;
			START=controller.getMapping().buttonStart;
		}else if("com.badlogic.gdx.controllers.android.AndroidController".equals(controller.getClass().getName())
				//&&SGControllerName.PS5ControllerOnAndroidPhone.equals(controller.getName())
				&&SGControllerName.isPS5Controller(controller.getName())
				) {

			/*
GDX-> A:96 B:97 X:99 Y:100 L1:102 R1:103 L2:104 R2:105 L3:106 R3:107 x1:0 y1:1 x2:2 y2:3 x3:- y3:- <:21 >:22 ^:19 v:20 +:108 -:4
			 */
			
			if(ISGPlatformConfig.PlatformType.AndroidTV== SGGameHelper.getPlatform().getPlatformType()) {
				//安卓电视(AndroidTV 12)上的PS5手柄也是--benjamin20250816,20251015
				//以前和手机一样，但换霍尔之后好像修乱了
				//也可能是gdx或安卓版本的问题-benjamin 20250817
				/*
	实际:
	102,a2		103,a3
	99			-
	  19		100
	21  22    96   -
	  20        97
	  			x2:5 y2:4
				 */			
//				CROSS=controller.getMapping().buttonB;
//				ROUND=0;//98;//controller.getMapping().buttonA;
//				SQUARE=controller.getMapping().buttonA;
//				TRIANGLE=100;//controller.getMapping().buttonX;
//				L1=99;//100;//controller.getMapping().buttonL1;
//				R1=101;//controller.getMapping().buttonR1;
	//			AXISL2=3;
	//			AXISR2=4;
//				AXISL2=4;
//				AXISR2=5;
//				AXISL2=2;
//				AXISR2=3;
//				axisRightX=5;
//				axisRightY=4;
//				BACK=104;
//				START=105;//controller.getMapping().buttonStart;
				
				//现在的电视好像又正常了...
				//Android TV 12
				//BRAVIA_VH21_M_CN-user 12 STT2.230505.001.S102681621 release-keys
				AXISL2=5;
				AXISR2=4;
				BACK=109;
//				START=108;
			}else {
			/*
			 * 20251015 OUKITEL手机测试正常
GDX-> A:96 B:97 X:99 Y:100 L1:102 R1:103 L2:104 R2:105 L3:106 R3:107 x1:0 y1:1 x2:2 y2:3 x3:- y3:- <:21 >:22 ^:19 v:20 +:108 -:4
SG->  A:96 B:97 X:99 Y:100 L1:102 R1:103 L2:104 R2:105 L3:106 R3:107 x1:0 y1:1 x2:2 y2:3 x3:4 y3:5 <:21 >:22 ^:19 v:20 +:105 -:104
			 */
				CROSS=controller.getMapping().buttonA;//buttonB;
				ROUND=controller.getMapping().buttonB;//98;
				SQUARE=controller.getMapping().buttonX;
				TRIANGLE=controller.getMapping().buttonY;
				L1=controller.getMapping().buttonL1;//controller.getMapping().buttonL1;
				R1=controller.getMapping().buttonR1;//controller.getMapping().buttonR1;
	//			AXISL2=3;
	//			AXISR2=4;
				AXISL2=4;
				AXISR2=5;
				BACK=109;
				START=controller.getMapping().buttonStart;//controller.getMapping().buttonStart;
			}
		}else if("com.badlogic.gdx.controllers.android.AndroidController".equals(controller.getClass().getName())
				&&SGControllerName.SwitchControllerOnAndroidPhone.equals(controller.getName())) {
			//android手机上识别的Switch手柄(Switch手柄需用数据线连接电源(或插电脑上), 按住手柄上的线插口旁边的小圆钮(不可松开), 灯闪匹配蓝牙)
			CROSS=controller.getMapping().buttonA;
			ROUND=controller.getMapping().buttonB;
			SQUARE=controller.getMapping().buttonX;
			TRIANGLE=controller.getMapping().buttonY;
			L1=controller.getMapping().buttonL1;
			R1=controller.getMapping().buttonR1;
			L2=104;
			R2=105;
			AXISL2=-1;
			AXISR2=-1;
			BACK=109;
			START=controller.getMapping().buttonStart;
		}else {
			CROSS=controller.getMapping().buttonA;
			ROUND=controller.getMapping().buttonB;
			SQUARE=controller.getMapping().buttonX;
			TRIANGLE=controller.getMapping().buttonY;
			L1=controller.getMapping().buttonL1;
			R1=controller.getMapping().buttonR1;
			AXISL2=4;
			AXISR2=5;
			BACK=controller.getMapping().buttonBack;
			START=controller.getMapping().buttonStart;
		}
		
		/**
这里记录一下其它环境下正常的映射情况：
1.win10--PS5手柄
  GDX-> A:0 B:1 X:2 Y:3 L1:9 R1:10 L2:-1 R2:-1 L3:7 R3:8 x1:0 y1:1 x2:2 y2:3 x3:- y3:-
  SG->  A:0 B:1 X:2 Y:3 L1:9 R1:10 L2:-1 R2:-1 L3:7 R3:8 x1:0 y1:1 x2:2 y2:3 x3:4 y3:5
		 */

		if(controller.supportsPlayerIndex()) {
			padUniqueName= controller.getName() + controller.getPlayerIndex();
		}else{
			padUniqueName= controller.getName();
		}
//		if(null!=setting){
//			axisRightSpace=setting.getAxisRightSpace();
//		}else{
//			axisRightSpace=new AxisSpace();
//		}

////		SGPS5GamepadSetting setting= new LocalSaveSettingHelper3().readGameKey(padUniqueName,new SGPS5GamepadSetting());
//		SGPS5GamepadSetting setting= SGGameHelper.getGameConfig().getGamepadSetting(this);
//		if(null!=setting){
//			axisLeftSpace=setting.getAxisLeftSpace();
//			axisRightSpace=setting.getAxisRightSpace();
//		}else{
//			axisLeftSpace=new AxisSpace();
//			axisRightSpace=new AxisSpace();
//		}
		loadSetting();

//		uuid=controller.getUniqueId();//getUniqueId在每次重连手柄时，都会变更，所以不能用于判断Gamepad和Controller的对应关系
//		controllerListener=new SGXInputControllerListener();
////		this.controller.addListener(controllerListener);
//		Controllers.addListener(controllerListener);//connect事件是全局的
//		//自动重连
//		allGamepad=new ArrayList<SGPS5Gamepad>();
//		allGamepad.add(this);
	}
//	protected List<SGPS5Gamepad> allGamepad;
	public void loadSetting() {
		SGPS5GamepadSetting setting= SGGameHelper.getGameConfig().getGamepadSetting(this);
		defaultSetting=true;
		if(null!=setting){
			axisLeftSpace=setting.getAxisLeftSpace();
			axisRightSpace=setting.getAxisRightSpace();
			l2Space=setting.getL2Space();
			r2Space=setting.getR2Space();
			defaultSetting=false;
		}else{
			axisLeftSpace=new AxisSpace();
			axisRightSpace=new AxisSpace();
			l2Space=0.1f;
			r2Space=0.1f;
//			defaultSetting=true;
		}
	}
	private boolean defaultSetting;
	public boolean isDefaultSetting() {
		return defaultSetting;
	}
//	public void applySetting(SGPS5GamepadSetting setting){
//
//	}
	public boolean isCROSS() {
		return controller.getButton(CROSS);
	}
	public boolean isROUND() {
		return controller.getButton(ROUND);
	}
	public boolean isSQUARE() {
		return controller.getButton(SQUARE);
	}
	public boolean isTRIANGLE() {
		return controller.getButton(TRIANGLE);
	}

	@Override
	public boolean isL1() {
		return controller.getButton(L1);
	}
	@Override
	public boolean isR1() {
		return controller.getButton(R1);
	}
	@Override
	public boolean isL2() {
		return l2Space< axisL2();
	}
	@Override
	public boolean isR2() {
		return r2Space< axisR2();
	}
	@Override
	public boolean isL3() {
		return controller.getButton(L3);
	}
	@Override
	public boolean isR3() {
		return controller.getButton(R3);
	}
	@Override
	public boolean isSTART() {
		return controller.getButton(START);
	}
	@Override
	public boolean isBACK() {
		return controller.getButton(BACK);
	}

	@Override
	public boolean isUP() {
		return controller.getButton(UP);
	}
	@Override
	public boolean isDOWN() {
		return controller.getButton(DOWN);
	}
	@Override
	public boolean isLEFT() {
		return controller.getButton(LEFT);
	}
	@Override
	public boolean isRIGHT() {
		return controller.getButton(RIGHT);
	}
	
	public float axisLeftX() {
		return axisLeftSpace.filterX( controller.getAxis(axisLeftX));
	}

	public float axisLeftY() {
		return axisLeftSpace.filterY(controller.getAxis(axisLeftY));
	}

	public float axisRightX() {
		return axisRightSpace.filterX( controller.getAxis(axisRightX));
	}
	public float axisRightY() {
		return axisRightSpace.filterY( controller.getAxis(axisRightY));
	}
	public float axisL2() {
		if(0>AXISL2){//在android中, switch手柄的L2 R2不是轴
			return controller.getButton(L2)?1f:-1f;
		}
//		return controller.getAxis(AXISL2);
		float r=controller.getAxis(AXISL2);
		if(l2Space>r) {
			return 0;
		}else {
			return (r-l2Space)/(1-r);
		}
	}
	public float axisR2() {
		if(0>AXISR2){
			return controller.getButton(R2)?1f:-1f;
		}
//		return controller.getAxis(AXISR2);
		float r=controller.getAxis(AXISR2);
		if(r2Space>r) {
			return 0;
		}else {
			return (r-r2Space)/(1-r);
		}
	}
	public boolean isStick1Up() {
		return axisLeftSpace.isUp(controller.getAxis(axisLeftY));
	}
	public boolean isStick1Down() {
		return axisLeftSpace.isDown(controller.getAxis(axisLeftY));
	}
	public boolean isStick1Left() {
		return axisLeftSpace.isLeft(controller.getAxis(axisLeftX));
	}
	public boolean isStick1Right() {
		return axisLeftSpace.isRight(controller.getAxis(axisLeftX));
	}
	public boolean isStick2Up() {
		return axisRightSpace.isUp(controller.getAxis(axisRightY));
	}
	public boolean isStick2Down() {
		return axisRightSpace.isDown(controller.getAxis(axisRightY));
	}
	public boolean isStick2Left() {
		return axisRightSpace.isLeft(controller.getAxis(axisRightX));
	}
	public boolean isStick2Right() {
		return axisRightSpace.isRight(controller.getAxis(axisRightX));
	}
	@Override
	public String getPadName() {
		return controller.getName();
	}
	@Override
	public String getPadUniqueName() {
//		if(controller.supportsPlayerIndex()) {
//			return controller.getName() + controller.getPlayerIndex();
//		}else{
//			return controller.getName();
//		}
		return padUniqueName;
	}

	@Override
	public int getQuickBtnKey() {


//////		//测试实际的按键
//		float axis=0;
//		for(int i=0;i<200;i++){
//			axis=controller.getAxis(i);
//			if(0.5f<axis//||-0.5f>axis  //注意L2 R2一直是-1的
//			 ){
//				SGDataHelper.getLog().print("i:"+i+" axisOn "+axis);
//			}
//			if(controller.getButton(i)){
//				SGDataHelper.getLog().print("i:"+i+" buttonOn");
//			}
//
//		}
		return getQuickBtnKey(this);
	}

	public enum QuickBtnType implements ISGFlagEnum{
		/**
		 * 常规按键
		 */
		NORMAL,
		/**
		 * 摇杆
		 */
		STICK;

		QuickBtnType() {
			mask = (1 << ordinal());
		}

		public final int mask;
		//就考虑把此方法也整合到PFEnumFlag
		public static boolean isEnabled(int features, QuickBtnType feature) {
			return (features & feature.mask) != 0; // 差不多相当于PFDataHelper.EnumHasFlag
		}
		@Override
		public int getValue() {
			return this.ordinal();
		}

		@Override
		public String getText() {
			return this.name();
		}

		@Override
		public int getBinary() {
			return this.mask;
		}
	}
	public static int getQuickBtnKey(ISGPS5Gamepad gamepad) {
		return SGPS5Gamepad.getQuickBtnKey(gamepad,QuickBtnType.NORMAL.getBinary());
	}
	public static int getQuickBtnKey(ISGPS5Gamepad gamepad,int quickBtnType) {
		//XBoxKey r=null;
		int mask=0;
//		if(SGKeyboardGamepad.class==gamepad.getClass()) {
//			SGKeyboardGamepad.getQuickBtnKey2();
////			if(Gdx.input.isKeyPressed(Keys.A)) {
////				mask|=(1 << Keys.A);
////			}if(Gdx.input.isKeyPressed(Keys.S)) {
////				mask|=(1 << Keys.S);
////			}if(Gdx.input.isKeyPressed(Keys.Z)) {
////				mask|=(1 << Keys.Z);
////			}if(Gdx.input.isKeyPressed(Keys.X)) {
////				mask|=(1 << Keys.X);
////			}if(Gdx.input.isKeyPressed(Keys.C)) {
////				mask|=(1 << Keys.C);
////			}if(Gdx.input.isKeyPressed(Keys.V)) {
////				mask|=(1 << Keys.V);
////			}
//		}else {

			if(gamepad.isSQUARE()) {
				//return XBoxKey.SQUARE;
//				mask=XBoxKey.config(mask, XBoxKey.SQUARE, true);
				mask|= XBoxKey.SQUARE.getBinary();
			}
			if(gamepad.isTRIANGLE()) {
				mask|=XBoxKey.TRIANGLE.getBinary();
			}
			if(gamepad.isCROSS()) {
				mask|=XBoxKey.CROSS.getBinary();
			}
	 if(gamepad.isROUND()) {
				mask|=XBoxKey.ROUND.getBinary();
			}
	 if(gamepad.isL1()) {
				mask|=XBoxKey.L1.getBinary();
			}
	 if(gamepad.isR1()) {
				mask|=XBoxKey.R1.getBinary();
			}

	 if(//0.1<gamepad.axisL2()
			 gamepad.isL2()
			 ) {
				mask|=XBoxKey.L2.getBinary();
			}
	 if(//0.1<gamepad.axisR2()
			 gamepad.isR2()
			 ) {
				mask|=XBoxKey.R2.getBinary();
			}

	 if(gamepad.isL3()) {
				mask|=XBoxKey.L3.getBinary();
			}
	 if(gamepad.isR3()) {
				mask|=XBoxKey.R3.getBinary();
			}

		if(gamepad.isUP()) {
			mask|=XBoxKey.UP.getBinary();
		}
		if(gamepad.isDOWN()) {
			mask|=XBoxKey.DOWN.getBinary();
		}
		if(gamepad.isLEFT()) {
			mask|=XBoxKey.LEFT.getBinary();
		}
		if(gamepad.isRIGHT()) {
			mask|=XBoxKey.RIGHT.getBinary();
		}

		if(gamepad.isBACK()) {
			mask|=XBoxKey.MENU.getBinary();
		}
		if(gamepad.isSTART()) {
			mask|=XBoxKey.START.getBinary();
		}

		if(QuickBtnType.isEnabled(quickBtnType, QuickBtnType.STICK)) {
			if(//-0.1>gamepad.axisLeftY()
					gamepad.isStick1Up()
					) {
				mask|=XBoxKey.stick1Up.getBinary();
			}
			if(//0.1<gamepad.axisLeftY()
					gamepad.isStick1Down()
					) {
				mask|=XBoxKey.stick1Down.getBinary();
			}
			if(//-0.1>gamepad.axisLeftX()
					gamepad.isStick1Left()
					) {
				mask|=XBoxKey.stick1Left.getBinary();
			}
			if(//0.1<gamepad.axisLeftX()
					gamepad.isStick1Right()
					) {
				mask|=XBoxKey.stick1Right.getBinary();
			}
			if(//-0.1>gamepad.axisRightY()
					gamepad.isStick2Up()
					) {
				mask|=XBoxKey.stick2Up.getBinary();
			}
			if(//0.1<gamepad.axisRightY()
					gamepad.isStick2Down()
					) {
				mask|=XBoxKey.stick2Down.getBinary();
			}
			if(//-0.1>gamepad.axisRightX()
					gamepad.isStick2Left()
					) {
				mask|=XBoxKey.stick2Left.getBinary();
			}
			if(//0.1<gamepad.axisRightX()
					gamepad.isStick2Right()
					) {
				mask|=XBoxKey.stick2Right.getBinary();
			}
		}
//		}
		return mask;
	}

	public AxisSpace getAxisLeftSpace() {
		return axisLeftSpace;
	}

	public void setAxisLeftSpace(float x1,float x2,float y1, float y2) {
		axisLeftSpace.x1=x1;
		axisLeftSpace.x2=x2;
		axisLeftSpace.y1=y1;
		axisLeftSpace.y2=y2;
	}
	public AxisSpace getAxisRightSpace() {
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

	/*----------------这些方法是便于用event模式,并修正---------------------*/
	public Controller getController(){
		return controller;
	}
	public void setController(Controller controller){
		this.controller=controller;
	}

	public int getCROSS() {
		return CROSS;
	}

	public int getROUND() {
		return ROUND;
	}

	public int getSQUARE() {
		return SQUARE;
	}

	public int getTRIANGLE() {
		return TRIANGLE;
	}

	public int getL1() {
		return L1;
	}

	public int getR1() {
		return R1;
	}

	public int getL2() {
		return L2;
	}

	public int getR2() {
		return R2;
	}

	public int getL3() {
		return L3;
	}

	public int getR3() {
		return R3;
	}

	public int getBACK() {
		return BACK;
	}

	public int getSTART() {
		return START;
	}

	public int getUP() {
		return UP;
	}

	public int getDOWN() {
		return DOWN;
	}

	public int getLEFT() {
		return LEFT;
	}

	public int getRIGHT() {
		return RIGHT;
	}
	/**
	 * 左摇杆x轴
	 * @return
	 */
	public int getX1() {
		return axisLeftX;
	}
	/**
	 * 左摇杆y轴
	 * @return
	 */
	public int getY1() {
		return axisLeftY;
	}
	/**
	 * 右摇杆x轴
	 * @return
	 */
	public int getX2() {
		return axisRightX;
	}
	/**
	 * 右摇杆y轴
	 * @return
	 */
	public int getY2() {
		return axisRightY;
	}
	/**
	 * L2轴
	 * @return
	 */
	public int getX3() {
		return this.AXISL2;
	}
	/**
	 * R2轴
	 * @return
	 */
	public int getY3() {
		return this.AXISR2;
	}
	

//	 private  class SGXInputControllerListener extends ControllerAdapter  {
//
//			@Override
//			public boolean buttonDown (Controller controller, int buttonIndex) {
////////////		         if(XBoxKey.CROSS.ordinal()==buttonIndex) {
////////////			         Gdx.app.log(TAG, "jump: " +buttonIndex);
////////////		         }
////////				 Gdx.app.log(TAG, "buttonDown: " +buttonIndex);
//////////		         if(buttonIndex==controller.getMapping().buttonA) {
//////////			         //Gdx.app.log(TAG, "jump: " +buttonIndex);
//////////			         //actor.setState(SashaState.JUMP);
//////////		         }
////				SGDataHelper.getLog().print(controller.getName()+"__"+controller.getPlayerIndex()+"__"+ buttonIndex);
//				return false;
//			}
//
//			@Override
//			public boolean buttonUp (Controller controller, int buttonIndex) {
//				return false;
//			}
//			@Override
//			public boolean axisMoved (Controller controller, int axisIndex, float value) {
//////				if(value>0.1||value<-0.1) {
////					if(value>0.5||value<-0.5) {
////
////					 Gdx.app.log(TAG, "axisMoved: " +axisIndex+"__"+value);	
////				}
//				return false;
//			}
//		   @Override
//		   public void connected(Controller controller) {
//			   if(uuid.equals(controller.getUniqueId())) {
//				   SGPS5Gamepad.this.controller=controller;
//				   SGPS5Gamepad.this.controller.addListener(controllerListener);
//				   msg="connected";
//			   }
//		   }
//
//		   @Override
//		   public void disconnected(Controller controller) {
//			   if(SGPS5Gamepad.this.controller==controller) {
//				   SGPS5Gamepad.this.controller.removeListener(controllerListener);
//				   msg="disconnected";
//			   }
//		   }
//		 }
	 protected String msg;
	 public String getMsg() {
		 return msg;
	 }
//	 public String getUUID() {
//		 return uuid;
//	 }
//	 public void setUUID(String uuid) {
//		 this.uuid=uuid;
//	 }
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
