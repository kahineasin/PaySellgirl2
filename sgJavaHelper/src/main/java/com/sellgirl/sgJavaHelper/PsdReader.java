/*     */ package com.sellgirl.sgJavaHelper;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PsdReader
/*     */ {
/*  18 */   private BufferedImage img = null;
/*     */   
/*     */   private int[] pixels;
/*     */   
/*     */   private RandomAccessFile raf;
/*     */   
/*     */   //private int[] byteArray;
/*     */   
/*     */   private int[][][] channelColor;
/*     */   
/*     */   private int[][] numOfBytePerLine;
/*     */   
/*     */   private short numOfChannel;
/*     */   private int height;
/*     */   private int width;
/*     */   private short isRle;
/*     */   private MappedByteBuffer mbbi;
/*     */   
/*     */   public PsdReader(File file) {
/*  37 */     FileChannel fc = null;
/*     */     try {
/*  39 */       this.raf = new RandomAccessFile(file, "r");
/*  40 */       fc = this.raf.getChannel();
/*  41 */       long size = fc.size();
/*  42 */       this.mbbi = fc.map(FileChannel.MapMode.READ_ONLY, 0L, size);
/*  43 */     } catch (FileNotFoundException e) {
/*  44 */       e.printStackTrace();
/*  45 */     } catch (IOException e) {
/*  46 */       e.printStackTrace();
/*     */     } 
/*  48 */     readFile();
/*  49 */     this.img = new BufferedImage(this.width, this.height, 2);
/*  50 */     this.pixels = new int[this.width * this.height];
/*  51 */     initPixels(this.pixels);
/*  52 */     setRGB(this.img, 0, 0, this.width, this.height, this.pixels);
/*     */     try {
/*  54 */       fc.close();
/*  55 */       this.raf.close();
/*  56 */     } catch (IOException e) {
/*  57 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public BufferedImage getImg() {
/*  62 */     return this.img;
/*     */   }
/*     */   
/*     */   private void initPixels(int[] pixels) {
/*  66 */     int index = 0;
/*  67 */     int a = 255;
/*  68 */     for (int h = 0; h < this.height; h++) {
/*  69 */       for (int w = 0; w < this.width; w++) {
/*  70 */         int r = this.channelColor[0][h][w];
/*  71 */         int g = this.channelColor[1][h][w];
/*  72 */         int b = this.channelColor[2][h][w];
/*  73 */         if (this.numOfChannel > 3) {
/*  74 */           a = this.channelColor[3][h][w];
/*     */         }
/*     */         
/*  77 */         pixels[index] = a << 24 | r << 16 | g << 8 | b;
/*     */         
/*  79 */         index++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
/*  85 */     int type = image.getType();
/*  86 */     if (type == 2 || type == 1) {
/*  87 */       image.getRaster().setDataElements(x, y, width, height, pixels);
/*     */     } else {
/*  89 */       image.setRGB(x, y, width, height, pixels, 0, width);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readFile() {
/*     */     try {
/*  97 */       this.mbbi.position(12);
/*     */       
/*  99 */       this.numOfChannel = this.mbbi.getShort();
/*     */ 
/*     */ 
/*     */       
/* 103 */       this.height = this.mbbi.getInt();
/*     */ 
/*     */ 
/*     */       
/* 107 */       this.width = this.mbbi.getInt();
/*     */ 
/*     */ 
/*     */       
/* 111 */       //short depth = this.mbbi.getShort();
/*     */ 
/*     */ 
/*     */       
/* 115 */       //short type = this.mbbi.getShort();
/*     */ 
/*     */ 
/*     */       
/* 119 */       int lenOfColorModel = this.mbbi.getInt();
/*     */ 
/*     */       
/* 122 */       this.mbbi.position(lenOfColorModel + this.mbbi.position());
/*     */ 
/*     */       
/* 125 */       int lenOfImageResourceBlock = this.mbbi.getInt();
/*     */ 
/*     */       
/* 128 */       this.mbbi.position(lenOfImageResourceBlock + this.mbbi.position());
/*     */ 
/*     */       
/* 131 */       int lenOfLayerInfo = this.mbbi.getInt();
/*     */ 
/*     */       
/* 134 */       this.mbbi.position(lenOfLayerInfo + this.mbbi.position());
/*     */ 
/*     */       
/* 137 */       this.isRle = this.mbbi.getShort();
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 142 */     catch (Exception e1) {
/* 143 */       e1.printStackTrace();
/*     */     } 
/*     */     
/* 146 */     this.channelColor = new int[this.numOfChannel][this.height][this.width];
/* 147 */     if (this.isRle == 1) {
/* 148 */       this.numOfBytePerLine = new int[this.numOfChannel][this.height];
/* 149 */       for (int i = 0; i < this.numOfChannel; i++) {
/* 150 */         for (int j = 0; j < this.height; j++) {
/*     */ 
/*     */           
/*     */           try {
/* 154 */             int ti = this.mbbi.getShort();
/* 155 */             if (ti < 0) ti += 65536; 
/* 156 */             this.numOfBytePerLine[i][j] = ti;
/* 157 */           } catch (Exception e) {
/* 158 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/* 162 */       for (int c = 0; c < this.numOfChannel; c++) {
/* 163 */         for (int h = 0; h < this.height; h++) {
/* 164 */           unpackbits(this.numOfBytePerLine[c][h], this.channelColor[c][h]);
/*     */         }
/*     */       } 
/* 167 */     } else if (this.isRle == 0) {
/* 168 */       for (int c = 0; c < this.numOfChannel; c++) {
/* 169 */         for (int h = 0; h < this.height; h++) {
/* 170 */           for (int w = 0; w < this.width; w++) {
/*     */             
/*     */             try {
/* 173 */               int ti = this.mbbi.get();
/* 174 */               if (ti < 0) ti += 256; 
/* 175 */               this.channelColor[c][h][w] = ti;
/* 176 */             } catch (Exception e) {
/* 177 */               e.printStackTrace();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void unpackbits(int lenOfInput, int[] channelColor) {
/* 186 */     short n = 0;
/* 187 */     int last = 0;
/*     */     
/* 189 */     while (lenOfInput > 0) {
/*     */       
/*     */       try {
/* 192 */         n = (short)this.mbbi.get();
/* 193 */         lenOfInput--;
/* 194 */       } catch (Exception e) {
/* 195 */         e.printStackTrace();
/*     */       } 
/*     */       
/* 198 */       if (0 <= n && n <= 127) {
/* 199 */         int repeatTime = n;
/* 200 */         repeatTime++;
/* 201 */         for (int t = 0; t < repeatTime; t++) {
/*     */           
/*     */           try {
/* 204 */             int ti = this.mbbi.get();
/* 205 */             if (ti < 0) ti += 256; 
/* 206 */             channelColor[last + t] = ti;
/*     */             
/* 208 */             lenOfInput--;
/* 209 */           } catch (Exception e) {
/* 210 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/* 213 */         last += repeatTime; continue;
/*     */       } 
/* 215 */       if (-1 >= n && n >= -127) {
/* 216 */         int val = 0;
/* 217 */         int repeatTime = -n;
/* 218 */         repeatTime++;
/*     */         
/*     */         try {
/* 221 */           int ti = this.mbbi.get();
/* 222 */           if (ti < 0) ti += 256; 
/* 223 */           val = ti;
/*     */           
/* 225 */           lenOfInput--;
/* 226 */         } catch (Exception e) {
/* 227 */           e.printStackTrace();
/*     */         } 
/* 229 */         for (int t = 0; t < repeatTime; t++) {
/* 230 */           channelColor[last + t] = val;
/*     */         }
/* 232 */         last += repeatTime; continue;
/*     */       } 
/* 234 */       if (n == -128);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\mylib\sellgirlPayHelper-0.0.1-SNAPSHOT.jar!\pf\java\pfHelper\PsdReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */