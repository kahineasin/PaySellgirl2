package org.sellgirlPayHelperNotSpring.model;

import java.math.BigDecimal;
/**
 * https://www.jb51.net/article/264401.htm
 * @author 1011712002
 *
 */
public class ByteUtil {
    public static final Integer KB_SIZE = 2 << 9; 
    public static final Integer MB_SIZE = 2 << 19;
    public static final Integer GB_SIZE = 2 << 29;

    public static BigDecimal bytes2Unit(long bytes, Integer unit) {
        BigDecimal size = new BigDecimal(bytes);
        BigDecimal u = new BigDecimal(unit);
        return size.divide(u, 2, BigDecimal.ROUND_DOWN);
    }

    public static Long unit2Byte(BigDecimal decimal,Integer unit) {
         return decimal.multiply(BigDecimal.valueOf(unit)).longValue();
    }

    public static Long kb2Byte(BigDecimal decimal) {
        return decimal.multiply(BigDecimal.valueOf(KB_SIZE)).longValue();
    }

    public static Long mb2Byte(BigDecimal decimal) {
        return decimal.multiply(BigDecimal.valueOf(MB_SIZE)).longValue();
    }

    public static Long gb2Byte(BigDecimal decimal) {
        return decimal.multiply(BigDecimal.valueOf(GB_SIZE)).longValue();
    }

    public static BigDecimal bytes2Kb(long bytes) {
        return bytes2Unit(bytes,KB_SIZE);
    }

    public static BigDecimal bytes2Mb(long bytes) {
        return bytes2Unit(bytes,MB_SIZE);
    }

    public static BigDecimal bytes2Gb(long bytes) {
        return bytes2Unit(bytes,GB_SIZE);
    }
}
