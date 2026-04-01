//package com.sellgirl.sellgirlPayWeb.configuration;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * Created by fangzhipeng on 2017/4/17.
// * 访问http://127.0.0.1:8080/v2/api-docs
// */
//@Configuration
//@EnableSwagger2
//public class Swagger3 {
//
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(
//        		DocumentationType.SWAGGER_2
//        		)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.sellgirl.sellgirlPayWeb.apiController"))//会包含子package:com.ideaspringboot.perfect.controller
//                .paths(PathSelectors.any())
//                .build();
//    }
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("springboot利用swagger构建api文档")
//                .description("简单优雅的restfun风格")
//                .termsOfServiceUrl("http://blog.csdn.net/forezp")
//                .version("1.0")
//                .build();
//    }
//}
