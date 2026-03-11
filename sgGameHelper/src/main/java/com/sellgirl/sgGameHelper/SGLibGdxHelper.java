package com.sellgirl.sgGameHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpStatus;
//import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
//import com.mygdx.game.sasha.bulletD3.actor.SashaActorD3;
import com.sellgirl.sgGameHelper.gamepad.ISGPS5Gamepad;
import com.sellgirl.sgGameHelper.gamepad.SGPS5Gamepad;
import com.sellgirl.sgJavaHelper.SGAction1;
import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.util.ArrayList;

//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import com.mygdx.game.ball.Paddle;

/**
 * 其实有些情况下,y轴的方向并不影响计算,但下面情况可能有影响:
 * 1. 当超过1次用y和height计算up或down时,因为up和down是有区分方向的
 */
public class SGLibGdxHelper {
	private static final String tag="SGLibGdxHelper";
//    public static boolean overlaps(ISGActor l,ISGActor r) {
//    	return (new Rectangle(l.getX(),l.getY(),l.getWidth(),l.getHeight())).overlaps(new Rectangle(r.getX(),r.getY(),r.getWidth(),r.getHeight()));
//    }



    /**
     * 画矩形,注意一定要disapose,否则内存泄漏
     * @param width
     * @param height
     * @param color
     * @return
     */
    public static Texture getRectTexture(int width,int height,Color color) {        
		Pixmap p=new Pixmap(width,height, Format.RGBA8888);
		p.setColor(color);
		p.fillRectangle(0, 0, width,height);//ok
		Texture t=new Texture(p);
		return t;
    }
    public static Texture getCircleTexture(int radius,Color color) {        
		Pixmap p=new Pixmap(radius*2+2,radius*2+2, Format.RGBA8888);//2是边框
		p.setColor(color);
//		p.fillCircle(0,0,radius);
		p.fillCircle(radius,radius,radius);//座标方向是y往下. xy是圆中座标, 画图是从左上角开始画
		Texture t=new Texture(p);
		return t;
    }

	/**
	 * 画渐变线 垂直丝, 比如天空
	 * 座标方向是y往下. 画图是从左上角开始画
	 * @param color 直线最上点的色
	 * @param color2 直线最下点的色
	 * @param height height应该小, 比如10
	 * @return
	 */
	public static Texture getGradientLine(Color color,Color color2,int height) {
		Pixmap p=new Pixmap(1,height, Format.RGBA8888);//2是边框
		p.setColor(color);
		p.drawPixel(0,0);
		float red=(color2.r-color.r)/(height-1);
		float green=(color2.g-color.g)/(height-1);
		float blue=(color2.b-color.b)/(height-1);
		for(int i=1;i<height;i++){
			p.setColor(color.r+(red*i),color.g+(green*i),color.b+(blue*i),1);
			p.drawPixel(0,i);
		}
////		p.fillCircle(0,0,radius);
//		p.fillCircle(radius,radius,radius);//
		Texture t=new Texture(p);
		return t;
	}
    /**
     * 画矩形边线
     * @param width
     * @param height
     * @param color
     * @return
     */
    public static Texture getRectTextureLine(int width,int height,Color color) {        
		Pixmap p=new Pixmap(width,height, Format.RGBA8888);
		p.setColor(color);
		p.drawRectangle(0, 0, width,height);//ok
		Texture t=new Texture(p);
		return t;
    }
	public static Texture getCircleTextureLine(int radius,Color color) {
		Pixmap p=new Pixmap(radius*2+2,radius*2+2, Format.RGBA8888);//2是边框
		p.setColor(color);
//		p.fillCircle(0,0,radius);
		p.drawCircle(radius,radius,radius);//座标方向是y往下. xy是圆中座标, 画图是从左上角开始画
		Texture t=new Texture(p);
		return t;
	}

//	public static Array<TextureRegion> getTextureRegionR(String[] urls, AssetManager manager) {
//		Array<TextureRegion> r = new Array<>(urls.length);
//		for (String url : urls) {
//			r.add(new TextureRegion(manager.get(url, EncryptTexture.class)));
//		}
//		return r;
//	}

