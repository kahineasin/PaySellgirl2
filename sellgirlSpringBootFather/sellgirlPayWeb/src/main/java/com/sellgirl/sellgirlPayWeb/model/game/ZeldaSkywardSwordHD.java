package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class ZeldaSkywardSwordHD implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return HomeGame.ZeldaSkywardSwordHD;
	}

	@Override
	public String getGameName() {
		return "塞尔达御天之剑HD";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {new GamePadCustomAction("注视锁定")};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getZR() {
		// TODO Auto-generated method stub
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.射击)};
	}

	@Override
	public GamePadCustomAction[] getR() {
		// TODO Auto-generated method stub
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.道具快捷菜单)};
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction("体感角度校正")};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return new GamePadCustomAction[] {new GamePadCustomAction("动作"),new GamePadCustomAction(GamePadAction.OK)};
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash),new GamePadCustomAction(GamePadAction.收起武器),new GamePadCustomAction(GamePadAction.Cancel)};
	}

	@Override
	public GamePadCustomAction[] getUP() {
		return new GamePadCustomAction[] {new GamePadCustomAction("环视"),new GamePadCustomAction("探测")};
	}

	@Override
	public GamePadCustomAction[] getDOWN() {
		return new GamePadCustomAction[] {new GamePadCustomAction("口哨")};
	}

	@Override
	public GamePadCustomAction[] getLEFT() {
		return new GamePadCustomAction[] {new GamePadCustomAction("盾切换")};
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return new GamePadCustomAction[] {new GamePadCustomAction("说明")};
	}

	@Override
	public GamePadCustomAction[] getLS() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GamePadCustomAction[] getRS() {
		// TODO Auto-generated method stub
		return null;
	}

}
