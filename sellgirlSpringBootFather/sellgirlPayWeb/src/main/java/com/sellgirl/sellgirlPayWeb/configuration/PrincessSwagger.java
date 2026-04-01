package com.sellgirl.sellgirlPayWeb.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.bind.annotation.RestController;

import com.sellgirl.sellgirlPayWeb.PayShopSwaggerAttr;
import com.sellgirl.sellgirlPayWeb.PrincessSwaggerAttr;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by fangzhipeng on 2017/4/17.
 * 访问地址 http://127.0.0.1:8080/v2/api-docs?group=princess
 */
@Configuration
@EnableSwagger2
public class PrincessSwagger {

    @Bean
    public Docket createSellerRestApi() {
        return new Docket(
//        		DocumentationType.SPRING_WEB
        		DocumentationType.SWAGGER_2
        		)
        		//似乎要定义这个才能建多版本的文档.
        		//http://127.0.0.1:8080/v2/api-docs?group=princess
        		.groupName("princess")
                .apiInfo(apiInfo())
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.perfect99.pfOpeningUpService.controller"))//会包含子package:com.perfect99.pfOpeningUpService.controller
//                .apis(RequestHandlerSelectors.withClassAnnotation(HealthSwaggerAttr.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(PrincessSwaggerAttr.class))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("princess接口文档")
                .description("By Benjamin")
                .termsOfServiceUrl("完美中国")
//                .licenseUrl("111")
                //.license("111")
                .version("1.0")
                .build();
    }
    

    @Bean
    public Docket createShopRestApi() {
        return new Docket(
//        		DocumentationType.SPRING_WEB
        		DocumentationType.SWAGGER_2
        		)
        		//似乎要定义这个才能建多版本的文档.
        		//http://127.0.0.1:8080/v2/api-docs?group=princess
        		.groupName("shop")
                .apiInfo(shopApiInfo())
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.perfect99.pfOpeningUpService.controller"))//会包含子package:com.perfect99.pfOpeningUpService.controller
//                .apis(RequestHandlerSelectors.withClassAnnotation(HealthSwaggerAttr.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(PayShopSwaggerAttr.class))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo shopApiInfo() {
        return new ApiInfoBuilder()
                .title("sgshop接口文档")
                .description("By Benjamin")
                .termsOfServiceUrl("me")
//                .licenseUrl("111")
                //.license("111")
                .version("1.0")
                .build();
    }
}
