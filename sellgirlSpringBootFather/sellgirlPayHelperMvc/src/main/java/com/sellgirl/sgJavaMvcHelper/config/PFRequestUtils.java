package com.sellgirl.sgJavaMvcHelper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Component
public class PFRequestUtils {

    private static HttpServletRequest request;

    @Autowired
    private HttpServletRequest request2;

    @PostConstruct
    public void beforeInit() {
        request=request2;
    }

    /**
     * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     *
     * @param request
     * @param name
     * @return
     */
    public static Map<String, String[]> getParam() {
        if(request!=null) {
        	return request.getParameterMap();
        }
        return null;
    }
}
