package com.sellgirl.sellgirlPayWeb.model;


public interface IGamePadSetting {

	/**
	 * @deprecated
	 * @return
	 */
	 HomeGame getGame() ;
	 String getGameName() ;
	 /**
	  * L2
	  * @return
	  */
	 GamePadCustomAction[] getZL() ;
	 /**
	  * L1
	  * @return
	  */
	 GamePadCustomAction[] getL() ;
	 /**
	  * R2
	  * @return
	  */
	 GamePadCustomAction[] getZR() ;
	 /**
	  * R1
	  * @return
	  */
	 GamePadCustomAction[] getR();
	 /**
	  * 相当于PS5的TRIANGLE
	  * @return
	  */
	 GamePadCustomAction[] getX() ;
	 /**
	  * 相当于PS5的SQUARE
	  * @return
	  */
	 GamePadCustomAction[] getY() ;
	 /**
	  * 相当于PS5的ROUND
	  * @return
	  */
	 GamePadCustomAction[] getA() ;
	 /**
	  * 相当于PS5的CROSS
	  * @return
	  */
	 GamePadCustomAction[] getB() ;
	 GamePadCustomAction[] getUP();
	 GamePadCustomAction[] getDOWN();
	 GamePadCustomAction[] getLEFT();
	 GamePadCustomAction[] getRIGHT();
	 /**
	  * 左摇杆下压
	  * @return
	  */
	 GamePadCustomAction[] getLS() ;
	 GamePadCustomAction[] getRS() ;
}
