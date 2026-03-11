package org.sellgirlPayHelperNotSpring.model;

import com.sellgirl.sgJavaHelper.sgEnum.IPFEnum;

public enum LiGeHyType implements IPFEnum{
	/**
	 * 卡状态：-3->未开卡；-2->未激活；-1->待激活；0->有效；1->已失效；2->已注销
	 */
	//未开卡("未开卡",-3),未激活("未激活",-2),待激活("待激活",-1),有效("有效",0),Fgs("Fgs",1),Fgs("Fgs",2)
	未开卡(-3),未激活(-2),待激活(-1),有效(0),已失效(1),已注销(2)
	;
	private int _value;
	
	@Override
	public int getValue() {
		return _value;
	}

	@Override
	public String getText() {
		return this.name();
	}
	private LiGeHyType(//String text,
			int value) {
		_value=value;
		//_text=text;	
	}
	public static LiGeHyType initByInt(int i){
		switch (i){
			case -3:
				return  未开卡;
			case -2:
				return  未激活;
			case -1:
				return  待激活;
			case 0:
				return  有效;
			case 1:
				return  已失效;
			case 2:
				return  已注销;
			default:
				return null;
		}
	}
}
