package com.sellgirl.sgGameHelper;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 4维矩阵
 * https://www.cnblogs.com/moxiaotao/p/11205082.html
 * 
 * 
 */
public class SGMatrix4Helper {
	/**
	 * 旋转矩阵(Z轴)
	 * @param degree(测试正确)
	 * @param out
	 * @return
	 */
	public static Matrix4 getRotateMZ(float degree, Matrix4 out) {
		double rad=Math.toRadians(degree);
		double sin=Math.sin(rad);
		double cos=Math.cos(rad);
		out.val[Matrix4.M00]=(float) cos;
		out.val[Matrix4.M01]=(float)- sin ;
		out.val[Matrix4.M10]=(float)sin;
		out.val[Matrix4.M11]=(float) cos;
		return out;
	}
	public static Matrix4 getRotateMY(float degree, Matrix4 out) {
		double rad=Math.toRadians(degree);
		double sin=Math.sin(rad);
		double cos=Math.cos(rad);
		out.val[Matrix4.M00]=(float) cos;
		out.val[Matrix4.M02]=(float)sin ;
		out.val[Matrix4.M20]=(float)-sin;
		out.val[Matrix4.M22]=(float) cos;
		return out;
	}
	public static Matrix4 getRotateMX(float degree, Matrix4 out) {
		double rad=Math.toRadians(degree);
		double sin=Math.sin(rad);
		double cos=Math.cos(rad);
		out.val[Matrix4.M11]=(float) cos;
		out.val[Matrix4.M12]=(float)-sin ;
		out.val[Matrix4.M21]=(float)sin;
		out.val[Matrix4.M22]=(float) cos;
		return out;
	}
	/**
	 * 矩阵乖法,a的行分别乘b的列(测试正确)
	 * @param a
	 * @param b
	 * @param out
	 */
	public static Matrix4 mul(Matrix4 a,Matrix4 b,Matrix4 out) {
		for(int i=0;4>i;i++) {//out的列
			for(int j=0;4>j;j++) {//out的行
				out.val[i*4+j]=a.val[j]*b.val[i*4]+a.val[j+4]*b.val[i*4+1]
						+a.val[j+8]*b.val[i*4+2]+a.val[j+12]*b.val[i*4+3];
			}	
		}
		return out;
	}
	public static Matrix4 resetScale(Matrix4 m4,Vector3 tmpVec1) {
		m4.getScale(tmpVec1);
		m4.scale(1f/tmpVec1.x,1f/tmpVec1.y,1f/tmpVec1.z);
		return m4;
	}
	public static void main(String[] args) {
	}
	public static void printMatrix4(Matrix4 m4) {
		System.out.println("["+m4.val[Matrix4.M00]+", "+m4.val[Matrix4.M01]+", "+m4.val[Matrix4.M02]+", "+m4.val[Matrix4.M03]);//0 0 0
		System.out.println(""+m4.val[Matrix4.M10]+", "+m4.val[Matrix4.M11]+", "+m4.val[Matrix4.M12]+", "+m4.val[Matrix4.M13]);
		System.out.println(""+m4.val[Matrix4.M20]+", "+m4.val[Matrix4.M21]+", "+m4.val[Matrix4.M22]+", "+m4.val[Matrix4.M23]);
		System.out.println(""+m4.val[Matrix4.M30]+", "+m4.val[Matrix4.M31]+", "+m4.val[Matrix4.M32]+", "+m4.val[Matrix4.M33]+"]");
	}

//	public static void printMatrix4(Matrix4 m4) {
//		System.out.println("["+SGDataHelper.ScientificNotation( m4.val[Matrix4.M00])+", "+SGDataHelper.ScientificNotation(m4.val[Matrix4.M01])+", "
//	+SGDataHelper.ScientificNotation(m4.val[Matrix4.M02])+", "+SGDataHelper.ScientificNotation(m4.val[Matrix4.M03]));//0 0 0
//		System.out.println(""+SGDataHelper.ScientificNotation(m4.val[Matrix4.M10])+", "+SGDataHelper.ScientificNotation(m4.val[Matrix4.M11])+", "
//	+SGDataHelper.ScientificNotation(m4.val[Matrix4.M12])+", "+SGDataHelper.ScientificNotation(m4.val[Matrix4.M13]));
//		System.out.println(""+SGDataHelper.ScientificNotation(m4.val[Matrix4.M20])+", "+SGDataHelper.ScientificNotation(m4.val[Matrix4.M21])+", "
//	+SGDataHelper.ScientificNotation(m4.val[Matrix4.M22])+", "+SGDataHelper.ScientificNotation(m4.val[Matrix4.M23]));
//		System.out.println(""+SGDataHelper.ScientificNotation(m4.val[Matrix4.M30])+", "+SGDataHelper.ScientificNotation(m4.val[Matrix4.M31])+", "
//	+SGDataHelper.ScientificNotation(m4.val[Matrix4.M32])+", "+SGDataHelper.ScientificNotation(m4.val[Matrix4.M33])+"]");
//	}

}
