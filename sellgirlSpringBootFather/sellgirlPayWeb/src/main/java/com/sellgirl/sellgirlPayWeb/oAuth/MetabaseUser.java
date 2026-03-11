package com.sellgirl.sellgirlPayWeb.oAuth;

public class MetabaseUser {

    public MetabaseUser( String first_name, String last_name,String email) {
		super();
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
	}
	public String email ;
    public String first_name ;
    public String last_name ;
}
