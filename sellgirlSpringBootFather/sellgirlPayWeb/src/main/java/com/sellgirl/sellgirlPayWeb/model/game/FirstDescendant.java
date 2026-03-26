package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
//import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class FirstDescendant implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return null;
	}

	@Override
	public String getGameName() {
		return "第一后裔";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.射击),new GamePadCustomAction(GamePadAction.Defend)};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.技能快捷菜单)};
	}

	@Override
	public GamePadCustomAction[] getZR() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.显示准星),new GamePadCustomAction("近战特殊攻击")};
	}

	@Override
	public GamePadCustomAction[] getR() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.爪钩飞索)};
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.ChangeWeapon),new GamePadCustomAction(GamePadAction.B4)};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.补充弹药),new GamePadCustomAction(GamePadAction.B3),new GamePadCustomAction(GamePadAction.LATK)};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dodge),new GamePadCustomAction(GamePadAction.B2)};
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Jump),new GamePadCustomAction(GamePadAction.B1)};
	}

	@Override
	public GamePadCustomAction[] getUP() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.敌人位置脉冲)};
	}

	@Override
	public GamePadCustomAction[] getDOWN() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.交互),new GamePadCustomAction(GamePadAction.救助同伴)};
	}

	@Override
	public GamePadCustomAction[] getLEFT() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.输入聊天)};
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return new GamePadCustomAction[] {new GamePadCustomAction("前往匹配介面"),new GamePadCustomAction(GamePadAction.放置标记) };
	}

	@Override
	public GamePadCustomAction[] getLS() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash)};
	}

	@Override
	public GamePadCustomAction[] getRS() {
		return new GamePadCustomAction[] {};
	}

}