	public static Array<TextureRegion> getTextureRegionL(Array<TextureRegion> arr) {
		Array<TextureRegion> r = new Array<>(arr.size);
		for (int i = 0; i < arr.size; i++) {
			TextureRegion atlasRegion3 = new TextureRegion(arr.get(i));
			atlasRegion3.flip(true, false);
			r.add(atlasRegion3);
		}
		return r;
	}
	public static Renderable getRectRenderable(Material m,Mesh mesh){
		//this.act(Gdx.graphics.getDeltaTime());
		Renderable renderable=new Renderable();
		renderable.material=m;

		renderable.meshPart.mesh=mesh;
		renderable.meshPart.offset = 0;
		renderable.meshPart.size = renderable.meshPart.mesh.getNumIndices();
		renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
		renderable.meshPart.update();

		//transform.setFromEulerAngles(0,0,0);


//        Array<Action> actions= this.getActions();
//        for(Action i:actions){
//            transform.setTranslation()
//        }

		//renderable.worldTransform.set(transform);

		return renderable;
	}
//public static Renderable getRectLineRenderable(float w,float h) {
//
//	Texture test = SGLibGdxHelper.getRectTextureLine(((Float) w).intValue(),
//			((Float) h).intValue(), Color.BLUE);
//
//	Material m = new Material(
//			//TextureAttribute.createDiffuse(i),
//			TextureAttribute.createDiffuse(test),
//			new BlendingAttribute(false, 1f),
//			FloatAttribute.createAlphaTest(0.5f));
//	//tmp.mesh=SGLibGdxHelper.getPaperMeshBaseOnFootPos(test.getWidth()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,test.getHeight()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,-x2L/ScreenSetting.PIXEL_DIVIDE_METER,manW,getTall(),i,!faceRight);
//	Mesh mesh = SGLibGdxHelper.getPaperMesh4(w, h, new TextureRegion(test), false, true);
//
//	//this.act(Gdx.graphics.getDeltaTime());
//	Renderable renderable = new Renderable();
//	renderable.material = m;
//
//	renderable.meshPart.mesh = mesh;
//	renderable.meshPart.offset = 0;
//	renderable.meshPart.size = renderable.meshPart.mesh.getNumIndices();
//	renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
//	renderable.meshPart.update();
//
//	//renderable.worldTransform.set(transform);
//	return renderable;
//}

	/**
	 * 获得最佳默认控制器
	 * @return
	 */
    public static Controller getGamepad() {
//    	Controller r=null;
//    	Controller d=null;
//		Controller notTV=null;
//    	//String lastName="";
//    	try {
//    		
//			//打成exe时,这句会报错,但似乎没影响
//			//Failed to load mapping with original location "gamecontrollerdb.txt", Falling back of SDL's built in mappings
//			//java.io.IOException: Cannot open resource from classpath gamecontrollerdb.txt
//			for (Controller controller : Controllers.getControllers()) {
////				System.out.println(controller.getName()+" ok");
////				lastName=controller.getName();
//				if(null==r&&SGControllerName.XInputController.equals(controller.getName())) {
//					r=controller;
//				}
////				else if(SGControllerName.XInputController.equals(controller.getName())){
////			    	//com.badlogic.gdx.controllers.desktop.support.JamepadController
////					r=controller;
////				}
//				if(null==notTV&&!SGControllerName.SonyTvController.equals(controller.getName())){
//					notTV=controller;
//				}
//			    if(null==d) {d=controller;}
//			}
//			//System.out.println(lastName+" finish");
//    	}catch(Throwable e) {
//			SGDataHelper.getLog().printException(e,null);
//			//System.out.println(lastName+" 之后报错");
//    	}
//		if(null!=r){
//			return r;
//		}
//		if(null!=notTV){
//			return notTV;
//		}
//		return d;
////		return r==null?d:r;
    	return getGamepad(Controllers.getControllers());
    }

