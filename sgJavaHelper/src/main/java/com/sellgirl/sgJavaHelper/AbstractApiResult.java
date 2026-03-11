package com.sellgirl.sgJavaHelper;

/*
 * 此类参考 wisesystem-bns 项目
 */
public abstract class AbstractApiResult<T> {


    protected int code;
    protected Boolean success;
    protected T data;
    protected String msg;

    public static AbstractApiResult<Object> success() {
        return new SuccessApiResult<Object>();
    }
    public static <T> AbstractApiResult<T> success(T data) {
        return new SuccessApiResult<T>(data);
    }
    public static <T> AbstractApiResult<T> success(String msg,T data) {
        return new SuccessApiResult<T>(msg,data);
    }
    public static <T>AbstractApiResult<T> success(Integer code,String msg,T data) {
        return new SuccessApiResult<T>(code,msg,data);
    }

    /**
     * 错误返回
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @return 错误返回体
     */
    public static AbstractApiResult<Object> error(int errorCode, String errorMessage) {
        return new ErrorApiResult<Object>(errorCode, errorMessage);
    }
    public static AbstractApiResult<Object> error(String errorMessage) {
        return new ErrorApiResult<Object>( errorMessage);
    }
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
    

}