package com.sellgirl.sgJavaHelper;

/*
 * rest返回类型,T是为了以后便于错误时也返回复杂类型的情况
 */
public class ErrorApiResult<T> extends AbstractApiResult<T> {

    public ErrorApiResult(int code, String msg) {
        this.success=false;
        this.code = code;
        this.msg = msg;
    }

    public ErrorApiResult( String msg) {
        this.success=false;
//        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = 200;
        this.msg = msg;
    }
}