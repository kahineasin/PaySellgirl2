package com.sellgirl.sellgirlPayWeb.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;

/**
 * 手柄设置
 * @author li
 *
 */
public class GamePadSetting {
//	private HomeGame game;
//	private String gameName;
//	private GamePadCustomAction[] ZL;
//	private GamePadCustomAction[] L;
//	private GamePadCustomAction[] ZR;
//	private GamePadCustomAction[] R;
//	private GamePadCustomAction[] X;
//	private GamePadCustomAction[] Y;
//	private GamePadCustomAction[] A;
//	private GamePadCustomAction[] B;
//	private GamePadCustomAction[] UP;
//	private GamePadCustomAction[] DOWN;
//	private GamePadCustomAction[] LEFT;
//	private GamePadCustomAction[] RIGHT;
//
//
//	public HomeGame getGame() {
//		return game;
//	}
//	public void setGame(HomeGame game) {
//		this.game = game;
//	}
//	public String getGameName() {
//		return gameName;
//	}
//	public void setGameName(String gameName) {
//		this.gameName = gameName;
//	}
//	public GamePadCustomAction[] getZL() {
//		return ZL;
//	}
//	public void setZL(GamePadCustomAction[] zL) {
//		ZL = zL;
//	}
//	public GamePadCustomAction[] getL() {
//		return L;
//	}
//	public void setL(GamePadCustomAction[] l) {
//		L = l;
//	}
//	public GamePadCustomAction[] getZR() {
//		return ZR;
//	}
//	public void setZR(GamePadCustomAction[] zR) {
//		ZR = zR;
//	}
//	public GamePadCustomAction[] getR() {
//		return R;
//	}
//	public void setR(GamePadCustomAction[] r) {
//		R = r;
//	}
//	public GamePadCustomAction[] getX() {
//		return X;
//	}
//	public void setX(GamePadCustomAction[] x) {
//		X = x;
//	}
//	public GamePadCustomAction[] getY() {
//		return Y;
//	}
//	public void setY(GamePadCustomAction[] y) {
//		Y = y;
//	}
//	public GamePadCustomAction[] getA() {
//		return A;
//	}
//	public void setA(GamePadCustomAction[] a) {
//		A = a;
//	}
//	public GamePadCustomAction[] getB() {
//		return B;
//	}
//	public void setB(GamePadCustomAction[] b) {
//		B = b;
//	}
//	public GamePadCustomAction[] getUP() {
//		return UP;
//	}
//	public void setUP(GamePadCustomAction[] uP) {
//		UP = uP;
//	}
//	public GamePadCustomAction[] getDOWN() {
//		return DOWN;
//	}
//	public void setDOWN(GamePadCustomAction[] dOWN) {
//		DOWN = dOWN;
//	}
//	public GamePadCustomAction[] getLEFT() {
//		return LEFT;
//	}
//	public void setLEFT(GamePadCustomAction[] lEFT) {
//		LEFT = lEFT;
//	}
//	public GamePadCustomAction[] getRIGHT() {
//		return RIGHT;
//	}
//	public void setRIGHT(GamePadCustomAction[] rIGHT) {
//		RIGHT = rIGHT;
//	}
//	@Deprecated
//	public static GamePadSetting Zelda(){
//		GamePadSetting r=new GamePadSetting();
//		r.setGame(HomeGame.Zelda);
//		r.setGameName("塞尔达");
//		r.setZL(new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.防御)});
//		r.setZR(new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.射击)});
//		r.setR( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.投  )});
//		r.setX( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.奔跑)});
//		r.setY( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.攻击)});
//		r.setA( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.确定)});
//		r.setB( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.跳),new GamePadCustomAction(GamePadAction.取消)});
//		r.setLEFT(new GamePadCustomAction[] {new GamePadCustomAction("盾切换")});
//		r.setRIGHT(new GamePadCustomAction[] {new GamePadCustomAction("剑切换")});
//		return r;
//	} 
//	@Deprecated
//	public static GamePadSetting SuperSmashBros(){
//		GamePadSetting r=new GamePadSetting();
//		r.setGame(HomeGame.SuperSmashBros);
//		r.setGameName("任天堂大乱斗");
//		r.setZL(new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.防御)});
//		r.setZR(new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.投)});
//		//r.setR( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.投  )});
//		//r.setX( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.奔跑)});
//		r.setY( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.攻击)});
//		r.setA( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.特殊攻击)});
//		r.setB( new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.跳)});
//		//r.setLEFT(new GamePadCustomAction[] {new GamePadCustomAction("盾切换")});
//		//r.setRIGHT(new GamePadCustomAction[] {new GamePadCustomAction("剑切换")});
//		return r;
//	} 
//	/**
//	 * 这种方式容易忘记在这里加
//	 * @return
//	 */
//	@Deprecated
//	public static GamePadSetting[] AllGame() {
//		return new GamePadSetting[] {Zelda(),SuperSmashBros()};
//	}
//	public static Map<String, IGamePadSetting> AllIGame() {
//		return PFDataHelper.GetClassByInterfaceT(IGamePadSetting.class);
//	}
	public static <T extends IGamePadSetting> Map<String, T> AllIGame(Class<T> gameCls) {
		return SGDataHelper.GetClassByInterfaceT(gameCls);
	}
	public static String[] AllBtnKey() {
		//Object funs=IGamePadSetting.class.getMethods();
		Object t1=(new GamePadCustomAction[1]).getClass();
//		Object t2=IGamePadSetting.class.getDeclaredMethods()[0].getAnnotatedReturnType().getType();
//		Boolean b=t1==t2;
//		Method[] funs=PFDataHelper.<Method>ArrayWhere(Method.class, IGamePadSetting.class.getMethods(), a->a.getAnnotatedReturnType().getType().toString().equals("class [Lcom.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;"));
		Method[] funs=SGDataHelper.<Method>ArrayWhere(Method.class, IGamePadSetting.class.getMethods(), a->a.getAnnotatedReturnType().getType()==t1);
		
		return SGDataHelper.ArraySelect(String.class, funs, a->a.getName().replace("get", ""));
		//return new String[] {"X","Y","A","B","L","R","ZL","ZR"};
	}
//	public static GamePadSetting GetGame(String gameName) {
//		GamePadSetting[] games=AllGame();
//		for(GamePadSetting game:games) {
//			if(gameName.equals(game.getGameName())) {
//				return game;
//			}
//		}
//		return null;
//	}
//	@Deprecated
//	public static GamePadSetting GetGame(HomeGame gameName) {
//		GamePadSetting[] games=AllGame();
//		for(GamePadSetting game:games) {
//			if(gameName==game.getGame()) {
//				return game;
//			}
//		}
//		return null;
//	}
	/**
	 * @deprecated
	 * @param gameName
	 * @param gameCls
	 * @return
	 */
	public static <T extends IGamePadSetting> T GetIGame(HomeGame gameName,Class<T> gameCls) {
		Map<String, T> m=AllIGame(gameCls);
//		if(m.containsKey(gameName.toString())) {
//			return m.get(gameName.toString());
//		}
//		if(m.containsKey(gameName.toString().toLowerCase())) {
//			return m.get(gameName.toString().toLowerCase());
//		}
		   Iterator<Entry<String, T>> iter = m.entrySet().iterator();
		   while(iter.hasNext()){
			   Entry<String, T> key=iter.next();
			   if(key.getKey().toLowerCase().equals(gameName.toString().toLowerCase())) {
				   return key.getValue();
			   }
		   }
		return null;
	}
	public static <T extends IGamePadSetting> T GetIGame(String gameName,Class<T> gameCls) {
		Map<String, T> m=AllIGame(gameCls);
//		if(m.containsKey(gameName.toString())) {
//			return m.get(gameName.toString());
//		}
//		if(m.containsKey(gameName.toString().toLowerCase())) {
//			return m.get(gameName.toString().toLowerCase());
//		}
		   Iterator<Entry<String, T>> iter = m.entrySet().iterator();
		   while(iter.hasNext()){
			   Entry<String, T> key=iter.next();
			   if(key.getKey().toLowerCase().equals(gameName.toLowerCase())) {
				   return key.getValue();
			   }
		   }
		return null;
	}
	/**
	 * 获得某功能键设置在某btn上的次数
	 * @param gameName
	 * @return
	 */
	public static int GetActionOnBtnTime(
			Class<IGamePadSetting> gameCls,
			GamePadAction action,
			SGFunc<IGamePadSetting,Object,Object,GamePadCustomAction[]> btn) {
		Map<String, IGamePadSetting> m=AllIGame(gameCls);
		int qty=0;
//		if(m.containsKey(gameName.toString())) {
//			return m.get(gameName.toString());
//		}
//		if(m.containsKey(gameName.toString().toLowerCase())) {
//			return m.get(gameName.toString().toLowerCase());
//		}
		   Iterator<Entry<String, IGamePadSetting>> iter = m.entrySet().iterator();
		   while(iter.hasNext()){
			   Entry<String, IGamePadSetting> key=iter.next();
			   GamePadCustomAction[] tmpBtn=btn.go(key.getValue(), null,null);
			   if(tmpBtn!=null&&SGDataHelper.ArrayAny(tmpBtn, a->action.toString().equals(a.toString()))) {
				   qty++;
			   }
		   }
		return qty;
	}
	public static <T extends IGamePadSetting> List<T> GetActionOnBtnOfAllGame(
			Class<T> gameCls,
			GamePadAction action,
			SGFunc<IGamePadSetting,Object,Object,GamePadCustomAction[]> btn) {
		Map<String, T> m=AllIGame(gameCls);
		//int qty=0;
		List<T> r=new ArrayList<T>();

		   Iterator<Entry<String, T>> iter = m.entrySet().iterator();
		   while(iter.hasNext()){
			   Entry<String, T> key=iter.next();
			   GamePadCustomAction[] tmpBtn=btn.go(key.getValue(), null,null);
			   if(tmpBtn!=null&&SGDataHelper.ArrayAny(tmpBtn, a->action.toString().equals(a.toString()))) {
				   //qty++;
				   r.add(key.getValue());
			   }
		   }
		return r;
	}
	public static String GetBtnText(GamePadCustomAction[] action) {
		if(action==null) {return "";}
		List<String> list=new ArrayList<String>();
		for(GamePadCustomAction i :action) {
			list.add(i.toString());
		}
		return String.join(",", list);
	}
	public static GamePadCustomAction[] GetBtnByText(IGamePadSetting game,String text) {
		Object b;
		try {
			b =game.getClass().getMethod("get"+text).invoke(game);
			//b = game.getClass().getMethod("get"+text)..get(game);
//			Field field= game.getClass().getField(text);
//			field.setAccessible(true);
//			b =field.get(game);
			if(b!=null) {
				return (GamePadCustomAction[])b;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
//	public static String GetNormalBtnText(GamePadCustomAction[] action) {
//		if(action==null) {return "";}
//		List<String> list=new ArrayList<String>();
//		for(GamePadCustomAction i :action) {
//			list.add(i.toString());
//		}
//		return String.join(",", list);
//	}
}
