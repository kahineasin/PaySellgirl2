package com.sellgirl.sellgirlPayWeb.oAuth.model;

//import com.perfect99.right.amsweb.BaseReturnInfo;

/**
 * 在sgShop中，此类可用作第三方登陆api的返回类型，当前是用自己的数据库验证用户
 * 
 * @Author ： ChenChao
 * @Create Date： 2020-11-26 14:36:30
 * @Description：
 * @Modified By：
 * @Version ：
 */
public class BaseReturnInfoDto {

	
    private String ShortNumber;
	private String FranchiserName;

    private String FranchiserNumber;

    private String OrgId;

    private String OrgName;

    private String OrgNumber;

    private boolean IsSuccess;

    private String Message;

    private String UserId;

    private String UserName;

    private String UserNumber;

    private String ZSNumber;

    private String YZNumber;

    private String OrgExtNumber;

    private String Precinct;

    private String PasswordError;

    public BaseReturnInfoDto() {}
    public BaseReturnInfoDto(BaseReturnInfoDto user) {
    	ShortNumber=user.getShortNumber();
    	FranchiserName=user.getFranchiserName();
    	FranchiserNumber=user.getFranchiserNumber();
    	OrgId=user.getOrgId();
    	OrgName=user.getOrgName();
    	OrgNumber=user.getOrgNumber();
    	IsSuccess=user.isIsSuccess();
    	Message=user.getMessage();
    	UserId=user.getUserId();
    	UserName=user.getUserName();
    	UserNumber=user.getUserNumber();
    	ZSNumber=user.getZSNumber();
    	YZNumber=user.getYZNumber();
    	OrgExtNumber=user.getOrgExtNumber();
    	Precinct=user.getPrecinct();
    	PasswordError=user.getPasswordError();
    }
    public String getShortNumber() {
		return ShortNumber;
	}

	public void setShortNumber(String shortNumber) {
		ShortNumber = shortNumber;
	}

	public String getFranchiserName() {
		return FranchiserName;
	}

	public void setFranchiserName(String franchiserName) {
		FranchiserName = franchiserName;
	}

	public String getFranchiserNumber() {
		return FranchiserNumber;
	}

	public void setFranchiserNumber(String franchiserNumber) {
		FranchiserNumber = franchiserNumber;
	}

	public String getOrgId() {
		return OrgId;
	}

	public void setOrgId(String orgId) {
		OrgId = orgId;
	}

	public String getOrgName() {
		return OrgName;
	}

	public void setOrgName(String orgName) {
		OrgName = orgName;
	}

	public String getOrgNumber() {
		return OrgNumber;
	}

	public void setOrgNumber(String orgNumber) {
		OrgNumber = orgNumber;
	}

	public boolean isIsSuccess() {
		return IsSuccess;
	}

	public void setIsSuccess(boolean isSuccess) {
		IsSuccess = isSuccess;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserNumber() {
		return UserNumber;
	}

	public void setUserNumber(String userNumber) {
		UserNumber = userNumber;
	}

	public String getZSNumber() {
		return ZSNumber;
	}

	public void setZSNumber(String zSNumber) {
		ZSNumber = zSNumber;
	}

	public String getYZNumber() {
		return YZNumber;
	}

	public void setYZNumber(String yZNumber) {
		YZNumber = yZNumber;
	}

	public String getOrgExtNumber() {
		return OrgExtNumber;
	}

	public void setOrgExtNumber(String orgExtNumber) {
		OrgExtNumber = orgExtNumber;
	}

	public String getPrecinct() {
		return Precinct;
	}

	public void setPrecinct(String precinct) {
		Precinct = precinct;
	}

	public String getPasswordError() {
		return PasswordError;
	}

	public void setPasswordError(String passwordError) {
		PasswordError = passwordError;
	}

}
