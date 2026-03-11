package org.sgGameHelper;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.sellgirl.sgGameHelper.SGMatrix4Helper;
import com.sellgirl.sgGameHelper.SGQuaternionHelper;
import com.sellgirl.sgGameHelper.gamepad.XBoxKey;

import junit.framework.TestCase;

public class UncheckMatrix4  extends TestCase{
	public static void main(String[] args) {
//		testIntRange();
//		testMatrix4();
		testRotate();
	}
	public static void testMatrix4() {
		Matrix4 m4=new Matrix4();
		Vector3 v3=new Vector3();
		m4.getTranslation(v3);
		System.out.println(""+v3.x+" "+v3.y+" "+v3.z);//0 0 0
		SGMatrix4Helper.printMatrix4(m4);
		
		//trn相对位移
		m4.trn(1,2,3);
		m4.getTranslation(v3);
		System.out.println(""+v3.x+" "+v3.y+" "+v3.z);//0 0 0
		SGMatrix4Helper.printMatrix4(m4);
		m4.trn(1,2,3);
		m4.getTranslation(v3);
		System.out.println(""+v3.x+" "+v3.y+" "+v3.z);//0 0 0
		SGMatrix4Helper.printMatrix4(m4);

		//setTranslation绝对位移
		m4.setTranslation(3,2,1);
		m4.getTranslation(v3);
		System.out.println(""+v3.x+" "+v3.y+" "+v3.z);//0 0 0
		SGMatrix4Helper.printMatrix4(m4);

		m4.setTranslation(0,0,0);
		System.out.println("重置");
		SGMatrix4Helper.printMatrix4(m4);
		m4.rotate(Vector3.Z, 90);
		System.out.println("绕z轴旋转90");
		SGMatrix4Helper.printMatrix4(m4);

	}
	public static void testMul() {

		Matrix4 a=new Matrix4();
		Matrix4 b=new Matrix4();
		Matrix4 c=new Matrix4();
		a.val[Matrix4.M01]=2;
		c=SGMatrix4Helper.mul(a,b,c);
		SGMatrix4Helper.printMatrix4(a);
		System.out.println(" ");
		SGMatrix4Helper.printMatrix4(b);
		System.out.println(" ");
		SGMatrix4Helper.printMatrix4(c);
	}

	public static void testRotate() {
		Matrix4 m4=new Matrix4();
		Matrix4 a=new Matrix4();
		Matrix4 b=new Matrix4();
		Matrix4 c=new Matrix4();
		Matrix4 out=new Matrix4();
		Quaternion q=new Quaternion();

		m4.setTranslation(0,0,0);
		System.out.println("重置");
		SGMatrix4Helper.printMatrix4(m4);
//		System.out.println(m4.getRotation(q).getAxisAngle(Vector3.X));
//		System.out.println(m4.getRotation(q).getAxisAngle(Vector3.Y));
//		System.out.println(m4.getRotation(q).getAxisAngle(Vector3.Z));
		System.out.println(m4.getRotation(q).getAngleAround(Vector3.X));
		System.out.println(m4.getRotation(q).getAngleAround(Vector3.Y));
		System.out.println(m4.getRotation(q).getAngleAround(Vector3.Z));
//		m4.rotate(Vector3.Z, 90);
//		m4.rotate(Vector3.Y, 90);
		m4.rotate(Vector3.X, 90);
		System.out.println("绕x轴旋转90");
		SGMatrix4Helper.printMatrix4(m4);
		System.out.println(m4.getRotation(q).getAngleAround(Vector3.X));
		System.out.println(m4.getRotation(q).getAngleAround(Vector3.Y));
		System.out.println(m4.getRotation(q).getAngleAround(Vector3.Z));
		System.out.println(SGQuaternionHelper.getUpFromQuaternion(q, new Vector3()));
		System.out.println("绕y轴旋转90");
		m4.rotate(Vector3.Y, 90);
		SGMatrix4Helper.printMatrix4(m4);

		
//		System.out.println(m4.getRotation(q).getAxisAngle(Vector3.X));
//		System.out.println(m4.getRotation(q).getAxisAngle(Vector3.Y));
//		System.out.println(m4.getRotation(q).getAxisAngle(Vector3.Z));
		System.out.println(m4.getRotation(q).getAngleAround(Vector3.X));
		System.out.println(m4.getRotation(q).getAngleAround(Vector3.Y));
		System.out.println(m4.getRotation(q).getAngleAround(Vector3.Z));

		System.out.println(SGQuaternionHelper.getUpFromQuaternion(q, new Vector3()));

		Quaternion q2=new Quaternion(q);
		Vector3 tmp=new Vector3(Vector3.Z);
		q2.getAxisAngle(tmp);
		System.out.println(tmp);

		System.out.println("-------------------");
		m4=new Matrix4();
		System.out.println("重置");
		SGMatrix4Helper.printMatrix4(m4);
//		SGMatrix4Helper.getRotateMZ(90, c);
//		SGMatrix4Helper.getRotateMY(90, c);
		SGMatrix4Helper.getRotateMX(90, c);
		System.out.println("旋转矩阵");
		SGMatrix4Helper.printMatrix4(c);
		SGMatrix4Helper.mul(m4, c, out) ;
		System.out.println("绕x轴旋转90(自乘)");
		SGMatrix4Helper.printMatrix4(out);
		m4.set(out);
		SGMatrix4Helper.getRotateMY(90, c);
		SGMatrix4Helper.mul(m4, c, out) ;
		System.out.println("绕y轴旋转90(自乘)");
		SGMatrix4Helper.printMatrix4(out);
		
		System.out.println(out.getRotation(new Quaternion()).getAxisAngle(Vector3.X));
		System.out.println(out.getRotation(new Quaternion()).getAxisAngle(Vector3.Y));
		System.out.println(out.getRotation(new Quaternion()).getAxisAngle(Vector3.Z));


	}
	private static Vector3 v3=new Vector3();
	public static void testRotate2() {
		Matrix4 m4=new Matrix4();
		Matrix4 a=new Matrix4();
		Matrix4 b=new Matrix4();
		Matrix4 c=new Matrix4();
		Matrix4 out=new Matrix4();
		Quaternion q=new Quaternion();
		m4.getRotation(q);
		SGQuaternionHelper.printQuaternion(q);
		
//		m4.rotate(Vector3.X, 90);
//		m4.getRotation(q);
//		SGQuaternionHelper.printQuaternion(q);
//		m4.rotate(Vector3.Y, 90);
//		m4.getRotation(q);
//		SGQuaternionHelper.printQuaternion(q);
		
		m4.rotate(Vector3.Y, 45);
		m4.getRotation(q);
		SGQuaternionHelper.printQuaternion(q);
		System.out.println("----------scale 0.01----------");
		m4.scale(0.01f, 0.01f, 0.01f);
		m4.getRotation(q);
		SGQuaternionHelper.printQuaternion(q);

		System.out.println("----------scale origin----------");
		m4.getScale(v3);
		m4.scale(1f/v3.x,1f/v3.y,1f/v3.z);
		m4.getRotation(q);
		SGQuaternionHelper.printQuaternion(q);
		v3=m4.getScale(v3);
		
	}
	public static void testQuaternion() {
		SGQuaternionHelper.exampleUsage();
	}
}
