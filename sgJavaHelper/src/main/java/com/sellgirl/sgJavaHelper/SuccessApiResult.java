package com.sellgirl.sgJavaHelper;

public class SuccessApiResult<T> extends AbstractApiResult<T> {

    public SuccessApiResult() {
        this.success=true;
        this.code = 200;
    }
    public SuccessApiResult(T data) {
        this.success=true;
        this.code = 200;
        this.data = data;
    }
    public SuccessApiResult(String msg) {
        this.success=true;
        this.code =200;
        this.msg = msg;
    }
    SuccessApiResult(String msg,T data) {
        this.success=true;
        this.code = 200;
        this.data = data;
        this.msg = msg;
    }

    SuccessApiResult(Integer code,String msg, T data) {
        this.success=true;
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

}
