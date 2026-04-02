package com.sellgirl.sellgirlPayWeb.shop.model;

import org.springframework.beans.factory.annotation.Value;

import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.SuccessApiResult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="通用响应结果")
public class SuccessApiResult2<T> extends SuccessApiResult<T> {
	@Override
	@ApiModelProperty(value="是否成功",example="true")
	public Boolean getSuccess() {
		return super.getSuccess();
	}
	@Override
	@ApiModelProperty(value="成功/失败信息",example="恭喜,更新成功!")
	public String getMsg() {
		return super.getMsg();
	}
}
