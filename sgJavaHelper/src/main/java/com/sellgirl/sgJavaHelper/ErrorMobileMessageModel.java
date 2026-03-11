package com.sellgirl.sgJavaHelper;


public class ErrorMobileMessageModel{
	private String mobile;
	private String message;
	public ErrorMobileMessageModel(String mobile, String message) {
		super();
		this.mobile = mobile;
		this.message = message;
	}
	//private PFDate lastSentTime;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
//	public PFDate getLastSentTime() {
//		return lastSentTime;
//	}
//	public void setLastSentTime(PFDate lastSentTime) {
//		this.lastSentTime = lastSentTime;
//	}
    @Override
    public int hashCode() {  
        return this.mobile != null ? this.mobile.hashCode() : 0;  
    }  

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ErrorMobileMessageModel) {
			ErrorMobileMessageModel other = (ErrorMobileMessageModel) obj;
			return other.mobile.equals( mobile) && other.message.equals( message)
				;
		}
		return false;
	}

}