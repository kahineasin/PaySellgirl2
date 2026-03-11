package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class SwordArtOnline implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return HomeGame.SwordArtOnline;
	}

	@Override
	public String getGameName() {
		return "刀剑神域*虚空幻界";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return GamePadCustomAction.Init(GamePadAction.收起武器);
	}

	@Override
	public GamePadCustomAction[] getL() {
		return GamePadCustomAction.Init(GamePadAction.Defend);
	}

	@Override
	public GamePadCustomAction[] getZR() {
		return new GamePadCustomAction[] {new GamePadCustomAction("Good")};
	}

	@Override
	public GamePadCustomAction[] getR() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash )};
	}

	@Override
	public GamePadCustomAction[] getX() {
		return GamePadCustomAction.Init(GamePadAction.特殊攻击);
	}

	@Override
	public GamePadCustomAction[] getY() {
		return GamePadCustomAction.Init(GamePadAction.LATK);
	}

	@Override
	public GamePadCustomAction[] getA() {
		return GamePadCustomAction.Init(GamePadAction.特殊攻击);
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Jump)};
	}

	@Override
	public GamePadCustomAction[] getUP() {
		return new GamePadCustomAction[] {new GamePadCustomAction("地图远近")};
	}

	@Override
	public GamePadCustomAction[] getDOWN() {
		return new GamePadCustomAction[] {new GamePadCustomAction("视角高低")};
	}

	@Override
	public GamePadCustomAction[] getLEFT() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getLS() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.锁定目标)};
	}

	@Override
	public GamePadCustomAction[] getRS() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.重置视角)};
	}

}
