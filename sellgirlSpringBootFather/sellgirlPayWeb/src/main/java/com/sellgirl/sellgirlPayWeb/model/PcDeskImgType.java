package com.sellgirl.sellgirlPayWeb.model;

import com.sellgirl.sgJavaHelper.PFEnumClass;

public final class PcDeskImgType extends PFEnumClass{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3185479406481624696L;
	public PcDeskImgType(String text, int value) {
		super(text, value);

	}
	
	public final static PcDeskImgType PrincessSasha=new PcDeskImgType("PrincessSasha",0);
	public final static PcDeskImgType Neris=new PcDeskImgType("Neris",1);
	public final static PcDeskImgType Normal=new PcDeskImgType("Normal",1<<1);
	public final static PcDeskImgType Athena=new PcDeskImgType("Athena",1<<2);
	public final static PcDeskImgType Alice=new PcDeskImgType("Alice",1<<3);
	public final static PcDeskImgType AliceGk=new PcDeskImgType("AliceGk",1<<4);
	public final static PcDeskImgType AliceGkNaked=new PcDeskImgType("AliceGkNaked",1<<5);
	public final static PcDeskImgType Asuna=new PcDeskImgType("Asuna",1<<6);
	public final static PcDeskImgType PrincessAlice=new PcDeskImgType("PrincessAlice",1<<7);
	public final static PcDeskImgType Tiara=new PcDeskImgType("Tiara",1<<8);
	public final static PcDeskImgType TiaraNude=new PcDeskImgType("TiaraNude",1<<9);
	public final static PcDeskImgType SashaCross=new PcDeskImgType("SashaCross",1<<10);//莎莎被绑十字架上
	

}
