package com.sellgirl.sgGameHelper.gamepad;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sellgirl.sgGameHelper.SGGameHelper;
//import com.mygdx.game.sasha.screen.MainMenuScreen;
import com.sellgirl.sgGameHelper.SGLibGdxHelper;
import com.sellgirl.sgGameHelper.ScreenSetting;

/**
 * gdxUi在调用安卓电视上的虚拟键盘时，好像有问题，尝试用此来代替(未完成)
 * 直接用ds的版本OnScreenKeyboardDialog.java
 * 
 * 使用方法：
        lrcTBox = new SGSysKeyboard(TXT);
        lrcTBox.setFont(game.font);
 */
@Deprecated
public class SGSysKeyboard  extends Actor//,
//Group
{
private BitmapFont font;
//private Array2<String> lines;
private static final char[] lines=new char[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','@','.'};
private static String[] lines2=null;
private int currentLine=-1;
private int lastLine=-1;

//// bullet pool.
//protected final Pool<Bubble> bubblePool = new Pool<Bubble>() {
//  @Override
//  protected Bubble newObject() {
//      return new Bubble();
//  }
//};
//private Language TXT;
public SGSysKeyboard(//Language language
		){
  super();
//  lines=new Array2<>();
//  this.TXT=language;
  if(null==lines2) {
	  lines2=new String[lines.length];
	  for(int i=0;i<lines.length;i++) {
		  lines2[i]=new String(lines,i,1);
	  }
  }
}
//private Array2<Bubble> bubbles;
@Override
public void act(float delta) {
  super.act(delta);
//  if(null!=bubbles){
//      for(int i=bubbles.size-1;0<=i;i--){
//          Bubble bubble=bubbles.get(i);
//          if(!bubble.alive){
//              bubblePool.free(bubble);
//              bubble.remove();
//              bubbles.removeIndex(i);
////              System.out.println("free bubble");
//          }else{
//              bubble.act(delta);
//          }
//      }
//  }
}
//private Vector2 tmpV2=null;
@Override
public void draw(Batch batch, float parentAlpha) {
  super.draw(batch,parentAlpha);
  if(-1==currentLine){
      return;//未开始
  }

//  if(null!=bubbles){
//      for(Bubble bubble:bubbles){
//          bubble.draw(batch,parentAlpha);
//      }
//  }

  if(null!=lines&&0<lines.length) {
	  float originX=0;
      float x=0;
      float y= getHeight()-(rowHeight/2f);
      int i=0;
//      int curWordCnt=0;
      float curY=0;
      int j=0;
      for(i=0;i<lines.length;i++){
    	  char line=lines[i];
    	  
          font.setColor(currentLine==i?Color.YELLOW:Color.WHITE);
//          float lineY=y+getY();
//          font.draw(batch, lines2[i], x, lineY);
//          y-=rowHeight;
          font.draw(batch, lines2[i], x+(10*j), y+rowHeight);
          
          if(10>j) {
        	  j++;
          }else {
        	  j=0;
        	  y+=rowHeight;
          }

//          if(currentLine==i&&lastLine!=currentLine){
//              curWordCnt=line.length();
//              curY=lineY;
//          }
//          i++;
      }

//      if(lastLine!=currentLine){
//          Bubble bubble = bubblePool.obtain();
//          if(null==tmpV2){
//              tmpV2=new Vector2();
//          }
//          float alpha=0.2f+(0.05f*curWordCnt);
//          if(1<alpha){alpha=0.8f;}
//
////          tmpV2.set(-1f,0);//测试横飞
////          tmpV2.set(0f,1f);
////          tmpV2.rotateDeg(MathUtils.random(10,170));//往屏幕左方的160度范围
//
//          //正式
//          tmpV2.set(-ScreenSetting.WORLD_WIDTH,(ScreenSetting.WORLD_HEIGHT/2f)-curY);//这是往屏幕左飞的方向
//          tmpV2.rotateDeg(MathUtils.random(-50,50));
//
//          float radius=(ScreenSetting.WORLD_WIDTH/(2f*4f))*(0.1f*curWordCnt)*0.5f;//字越多，泡泡越大，10个字时，半屏可放4个泡泡
//          if( ScreenSetting.WORLD_WIDTH/12f<radius){radius=ScreenSetting.WORLD_WIDTH/12f;};
//          float speed=(ScreenSetting.WORLD_WIDTH/(2f*5f))*(0.1f*MathUtils.random(5,8));//速度是5秒跑完一个屏幕
//          bubble.init(x+(ScreenSetting.WORLD_WIDTH/2f),
//              //ScreenSetting.WORLD_HEIGHT*0.5f,
//              curY,//tmpPos.z,
//              radius,
//              tmpV2,
//              speed//, manager,
//              ,alpha
//               ,this.getStage()//,1,1
//          );
////          System.out.println("new bubble");
//
//          if(null==bubbles){bubbles=new Array2<>();}
//          bubbles.add(bubble);
////          this.addActor(bubble);
//      }
      //在这里显示文字的话，会随着滚动的
      //font.draw(batch,playedTime.toString(),ScreenSetting.WORLD_WIDTH-200f,ScreenSetting.WORLD_HEIGHT-50f);
  }else{
//      font.setColor(Color.WHITE);
//      font.draw(batch, TXT.g("No lyrics"), ScreenSetting.WORLD_WIDTH/4f, ScreenSetting.WORLD_HEIGHT/2f);
  }
//  lastLine=currentLine;
}
private float rowHeight=36f;

public BitmapFont getFont() {
  return font;
}

public void setFont(BitmapFont font) {
  this.font = font;
  font.setColor(Color.WHITE);
  rowHeight=this.font.getLineHeight();
}

//public Array2<String> getLines() {
//  return lines;
//}
//
//public void setLines(Array2<String> lines) {
//  this.lines = lines;
//}
//public void clearLines(){
//  this.lines.clear();
//}
//public void addLine(String line){
//  this.lines.add(line);
//}
//public void updateHeight(){
////  this.setHeight(font.getLineHeight()* lines.size);
//  this.setHeight(rowHeight* lines.size);
////  System.out.println("lrcBox getLineHeight:"+font.getLineHeight());//41.0
////  System.out.println("lrcBox getLineHeight:"+rowHeight);//41.0
////  System.out.println("lrcBox height:"+this.getHeight());
//}
public void setCurrentLine(int currentLine){
  this.currentLine=currentLine;
}
public float getRowHeight(){
  return rowHeight;
}
//public void updatePlayedTime(float second){
//
//  playedTime.initByTime((long)(second*1000f));
//}
//public void startPlay(){
//  if(0>currentLine){currentLine=0;}
//}
}