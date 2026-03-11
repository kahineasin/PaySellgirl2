package com.sellgirl.sgJavaHelper;


//import com.badlogic.gdx.math.Vector3;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 向量运算类(3维向量帮助类)
 * 本类使用float[3]表示向量
 *
 * 返回值的原则, 如果运算结果方向不变, 直接修改参数a, 如果方向改变, 返回new float[3]
 *
 * 写这个类主要是因为网上教程很多搞混 点乘 叉乘, 各种教程错误, 各种教程之间的矛盾
 * 本类把这些统一起来, 清晰说明
 */
public class SGVectorHelper {
    /**
     *
     * a*b 点乘 内积
     * 理解为向量b投影到向量a的值和b相乘
     * a*b=|a||b|*cos<a,b>
     *     (x1,y1,z1)*(x2,y2,z2)=(x1x2,y1y2,z1z2)=x1x2+y1y2+z1z2
     *
     * 用途
     * 1. 当a b为单位向量时, 如果 a*b=1, 那a平行于b
     * 2. 把向量b看作点b的话, 相当于计算点b在单位向量a的投影
     * 3. 光为a, 平面的法向量为b, 投影光的强弱为 a*b
     *
     *  比如(1,0,0)*(0,1,0)=(0,0,0)=0
     *      (1,0,0)*(1,1,0)=(1,0,0)=1
     *
     * Vector3.dot
     *这个矢量和另一个矢量之间的点积
     *The dot product between this and the other vector
     * @param a
     * @param b
     * @return
     */
    public static float dotProduct(float[] a, float[] b){
        return a[0]*b[0]+ a[1] * b[1] + a[2] * b[2] ;
    }
    /**
     * axb |ab| 叉积 外积
     * 理解
     * 1. 为起点相同的向量a和b所形成的平行4边形的面积
     * 2. 为a和b构成的平面的法向量
     *
     * 用途
     * 1. 当a为单位向量时, 计算b终点到a向量的距离
     * 2. 外积为0, 说明两向量平行
     *
     * Sets this vector to the cross product between it and the other vector.
     * 将此向量设置为它与另一个向量之间的叉积
     * @return
     */
    public  static float[] crossProduct(float[] a, float[] b){
        return new float[]{a[1] * b[2] - a[2]*b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0]};
    }

    /**
     * 减
     * @param a
     * @param b
     * @return
     */
    public static float[] sub (float[] a,float[] b) {
        return new float[]{a[0] - b[0], a[1] - b[1], a[2] - b[2]};
    }
    public static float[] add (float[] a,float[] b) {
        return new float[]{a[0] + b[0], a[1] + b[1], a[2] + b[2]};
    }

    /**
     * 向量长度的平方
     * @param a
     * @return
     */
    public static float len2 (float[] a) {
        return a[0] * a[0] + a[1] * a[1] + a[2] * a[2];
    }

    /**
     * 向量缩放
     * Scales this vector by a scalar
     * @param a
     * @return
     */
    public static float[] scl (float[] a,float scalar) {
        a[0]*=scalar;
        a[1]*=scalar;
        a[2]*=scalar;
        return a;
        //return new float[]{a[0]*scalar, a[1] *scalar, a[2] *scalar};
    }

    /**
     * 单位向量
     * @param a
     * @return
     */
    public static float[] nor (float[] a) {
        final float len2 = len2(a);
        if (len2 == 0f || len2 == 1f) return a;
        return scl(a,1f / (float)Math.sqrt(len2));
    }

    /**
     * 线段相交(线段,超出线段返回false)
     * https://www.cnblogs.com/xiangtingshen/p/12329951.html
     * @param line1P1
     * @param line1P2
     * @param line2P1
     * @param line2P2
     * @param intersectP
     * @return
     */
    @Deprecated
    public static boolean lineLineIntersect2(float[] line1P1, float[]line1P2, float[] line2P1,float[] line2P2, float[] intersectP)
    {
    /*
    设两条线段的端点分别为 (x1,y1,z1), (x2,y2,z2) 和 （x3,y3,z3), (x4,y4,z4)
    那么第一条线段方程为U(t)
    x=x1+(x2-x1)t
    y=y1+(y2-y1)t
    z=z1+(z2-z1)t
    0<=t<=1
    同样，第二条线段方程为V(t)
    x=x3+(x4-x3)t
    y=y3+(y4-y3)t
    z=z3+(z4-z3)t
    0<=t<=1
    我们的问题就成为是否存在t1,t2,使得U(t1)=V(t2)
    也就是求t1,t2,使得
    x1+(x2-x1)t1=x3+(x4-x3)t2
    y1+(y2-y1)t1=y3+(y4-y3)t2
    z1+(z2-z1)t1=z3+(z4-z3)t2
    可以通过前面两条方程求出t1,t2,然后带入第三条方程进行检验解是否符合。此外还要求0<=t1,t2<=1，否则还是不相交
    *///    方法一
//    float x1 = line1P1.x();
//    float y1 = line1P1.y();
//    float z1 = line1P1.z();
//    float x2 = line1P2.x();
//    float y2 = line1P2.y();
//    float z2 = line1P2.z();

//    float x3 = line2P1.x();
//    float y3 = line2P1.y();
//    float z3 = line2P1.z();
//    float x4 = line2P2.x();
//    float y4 = line2P2.y();
//    float z4 = line2P2.z();

//    float t1 = 0,t2 = 0;

//    t2 = ( (z3-z1)/(z2-z1) - (x3-x1)/(x2-x1) ) / ( (x4-x3)/(x2-x1) - (z4-z3)/(z2-z1) );
//    t1 = ( z3-z1 + (z4-z3)*t2) / (z2-z1);

//    if (t1 <= 1 && t2 <= 1) {
//        intersectP.setX(x1+(x2-x1)*t1);
//        intersectP.setY(y1+(y2-y1)*t1);
//        intersectP.setZ(z1+(z2-z1)*t1);
//        return true;
//    }
//    else
//        return false;
//
//
//  方法二
        /// 判断线与线之间的相交
        /// </summary>
        /// <param name="intersection">交点</param>
        /// <param name="p1">直线1上一点</param>
        /// <param name="v1">直线1方向</param>
        /// <param name="p2">直线2上一点</param>
        /// <param name="v2">直线2方向</param>
        /// <returns>是否相交</returns>
        //{
            float[] v1 =sub( line1P2 , line1P1);
            float[] v2 = sub( line2P2,line2P1);
            v1=nor(v1);
            v2=nor(v2);
            if (dotProduct(v1, v2) == 1)
            {
                // 两线平行
                return false;
            }

            float[] startPointSeg = sub(line2P1,line1P1);
            float[] vecS1 = crossProduct(v1, v2);            // 有向面积1
            float[] vecS2 = crossProduct(startPointSeg, v2); // 有向面积2
            float num = dotProduct(startPointSeg, vecS1);

            // 判断两这直线是否共面
            if (num >= 1E-05f || num <= -1E-05f)
            {
                return false;
            }

//            // 有向面积比值，利用点乘是因为结果可能是正数或者负数　　　　　　　　　　　　　　　　
//            if (qFuzzyIsNull(len2(vecS1))) {
//                return false;
//            }
            float num2 =dotProduct(vecS2, vecS1) / len2(vecS1);

//            intersectP = line1P1 + v1 * num2;
        intersectP[0]=line1P1[0]+(v1[0]*num2);
        intersectP[1]=line1P1[1]+(v1[1]*num2);
        intersectP[2]=line1P1[2]+(v1[2]*num2);

        return !(num2 > 1) && !(num < 0);//num2的大小还可以判断是延长线相交还是线段相交
//}

    }
    /// <summary>
    /// </summary>
    /// <param name="intersection">交点</param>
    /// <param name="p1">直线1上一点</param>
    /// <param name="v1">直线1方向(单位向量)</param>
    /// <param name="p2">直线2上一点</param>
    /// <param name="v2">直线2方向(单位向量)</param>
    /// <returns>是否相交</returns>

    /**
     *
      判断直线与直线之间的相交
     https://blog.csdn.net/xdedzl/article/details/86009147
     * @param p1
     * @param v1In
     * @param p2
     * @param v2In
     * @param intersection
     * @return
     */
    public static boolean lineLineIntersect( float[] p1, float[] v1In, float[] p2, float[] v2In,float[] intersection)
    {
//        intersection = Vector3.zero;
        float[] v1=v1In.clone();
        float[] v2=v2In.clone();
        nor(v1);
        nor(v2);
        if (dotProduct(v1, v2) == 1)
        {
            // 两线平行
            return false;
        }

        float[] startPointSeg = sub(p2 , p1);
        float[] vecS1 =crossProduct(v1, v2);            // 有向面积1
        float[] vecS2 = crossProduct(startPointSeg, v2); // 有向面积2
        float num =dotProduct(startPointSeg, vecS1);

        // 打开可以在场景中观察向量
        //Debug.DrawLine(p1, p1 + v1, Color.white, 20000);
        //Debug.DrawLine(p2, p2 + v2, Color.black, 20000);

        //Debug.DrawLine(p1, p1 + startPointSeg, Color.red, 20000);
        //Debug.DrawLine(p1, p1 + vecS1, Color.blue, 20000);
        //Debug.DrawLine(p1, p1 + vecS2, Color.yellow, 20000);

        // 判断两这直线是否共面
        if (num >= 1E-05f || num <= -1E-05f)
        {
            return false;
        }

        // 有向面积比值，利用点乘是因为结果可能是正数或者负数
//        float num2 = dotProduct(vecS2, vecS1) / vecS1.sqrMagnitude;
        float num2 = dotProduct(vecS2, vecS1) /len2( vecS1);

//        intersection = p1 + v1 * num2;
        intersection[0]=p1[0]+(v1[0]*num2);
        intersection[1]=p1[1]+(v1[1]*num2);
        intersection[2]=p1[2]+(v1[2]*num2);
        return true;
    }


//    public static short epsilonEquals (float[] l,float[] other, float epsilon) {
//        if (other == null) return 0;
//        if (Math.abs(other[0] - l[0]) > epsilon) return 0;
//        if (Math.abs(other[1] - l[1]) > epsilon) return 0;
//        if (Math.abs(other[2] - l[2]) > epsilon) return 0;
//        SGGameHelper.isOnSomething()
//        return 1;
//    }
//
//    /**
//     * 忽略y轴
//     * l on r
//     * @param l
//     * @param other
//     * @param epsilon
//     * @return
//     */
//    public static boolean epsilonEqualsOn (float[] l,float[] other, float epsilon) {
//        if (other == null) return false;
//        if (Math.abs(other[0] - l[0]) > epsilon) return false;
//        if (Math.abs(other[1] - l[1]) > epsilon||l[1]>other[1]) return false;
//        if (Math.abs(other[2] - l[2]) > epsilon) return false;
//        return true;
//    }
}
