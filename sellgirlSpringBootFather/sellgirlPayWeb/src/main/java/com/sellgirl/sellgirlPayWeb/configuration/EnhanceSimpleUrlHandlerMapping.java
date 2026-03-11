//package com.sellgirl.sellgirlPayWeb.configuration;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
//
//import com.sellgirl.sellgirlPayWeb.controller.PrincessController;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author hujunzheng
// *https://www.cnblogs.com/hujunzheng/p/9902475.html
// * @create 2018-08-10 13:57
// **/
//public class EnhanceSimpleUrlHandlerMapping extends SimpleUrlHandlerMapping {
//
//    public EnhanceSimpleUrlHandlerMapping(List<PrincessController> controllers) {
//        if (CollectionUtils.isEmpty(controllers)) {//NOSONAR
//            return;
//        }
//
//        Map<String, PrincessController> urlMappings = new HashMap<>();
//        controllers.forEach(controller -> {
//            String basePath = controller.getBasePath();
//            if (StringUtils.isNotBlank(basePath)) {
//                if (!basePath.endsWith("/*")) {
//                    basePath = basePath + "/*";
//                }
//                urlMappings.put(basePath, controller);
//            }
//        });
//        this.setUrlMap(urlMappings);
//    }
//}