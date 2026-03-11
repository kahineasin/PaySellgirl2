package org.sellgirlPayHelperNotSpring;

import junit.framework.TestCase;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;

import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UncheckEmpty001 extends TestCase {
    public static void initPFHelper() {
        SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
        new SGDataHelper(new PFAppConfig());
    }
    public void testXx() throws Exception{
        initPFHelper();
        //...
    }
}
