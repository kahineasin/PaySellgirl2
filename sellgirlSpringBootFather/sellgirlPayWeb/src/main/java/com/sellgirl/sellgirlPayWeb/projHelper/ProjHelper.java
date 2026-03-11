package com.sellgirl.sellgirlPayWeb.projHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
public class ProjHelper  {
    private static final HanyuPinyinOutputFormat FORMAT = new HanyuPinyinOutputFormat();
    
    static {
        FORMAT.setCaseType(HanyuPinyinCaseType.UPPERCASE);   // 输出大写
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 不带声调
    }

    /**
     * 获取汉字字符串的第一个字符的拼音首字母
     * @param chinese 中文字符串（可包含英文数字等）
     * @return 大写首字母，若非中英文则返回 '#'
     */
    public static String getFirstLetter(String chinese) {
        if (chinese == null || chinese.isEmpty()) {
            return "#";
        }
        char firstChar = chinese.charAt(0);
        
        // 如果首字符是英文字母，直接返回大写
        if ((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z')) {
            return String.valueOf(Character.toUpperCase(firstChar));
        }
        
        // 尝试转换为拼音（仅处理汉字）
        try {
            String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(firstChar, FORMAT);
            if (pinyins != null && pinyins.length > 0) {
                // 对于多音字，通常取第一个拼音的首字母（可根据业务调整）
                return String.valueOf(pinyins[0].charAt(0));
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            // 日志记录异常
            e.printStackTrace();
        }
        
        // 非中英文字符，返回 '#'
        return "#";
    }
    
    // 测试
    public static void main(String[] args) {
        System.out.println(getFirstLetter("北")); // B
        System.out.println(getFirstLetter("朱")); // Z
        System.out.println(getFirstLetter("重庆")); // C (取第一个字“重”的拼音首字母，通常为C)
        System.out.println(getFirstLetter("银行")); // Y
    }
}