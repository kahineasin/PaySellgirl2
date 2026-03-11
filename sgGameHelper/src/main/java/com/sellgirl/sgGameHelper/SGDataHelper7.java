package com.sellgirl.sgGameHelper;

import java.util.List;

/**
 * jdk1.7中没有的方法
 */
public class SGDataHelper7 {
    public static String join(CharSequence delimiter, CharSequence... elements) {
//		// 空指针判断
//		Objects.requireNonNull(delimiter);
//		Objects.requireNonNull(elements);

        // Number of elements not likely worth Arrays.stream overhead.
        // 此处用到了StringJoiner(JDK 8引入的类）
        // 先构造一个以参数delimiter为分隔符的StringJoiner对象
        StringBuilder joiner = new StringBuilder();
        boolean isFirst=true;
        for (CharSequence cs: elements) {
            // 拼接字符
            if(isFirst){
                isFirst=false;
            }else{
                joiner.append(delimiter);
            }
            joiner.append(cs);
        }
        return joiner.toString();
    }
    public static String join(CharSequence delimiter, List<String> elements) {
//		// 空指针判断
//		Objects.requireNonNull(delimiter);
//		Objects.requireNonNull(elements);

        // Number of elements not likely worth Arrays.stream overhead.
        // 此处用到了StringJoiner(JDK 8引入的类）
        // 先构造一个以参数delimiter为分隔符的StringJoiner对象
        StringBuilder joiner = new StringBuilder();
        boolean isFirst=true;
        for (CharSequence cs: elements) {
            // 拼接字符
            if(isFirst){
                isFirst=false;
            }else{
                joiner.append(delimiter);
            }
            joiner.append(cs);
        }
        return joiner.toString();
    }
}
