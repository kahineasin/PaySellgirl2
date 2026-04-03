package com.sellgirl.sellgirlPayService.product.model;

/**
 * 支付的产品类型,注意命名要和ResourceType的统一,因为用于在api接受参数时自动转换了
 */
public enum ProductType {
	book,
	movie,image,comic
}