    public static Controller getGamepad(Iterable<? extends Controller> controllers) {
    	Controller r=null;
    	Controller d=null;
		Controller notTV=null;
    	//String lastName="";
    	try {
    		
			//打成exe时,这句会报错,但似乎没影响
			//Failed to load mapping with original location "gamecontrollerdb.txt", Falling back of SDL's built in mappings
			//java.io.IOException: Cannot open resource from classpath gamecontrollerdb.txt
			for (Controller controller : controllers) {
//				System.out.println(controller.getName()+" ok");
//				lastName=controller.getName();
				if(null==r&&SGControllerName.XInputController.equals(controller.getName())) {
					r=controller;
				}
//				else if(SGControllerName.XInputController.equals(controller.getName())){
//			    	//com.badlogic.gdx.controllers.desktop.support.JamepadController
//					r=controller;
//				}
				if(null==notTV&&!SGControllerName.SonyTvController.equals(controller.getName())){
					notTV=controller;
				}
			    if(null==d) {d=controller;}
			}
			//System.out.println(lastName+" finish");
    	}catch(Throwable e) {
			SGDataHelper.getLog().printException(e,null);
			//System.out.println(lastName+" 之后报错");
    	}
		if(null!=r){
			return r;
		}
		if(null!=notTV){
			return notTV;
		}
		return d;
//		return r==null?d:r;

    }
	/**
	 * 获得最佳默认控制器
	 * @return
	 */
	public static ISGPS5Gamepad getSGGamepad() {
		Controller c=SGLibGdxHelper.getGamepad();
		if(null!=c){
			return new SGPS5Gamepad(c);
		}
		return null;
	}

//	/**
//	 * 注意,如果每帧都new Mesh, 会爆内存
//	 * @param width
//	 * @param height
//	 * @param png
//	 * @param flip
//	 * @return
//	 */
//	//@Deprecated
//	public static Mesh getPaperMesh(float width, float height, TextureRegion png,boolean flip){
//
////		Sprite front = null;
////		Sprite back = null;
////
////		if(TextureAtlas.AtlasRegion.class==png.getClass()){
////
////			front = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
////			back = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
////		}else{
////			 front = new Sprite(png);
////			 back = new Sprite(png);
////		}
////		front.setSize(width, height);
////		back.setSize(width, height);
////		front.setPosition(-front.getWidth() * 0.5f, -front.getHeight() * 0.5f);
////		back.setPosition(-back.getWidth() * 0.5f, -back.getHeight() * 0.5f);
////		if(flip){
////			front.flip(true,false);
////			back.flip(true,false);
////		}
////
////		float[] vertices =com.mygdx.game.share.SGDataHelper.convertPaperZ(front.getVertices(), back.getVertices());
////		short[] indices = new short[]{0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4};
////		Mesh mesh = new Mesh(true, 8, 12, VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
////		mesh.setVertices(vertices);
////		mesh.setIndices(indices);
////		return mesh;
//
//		return getPaperMesh4(width, height, png,flip,true);
//	}
//
//	/**
//	 * png转为mesh
//	 * 测试可以
//	 *
//	 * 原理可以参考 https://blog.csdn.net/weixin_51481499/article/details/135546719 ,但不完全一样, libGDX的vertex是混合的
//	 * @param width
//	 * @param height
//	 * @param png
//	 * @param flip
//	 * @param isStatic
//	 * @return
//	 */
//	public static Mesh getPaperMesh4(float width, float height, TextureRegion png,boolean flip,boolean isStatic){
//
//		Sprite front = null;
//		Sprite back = null;
//
//		if(TextureAtlas.AtlasRegion.class==png.getClass()){
//
//			front = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
//			back = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
//		}else{
//			front = new Sprite(png);
//			back = new Sprite(png);
//		}
//		front.setSize(width, height);
//		back.setSize(width, height);
//		front.setPosition(-front.getWidth() * 0.5f, -front.getHeight() * 0.5f);
//		back.setPosition(-back.getWidth() * 0.5f, -back.getHeight() * 0.5f);
//		if(flip){
//			front.flip(true,false);
//			back.flip(true,false);
//		}
//
//		float[] vertices = SGGameHelper.convertPaperZ(front.getVertices(), back.getVertices());
//		short[] indices = new short[]{0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4};
//		Mesh mesh = new Mesh(isStatic, 8, 12, VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
//		mesh.setVertices(vertices);
//		mesh.setIndices(indices);
//		return mesh;
//	}
//
//	/**
//	 * 如果TextureAtlas类型用getPaperMesh()显示不了图形,试试这个方法
//	 * @param width
//	 * @param height
//	 * @param atlas
//	 * @param pngNode
//	 * @param pngNodeIndex
//	 * @param flip
//	 * @return
//	 */
//	@Deprecated
//	public static Mesh getPaperMesh3(float width, float height, TextureAtlas atlas,String pngNode,int pngNodeIndex,boolean flip,boolean isStatic){
//
//		Sprite front = null;
//		Sprite back = null;
//
////		if(TextureAtlas.AtlasRegion.class==png.getClass()){
////
////			front = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
////			back = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
////		}else{
////			front = new Sprite(png);
////			back = new Sprite(png);
////		}
//		front=atlas.createSprite(pngNode,pngNodeIndex);
//		back=atlas.createSprite(pngNode,pngNodeIndex);
//		front.setSize(width, height);
//		back.setSize(width, height);
//		front.setPosition(-front.getWidth() * 0.5f, -front.getHeight() * 0.5f);
//		back.setPosition(-back.getWidth() * 0.5f, -back.getHeight() * 0.5f);
//		if(flip){
//			front.flip(true,false);
//			back.flip(true,false);
//		}
//
//		float[] vertices = SGGameHelper.convertPaperZ(front.getVertices(), back.getVertices());
//		short[] indices = new short[]{0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4};
//		Mesh mesh = new Mesh(isStatic, 8, 12, VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
//		mesh.setVertices(vertices);
//		mesh.setIndices(indices);
//		return mesh;
//	}
//	/**
//	 * 右脚对齐法
//	 * 单位都是米
//	 * 测试可用
//	 * 注意:配合此方法使用的Material一定要是TextureRegion.getTexture()的结果
//	 * @deprecated 改用getPaperMeshBaseOnFootPos2
//	 * @param width
//	 * @param height
//	 * @param rightFootX
//	 * @param manW
//	 * @param manH
//	 * @param png
//	 * @param flip
//	 * @return
//	 */
//	@Deprecated
//	public static Mesh getPaperMeshBaseOnFootPos(float width, float height,float rightFootX,
//												 float manW, float manH, TextureRegion png,boolean flip){
//
//		Sprite front = null;
//		Sprite back = null;
//
//		if(TextureAtlas.AtlasRegion.class==png.getClass()){
//
//			front = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
//			back = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
//		}else{
//			front = new Sprite(png);
//			back = new Sprite(png);
//		}
//		front.setSize(width, height);
//		back.setSize(width, height);
////		front.setPosition(-front.getWidth() * 0.5f, -front.getHeight() * 0.5f);//图片尺寸不一样时,中心点不一样
////		back.setPosition(-back.getWidth() * 0.5f, -back.getHeight() * 0.5f);
//
//		if(flip){
//			front.setPosition(-(width - (manW + rightFootX)) -(manW * 0.5f), -manH/2);
//			back.setPosition(-(width - (manW + rightFootX)) -(manW * 0.5f), -manH/2);
//		}else {
//			front.setPosition(-rightFootX-(manW * 0.5f), -manH/2);
//			back.setPosition(-rightFootX-(manW * 0.5f), -manH/2);
//		}
////		if(flip){
////			front.flip(true,false);
////			back.flip(true,false);
////		}else {
////
////		}
//
//		float[] vertices = SGGameHelper.convertPaperZ(front.getVertices(), back.getVertices());
//		short[] indices = new short[]{0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4};
//		Mesh mesh = new Mesh(true, 8, 12, VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
//		mesh.setVertices(vertices);
//		mesh.setIndices(indices);
//		return mesh;
//	}
//
//	/**
//	 * 右脚对齐法
//	 * 单位都是米
//	 * 测试可用
//	 * 注意:配合此方法使用的Material一定要是TextureRegion.getTexture()的结果
//	 * @param width 素材的宽度
//	 * @param height 素材的高度
//	 * @param rightFootX 右脚在图中的x坐标, 正数
//	 * @param rightFootY 右脚底在图中的y坐标, 正数, 默认传0
//	 * @param manW
//	 * @param manH
//	 * @param png
//	 * @param flip
//	 * @return
//	 */
//	public static Mesh getPaperMeshBaseOnFootPos2(float width, float height,float rightFootX,float rightFootY,
//												  float manW, float manH, TextureRegion png,boolean flip){
//
//		Sprite front = null;
//		Sprite back = null;
//
//		if(TextureAtlas.AtlasRegion.class==png.getClass()){
//
//			front = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
//			back = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)png);
//		}else{
//			front = new Sprite(png);
//			back = new Sprite(png);
//		}
//		front.setSize(width, height);
//		back.setSize(width, height);
////		front.setPosition(-front.getWidth() * 0.5f, -front.getHeight() * 0.5f);//图片尺寸不一样时,中心点不一样
////		back.setPosition(-back.getWidth() * 0.5f, -back.getHeight() * 0.5f);
//
//		if(flip){
////			front.setPosition(-(width - (manW + rightFootX)) -(manW * 0.5f), -manH/2);
////			back.setPosition(-(width - (manW + rightFootX)) -(manW * 0.5f), -manH/2);
//			front.setPosition(-(width - (manW + rightFootX)) -(manW * 0.5f), -rightFootY-(manH/2));
//			back.setPosition(-(width - (manW + rightFootX)) -(manW * 0.5f), -rightFootY-(manH/2));
//		}else {
////			front.setPosition(-rightFootX-(manW * 0.5f), -manH/2);
////			back.setPosition(-rightFootX-(manW * 0.5f), -manH/2);
//			front.setPosition(-rightFootX-(manW * 0.5f), -rightFootY-(manH/2));
//			back.setPosition(-rightFootX-(manW * 0.5f), -rightFootY-(manH/2));
//		}
////		if(flip){
////			front.flip(true,false);
////			back.flip(true,false);
////		}else {
////
////		}
//
//		float[] vertices = SGGameHelper.convertPaperZ(front.getVertices(), back.getVertices());
//		short[] indices = new short[]{0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4};
//		Mesh mesh = new Mesh(true, 8, 12, VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
//		mesh.setVertices(vertices);
//		mesh.setIndices(indices);
//		return mesh;
//	}
//
//	/**
//	 * 当素材和人物根据右脚底对齐时
//	 * 单位meter
//	 * @param manW
//	 * @param manH
//	 * @param yRate2 应该画的px/素材的px
//	 * @param rightFootX
//	 * @param rightFootY
//	 * @param ani
//	 * @param faceRight
//	 * @return
//	 */
//	public static Animation<SGAnimationFrameD3> convertAnimationD2ToD3BaseOnFootPos
//			(float manW,float manH,float yRate2,float rightFootX,float rightFootY,
//									  Animation<? extends TextureRegion> ani, boolean faceRight){
////		if(null==ani){return null;}
////		Animation<Mesh> r=null;
////		Animation<SGAnimationFrameD3> r2=null;
////		ArrayList<Mesh> standMeshRArr=new ArrayList<>();
////		ArrayList<SGAnimationFrameD3> aniRArr=new ArrayList<>();
////		Object[] arr=ani.getKeyFrames();
//////        for(Object frame:ani.getKeyFrames()){
//////            TextureRegion i=(TextureRegion)frame;
//////            standMeshRArr.add(SGLibGdxHelper.getPaperMesh(meterW,meterH,i));
//////        }
////		for(int j=0;j<arr.length;j++){
////			TextureRegion i=(TextureRegion)arr[j];
////			if(null==i){return null;}
////			//i.flip(true,false);
//////            standMeshRArr.add(SGLibGdxHelper.getPaperMesh(meterW,meterH,i));
////			SashaActorD3.AnimationFrame tmp=new SashaActorD3.AnimationFrame();
////			tmp.m= new Material(
////					//TextureAttribute.createDiffuse(i),
////					TextureAttribute.createDiffuse(i.getTexture()),
////					new BlendingAttribute(false, 1f),
////					FloatAttribute.createAlphaTest(0.5f));
////			tmp.mesh=SGLibGdxHelper.getPaperMeshBaseOnFootPos2(
//////					i.getRegionWidth()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,
//////					i.getRegionHeight()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,
////					i.getRegionWidth()*yRate2,
////					i.getRegionHeight()*yRate2,
////					rightFootX,rightFootY,manW,manH,i,!faceRight);
////
////			aniRArr.add(tmp);
////		}
////
//////        r = new Animation<Mesh>(ani.getAnimationDuration(), standMeshRArr.toArray(new Mesh[standMeshRArr.size()]));
////		//r.setPlayMode(ani.getPlayMode());
////		r2 = new Animation<SGAnimationFrameD3>(ani.getFrameDuration(), aniRArr.toArray(new SashaActorD3.AnimationFrame[aniRArr.size()]));
////		r2.setPlayMode(ani.getPlayMode());
//////        r2=new AnimationFrame();
//////        r2.m= new Material(
//////                TextureAttribute.createDiffuse(standR.getKeyFrame(0)),
//////                new BlendingAttribute(false, 1f),
//////                FloatAttribute.createAlphaTest(0.5f));
//////        r2.mesh=r;
////		return r2;
//		return convertAnimationD2ToD3BaseOnFootPos( manW, manH, yRate2, rightFootX, rightFootY,
//		 ani,  faceRight,false);
//	}
//
//	public static Animation<SGAnimationFrameD3> convertAnimationD2ToD3BaseOnFootPos
//			(float manW,float manH,float yRate2,float rightFootX,float rightFootY,
//			 Animation<? extends TextureRegion> ani, boolean faceRight, boolean blended){
//		if(null==ani){return null;}
//		Animation<Mesh> r=null;
//		Animation<SGAnimationFrameD3> r2=null;
//		ArrayList<Mesh> standMeshRArr=new ArrayList<>();
//		ArrayList<SGAnimationFrameD3> aniRArr=new ArrayList<>();
//		Object[] arr=ani.getKeyFrames();
////        for(Object frame:ani.getKeyFrames()){
////            TextureRegion i=(TextureRegion)frame;
////            standMeshRArr.add(SGLibGdxHelper.getPaperMesh(meterW,meterH,i));
////        }
//		for(int j=0;j<arr.length;j++){
//			TextureRegion i=(TextureRegion)arr[j];
//			if(null==i){return null;}
//			//i.flip(true,false);
////            standMeshRArr.add(SGLibGdxHelper.getPaperMesh(meterW,meterH,i));
//			SashaActorD3.AnimationFrame tmp=new SashaActorD3.AnimationFrame();
//			tmp.m= new Material(
//					//TextureAttribute.createDiffuse(i),
//					TextureAttribute.createDiffuse(i.getTexture()),
//					new BlendingAttribute(blended, 1f),
//					FloatAttribute.createAlphaTest(0.5f));
//			tmp.mesh=SGLibGdxHelper.getPaperMeshBaseOnFootPos2(
////					i.getRegionWidth()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,
////					i.getRegionHeight()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,
//					i.getRegionWidth()*yRate2,
//					i.getRegionHeight()*yRate2,
//					rightFootX,rightFootY,manW,manH,i,!faceRight);
//
//			aniRArr.add(tmp);
//		}
//
////        r = new Animation<Mesh>(ani.getAnimationDuration(), standMeshRArr.toArray(new Mesh[standMeshRArr.size()]));
//		//r.setPlayMode(ani.getPlayMode());
//		r2 = new Animation<SGAnimationFrameD3>(ani.getFrameDuration(), aniRArr.toArray(new SashaActorD3.AnimationFrame[aniRArr.size()]));
//		r2.setPlayMode(ani.getPlayMode());
////        r2=new AnimationFrame();
////        r2.m= new Material(
////                TextureAttribute.createDiffuse(standR.getKeyFrame(0)),
////                new BlendingAttribute(false, 1f),
////                FloatAttribute.createAlphaTest(0.5f));
////        r2.mesh=r;
//		return r2;
//	}
//
//	/**
//	 * 当素材和人物根据中心对齐时
//	 * @deprecated yRate2这种方案只是临时用的
//	 * @param yRate2 应该画的px/素材的px
//	 * @param ani
//	 * @param faceRight
//	 * @return
//	 */
//	@Deprecated
//	public static Animation<SGAnimationFrameD3> convertAnimationD2ToD3
//			(//float manW,float manH,
//			 float yRate2,//float rightFootX,float rightFootY,
//			 Animation<? extends TextureRegion> ani, boolean faceRight){
//		if(null==ani){return null;}
//		Animation<Mesh> r=null;
//		Animation<SGAnimationFrameD3> r2=null;
//		ArrayList<Mesh> standMeshRArr=new ArrayList<>();
//		ArrayList<SGAnimationFrameD3> aniRArr=new ArrayList<>();
//		Object[] arr=ani.getKeyFrames();
////        for(Object frame:ani.getKeyFrames()){
////            TextureRegion i=(TextureRegion)frame;
////            standMeshRArr.add(SGLibGdxHelper.getPaperMesh(meterW,meterH,i));
////        }
//		for(int j=0;j<arr.length;j++){
//			TextureRegion i=(TextureRegion)arr[j];
//			if(null==i){return null;}
//			//i.flip(true,false);
////            standMeshRArr.add(SGLibGdxHelper.getPaperMesh(meterW,meterH,i));
//			SashaActorD3.AnimationFrame tmp=new SashaActorD3.AnimationFrame();
//			tmp.m= new Material(
//					//TextureAttribute.createDiffuse(i),
//					TextureAttribute.createDiffuse(i.getTexture()),
//					new BlendingAttribute(false, 1f),
//					FloatAttribute.createAlphaTest(0.5f));
//
//////			tmp.mesh=SGLibGdxHelper.getPaperMeshBaseOnFootPos2(i.getRegionWidth()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,i.getRegionHeight()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,rightFootX,rightFootY,manW,manH,i,!faceRight);
////			tmp.mesh=SGLibGdxHelper.getPaperMesh(i.getRegionWidth()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,i.getRegionHeight()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,
////					//rightFootX,rightFootY,manW,manH,
////					i,
////					!faceRight);
//			tmp.mesh=SGLibGdxHelper.getPaperMesh(i.getRegionWidth()*yRate2,i.getRegionHeight()*yRate2,
//					//rightFootX,rightFootY,manW,manH,
//					i,
//					!faceRight);
//
//			aniRArr.add(tmp);
//		}
//
////        r = new Animation<Mesh>(ani.getAnimationDuration(), standMeshRArr.toArray(new Mesh[standMeshRArr.size()]));
//		//r.setPlayMode(ani.getPlayMode());
//		r2 = new Animation<SGAnimationFrameD3>(ani.getFrameDuration(), aniRArr.toArray(new SashaActorD3.AnimationFrame[aniRArr.size()]));
//		r2.setPlayMode(ani.getPlayMode());
//
//		return r2;
//	}
//
//	/**
//	 *
//	 * @param width
//	 * @param height
//	 * @param png
//	 * @param blended 默认false. 如果png是透明时, 设置为true才显示正常(透明时如果设置为false,到镜头中间时,会反相,本人未了解相关知识)
//	 * @return
//	 */
//	public static SGAnimationFrameD3 getAnimationFrameD3(float width,float height,TextureRegion png,boolean blended){
//
//		SGAnimationFrameD3 tmp=new SGAnimationFrameD3();
//		tmp.m= new Material(
//				//TextureAttribute.createDiffuse(i),
//				TextureAttribute.createDiffuse(png.getTexture()),
//				new BlendingAttribute(blended, 1f),
//				FloatAttribute.createAlphaTest(0.5f));
//
////////			tmp.mesh=SGLibGdxHelper.getPaperMeshBaseOnFootPos2(i.getRegionWidth()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,i.getRegionHeight()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,rightFootX,rightFootY,manW,manH,i,!faceRight);
//////			tmp.mesh=SGLibGdxHelper.getPaperMesh(i.getRegionWidth()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,i.getRegionHeight()*yRate2/ScreenSetting.PIXEL_DIVIDE_METER,
//////					//rightFootX,rightFootY,manW,manH,
//////					i,
//////					!faceRight);
////		tmp.mesh=SGLibGdxHelper.getPaperMesh4(i.getRegionWidth()*yRate2,i.getRegionHeight()*yRate2,
////				//rightFootX,rightFootY,manW,manH,
////				i,
////				true);
//		tmp.mesh=SGLibGdxHelper.getPaperMesh4(width,height,png,
//				false,
//				true);
//		return tmp;
//	}
//
//	public static Renderable getRenderable(@Nonnull SGAnimationFrameD3 af, @Nullable Pool<Renderable> pool, Matrix4 transform){
//		Renderable renderable=null==pool?new Renderable():pool.obtain();
//		renderable.material=af.m;
//		renderable.meshPart.mesh=af.mesh;
//		renderable.meshPart.offset = 0;
//		renderable.meshPart.size = renderable.meshPart.mesh.getNumIndices();
//		renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
//		renderable.meshPart.update();
//		renderable.worldTransform.set(transform);
//		return renderable;
//	}
//	public static void setManBulletBody(btRigidBody body){
//
////		body.setRestitution(1.0f);
////		body.setFriction(1.0f);
//	}

