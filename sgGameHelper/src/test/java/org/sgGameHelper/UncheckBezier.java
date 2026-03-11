package org.sgGameHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sellgirl.sgGameHelper.gamepad.XBoxKey;

import junit.framework.TestCase;

public class UncheckBezier extends TestCase  {
	/**
	 * bezier原生只支持单段曲线
	 * 
-0.2-----(-0.82960004,0.6680001)
-0.1-----(-0.4147,0.61600006)
0.0-----(-0.1,0.6)
0.1-----(0.1307,0.61399996)
0.2-----(0.29360005,0.65200007)
0.3-----(0.40489998,0.708)
0.4-----(0.4808,0.776)
0.5-----(0.5375,0.85)
0.6-----(0.5912,0.924)
0.70000005-----(0.6581001,0.9920001)
0.8000001-----(0.75440013,1.0480001)
0.9000001-----(0.8963002,1.086)
1.0000001-----(1.1000003,1.1)
1.1000001-----(1.3817004,1.0839999)
1.2000002-----(1.7576008,1.0319998)

	 */
	public static void testBezierValue() {
		//(-0.1,0.6),(0.9,0.6),(0.3,1.1),(1.2,1.1)
		Vector2 v1= new Vector2(-0.1f,0.6f);
		Vector2 v2= new Vector2(0.8f,0.6f);
		Vector2 v3= new Vector2(0.3f,1.1f);
		Vector2 v4= new Vector2(1.1f,1.1f);
		Bezier<Vector2> be=new Bezier<Vector2>(
				v1,v2,v3,v4
				);
		Vector2 out=new Vector2();
		for(float i=-0.2f;i<=1.3f;i+=0.1f) {
			System.out.println(""+i+"-----"+be.valueAt(out, i));
		}

//		for(float i=0.1f;i<=1;i+=0.1f) {
//			System.out.println(""+i+"-----"+be.approximate(v4).valueAt(out, i));
//		}
	}

	/**
	 * BSpline支持多段曲线
	 */
	public static void testBSpline() {
		//(-0.1,0.6),(0.9,0.6),(0.3,1.1),(1.2,1.1)
		Vector2 v1= new Vector2(-0.1f,0.6f);
		Vector2 v2= new Vector2(0.8f,0.6f);
		Vector2 v3= new Vector2(0.3f,1.1f);
		Vector2 v4= new Vector2(1.1f,1.1f);
		Vector2 v5= new Vector2(1.1f,1.1f);
		Vector2 v6= new Vector2(1.1f,1.1f);
		Vector2 v7= new Vector2(1.1f,1.1f);
		Vector2 v8= new Vector2(1.1f,1.1f);
		Vector2 v9= new Vector2(1.1f,1.1f);
//		Bezier<Vector2> be=new Bezier<Vector2>(//bezier不支持多段
//				v1,v2,v3//,v4//,v5//,v6//,v7
//				);
//        List<Vector2> list= new ArrayList<>(Arrays.asList(v1,v2,v3,v4,v5,v6,v7));
		BSpline<Vector2> be=new BSpline<Vector2>(new Vector2[] {v1,v2,v3,v4,v5,v6,v7},3,false
				);
		Vector2 out=new Vector2();
		for(float i=-0.2f;i<=1.3f;i+=0.1f) {
			System.out.println(""+i+"-----"+be.valueAt(out, i));
		}

//		for(float i=0.1f;i<=1;i+=0.1f) {
//			System.out.println(""+i+"-----"+be.approximate(v4).valueAt(out, i));
//		}
	}
	
	/**
	 * BSpline支持多段曲线
	 */
	public static void testBSpline2() {
		//(-0.1,0.0),(0.0,0.0),(0.2,0.0),(0.6,0.1),(0.8,0.6),
		//(0.9,0.9),(1.0,1.3),(1.1,1.9),(1.1,2.3)
		Vector2 v1= new Vector2(-0.1f,0.0f);
		Vector2 v2= new Vector2(0.0f,0.0f);
		Vector2 v3= new Vector2(0.2f,0.0f);
		Vector2 v4= new Vector2(0.6f,0.1f);
		Vector2 v5= new Vector2(0.8f,0.6f);
		Vector2 v6= new Vector2(0.9f,0.9f);
		Vector2 v7= new Vector2(1.0f,1.3f);
		Vector2 v8= new Vector2(1.1f,1.9f);
		Vector2 v9= new Vector2(1.1f,2.3f);
//		Bezier<Vector2> be=new Bezier<Vector2>(//bezier不支持多段
//				v1,v2,v3//,v4//,v5//,v6//,v7
//				);
//        List<Vector2> list= new ArrayList<>(Arrays.asList(v1,v2,v3,v4,v5,v6,v7));
		BSpline<Vector2> be=new BSpline<Vector2>(new Vector2[] {v1,v2,v3,v4,v5,v6,v7,v8,v9},3,false
				);
		Vector2 out=new Vector2();
		for(float i=-0.2f;i<=1.3f;i+=0.1f) {
			System.out.println(""+i+"-----"+be.valueAt(out, i));
		}

//		for(float i=0.1f;i<=1;i+=0.1f) {
//			System.out.println(""+i+"-----"+be.approximate(v4).valueAt(out, i));
//		}
	}

}