	/**
	 * 只触发一个好像有问题，用在dialog的close按钮上时，不能触发关闭弹窗的事件
	 * @param actor
	 */
	@Deprecated
	public static void executeButtonListener(Actor actor){
		EventListener last=actor.getListeners().get(actor.getListeners().size-1);
		//注意匿名类不能这样判断 ClickListener.class==last.getClass()
		if(last instanceof ClickListener){
			((ClickListener)last).clicked(null,0,0);
		}else if(last instanceof ChangeListener){
			((ChangeListener)last).changed(null,null);
		}
	}

	public static void executeButtonListener(Actor actor,Event event){
		for(int i=actor.getListeners().size-1;0<=i;i--) {
			EventListener listener=actor.getListeners().get(i);
			if(event.isCancelled()||event.isCapture()||event.isHandled()||event.isStopped()) {
					
			}else {
				listener.handle(event);
			}
		}		
	}

	/**
	 * 自动使scrollPanel中的某项滚动到中间可示区域（暂时只做y向滚动）
	 * 此方法通用性非常好，直接拿来就能用
	 * @param pan
	 * @param actor2
	 * @param tmp4
	 */
    public static void ensureActorVisibleInScroll(ScrollPane pan,Actor actor2,Vector2 tmp4){
        //旧方法，因为combineBtn是在一个较大的容器中，这样会一下滚到容器的最后
//				float panMaxHeight=scrollPane.getActor().getHeight();//pan.getMaxHeight()永远是0，gdx旧版本就有值
        float needHeight=0f;
        boolean isInView=false;
        if(null==tmp4){
//					tmp2 = new Vector2();
//					tmp3 = new Vector2();
            tmp4 = new Vector2();
        }

        Vector2 actorCoords =
            actor2.getParent()
                .localToAscendantCoordinates(pan,tmp4.set(actor2.getX(), actor2.getY()));

        needHeight=(pan.getHeight()/2f);
        isInView=actorCoords.y>0f
            &&actor2.getHeight()+ actorCoords.y<pan.getHeight();

        float scrollY=needHeight-((actor2.getHeight()/2f)+actorCoords.y);
//        if(test) {
//            System.out.println(SGDataHelper.FormatString(
//                "x:{0} y{1} x2:{2} y2:{3}\r\n scrollPanelH:{4} needHeight:{5} isInView:{6} scrollY:{7}"
//                , actorCoords.x, actorCoords.y, 0,0//actorCoords2.x, actorCoords2.y
//                , pan.getHeight(), needHeight, isInView, scrollY
//            ));
//        }
        if(!isInView){
            pan.setScrollY(pan.getScrollY()+ scrollY);
        }
    }

	public static void getHttpStringAsync(String url,SGAction1<String> action) {
		// final SashaData[] r= new SashaData[] {};
		final SGRef<String> r = new SGRef<String>();
		String requestContent = null;

		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
//		httpRequest.setUrl(url.getCharacterBakup().replace("{character}", sasha.getCharacter().name()));
		httpRequest.setUrl(url);
		httpRequest.setHeader("Content-Type", "text/plain");
		httpRequest.setContent(requestContent);

		long now = System.currentTimeMillis();
		final SGRef<Boolean> end = new SGRef<Boolean>(false);
		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {

			public void handleHttpResponse(HttpResponse httpResponse) {

				try {

					if (HttpStatus.SC_OK != httpResponse.getStatus().getStatusCode()) {
						end.SetValue(true);
						return;
					}
//					String s = AES.AESEncryptDemo(sasha.getUserId(), SGDataHelper.decodeBase64(key));
					String s2 = httpResponse.getResultAsString();					
					r.SetValue(s2);
					action.go(s2);
//					int idx = s2.indexOf("|" + s);
//					if (idx > -1) {
//						int idx2 = s2.indexOf("|", idx + s.length());
//						if (idx2 > -1) {
//							// r[0]= initSashaByEncodeStr(s2.substring(idx+s.length(), idx2));
//							r.SetValue(initSashaByEncodeStr(s2.substring(idx + s.length() + 1, idx2)));
//						}
//					} else {
//
//					}
					// System.out.println();
				} catch (Exception e) {
					SGDataHelper.getLog().printException(e,tag);
				}
				end.SetValue(true);
				// System.out.println("33333333333----"+end.GetValue());
			}

			public void failed(Throwable t) {
				//System.out.println("HTTP request failed!");
				end.SetValue(true);
			}

			@Override
			public void cancelled() {
				//System.out.println("HTTP request failed!");
				end.SetValue(true);
			}

		});
		return;
		// System.out.println("222222222222222");
//		while (!end.GetValue() && System.currentTimeMillis() - now < 10000) {
//			try {
//				// System.out.println("44444444444----"+end.GetValue());
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		} 
//		return r.GetValue();
//		java.util.concurrent.BlockingDeque<E>
//		return 
//		        new Thread() {//线程操作
//            public void run() {
//                while(_isAutoRefreshService&&(!_isAutoRefreshing)){
//             	   _isAutoRefreshing=true;
//                        //对Label进行实时刷新，需要加上这句
//                      _tree.getDisplay().asyncExec(new Runnable() {       
//                         @Override
//                         public void run() {
//                         	RefreshServiceTree();   
//			                       _isAutoRefreshing=false;
//                         }
//                      });
//	                   try {
//                      Thread.sleep(_isAutoRefreshServiceTime);//每隔一秒刷新一次
//                    } catch (Exception e) {
//                 	//_isAutoRefreshService=false;
//	                      // _isAutoRefreshing=false;
//                    }
//                }
//            }
//		}.start();
	}
}
